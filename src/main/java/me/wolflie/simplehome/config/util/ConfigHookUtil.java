/*
 * Copyright (c) 2019. This is property of the Patrone Network and it's corresponding entities. This code may not be re-distributed in any way shape or form.
 */

package me.wolflie.simplehome.config.util;

import com.google.common.collect.Sets;
import me.wolflie.simplehome.config.Config;
import me.wolflie.simplehome.config.annotation.ConfigKey;
import me.wolflie.simplehome.config.annotation.ConfigSection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigHookUtil {

    public static boolean isValidHook(Object hook) {

        if (isNestedHook(hook)) {
            return true;
        }
        return Arrays.stream(hook.getClass().getDeclaredFields()).anyMatch(field -> field.isAnnotationPresent(ConfigKey.class));
    }

    public static boolean isNestedHook(Object hook) {
        return hook.getClass().getDeclaredClasses().length > 0 && Arrays.stream(hook.getClass().getDeclaredClasses())
                .filter(clazz -> clazz.isAnnotationPresent(ConfigSection.class))
                .anyMatch(clazz ->
                        Arrays.stream(clazz.getDeclaredFields()).anyMatch(field -> field.isAnnotationPresent(ConfigKey.class))
                );
    }

    public static void initializeHook(Object hook, Config config) {
        if (!isValidHook(hook))
            throw new IllegalArgumentException("The object you've provided is not a configuration hook.");

        Set<Field> fields = Sets.newHashSet();
        fields.addAll(getHookFields(hook.getClass().getDeclaredFields()));
        if (isNestedHook(hook)) {
            for (Class<?> clazz : hook.getClass().getDeclaredClasses()) {
                fields.addAll(getHookFields(clazz.getDeclaredFields()));
            }
        }

        for (Field field : fields) {
            initializeHookField(hook, field, field.getAnnotation(ConfigKey.class), config);
        }
    }

    private static void initializeHookField(Object hook, Field field, ConfigKey key, Config config) {

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        String path = key.value();
        Object parent = hook;

        if (field.getDeclaringClass().isAnnotationPresent(ConfigSection.class)) {
            path = field.getDeclaringClass().getAnnotation(ConfigSection.class).value() + "." + key.value();
            try {
                parent = field.getDeclaringClass().getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        if (config.get(path) == null) {
            try {
                config.set(path, field.get(parent));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            if (field.getType().isAssignableFrom(Collection.class)) {
                try {
                    field.set(parent, config.getList(path));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    field.set(parent, config.get(path));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Set<Field> getHookFields(Field[] fields) {
        return Arrays.stream(fields).filter(field -> field.isAnnotationPresent(ConfigKey.class)).collect(Collectors.toSet());
    }

}
