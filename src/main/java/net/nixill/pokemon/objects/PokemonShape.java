package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.util.HashMap;

public class PokemonShape extends DBObject {
  private HashMap<Language, String> names;
  private HashMap<Language, String> awesomeNames;
  private HashMap<Language, String> descriptions;
  
  public PokemonShape(int id, String identifier) {
    super(id, identifier);
  }
  
  @Override
  public void complete(ResultSet res) {
    if (complete(res, null)) {
      names = getLangTable("pokemon_shape_prose", "pokemon_shape_id",
          "name");
      awesomeNames = getLangTable("pokemon_shape_prose",
          "pokemon_shape_id", "awesome_name");
      descriptions = getLangTable("pokemon_shape_prose",
          "pokemon_shape_id", "description");
    }
  }
  
  public String getName(Language lang) {
    return names.get(lang);
  }
  
  public String getAwesomeName(Language lang) {
    return awesomeNames.get(lang);
  }
  
  public String getDescription(Language lang) {
    return descriptions.get(lang);
  }
}
