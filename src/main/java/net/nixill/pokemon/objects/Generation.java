package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.util.HashMap;

import lombok.Getter;

public class Generation extends DBObject {
  private static HashMap<String, Class<?>> types;
  
  @Getter(lazy = true) private final Region mainRegion = getProperty(
      "main_region_id", Region.class);
  private HashMap<Language, String> names;
  
  static {
    types = new HashMap<>();
    types.put("main_region_id", Region.class);
  }
  
  public Generation(int id, String identifier) {
    super(id, identifier);
  }
  
  @Override
  public void complete(ResultSet res) {
    if (complete(res, types)) {
      names = getLangTable("generation_names", "generation_id", "name");
    }
  }
  
  public String getName(Language lang) {
    return names.get(lang);
  }
}
