package net.nixill.pokemon.objects;

import java.sql.ResultSet;
import java.util.HashMap;

import lombok.Getter;

public class PokemonSpecies extends DBObject {
    private static HashMap<String, Class<?>> types;
    
    private HashMap<Language, String> names;
    private HashMap<Language, String> genera;
    private HashMap<Language, String> formDescriptions;
    
    @Getter(lazy = true)
    private final Generation     generation         = getProperty(
            "generation_id", Generation.class);
    @Getter(lazy = true)
    private final EvolutionChain evolutionChain     = getProperty(
            "evolution_chain_id", EvolutionChain.class);
    @Getter(lazy = true)
    private final PokemonColor   color              = getProperty(
            "color_id", PokemonColor.class);
    @Getter(lazy = true)
    private final PokemonShape   shape              = getProperty(
            "shape_id", PokemonShape.class);
    @Getter(lazy = true)
    private final PokemonHabitat habitat            = getProperty(
            "habitat_id", PokemonHabitat.class);
    @Getter(lazy = true)
    private final int            genderRate         = getProperty(
            "gender_rate", int.class);
    @Getter(lazy = true)
    private final int            captureRate        = getProperty(
            "capture_rate", int.class);
    @Getter(lazy = true)
    private final int            baseHappiness      = getProperty(
            "base_happiness", int.class);
    @Getter(lazy = true)
    private final boolean        baby               = getProperty(
            "is_baby", boolean.class);
    @Getter(lazy = true)
    private final int            hatchCounter       = getProperty(
            "hatch_counter", int.class);
    @Getter(lazy = true)
    private final boolean        differentPerGender = getProperty(
            "has_gender_differences", boolean.class);
    @Getter(lazy = true)
    private final GrowthRate     growthRate         = getProperty(
            "growth_rate_id", GrowthRate.class);
    @Getter(lazy = true)
    private final boolean        formSwitchable     = getProperty(
            "forms_switchable", boolean.class);
    @Getter(lazy = true)
    private final int            order              = getProperty("order",
            int.class);
    
    static {
        types = new HashMap<>();
        types.put("generation_id", Generation.class);
        types.put("evolution_chain_id", EvolutionChain.class);
        types.put("color_id", PokemonColor.class);
        types.put("shape_id", PokemonShape.class);
        types.put("habitat_id", PokemonHabitat.class);
        types.put("gender_rate", Integer.class);
        types.put("capture_rate", Integer.class);
        types.put("base_happiness", Integer.class);
        types.put("is_baby", Boolean.class);
        types.put("hatch_counter", Integer.class);
        types.put("has_gender_differences", Boolean.class);
        types.put("growth_rate_id", GrowthRate.class);
        types.put("forms_switchable", Boolean.class);
        types.put("order", Integer.class);
    }
    
    public PokemonSpecies(int id, String identifier) {
        super(id, identifier);
    }
    
    @Override
    public void complete(ResultSet res) {
        if (complete(res, types)) {
            names = getLangTable("pokemon_species_names",
                    "pokemon_species_id", "name");
            genera = getLangTable("pokemon_species_names",
                    "pokemon_species_id", "genus");
            formDescriptions = getLangTable("pokemon_species_prose",
                    "pokemon_species_id", "form_description");
        }
    }
    
    public String getName(Language lang) {
        return names.get(lang);
    }
    
    public String getGenus(Language lang) {
        return genera.get(lang);
    }
    
    public String getFormDescription(Language lang) {
        return formDescriptions.get(lang);
    }
}
