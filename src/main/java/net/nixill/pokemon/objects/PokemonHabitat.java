package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.util.HashMap;

/**
 * Represents possible habitats that Pokémon from the first three
 * generations are classified into.
 */
public class PokemonHabitat extends DBObject {
  private HashMap<Language, String> names;
  
  private PokemonHabitat(int id, String identifier) {
    super(id, identifier);
  }
  
  @Override
  public void complete(ResultSet res) {
    if (complete(res, null)) {
      names = getLangTable("pokemon_habitat_names", "pokemon_habitat_id",
          "name");
    }
  }
  
  /** Returns the name of this PokemonHabitat in a given language. */
  public String getName(Language lang) {
    return names.get(lang);
  }
}
