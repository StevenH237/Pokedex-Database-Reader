package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.util.HashMap;

import lombok.Getter;

/**
 * A Generation refers to a major iteration of the Pokémon franchise.
 * <p>
 * Generations are kept apart by new Pokémon, Moves, Abilities, and Items,
 * and a brand-new Region to explore. Most new generations have also been
 * on different consoles to the previous generation, with the exception of
 * 5 and 7.
 */
public class Generation extends DBObject {
  private static HashMap<String, Class<?>> types;
  
  /**
   * The main {@link Region} of the Generation, i.e. the one first
   * introduced in main series games in this Generation.
   */
  @Getter(lazy = true) private final Region mainRegion = getProperty(
      "main_region_id", Region.class);
  
  private HashMap<Language, String> names;
  
  static {
    types = new HashMap<>();
    types.put("main_region_id", Region.class);
  }
  
  private Generation(int id, String identifier) {
    super(id, identifier);
  }
  
  @Override
  public void complete(ResultSet res) {
    if (complete(res, types)) {
      names = getLangTable("generation_names", "generation_id", "name");
    }
  }
  
  /**
   * Returns the name of this Generation in the given {@link Language}.
   * 
   * @param lang
   *   The Language to use.
   * @return The name in that language.
   */
  public String getName(Language lang) {
    return names.get(lang);
  }
}
