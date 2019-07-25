/*
 * Copyright (c) 2019. This is property of the Patrone Network and it's corresponding entities. This code may not be re-distributed in any way shape or form.
 */

package me.wolflie.simplehome.command;

import me.wolflie.simplehome.command.adapter.TypeAdapter;
import me.wolflie.simplehome.command.annotation.CommandInfo;
import me.wolflie.simplehome.command.annotation.OptionalParameter;
import me.wolflie.simplehome.command.annotation.ParameterType;
import me.wolflie.simplehome.command.annotation.sender.ConsoleOnly;
import me.wolflie.simplehome.command.annotation.sender.PlayerOnly;
import me.wolflie.simplehome.util.TranslateUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class CommandUtil {

    public static Map<CommandInfo, Method> getInfoToMethodMapping(Object object) {
        Map<CommandInfo, Method> methods = new HashMap<>();
        Arrays.stream(object.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(CommandInfo.class))
                .forEach(method -> {
                    CommandInfo info = method.getAnnotation(CommandInfo.class);
                    methods.put(info, method);
                });
        return methods;
    }

    public static void executeCommand(CommandHandler handler, CommandSender sender, Object object, CommandInfo info, Method method, String[] args) throws InvocationTargetException, IllegalAccessException {

        if (method == null) {
            sender.sendMessage(TranslateUtil.translate(info.usage()));
            return;
        }

        //sender and permission check start.
        int player = 0;
        if (method.isAnnotationPresent(PlayerOnly.class))
            player = 1;

        if (method.isAnnotationPresent(ConsoleOnly.class))
            player = -1;

        if (!info.permission().isEmpty() && !sender.hasPermission(info.permission())) {
            sender.sendMessage(TranslateUtil.translate(info.noPermissionMessage()));
            return;
        }

        if (player == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(TranslateUtil.translate("&cPlayer only command."));
                return;
            }
        }

        if (player == -1) {
            if (!(sender instanceof ConsoleCommandSender)) {
                sender.sendMessage(TranslateUtil.translate("&cConsole only command."));
                return;
            }
        }

        //sender and permission check end.
        if (method.getParameters().length == 0) {
            method.invoke(object);
            return;
        }

        if (args.length == 0 && method.getParameters().length == 1 && method.getParameters()[0].getType().isAssignableFrom(CommandSender.class)) {
            method.invoke(object, sender);
            return;
        }

        Parameter[] excludeSenderParams = Arrays.copyOfRange(method.getParameters(), 1, method.getParameters().length);


        int excludeOptionals = (int) Arrays.stream(excludeSenderParams).filter(parameter -> !parameter.isAnnotationPresent(OptionalParameter.class)).count();

        System.out.println(excludeOptionals);

        if (args.length < excludeOptionals) {
            sender.sendMessage(TranslateUtil.translate(info.usage()));
            return;
        }

        List<Object> arguments = new ArrayList<>();
        arguments.add(sender);
        for (int i = 0; i < excludeSenderParams.length; i++) {

            Parameter parameter = excludeSenderParams[i];
            String argument;
            try {
                argument = args[i];
            } catch (ArrayIndexOutOfBoundsException e) {
                arguments.add(null);
                continue;
            }
            Class<?> type = parameter.getType();
            TypeAdapter<?> adapter;

            if (parameter.isAnnotationPresent(ParameterType.class)) {
                adapter = handler.getAdapterByAdapterClass(parameter.getAnnotation(ParameterType.class).value());
            } else adapter = handler.getAdapter(type);

            Optional<?> optional = adapter.parse(excludeSenderParams, args, parameter, argument);

            if (optional == null || !optional.isPresent() && parameter.isAnnotationPresent(OptionalParameter.class)) {
                arguments.add(null);
                continue;
            }

            if (!optional.isPresent()) {
                adapter.onError(sender, argument);
                return;
            } else {
                arguments.add(optional.get());
            }
        }

        method.invoke(object, arguments.toArray());
        return;
    }
}
