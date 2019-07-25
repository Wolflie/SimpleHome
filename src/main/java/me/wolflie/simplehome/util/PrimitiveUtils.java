package me.wolflie.simplehome.util;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PrimitiveUtils {

    public static final Map<String, Boolean> POSSIBLE_BOOLEAN_COMBINATIONS = new HashMap<>();

    static {
        POSSIBLE_BOOLEAN_COMBINATIONS.put("yes", true);
        POSSIBLE_BOOLEAN_COMBINATIONS.put("no", false);
        POSSIBLE_BOOLEAN_COMBINATIONS.put("true", true);
        POSSIBLE_BOOLEAN_COMBINATIONS.put("false", false);
        POSSIBLE_BOOLEAN_COMBINATIONS.put("1", true);
        POSSIBLE_BOOLEAN_COMBINATIONS.put("0", false);
    }

    public static Optional<Boolean> parseBoolean(String string) {
        if (!POSSIBLE_BOOLEAN_COMBINATIONS.containsKey(string.toLowerCase()))
            return Optional.empty();
        return Optional.of(POSSIBLE_BOOLEAN_COMBINATIONS.get(string.toLowerCase()));
    }
}