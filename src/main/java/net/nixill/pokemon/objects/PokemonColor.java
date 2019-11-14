package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.util.HashMap;

public class PokemonColor extends DBObject {
  private HashMap<Language, String> names;
  
  public PokemonColor(int id, String identifier) {
    super(id, identifier);
  }
  
  @Override
  public void complete(ResultSet res) {
    if (complete(res, null)) {
      names = getLangTable("pokemon_color_names", "pokemon_color_id",
          "name");
    }
  }
  
  public String getName(Language lang) {
    return names.get(lang);
  }
}
