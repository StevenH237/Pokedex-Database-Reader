package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.util.HashMap;

import lombok.Getter;
import net.nixill.pokemon.objects.factory.DBObjectReader;

/**
 * A <tt>Language</tt> object represents an example of a system that
 * consists of the development, acquisition, maintenance and use of complex
 * systems of communication, particularly the human ability to do so.
 * <p>
 * (Description copied from Wikipedia)
 */
public class Language extends DBObject {
  private static HashMap<String, Class<?>> types;
  static {
    types = new HashMap<>();
    types.put("iso639", String.class);
    types.put("iso3166", String.class);
    types.put("official", boolean.class);
    types.put("order", int.class);
  }
  
  /** The ISO 639-compliant identifier of the language. */
  @Getter(lazy = true) private final String iso639 = (String) getProperty(
      "iso639");
  /** The ISO 3166-compliant identifier of the language. */
  @Getter(lazy = true) private final String iso3166 = (String) getProperty(
      "iso3166");
  /** Whether or not a language is used in official Pokémon media. */
  @Getter(
    lazy = true) private final boolean official = (Boolean) getProperty(
        "official");
  /** The sorting order of the languages. */
  @Getter(
    lazy = true) private final int order = (Integer) getProperty("order");
  
  /** A shortcut to <tt>DBObjectReader.get(Language.class, "en")</tt>. */
  @Getter(
    lazy = true) private final static Language english = DBObjectReader
        .get(Language.class, "en");
  
  private HashMap<Language, String> names;
  
  private Language(int id, String identifier) {
    super(id, identifier);
  }
  
  public void complete(ResultSet res) {
    if (complete(res, types)) {
      names = getLangTable("language_names", "language_id", "name");
    }
  }
  
  /**
   * Returns the name of this Language in a given Language.
   * <p>
   * Language data is incomplete, and <tt>null</tt> may be returned.
   * 
   * @param lang
   *   The language to name this language in.
   * @return The name of this language in that language.
   */
  public String getName(Language lang) {
    complete();
    return names.get(lang);
  }
}
