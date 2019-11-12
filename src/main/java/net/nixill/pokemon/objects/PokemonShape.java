package net.nixill.pokemon.objects;

import java.sql.ResultSet;

public class PokemonShape extends DBObject {
  
  public PokemonShape(int id, String identifier) {
    super(id, identifier);
  }
  
  @Override
  public void complete(ResultSet res) {
    if (complete(res, null)) {
      getLangTable("pokemon_shape_prose", "pokemon_shape_id", "name");
      getLangTable("pokemon_shape_prose", "pokemon_shape_id",
          "awesome_name");
      getLangTable("pokemon_shape_prose", "pokemon_shape_id",
          "description");
    }
  }
  
  public String getName(Language lang) {
    return getLanguageValue("pokemon_shape_prose.name", lang);
  }
  
  public String getAwesomeName(Language lang) {
    return getLanguageValue("pokemon_shape_prose.awesome_name", lang);
  }
  
  public String getDescription(Language lang) {
    return getLanguageValue("pokemon_shape_prose.description", lang);
  }
}