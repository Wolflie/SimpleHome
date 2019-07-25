/*
 * Copyright (c) 2019. This is property of the Patrone Network and it's corresponding entities. This code may not be re-distributed in any way shape or form.
 */

package me.wolflie.simplehome.command;

import me.wolflie.simplehome.command.annotation.CommandInfo;
import me.wolflie.simplehome.command.annotation.Default;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import static me.wolflie.simplehome.util.TranslateUtil.translate;

public class BaseCommand extends Command {

    private final CommandHandler commandHandler;

    private final Object command;
    private final CommandInfo info;
    private final Method executeMethod;
    private final Map<CommandInfo, Method> subCommands;

    protected BaseCommand(CommandHandler handler, Object command) {
        super(command.getClass().getAnnotation(CommandInfo.class).name());

        this.info = command.getClass().getAnnotation(CommandInfo.class);
        this.commandHandler = handler;
        this.command = command;

        this.setAliases(Arrays.asList(info.aliases()));

        this.subCommands = CommandUtil.getInfoToMethodMapping(command);
        this.executeMethod = Arrays
                .stream(command.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(Default.class))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {

        if (!info.permission().isEmpty() && !sender.hasPermission(info.permission())) {
            sender.sendMessage(translate(info.noPermissionMessage()));
            return true;
        }

        if (args.length == 0) {
            if (executeMethod == null) {
                sender.sendMessage(translate(info.usage()));
                return true;
            }
            try {
                CommandUtil.executeCommand(commandHandler, sender, command, info, executeMethod, args);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return true;
        }
        if (args.length >= 1) {
            for (Map.Entry<CommandInfo, Method> entry : subCommands.entrySet()) {
                CommandInfo info = entry.getKey();
                if (ArrayUtils.contains(info.aliases(), args[0]) || args[0].equalsIgnoreCase(info.name())) {
                    try {
                        CommandUtil.executeCommand(commandHandler, sender, command, info, entry.getValue(), Arrays.copyOfRange(args, 1, args.length));
                        return true;
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                        return true;
                    }
                }
            }
            try {
                CommandUtil.executeCommand(commandHandler, sender, command, info, executeMethod, args);
            } catch (InvocationTargetException | IllegalAccessException e) {
                sender.sendMessage(translate(info.usage()));
            }
            return true;
        }
        return true;
    }
}