package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;

import net.nixill.pokemon.database.DBConnection;
import net.nixill.pokemon.database.DBException;

// (((ax+b)/c+d)x³/e+fx²/g+hx/i+j)*k/l
public class GrowthRate extends DBObject {
  private static TreeMap<Integer, GrowthRateFunction> functions;
  private static TreeMap<Integer, Integer>            predefinedExperience;
  
  public GrowthRate(int id, String identifier) {
    super(id, identifier);
  }
  
  @Override
  public void complete(ResultSet res) {
    if (complete(res, null)) {
      functions = new TreeMap<>();
      predefinedExperience = new TreeMap<>();
      try {
        ResultSet set = DBConnection.query(
            "SELECT * FROM growth_rate_functions WHERE growth_rate_id = "
                + id);
        
        while (set.next()) {
          int maxLevel = set.getInt("max_level");
          GrowthRateFunction func = new GrowthRateFunction(this, maxLevel,
              set.getInt("a"), set.getInt("b"), set.getInt("c"),
              set.getInt("d"), set.getInt("e"), set.getInt("f"),
              set.getInt("g"), set.getInt("h"), set.getInt("i"),
              set.getInt("j"), set.getInt("k"), set.getInt("l"));
          
          functions.put(maxLevel, func);
        }
        
        set.close();
        
        set = DBConnection.query(
            "SELECT * FROM experience WHERE growth_rate_id = " + id);
        while (set.next()) {
          int level = set.getInt("level");
          int experience = set.getInt("experience");
          
          predefinedExperience.put(level, experience);
        }
        
        set.close();
      } catch (SQLException e) {
        throw new DBException(e);
      }
    }
  }
  
  public int calculateExpForLevel(int level) {
    GrowthRateFunction func = functions.higherEntry(level).getValue();
    return func.calculate(level);
  };
  
  public int lookupExpForLevel(int level) {
    return predefinedExperience.get(level);
  }
}