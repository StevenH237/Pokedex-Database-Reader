package net.nixill.pokemon.database;

import java.sql.SQLException;

public class DBException extends RuntimeException {
  private static final long serialVersionUID = 5781934795602974L;
  
  public DBException(SQLException cause) {
    super(cause);
  }
}