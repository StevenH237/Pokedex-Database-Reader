package net.nixill.pokemon.objects;

import java.sql.ResultSet;

public class PokemonColor extends DBObject {
  
  public PokemonColor(int id, String identifier) {
    super(id, identifier);
  }
  
  @Override
  public void complete(ResultSet res) {
    if (complete(res, null)) {
      getLangTable("pokemon_color_names", "pokemon_color_id", "name");
    }
  }
  
  public String getName(Language lang) {
    return getLanguageValue("pokemon_color_names.name", lang);
  }
}