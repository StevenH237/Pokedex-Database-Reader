package net.nixill.pokemon.objects.factory;

import net.nixill.pokemon.objects.Generation;
import net.nixill.pokemon.objects.Language;
import net.nixill.pokemon.objects.Region;

public class DBObjectReaders {
  private static boolean initialized;
  
  public static void init() {
    if (!initialized) {
      DBObjectReader.initReader(Generation.class, "generations");
      DBObjectReader.initReader(Language.class, "languages");
      DBObjectReader.initReader(Region.class, "regions");
      
      initialized = true;
    }
  }
}