/*
 * Copyright (c) 2019. This is property of the Patrone Network and it's corresponding entities. This code may not be re-distributed in any way shape or form.
 */

package me.wolflie.simplehome.command.adapter;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

public class IntegerTypeAdapter implements TypeAdapter<Integer> {

    @Override
    public Optional<Integer> parse(Parameter[] parameters, String[] args, Parameter currentParameter, String currentArgument) {
        try {
            return Optional.of(Integer.parseInt(currentArgument));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Override
    public void onError(CommandSender executor, String argument) {
        executor.sendMessage(ChatColor.RED + "That isn't a number.");
    }

    @Override
    public List<String> tabComplete(String[] args, String currentArgument) {
        return Lists.newArrayList("1", "2", "3", "4", "5");
    }
}
