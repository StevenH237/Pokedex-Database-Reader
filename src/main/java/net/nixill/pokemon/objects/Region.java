package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.util.HashMap;

/**
 * A Region is the setting of a Pokémon game. A new one is introduced every
 * {@link Generation}, and some can be revisited in future games.
 */
public class Region extends DBObject {
  private HashMap<Language, String> names;
  
  private Region(int id, String identifier) {
    super(id, identifier);
  }
  
  @Override
  public void complete(ResultSet res) {
    if (complete(res, null)) {
      names = getLangTable("region_names", "region_id", "name");
    }
  }
  
  /**
   * Gets the name of this Region in a given Language.
   * 
   * @param lang
   *   The language to use.
   * @return The name of the Region.
   */
  public String getName(Language lang) {
    return names.get(lang);
  }
}
