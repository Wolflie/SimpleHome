package me.wolflie.simplehome.commands;

import me.wolflie.simplehome.SimpleHome;
import me.wolflie.simplehome.command.annotation.CommandInfo;
import me.wolflie.simplehome.command.annotation.Default;
import me.wolflie.simplehome.command.annotation.OptionalParameter;
import me.wolflie.simplehome.command.annotation.Single;
import me.wolflie.simplehome.command.annotation.sender.PlayerOnly;
import me.wolflie.simplehome.home.Home;
import me.wolflie.simplehome.home.HomeManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

@CommandInfo(name = "home", usage = "&c/home &e[name]")
public class HomeCommand {

    private final SimpleHome plugin;
    private final HomeManager homeManager;

    public HomeCommand(SimpleHome plugin) {
        this.plugin = plugin;
        this.homeManager = plugin.getHomeManager();
    }

    @Default
    @PlayerOnly
    public void onExecute(CommandSender sender, @OptionalParameter @Single String name) {
        Player player = (Player) sender;
        Collection<Home> homes = plugin.getHomeManager().getHomesOfPlayer(player);
        if (homes.size() == 0) {
            player.sendMessage(ChatColor.RED + "You have no homes set. Set one by doing /sethome [name]");
            return;
        }
        if (name == null) name = "home";
        if (homeManager.getHomeByName(player, name) == null) {
            player.sendMessage(ChatColor.RED + "Your homes: ");
            homeManager.getHomesOfPlayer(player).forEach(home -> {
                player.sendMessage(ChatColor.BLUE + StringUtils.capitalize(home.getName()));
            });
            return;
        } else {
            player.teleport(homeManager.getHomeByName(player, name).getLocation());
            player.sendMessage(ChatColor.RED + "Whoosh!");
        }
    }

    @PlayerOnly
    @CommandInfo(name = "list")
    public void onList(CommandSender sender) {
        Player player = (Player) sender;
        if (homeManager.getHomesOfPlayer(player).isEmpty()) {
            player.sendMessage(ChatColor.RED + "You don't have any homes.");
            return;
        }
        homeManager.getHomesOfPlayer(player).forEach(home -> {
            player.sendMessage(ChatColor.BLUE + StringUtils.capitalize(home.getName()));
        });
    }

}
