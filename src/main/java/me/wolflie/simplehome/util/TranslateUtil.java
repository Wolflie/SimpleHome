package me.wolflie.simplehome.util;


import com.google.common.collect.Lists;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TranslateUtil {

    public static final String LINE = "--------------------------------";
    private static final char ALTERNATE_COLOR_CHAR = '&';

    public static String translate(String input) {
        return ChatColor.translateAlternateColorCodes(ALTERNATE_COLOR_CHAR, input);
    }

    public static Collection<String> translateCollection(Collection<String> collection) {
        List<String> list = Lists.newArrayList(collection);
        list.replaceAll(TranslateUtil::translate);
        return list;
    }

    public static String[] translate(String... inputs) {
        return translateCollection(Arrays.asList(inputs)).toArray(new String[0]);
    }

    public static String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

}