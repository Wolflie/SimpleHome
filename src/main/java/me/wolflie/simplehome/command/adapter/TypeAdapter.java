/*
 * Copyright (c) 2019. This is property of the Patrone Network and it's corresponding entities. This code may not be re-distributed in any way shape or form.
 */

package me.wolflie.simplehome.command.adapter;

import org.bukkit.command.CommandSender;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

public interface TypeAdapter<V> {

    Optional<V> parse(Parameter[] parameters, String[] args, Parameter currentParameter, String currentArgument);

    void onError(CommandSender executor, String argument);

    List<String> tabComplete(String[] args, String currentArgument);

}
