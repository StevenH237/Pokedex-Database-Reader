package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.util.HashMap;

/**
 * Represents possible colors that Pokémon can be categorized as.
 */
public class PokemonColor extends DBObject {
  private HashMap<Language, String> names;
  
  private PokemonColor(int id, String identifier) {
    super(id, identifier);
  }
  
  @Override
  public void complete(ResultSet res) {
    if (complete(res, null)) {
      names = getLangTable("pokemon_color_names", "pokemon_color_id",
          "name");
    }
  }
  
  /** Gets the name of this PokemonColor in the given Language. */
  public String getName(Language lang) {
    return names.get(lang);
  }
}
