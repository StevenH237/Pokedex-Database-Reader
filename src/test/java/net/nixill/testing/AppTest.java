package net.nixill.testing;

import java.util.Map.Entry;

import net.nixill.pokemon.database.DBConnection;
import net.nixill.pokemon.objects.Generation;
import net.nixill.pokemon.objects.Language;
import net.nixill.pokemon.objects.factory.DBObjectReader;
import net.nixill.pokemon.objects.factory.DBObjectReaders;

/**
 * Use a simple program to test it
 */
public class AppTest {
  private static Language ENGLISH;
  
  public static void main(String[] args) {
    DBConnection.init();
    DBObjectReaders.init();
    
    DBObjectReader<Language> langReader = DBObjectReader
        .getReader(Language.class);
    
    ENGLISH = langReader.getShallow("en");
    
    // langTest(langReader);
    // genTest();
  }
  
  public static void langTest(DBObjectReader<Language> langReader) {
    // Let's get details on all the supported languages
    // Get their names in English and in their own language
    for (Entry<Integer, Language> entry : langReader.getAll().entrySet()) {
      Language lang = entry.getValue();
      String nameInEng = lang.getName(ENGLISH);
      String nameInLang = lang.getName(lang);
      if (nameInLang == null) {
        nameInLang = "(no " + nameInEng + " name)";
      }
      System.out.println(nameInEng + "/" + nameInLang);
    }
  }
  
  public static void genTest() {
    DBObjectReader<Generation> genReader = DBObjectReader
        .getReader(Generation.class);
    
    for (Entry<Integer, Generation> genEntry : genReader.getAll()
        .entrySet()) {
      Generation gen = genEntry.getValue();
      System.out.println("Information on " + gen.getName(ENGLISH));
      System.out
          .println("Main region: " + gen.getMainRegion().getName(ENGLISH));
      System.out.println();
    }
  }
  
  /*
   * public static void main(String[] args) { DBConnection.init();
   * 
   * ENGLISH = Language.getByIdentifier("en");
   * 
   * // Let's get details on Pokémon #133 Pokemon pkmn = Pokemon.get(133);
   * System.out.println("Information on Pokémon 133:");
   * System.out.println("  Identifier: " + pkmn.getIdentifier());
   * System.out.println("  Height: " + pkmn.getHeight() * 0.1 + " m");
   * System.out.println("  Weight: " + pkmn.getWeight() * 0.1 + " kg");
   * System.out .println("  Base experience yield: " +
   * pkmn.getBaseExperience());
   * 
   * System.out.println("Information on its abilities:");
   * 
   * PokemonSpecies species = pkmn.getSpecies(); System.out.println("");
   * System.out.println("Information on its species:");
   * System.out.println("  ID: " + species.getId());
   * System.out.println("  Identifier: " + species.getIdentifier());
   * System.out.println("  Name in English: " + species.getName(ENGLISH));
   * System.out.println("  Genus: " + species.getGenus(ENGLISH));
   * System.out.println("  Color: " + species.getColor().getName(ENGLISH));
   * }
   */
}
