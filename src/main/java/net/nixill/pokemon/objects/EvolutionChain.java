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

/**
 * An EvolutionChain is a group of {@link PokemonSpecies}, all related in
 * that its members evolve into or from each other.
 */
public class EvolutionChain extends DBObject {
  private static HashMap<String, Class<?>> props;
  
  /**
   * The list of member {@link PokemonSpecies} of this EvolutionChain.
   */
  @Getter(
    lazy = true) private final List<PokemonSpecies> members = makeMemberList();
  
  /**
   * The ID of the item that causes babies to be made.
   * <p>
   * More specifically, this is the item that either of the parents must
   * hold in order to produce the baby species of an EvolutionChain from
   * eggs instead of the basic species.
   * <p>
   * This property and method will be replaced with
   * <tt>getBabyTriggerItem()</tt> when the <tt>Item</tt> class is made in
   * a future version of the API.
   */
  @Getter(
    lazy = true) @Deprecated private final Integer babyTriggerItemId = (Integer) getProperty(
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
        out.add(DBObjectReader.get(PokemonSpecies.class, specId));
      }
      return out;
    } catch (SQLException ex) {
      throw new DBException(ex);
    }
  }
}
