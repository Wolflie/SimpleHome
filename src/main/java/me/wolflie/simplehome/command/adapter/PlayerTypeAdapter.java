/*
 * Copyright (c) 2019. This is property of the Patrone Network and it's corresponding entities. This code may not be re-distributed in any way shape or form.
 */

package me.wolflie.simplehome.command.adapter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerTypeAdapter implements TypeAdapter<Player> {

    @Override
    public Optional<Player> parse(Parameter[] parameters, String[] args, Parameter currentParameter, String currentArgument) {
        Player player = Bukkit.getPlayer(currentArgument);
        if (player == null) return Optional.empty();
        return Optional.of(player);
    }

    @Override
    public void onError(CommandSender executor, String argument) {
        executor.sendMessage(ChatColor.RED + "Couldn't find player by name: " + argument);
    }

    @Override
    public List<String> tabComplete(String[] args, String currentArgument) {
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .filter(name -> currentArgument.startsWith(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}
