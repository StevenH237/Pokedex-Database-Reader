package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import net.nixill.pokemon.objects.factory.DBObjectReader;

public class Pokemon extends DBObject {
  private static HashMap<String, Class<?>> types;
  
  @Getter(
    lazy = true) private final PokemonSpecies species = (PokemonSpecies) getProperty(
        "species_id");
  @Getter(
    lazy = true) private final Pokemon preEvolvedForm = (Pokemon) getProperty(
        "evolves_from_pokemon_id");
  @Getter(
    lazy = true) private final int height = (int) getProperty("height");
  @Getter(
    lazy = true) private final int weight = (int) getProperty("weight");
  @Getter(
    lazy = true) private final int baseExperience = (int) getProperty(
        "base_experience");
  @Getter(
    lazy = true) private final int order = (int) getProperty("order");
  @Getter(
    lazy = true) private final boolean isDefault = (boolean) getProperty(
        "is_default");
  
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
