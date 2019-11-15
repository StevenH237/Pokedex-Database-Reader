package net.nixill.pokemon.objects.factory;

import net.nixill.pokemon.objects.DBObject;
import net.nixill.pokemon.objects.EvolutionChain;
import net.nixill.pokemon.objects.Generation;
import net.nixill.pokemon.objects.GrowthRate;
import net.nixill.pokemon.objects.Language;
import net.nixill.pokemon.objects.PokemonColor;
import net.nixill.pokemon.objects.PokemonHabitat;
import net.nixill.pokemon.objects.PokemonShape;
import net.nixill.pokemon.objects.PokemonSpecies;
import net.nixill.pokemon.objects.Region;

public class DBObjectReaders {
  private static boolean initialized;
  
  private static <O extends DBObject> void init(Class<O> cls,
      String name) {
    DBObjectReader.initReader(cls, name, true);
  }
  
  private static <O extends DBObject> void init(Class<O> cls, String name,
      boolean identifiers) {
    DBObjectReader.initReader(cls, name, identifiers);
  }
  
  public static void init() {
    if (!initialized) {
      init(Language.class, "languages"); // needs to be first
      // init(Ability.class, "abilities");
      // init(AbilityChange.class, "ability_changelog", false);
      // init(Berry.class, "berries", false);
      // init(BerryFirmness.class, "berry_firmness");
      // init(Characteristic.class, "characteristics", false);
      // init(ContestEffect.class, "contest_effects", false);
      // init(ContestType.class, "contest_types");
      // init(EggGroup.class, "egg_groups");
      // init(EncounterConditionValue.class, "encounter_condition_values");
      // init(EncounterCondition.class, "encounter_conditions");
      // init(EncounterMethod.class, "encounter_methods");
      // init(EncounterSlot.class, "encounter_slots", false);
      // init(Encounter.class, "encounters", false);
      init(EvolutionChain.class, "evolution_chains", false);
      // init(EvolutionTrigger.class, "evolution_triggers");
      // init(GameSeries.class, "game_series");
      // init(Gender.class, "genders");
      init(Generation.class, "generations");
      init(GrowthRate.class, "growth_rates");
      // init(ItemCategory.class, "item_categories");
      // init(ItemFlag.class, "item_flags");
      // init(ItemFlingEffect.class, "item_fling_effects");
      // init(ItemPocket.class, "item_pockets");
      // init(Item.class, "items");
      // init(LocationArea.class, "location_areas");
      // init(LocationFlag.class, "location_flags");
      // init(Location.class, "locations");
      // init(MoveBattleStyle.class, "move_battle_styles");
      // init(MoveDamageClass.class, "move_damage_classes");
      // init(MoveEffectChange.class, "move_effect_changelog", false);
      // init(MoveEffect.class, "move_effect", false);
      // init(MoveFlag.class, "move_flags");
      // init(MoveMetaAilment.class, "move_meta_ailments");
      // init(MoveMetaCategory.class, "move_meta_categories");
      // init(MoveTarget.class, "move_targets");
      // init(Move.class, "moves");
      // init(Nature.class, "natures");
      // init(PalParkArea.class, "pal_park_areas");
      // init(PokeathlonStat.class, "pokeathlon_stats");
      // init(Pokedex, "pokedexes");
      init(PokemonColor.class, "pokemon_colors");
      // init(PokemonForm.class, "pokemon_forms");
      init(PokemonHabitat.class, "pokemon_habitats");
      // init(PokemonMoveMethod.class, "pokemon_move_methods");
      init(PokemonShape.class, "pokemon_shapes");
      init(PokemonSpecies.class, "pokemon_species");
      // init(PokemonType.class, "types");
      // init(Pokemon.class, "pokemon");
      init(Region.class, "regions");
      // init(StarterPokemon.class, "starter_pokemon", false);
      // init(StarterPokemonGroup.class, "starter_pokemon_groups");
      // init(Stat.class, "stats");
      // init(SuperContestEffect.class, "super_contest_effects", false);
      // init(VersionGroup.class, "version_groups");
      // init(Version.class, "versions");
      
      initialized = true;
    }
  }
}
