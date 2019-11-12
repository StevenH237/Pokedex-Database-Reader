package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.util.HashMap;

public class PokemonSpecies extends DBObject {
  private static HashMap<String, Class<?>> types;
  
  private static HashMap<Language, String> names;
  private static HashMap<Language, String> genera;
  private static HashMap<Language, String> formDescriptions;
  
  static {
    types = new HashMap<>();
    types.put("generation_id", Generation.class);
    types.put("evolution_chain_id", Integer.class);
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
  
  public PokemonSpecies(int id, String identifier) {
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
  
  public String getName(Language lang) {
    return names.get(lang);
  }
  
  public String getGenus(Language lang) {
    return genera.get(lang);
  }
  
  public String getFormDescription(Language lang) {
    return formDescriptions.get(lang);
  }
  
  public String toString() {
    return names.get(Language.ENGLISH);
  }
}