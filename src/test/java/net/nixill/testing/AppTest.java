package net.nixill.testing;

import java.util.List;
import java.util.Map.Entry;

import net.nixill.pokemon.database.DBConnection;
import net.nixill.pokemon.objects.EvolutionChain;
import net.nixill.pokemon.objects.Generation;
import net.nixill.pokemon.objects.Language;
import net.nixill.pokemon.objects.Pokemon;
import net.nixill.pokemon.objects.PokemonHabitat;
import net.nixill.pokemon.objects.PokemonSpecies;
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
    // speciesTest((int) (Math.random() * 807));
    // evolutionTest((int) (Math.random() * 807));
    evolutionTest2((int) (Math.random() * 807));
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
  
  public static void speciesTest(int id) {
    PokemonSpecies species = DBObjectReader.get(PokemonSpecies.class, id);
    
    System.out.println("Identifier: " + species.getIdentifier());
    System.out.println("Name in English: " + species.getName(ENGLISH));
    System.out.println("Genus: " + species.getGenus(ENGLISH));
    System.out.println("Color: " + species.getColor().getName(ENGLISH));
    PokemonHabitat hab = species.getHabitat();
    if (hab != null) {
      System.out.println("Habitat: " + hab.getName(ENGLISH));
    } else {
      System.out.println("Habitat: null");
    }
  }
  
  public static void evolutionTest(int id) {
    PokemonSpecies species = DBObjectReader.get(PokemonSpecies.class, id);
    
    System.out.println("Selected Pokémon: " + species.getName(ENGLISH));
    
    EvolutionChain evo = species.getEvolutionChain();
    
    System.out.println("Evolution chain id: " + evo.getId());
    System.out
        .println("Baby trigger item id: " + evo.getBabyTriggerItemId());
    System.out.println("Contains:");
    
    for (PokemonSpecies spec : evo.getMembers()) {
      System.out.println(
          "  " + spec.getName(ENGLISH) + " (" + spec.getId() + ")");
    }
  }
  
  public static void evolutionTest2(int id) {
    Pokemon pokemon = DBObjectReader.get(Pokemon.class, id);
    
    System.out.println("Selected Pokémon: " + pokemon.getIdentifier());
    
    Pokemon preEvo = pokemon.getPreEvolvedForm();
    
    if (preEvo != null) {
      System.out.println("Evolves from: " + preEvo.getIdentifier());
    } else {
      System.out.println("Has no pre-evolution.");
    }
    
    List<Pokemon> postEvos = pokemon.getEvolvedForms();
    
    if (!postEvos.isEmpty()) {
      System.out.println("Evolves to:");
      for (Pokemon postEvo : postEvos) {
        System.out.println("  " + postEvo.getIdentifier());
      }
    } else {
      System.out.println("Has no post-evolutions.");
    }
  }
}
