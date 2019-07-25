/*
 * Copyright (c) 2019. This is property of the Patrone Network and it's corresponding entities. This code may not be re-distributed in any way shape or form.
 */

package me.wolflie.simplehome.command.adapter;

import com.google.common.collect.Lists;
import me.wolflie.simplehome.command.annotation.Single;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

public class StringTypeAdapter implements TypeAdapter<String> {

    @Override
    public Optional<String> parse(Parameter[] parameters, String[] args, Parameter currentParameter, String currentArgument) {
        if (currentArgument.isEmpty()) {
            return Optional.empty();
        }
        if (ArrayUtils.indexOf(parameters, currentParameter) == parameters.length - 1 && !currentParameter.isAnnotationPresent(Single.class)) {
            StringBuilder builder = new StringBuilder();
            for (int i = ArrayUtils.indexOf(args, currentArgument); i < args.length; i++) {
                builder.append(args[i]);
                if (i != args.length - 1)
                    builder.append(" ");
            }
            return Optional.of(builder.toString());
        }
        return Optional.of(currentArgument);
    }

    @Override
    public void onError(CommandSender executor, String argument) {
        executor.sendMessage(ChatColor.RED + "Provide a string argument.");
    }

    @Override
    public List<String> tabComplete(String[] args, String currentArgument) {
        return Lists.newArrayList("text");
    }
}
