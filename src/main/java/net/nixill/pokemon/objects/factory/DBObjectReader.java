package net.nixill.pokemon.objects.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.nixill.pokemon.database.DBConnection;
import net.nixill.pokemon.database.DBException;
import net.nixill.pokemon.database.IDStatements;
import net.nixill.pokemon.objects.DBObject;

/**
 * A factory class that reads objects out of a database.
 * <p>
 * All such read objects are immutable and must have at minimum an integer
 * id and a text identifier.
 * 
 * @param <O>
 *   The type of objects being read.
 */
@SuppressWarnings("unchecked")
public class DBObjectReader<O extends DBObject> {
  private static HashMap<Class<?>, DBObjectReader<?>> readers = new HashMap<>();
  
  private Class<O> cls;
  private Constructor<O> cnstr;
  private String tableName;
  private HashMap<Integer, O> idPool;
  private HashMap<String, O> identPool;
  
  private IDStatements idStatements;
  
  static <O extends DBObject> boolean initReader(Class<O> cls, String name,
      boolean identifiers) {
    if (!readers.containsKey(cls)) {
      new DBObjectReader<>(name, cls, identifiers);
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * Retrieves a DBObjectReader by its type.
   * 
   * @param cls
   *   The type of Reader to return.
   * @return The DBObjectReader in question.
   */
  public static <O extends DBObject> DBObjectReader<O> getReader(
      Class<O> cls) {
    return (DBObjectReader<O>) readers.get(cls);
  }
  
  /**
   * Reads and returns an object by its type and ID.
   * <p>
   * If the object is incomplete (doesn't have its properties loaded from
   * the database yet), this method completes it.
   * 
   * @param cls
   *   The type of object to get.
   * @param id
   *   The id of the object to get.
   * @return The object of that type and ID.
   */
  public static <O extends DBObject> O get(Class<O> cls, int id) {
    DBObjectReader<O> reader = getReader(cls);
    O obj = reader.get(id);
    obj.complete(reader.idStatements.getByID(id));
    return obj;
  }
  
  /**
   * Reads and returns an object by its type and ID.
   * <p>
   * If the object is incomplete (doesn't have its properties loaded from
   * the database yet), this method leaves it as-is. The
   * {@link DBObject#complete()} method can be used to complete it.
   * 
   * @param cls
   *   The type of object to get.
   * @param id
   *   The id of the object to get.
   * @return The object of that type and ID.
   */
  public static <O extends DBObject> O getShallow(Class<O> cls, int id) {
    return getReader(cls).getShallow(id);
  }
  
  /**
   * Reads and returns an object by its type and identifier.
   * <p>
   * If the object is incomplete (doesn't have its properties loaded from
   * the database yet), this method completes it.
   * 
   * @param cls
   *   The type of object to get.
   * @param identifier
   *   The identifier of the object to get.
   * @return The object of that type and identifier.
   * @throws NullPointerException
   *   if the object doesn't use textual identifiers.
   */
  public static <O extends DBObject> O get(Class<O> cls,
      String identifier) {
    DBObjectReader<O> reader = getReader(cls);
    O obj = reader.get(identifier);
    obj.complete(reader.idStatements.getByIdentifier(identifier));
    return obj;
  }
  
  /**
   * Reads and returns an object by its type and ID.
   * <p>
   * If the object is incomplete (doesn't have its properties loaded from
   * the database yet), this method leaves it as-is. The
   * {@link DBObject#complete()} method can be used to complete it.
   * 
   * @param cls
   *   The type of object to get.
   * @param id
   *   The id of the object to get.
   * @return The object of that type and ID.
   * @throws NullPointerException
   *   if the object doesn't use textual identifiers.
   */
  public static <O extends DBObject> O getShallow(Class<O> cls,
      String identifier) {
    return getReader(cls).get(identifier);
  }
  
  /**
   * Creates a new DBObjectReader.
   * 
   * @param singular
   *   The singular name of the base table.
   * @param classRef
   *   The class of the object the reader produces.
   * @param identifiers
   *   Whether or not objects of this class have identifiers.
   */
  private DBObjectReader(String singular, Class<O> classRef,
      boolean identifiers) {
    tableName = singular;
    cls = classRef;
    try {
      if (identifiers) {
        cnstr = cls.getDeclaredConstructor(int.class, String.class);
      } else {
        cnstr = cls.getDeclaredConstructor(int.class);
      }
    } catch (NoSuchMethodException | SecurityException e) {
      throw (NullPointerException) (new NullPointerException()
          .initCause(e));
    }
    
    idStatements = DBConnection.prepareIDStatements(tableName,
        identifiers);
    
    ResultSet res = idStatements.getAllIDs();
    
    idPool = new HashMap<>();
    if (identifiers) {
      identPool = new HashMap<>();
    } else {
      identPool = null;
    }
    
    // Index all the objects we have
    try {
      cnstr.setAccessible(true);
      while (res.next()) {
        int id = res.getInt("id");
        O obj;
        
        if (identifiers) {
          String identifier = res.getString("identifier");
          obj = cnstr.newInstance(id, identifier);
          identPool.put(identifier, obj);
        } else {
          obj = cnstr.newInstance(id);
        }
        
        idPool.put(id, obj);
      }
      cnstr.setAccessible(false);
      res.close();
    } catch (SQLException ex) {
      throw new DBException(ex);
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    
    readers.put(cls, this);
  }
  
  /**
   * Gets an object by its numeric internal ID.
   * <p>
   * If the object is incomplete (doesn't have its properties loaded from
   * the database yet), this method completes it.
   * 
   * @param id
   *   The internal ID.
   * @return The object in question.
   */
  public O get(int id) {
    O obj = idPool.get(id);
    obj.complete(idStatements.getByID(id));
    return obj;
  }
  
  /**
   * Gets an object by its numeric internal ID.
   * <p>
   * If the object is incomplete (doesn't have its properties loaded from
   * the database yet), this method leaves it as-is. The
   * {@link DBObject#complete()} method can be used to complete it.
   * 
   * @param id
   *   The internal ID.
   * @return The object in question.
   */
  public O getShallow(int id) {
    return idPool.get(id);
  }
  
  /**
   * Gets an object by its textual identifier.
   * <p>
   * If the object is incomplete (doesn't have its properties loaded from
   * the database yet), this method completes it.
   * 
   * @param identifier
   *   The textual identifier.
   * @return The object in question.
   * @throws NullPointerException
   *   if the object doesn't use textual identifiers.
   */
  public O get(String identifier) {
    O obj = getShallow(identifier);
    obj.complete(idStatements.getByIdentifier(identifier));
    return obj;
  }
  
  /**
   * Gets an object by its textual identifier.
   * <p>
   * If the object is incomplete (doesn't have its properties loaded from
   * the database yet), this method leaves it as-is. The
   * {@link DBObject#complete()} method can be used to complete it.
   * 
   * @param identifier
   *   The textual identifier.
   * @return The Pokemon in question.
   * @throws NullPointerException
   *   if the object doesn't use textual identifiers.
   */
  public O getShallow(String identifier) {
    return identPool.get(identifier);
  }
  
  /**
   * Returns an unmodifiable view of the ID-to-object pool.
   * <p>
   * For all incomplete objects (don't have their properties loaded from
   * the database yet), this method completes them.
   * 
   * @return As above
   */
  public Map<Integer, O> getAll() {
    for (Entry<Integer, O> ent : idPool.entrySet()) {
      O val = ent.getValue();
      if (!val.isComplete()) {
        val.complete(idStatements.getByID(val.getId()));
      }
    }
    return Collections.unmodifiableMap(idPool);
  }
  
  /**
   * Returns an unmodifiable view of the ID-to-object pool.
   * <p>
   * For all incomplete objects (don't have their properties loaded from
   * the database yet), this method completes them.
   * 
   * @return As above
   */
  public Map<Integer, O> getAllShallow() {
    return Collections.unmodifiableMap(idPool);
  }
  
  public static <O extends DBObject> void completes(O obj) {
    ((DBObjectReader<O>) getReader(obj.getClass())).complete(obj);
  }
  
  public void complete(O obj) {
    obj.complete(idStatements.getByID(obj.getId()));
  }
  
  /**
   * Returns the first result of the query.
   * <p>
   * For the method to work properly, the first column of the output should
   * either be the id column of this object's table, or a column
   * referencing that.
   * 
   * @param sql
   *   The statement to execute
   * @return The object returned, or <tt>null</tt> if no object was
   * returned.
   */
  public O resultOf(String sql) {
    try {
      ResultSet res = DBConnection.query(sql);
      if (res.next()) {
        int id = res.getInt(1);
        res.close();
        return get(id);
      } else {
        res.close();
        return null;
      }
    } catch (SQLException ex) {
      throw new DBException(ex);
    }
  }
  
  /**
   * Returns the results of the query.
   * <p>
   * For the method to work properly, the first column of the output should
   * either be the id column of this object's table, or a column
   * referencing that.
   * 
   * @param sql
   *   The statement to execute
   * @return The objects returned, or an empty list if no objects were
   * returned.
   */
  public List<O> resultsOf(String sql) {
    try {
      ResultSet res = DBConnection.query(sql);
      List<O> out = new ArrayList<>();
      while (res.next()) {
        int id = res.getInt(1);
        out.add(get(id));
      }
      res.close();
      return Collections.unmodifiableList(out);
    } catch (SQLException ex) {
      throw new DBException(ex);
    }
  }
}
