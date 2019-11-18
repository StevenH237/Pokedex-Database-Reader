package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.util.HashMap;

/**
 * Represents possible shapes of a {@link Pokemon}'s body.
 */
public class PokemonShape extends DBObject {
  private HashMap<Language, String> names;
  private HashMap<Language, String> awesomeNames;
  private HashMap<Language, String> descriptions;
  
  private PokemonShape(int id, String identifier) {
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
  
  /** Gets the basic name of this shape in a given language. */
  public String getName(Language lang) {
    return names.get(lang);
  }
  
  /**
   * Gets the "awesome" (scientific) name of this shape in a given
   * language.
   */
  public String getAwesomeName(Language lang) {
    return awesomeNames.get(lang);
  }
  
  /** Gets a description of this shape in a given language. */
  public String getDescription(Language lang) {
    return descriptions.get(lang);
  }
}
