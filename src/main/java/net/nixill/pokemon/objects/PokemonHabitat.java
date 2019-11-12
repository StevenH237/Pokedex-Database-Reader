package net.nixill.pokemon.objects;

import java.sql.ResultSet;

public class PokemonHabitat extends DBObject {
  
  public PokemonHabitat(int id, String identifier) {
    super(id, identifier);
  }
  
  @Override
  public void complete(ResultSet res) {
    if (complete(res, null)) {
      getLangTable("pokemon_habitat_names", "pokemon_habitat_id", "name");
    }
  }
  
}