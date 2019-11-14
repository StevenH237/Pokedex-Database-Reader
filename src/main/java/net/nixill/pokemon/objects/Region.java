package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.util.HashMap;

public class Region extends DBObject {
  private HashMap<Language, String> names;
  
  public Region(int id, String identifier) {
    super(id, identifier);
  }
  
  @Override
  public void complete(ResultSet res) {
    if (complete(res, null)) {
      names = getLangTable("region_names", "region_id", "name");
    }
  }
  
  public String getName(Language lang) {
    return names.get(lang);
  }
}
