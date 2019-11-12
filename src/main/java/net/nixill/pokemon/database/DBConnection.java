package net.nixill.pokemon.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBConnection {
  private static boolean    initialized;
  private static Connection conn;
  private static Statement  stmt;
  
  private static ArrayList<IDStatements>   idses;
  private static ArrayList<LangStatements> langses;
  
  public static void init() {
    if (initialized) {
      throw new IllegalStateException(
          "The connection is already initialized.");
    }
    try {
      conn = DriverManager.getConnection("jdbc:sqlite:pokedex.db");
      stmt = conn.createStatement();
    } catch (SQLException ex) {
      throw new DBException(ex);
    }
    idses = new ArrayList<>();
    langses = new ArrayList<>();
    initialized = true;
  }
  
  public static ResultSet query(String sql) throws SQLException {
    return stmt.executeQuery(sql);
  }
  
  public static IDStatements prepareIDStatements(String tableName) {
    IDStatements ids = new IDStatements(tableName, conn);
    idses.add(ids);
    return ids;
  }
  
  public static LangStatements prepareLangStatements(String tableName,
      String objectColumn, String nameColumn) {
    LangStatements langs = new LangStatements(tableName, objectColumn,
        nameColumn, conn);
    langses.add(langs);
    return langs;
  }
  
  public static void closeAll() {
    try {
      stmt.close();
      for (IDStatements ids : idses) {
        ids.close();
      }
      for (LangStatements langs : langses) {
        langs.close();
      }
      conn.close();
    } catch (SQLException ex) {
      throw new DBException(ex);
    }
  }
}