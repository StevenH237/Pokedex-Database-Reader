package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;

import net.nixill.pokemon.database.DBConnection;
import net.nixill.pokemon.database.DBException;

/**
 * A GrowthRate is a rate at which a {@link PokemonSpecies} gains levels
 * from experience points.
 * <p>
 * In generations 1 and 2, growth was calculated from a function, whereas
 * in generations 3 and beyond, the points per level values were stored in
 * tables. Both methods are available here, though they should produce
 * identical results.
 * <p>
 * The experience point values per level are cumulative; that is, the
 * numbers of points returned by the functions are the difference between
 * level 1 and the requested level <i>n</i>, not between <i>n-1</i> and
 * <i>n</i>.
 */
public class GrowthRate extends DBObject {
  private static TreeMap<Integer, GrowthRateFunction> functions;
  private static TreeMap<Integer, Integer> predefinedExperience;
  
  private GrowthRate(int id, String identifier) {
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
  
  /**
   * Calculates the experience points required to reach a given level.
   * <p>
   * This method uses the functions to calculate the level on-demand. The
   * output of this and {@link #lookupExpForLevel(int)} should be identical
   * for any given level between 1 and 100, but can also be used to look up
   * values outside that range.
   * <p>
   * For level 1, the function is not used; experience at level 1 is always
   * 0. To override this and use the function, use the
   * {@link #calculateLevelOne()} method.
   * 
   * @param level
   *   The level for which to get the required experience.
   * @return The experience required to reach that level.
   */
  public int calculateExpForLevel(int level) {
    if (level == 1) { return 0; }
    
    GrowthRateFunction func = functions.higherEntry(level).getValue();
    return func.calculate(level);
  };
  
  /**
   * Calculates what the output of the function would be for level 1.
   * <p>
   * Growth rates do not, by default, use the function for level 1;
   * instead, level 1 experience is always 0. This method uses the
   * function.
   */
  public int calculateLevelOne() {
    GrowthRateFunction func = functions.higherEntry(1).getValue();
    return func.calculate(1);
  }
  
  /**
   * Looks up the required experience from built-in lookup tables.
   * <p>
   * This method uses pre-defined values of experience for each level. It
   * should produce identical results with
   * {@link #calculateExpForLevel(int)} for levels 1 to 100, but cannot go
   * outside this range.
   * 
   * @param level
   *   The level for which to get the required experience.
   * @return The experience required to reach that level.
   */
  public int lookupExpForLevel(int level) {
    return predefinedExperience.get(level);
  }
}
