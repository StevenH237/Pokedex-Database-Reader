package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import net.nixill.pokemon.database.DBConnection;
import net.nixill.pokemon.database.DBException;
import net.nixill.pokemon.objects.factory.DBObjectReader;

public class EvolutionChain extends DBObject {
  private static HashMap<String, Class<?>> props;
  
  @Getter(lazy = true)
  private final List<PokemonSpecies> members = makeMemberList();
  
  @Getter(lazy = true)
  private final Integer babyTriggerItemId = (Integer) getProperty(
      "baby_trigger_item_id");
  
  static {
    props = new HashMap<>();
    props.put("baby_trigger_item_id", Integer.class);
  }
  
  private EvolutionChain(int id) {
    super(id, null);
  }
  
  @Override
  public void complete(ResultSet res) {
    complete(res, props);
  }
  
  private List<PokemonSpecies> makeMemberList() {
    try {
      List<PokemonSpecies> out = new ArrayList<>();
      ResultSet res = DBConnection.query(
          "SELECT id FROM pokemon_species WHERE evolution_chain_id = "
              + id);
      while (res.next()) {
        int specId = res.getInt("id");
        // System.out.println("Added species " + specId + " to chain " +
        // id);
        out.add(DBObjectReader.get(PokemonSpecies.class, specId));
      }
      return out;
    } catch (SQLException ex) {
      throw new DBException(ex);
    }
  }
}
