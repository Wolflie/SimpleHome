/*
 * Copyright (c) 2019. This is property of the Patrone Network and it's corresponding entities. This code may not be re-distributed in any way shape or form.
 */

package me.wolflie.simplehome.config;

import java.util.List;
import java.util.Set;

public interface Config {

    Set<Object> getHooks();

    void addHook(Object object);

    void updateHook(Object object);

    void reloadConfig();

    void set(String path, Object value);

    Object get(String path);

    int getInt(String path);

    boolean getBoolean(String path);

    String getString(String path);

    List<?> getList(String path);

    List<String> getStringList(String path);

    void save();


}
