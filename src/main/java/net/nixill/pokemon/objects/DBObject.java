package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import net.nixill.pokemon.database.DBConnection;
import net.nixill.pokemon.database.DBException;
import net.nixill.pokemon.database.LangStatements;
import net.nixill.pokemon.objects.factory.DBObjectReader;

@SuppressWarnings("unchecked")
public abstract class DBObject {
  protected HashMap<String, Object>                    properties = new HashMap<>();
  protected HashMap<String, HashMap<Language, String>> langTables = new HashMap<>();
  
  @Getter
  protected int     id;
  @Getter
  protected String  identifier;
  @Getter
  protected boolean isComplete = false;
  
  public DBObject(int id, String identifier) {
    this.id = id;
    this.identifier = identifier;
  }
  
  /**
   * Gets a property of this object.
   * 
   * @param key
   *   The property to get.
   * @return The value, or <tt>null</tt> if no such property exists.
   */
  public Object getProperty(String key) {
    Object out = properties.get(key);
    if (out instanceof DBObject) {
      ((DBObject) out).complete();
    }
    return out;
  }
  
  /**
   * Gets a property of this object as a specific type.
   * 
   * @param key
   *   The property to get.
   * @param cls
   *   The class of the property.
   * @return The value, or <tt>null</tt> if no such property exists.
   */
  public <C> C getProperty(String key, Class<C> cls) {
    C out = (C) (properties.get(key));
    if (out instanceof DBObject) {
      ((DBObject) out).complete();
    }
    return out;
  }
  
  /**
   * Returns whether or not two objects are equal.
   * <p>
   * They're equal iff they're the same class and have the same id.
   * 
   * @param other
   *   The other object to check
   * @return <tt>true</tt> iff they're the same class and same id.
   */
  public boolean equals(Object other) {
    if (other.getClass() != getClass()) { return false; }
    
    DBObject oth = (DBObject) other;
    
    return oth.id == id;
  }
  
  /**
   * Returns the hash code of this object.
   * <p>
   * The hash code of a DBObject is the same as the hash code of the
   * subclass's name, plus the ID of this specific object.
   * 
   * @return The object's hash code.
   */
  public int hashCode() {
    return getClass().getSimpleName().hashCode() + id;
  }
  
  public abstract void complete(ResultSet res);
  
  /**
   * Asserts that a ResultSet corresponds to the proper item.
   * 
   * @param res
   *   The result set of the given item
   * @param props
   *   A map representing the expected type of each column. Should be a
   *   {@link String}, {@link Integer}, or a subclass of {@link DBObject}.
   * @return <tt>true</tt> iff the object isn't already completed.
   */
  protected boolean complete(ResultSet res,
      HashMap<String, Class<?>> props) {
    if (isComplete) { return false; }
    
    try {
      // Make sure the row actually corresponds to the object calling it
      if (id != res.getInt("id") || (identifier != null
          && !identifier.equals(res.getString("identifier")))) {
        throw new IllegalArgumentException(
            "Row retrieved doesn't correspond to object.");
      }
      
      // Get all the properties, if a table exists
      if (props != null) {
        for (Entry<String, Class<?>> ent : props.entrySet()) {
          Class<?> cl = ent.getValue();
          String key = ent.getKey();
          
          if (cl.equals(String.class)) {
            properties.put(key, res.getString(key));
          } else {
            int i = res.getInt(key);
            if (res.wasNull()) {
              properties.put(key, null);
            } else {
              if (cl.equals(Integer.class) || cl.equals(int.class)) {
                properties.put(key, i);
              } else if (cl.equals(Boolean.class)
                  || cl.equals(boolean.class)) {
                properties.put(key, i == 1);
              } else if (DBObject.class.isAssignableFrom(cl)) {
                Class<? extends DBObject> cldb = (Class<? extends DBObject>) cl;
                properties.put(key, DBObjectReader.getShallow(cldb, i));
              }
            }
          }
        }
      }
    } catch (SQLException ex) {
      throw new DBException(ex);
    }
    
    return true;
  }
  
  protected DBObject complete() {
    if (!isComplete) {
      DBObjectReader.completes(this);
    }
    return this;
  }
  
  /**
   * Creates a lang table - a table that maps languages to strings - and
   * stores it in the object's {@link #langTables} map by name
   * "(table).(column)".
   * 
   * @param tableName
   *   The name of the table to load.
   * @param objectColumn
   *   The name of the column referencing the object.
   * @param nameColumn
   *   The name of the text column.
   * @return The language table.
   */
  protected HashMap<Language, String> getLangTable(String tableName,
      String objectColumn, String nameColumn) {
    LangStatements lns = DBConnection.prepareLangStatements(tableName,
        objectColumn, nameColumn);
    ResultSet res = lns.getObjectNames(id);
    
    HashMap<Language, String> out = new HashMap<>();
    try {
      while (res.next()) {
        String str = res.getString(nameColumn);
        Language lang = DBObjectReader.getShallow(Language.class,
            res.getInt("local_language_id"));
        out.put(lang, str);
      }
    } catch (SQLException e) {
      throw new DBException(e);
    }
    
    langTables.put(tableName + "." + nameColumn, out);
    
    return out;
  }
  
  /**
   * Gets a single-language value from one language table.
   * 
   * @param table
   *   The table from which to retrieve a value
   * @param lang
   *   The language of value to retrieve
   * @return The value in that language, or <tt>null</tt> if there's no
   * value in that language.
   * @throws NullPointerException
   *   If there's no table by that name
   */
  public String getLanguageValue(String table, Language lang) {
    return langTables.get(table).get(lang);
  }
  
  /**
   * Gets all the values, and associated languages, from one language
   * table.
   * 
   * @param table
   *   The table of values to retrieve
   * @return An unmodifiable view of the map of languages to values
   */
  public Map<Language, String> getLanguageTable(String table) {
    return Collections.unmodifiableMap(langTables.get(table));
  }
  
  public String toString() {
    return identifier;
  }
}
