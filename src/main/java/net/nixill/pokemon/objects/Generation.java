package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.util.HashMap;

public class Generation extends DBObject {
  private static HashMap<String, Class<?>> types;
  
  private Region                    mainRegion;
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
      mainRegion = (Region) getProperty("main_region_id");
    }
  }
  
  public Region getMainRegion() {
    return (Region) mainRegion.complete();
  }
  
  public String getName(Language lang) {
    return names.get(lang);
  }
  
  public String toString() {
    return getName(Language.ENGLISH);
  }
}