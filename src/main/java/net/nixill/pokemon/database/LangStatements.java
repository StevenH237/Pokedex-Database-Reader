package net.nixill.pokemon.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.nixill.pokemon.objects.Language;

public class LangStatements {
  private PreparedStatement oneObject;
  private PreparedStatement oneLanguage;
  private PreparedStatement oneRecord;
  
  LangStatements(String name, String objCol, String nameCol,
      Connection conn) {
    try {
      oneObject = conn.prepareStatement("SELECT local_language_id, "
          + nameCol + " FROM " + name + " WHERE " + objCol + " = ?;");
      oneLanguage = conn.prepareStatement("SELECT " + objCol + ", "
          + nameCol + " FROM " + name + " WHERE local_language_id = ?;");
      oneRecord = conn.prepareStatement("SELECT " + nameCol + " FROM "
          + name + " WHERE " + objCol + " = ? AND local_language_id = ?;");
    } catch (SQLException ex) {
      throw new DBException(ex);
    }
  }
  
  public ResultSet getObjectNames(int objectID) {
    ResultSet res = null;
    try {
      oneObject.setInt(1, objectID);
      res = oneObject.executeQuery();
      oneObject.clearParameters();
    } catch (SQLException ex) {
      throw new DBException(ex);
    }
    return res;
  }
  
  public ResultSet getOneLanguage(Language lang) {
    return getOneLanguage(lang.getId());
  }
  
  public ResultSet getOneLanguage(int langID) {
    ResultSet res = null;
    try {
      oneLanguage.setInt(1, langID);
      res = oneLanguage.executeQuery();
      oneLanguage.clearParameters();
    } catch (SQLException ex) {
      throw new DBException(ex);
    }
    return res;
  }
  
  public ResultSet getOneRecord(int objectID, Language lang) {
    return getOneRecord(objectID, lang.getId());
  }
  
  public ResultSet getOneRecord(int objectID, int langID) {
    ResultSet res = null;
    try {
      oneRecord.setInt(1, objectID);
      oneRecord.setInt(2, langID);
      res = oneRecord.executeQuery();
      oneRecord.clearParameters();
    } catch (SQLException ex) {
      throw new DBException(ex);
    }
    return res;
  }
  
  public void close() throws SQLException {
    oneLanguage.close();
    oneObject.close();
    oneRecord.close();
  }
}