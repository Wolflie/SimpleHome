package me.wolflie.simplehome.util;

import me.wolflie.simplehome.command.CommandHandler;
import me.wolflie.simplehome.command.adapter.*;
import org.bukkit.entity.Player;

public class TypeUtils {

    public static void registerBasicTypeAdapters(CommandHandler commandHandler) {

        IntegerTypeAdapter intAdapter = new IntegerTypeAdapter();
        DoubleTypeAdapter doubleAdapter = new DoubleTypeAdapter();
        BooleanTypeAdapter boolAdapter = new BooleanTypeAdapter();

        //Primitives
        commandHandler.registerTypeAdapter(boolean.class, boolAdapter);
        commandHandler.registerTypeAdapter(int.class, intAdapter);
        commandHandler.registerTypeAdapter(double.class, doubleAdapter);

        //Boxed primitives
        commandHandler.registerTypeAdapter(Boolean.class, boolAdapter);
        commandHandler.registerTypeAdapter(String.class, new StringTypeAdapter());
        commandHandler.registerTypeAdapter(Integer.class, intAdapter);
        commandHandler.registerTypeAdapter(Double.class, doubleAdapter);

        //Objects
        commandHandler.registerTypeAdapter(Player.class, new PlayerTypeAdapter());
    }

}
