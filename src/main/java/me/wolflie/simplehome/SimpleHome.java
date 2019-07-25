package me.wolflie.simplehome;

import me.wolflie.simplehome.command.CommandHandler;
import me.wolflie.simplehome.commands.DeleteHomeCommand;
import me.wolflie.simplehome.commands.HomeCommand;
import me.wolflie.simplehome.commands.SetHomeCommand;
import me.wolflie.simplehome.home.HomeManager;
import me.wolflie.simplehome.util.TypeUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleHome extends JavaPlugin {

    private CommandHandler commandHandler;
    private HomeManager homeManager;

    @Override
    public void onEnable() {
        homeManager = new HomeManager(this);
        commandHandler = new CommandHandler(this);
        registerCommands();
    }

    private void registerTypeAdapters() {
        TypeUtils.registerBasicTypeAdapters(commandHandler);
    }

    private void registerCommands() {
        registerTypeAdapters();
        commandHandler.registerCommand(new HomeCommand(this));
        commandHandler.registerCommand(new SetHomeCommand(this));
        commandHandler.registerCommand(new DeleteHomeCommand(this));
    }

    @Override
    public void onDisable() {

    }

    public HomeManager getHomeManager() {
        return homeManager;
    }
}
