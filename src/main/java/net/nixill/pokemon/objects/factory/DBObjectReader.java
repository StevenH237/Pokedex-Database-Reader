package net.nixill.pokemon.objects.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
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
  
  private Class<O>            cls;
  private Constructor<O>      cnstr;
  private String              tableName;
  private HashMap<Integer, O> idPool;
  private HashMap<String, O>  identPool;
  
  private IDStatements idStatements;
  
  static <O extends DBObject> boolean initReader(Class<O> cls,
      String name) {
    if (!readers.containsKey(cls)) {
      new DBObjectReader<>(name, cls);
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
   */
  private DBObjectReader(String singular, Class<O> classRef) {
    tableName = singular;
    cls = classRef;
    try {
      cnstr = cls.getDeclaredConstructor(int.class, String.class);
    } catch (NoSuchMethodException | SecurityException e) {
      throw (NullPointerException) (new NullPointerException()
          .initCause(e));
    }
    
    idStatements = DBConnection.prepareIDStatements(tableName);
    
    ResultSet res = idStatements.getAllIDs();
    
    idPool = new HashMap<>();
    identPool = new HashMap<>();
    
    // Index all the objects we have
    try {
      cnstr.setAccessible(true);
      while (res.next()) {
        int id = res.getInt("id");
        String identifier = res.getString("identifier");
        
        O obj = cnstr.newInstance(id, identifier);
        
        idPool.put(id, obj);
        identPool.put(identifier, obj);
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
   */
  public O get(String identifier) {
    O obj = identPool.get(identifier);
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
}