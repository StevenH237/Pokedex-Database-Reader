package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.util.HashMap;

public class PokemonHabitat extends DBObject {
  private HashMap<Language, String> names;
  
  public PokemonHabitat(int id, String identifier) {
    super(id, identifier);
  }
  
  @Override
  public void complete(ResultSet res) {
    if (complete(res, null)) {
      names = getLangTable("pokemon_habitat_names", "pokemon_habitat_id",
          "name");
    }
  }
  
  public String getName(Language lang) {
    return names.get(lang);
  }
}
