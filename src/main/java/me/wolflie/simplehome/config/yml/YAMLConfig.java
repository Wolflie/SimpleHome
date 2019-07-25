/*
 * Copyright (c) 2019. This is property of the Patrone Network and it's corresponding entities. This code may not be re-distributed in any way shape or form.
 */

package me.wolflie.simplehome.config.yml;

import com.google.common.collect.Sets;
import me.wolflie.simplehome.config.Config;
import me.wolflie.simplehome.config.util.ConfigHookUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class YAMLConfig extends YamlConfiguration implements Config {

    private File file;
    private File directory;
    private Set<Object> hooks = Sets.newHashSet();

    public YAMLConfig(File directory, String name) {
        this.directory = directory;
        if (!directory.exists()) directory.mkdirs();

        file = new File(directory, name + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        reloadConfig();
    }

    @Override
    public Set<Object> getHooks() {
        return hooks;
    }

    @Override
    public void addHook(Object object) {
        if (hooks.contains(object)) return;
        updateHook(object);
        hooks.add(object);
    }

    @Override
    public void updateHook(Object hook) {
        ConfigHookUtil.initializeHook(hook, this);
        save();
    }


    @Override
    public void reloadConfig() {
        try {
            load(file);
            hooks.forEach(this::updateHook);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
