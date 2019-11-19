package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import net.nixill.pokemon.objects.factory.DBObjectReader;

/**
 * An subclass of a PokemonSpecies instance. See {@link PokemonSpecies} for
 * more details.
 * 
 * @see PokemonSpecies
 */
public class Pokemon extends DBObject {
  private static HashMap<String, Class<?>> types;
  
  /** The {@link PokemonSpecies} of which this Pokemon is a member. */
  @Getter(
    lazy = true) private final PokemonSpecies species = (PokemonSpecies) getProperty(
        "species_id");
  /**
   * The Pokémon from which this Pokémon evolves, or <tt>null</tt> if
   * there's no pre-evolved form.
   */
  @Getter(
    lazy = true) private final Pokemon preEvolvedForm = (Pokemon) getProperty(
        "evolves_from_pokemon_id");
  /** The height of this Pokémon, in decimeters (tenths of a meter). */
  @Getter(
    lazy = true) private final int height = (int) getProperty("height");
  /**
   * The weight of this Pokémon, in hectograms (hundreds of grams, or
   * tenths of a kilogram).
   */
  @Getter(
    lazy = true) private final int weight = (int) getProperty("weight");
  /** The base experience yield of this Pokémon. */
  @Getter(
    lazy = true) private final int baseExperience = (int) getProperty(
        "base_experience");
  /** A sorting order among Pokémon. */
  @Getter(
    lazy = true) private final int order = (int) getProperty("order");
  /** Whether or not this Pokémon is the default form for the species. */
  @Getter(
    lazy = true) private final boolean isDefault = (boolean) getProperty(
        "is_default");
  
  /**
   * All the Pokémon that evolve from this one, or an empty list if there
   * are no such Pokémon.
   */
  @Getter(
    lazy = true) private final List<Pokemon> evolvedForms = DBObjectReader
        .getReader(Pokemon.class).resultsOf(
            "select id from pokemon where evolves_from_pokemon_id = " + id
                + ";");
  
  static {
    types = new HashMap<>();
    types.put("species_id", PokemonSpecies.class);
    types.put("evolves_from_pokemon_id", Pokemon.class);
    types.put("height", Integer.class);
    types.put("weight", Integer.class);
    types.put("base_experience", Integer.class);
    types.put("order", Integer.class);
    types.put("is_default", Boolean.class);
  }
  
  private Pokemon(int id, String identifier) {
    super(id, identifier);
  }
  
  @Override
  public void complete(ResultSet res) {
    complete(res, types);
  }
}
