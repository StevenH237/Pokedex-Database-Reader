package net.nixill.pokemon.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IDStatements {
  private PreparedStatement selectID;
  private PreparedStatement selectIdentifier;
  private PreparedStatement allIDs;
  
  IDStatements(String name, Connection conn) {
    try {
      selectID = conn
          .prepareStatement("SELECT * FROM " + name + " WHERE id = ?;");
      selectIdentifier = conn.prepareStatement(
          "SELECT * FROM " + name + " WHERE identifier = ?;");
      allIDs = conn
          .prepareStatement("SELECT id, identifier FROM " + name + ";");
    } catch (SQLException ex) {
      throw new DBException(ex);
    }
  }
  
  public ResultSet getByID(int id) {
    ResultSet res = null;
    try {
      selectID.setInt(1, id);
      res = selectID.executeQuery();
      selectID.clearParameters();
    } catch (SQLException ex) {
      throw new DBException(ex);
    }
    return res;
  }
  
  public ResultSet getByIdentifier(String identifier) {
    ResultSet res = null;
    try {
      selectIdentifier.setString(1, identifier);
      res = selectIdentifier.executeQuery();
      selectIdentifier.clearParameters();
    } catch (SQLException ex) {
      throw new DBException(ex);
    }
    return res;
  }
  
  public ResultSet getAllIDs() {
    ResultSet res = null;
    try {
      res = allIDs.executeQuery();
    } catch (SQLException ex) {
      throw new DBException(ex);
    }
    return res;
  }
  
  public void close() throws SQLException {
    selectID.close();
    selectIdentifier.close();
    allIDs.close();
  }
}