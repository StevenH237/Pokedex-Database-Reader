package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import net.nixill.pokemon.objects.factory.DBObjectReader;

/**
 * Pokémon are fictional creatures that are central to the Pokémon
 * franchise. They come in various different species with many different
 * origins, and have the ability to manipulate various elemental energies
 * to engage in combat with one another.
 * <p>
 * A PokemonSpecies object is an instance of one species of Pokémon,
 * including all of the various forms that species can take.
 * <p>
 * A Pokemon object is more specific; there is always at least one Pokemon
 * for each PokemonSpecies, but when there are multiple forms of a Pokémon
 * species that learn different moves, have different abilities or types,
 * or have other battle-relevant differences, they use distinct Pokemon
 * that are part of the same PokemonSpecies.
 * <p>
 * A PokemonForm object is even more specific than that; there is always at
 * least one PokemonForm for each Pokemon. Multiple PokemonForms for a
 * Pokemon exist when there are cosmetic differences between members of the
 * same Pokemon besides gender dimorphism.
 */
public class PokemonSpecies extends DBObject {
  private static HashMap<String, Class<?>> types;
  
  private HashMap<Language, String> names;
  private HashMap<Language, String> genera;
  private HashMap<Language, String> formDescriptions;
  
  /**
   * The {@link Generation} in which this Pokémon species was introduced.
   */
  @Getter(lazy = true) private final Generation generation = getProperty(
      "generation_id", Generation.class);
  /**
   * The {@link EvolutionChain} of which this Pokémon species is a part.
   */
  @Getter(
    lazy = true) private final EvolutionChain evolutionChain = getProperty(
        "evolution_chain_id", EvolutionChain.class);
  /** The {@link PokemonColor} of non-shiny members of this species. */
  @Getter(lazy = true) private final PokemonColor color = getProperty(
      "color_id", PokemonColor.class);
  /** The {@link PokemonShape} of members of this species. */
  @Getter(lazy = true) private final PokemonShape shape = getProperty(
      "shape_id", PokemonShape.class);
  /**
   * The {@link PokemonHabitat} within which members of this species can be
   * found.
   * <p>
   * Data may not exist for Pokémon introduced in gen 4 or later.
   * <tt>null</tt> will be returned in such cases.
   */
  @Getter(lazy = true) private final PokemonHabitat habitat = getProperty(
      "habitat_id", PokemonHabitat.class);
  /**
   * The gender ratio of this Pokémon species.
   * <p>
   * The ratio is described as a number from 0 to 8, where 0 is all male
   * and 8 is all female. The result can also be -1, which indicates that a
   * Pokémon species is genderless.
   */
  @Getter(lazy = true) private final int genderRate = getProperty(
      "gender_rate", int.class);
  /** The capture rate of the members of this Pokémon species. */
  @Getter(lazy = true) private final int captureRate = getProperty(
      "capture_rate", int.class);
  /**
   * The initial happiness upon capture of the members of this Pokémon
   * species.
   */
  @Getter(lazy = true) private final int baseHappiness = getProperty(
      "base_happiness", int.class);
  /**
   * Whether or not this Pokémon species counts as a baby form.
   * <p>
   * Baby forms are Pokémon that:
   * <ul>
   * <li>Do not have a pre-evolution, but can evolve.</li>
   * <li>Can be bred, but can't themselves breed.</li>
   * </ul>
   */
  @Getter(lazy = true) private final boolean isBaby = getProperty(
      "is_baby", boolean.class);
  /** The number of hatch cycles a Pokémon needs to hatch. */
  @Getter(lazy = true) private final int hatchCounter = getProperty(
      "hatch_counter", int.class);
  /**
   * Whether or not the species displays gender dimorphism in appearances
   * only.
   */
  @Getter(
    lazy = true) private final boolean differentPerGender = getProperty(
        "has_gender_differences", boolean.class);
  /** The {@link GrowthRate} of members of this species. */
  @Getter(lazy = true) private final GrowthRate growthRate = getProperty(
      "growth_rate_id", GrowthRate.class);
  /**
   * Whether or not the forms of this Pokémon species can change once it's
   * generated.
   */
  @Getter(lazy = true) private final boolean formSwitchable = getProperty(
      "forms_switchable", boolean.class);
  /** A sorting order for this Pokémon species among others. */
  @Getter(
    lazy = true) private final int order = getProperty("order", int.class);
  
  /** The default Pokémon of this species. */
  @Getter(
    lazy = true) private final Pokemon defaultPokemon = DBObjectReader
        .getReader(Pokemon.class)
        .resultOf("SELECT id FROM pokemon WHERE species_id = " + id
            + " AND is_default = 1;");
  /** All members of this species. */
  @Getter(
    lazy = true) private final List<Pokemon> allPokemon = DBObjectReader
        .getReader(Pokemon.class).resultsOf(
            "SELECT id FROM pokemon WHERE species_id = " + id + ";");
  
  static {
    types = new HashMap<>();
    types.put("generation_id", Generation.class);
    types.put("evolution_chain_id", EvolutionChain.class);
    types.put("color_id", PokemonColor.class);
    types.put("shape_id", PokemonShape.class);
    types.put("habitat_id", PokemonHabitat.class);
    types.put("gender_rate", Integer.class);
    types.put("capture_rate", Integer.class);
    types.put("base_happiness", Integer.class);
    types.put("is_baby", Boolean.class);
    types.put("hatch_counter", Integer.class);
    types.put("has_gender_differences", Boolean.class);
    types.put("growth_rate_id", GrowthRate.class);
    types.put("forms_switchable", Boolean.class);
    types.put("order", Integer.class);
  }
  
  private PokemonSpecies(int id, String identifier) {
    super(id, identifier);
  }
  
  @Override
  public void complete(ResultSet res) {
    if (complete(res, types)) {
      names = getLangTable("pokemon_species_names", "pokemon_species_id",
          "name");
      genera = getLangTable("pokemon_species_names", "pokemon_species_id",
          "genus");
      formDescriptions = getLangTable("pokemon_species_prose",
          "pokemon_species_id", "form_description");
    }
  }
  
  /**
   * Returns the name of this Pokémon species in a given language.
   * 
   * @param lang
   *   The language to use.
   * @return The name in that language.
   */
  public String getName(Language lang) {
    return names.get(lang);
  }
  
  /**
   * Returns the genus of this Pokémon species in a given language.
   * <p>
   * The genus is also called the "category" or "species", and is a one- or
   * two-word description of the Pokémon species. For example, for Eevee in
   * English, "Evolution Pokémon" would be returned.
   * 
   * @param lang
   *   The language to use.
   * @return The genus in that language.
   */
  public String getGenus(Language lang) {
    return genera.get(lang);
  }
  
  /**
   * Returns the form descriptions of a Pokémon in a given language.
   * <p>
   * This value describes the difference between a Pokémon's forms and how
   * to get them, but can be <tt>null</tt> when there's no description.
   * 
   * @param lang
   *   The language to use.
   * @return The description.
   */
  public String getFormDescription(Language lang) {
    return formDescriptions.get(lang);
  }
}
