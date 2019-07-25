package me.wolflie.simplehome.home;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import me.wolflie.simplehome.SimpleHome;
import me.wolflie.simplehome.config.yml.YAMLConfig;
import me.wolflie.simplehome.util.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class HomeManager implements Listener {

    private final Multimap<Player, Home> homes = HashMultimap.create();
    private final YAMLConfig config;

    public HomeManager(SimpleHome plugin) {
        this.config = new YAMLConfig(plugin.getDataFolder(), "homes");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void setHome(Player player, Home home) {
        Optional<Home> optionalHome = homes.get(player).stream().filter(found -> found.getName().equalsIgnoreCase(home.getName())).findFirst();
        if (optionalHome.isPresent()) {
            optionalHome.get().setLocation(player.getLocation());
        } else homes.put(player, home);
        String path = String.format("%s.homes.%s", player.getUniqueId().toString(), home.getName().toLowerCase());
        config.set(path + ".location", LocationUtil.serializeLiteLocation(home.getLocation()));
        config.save();
    }

    public void deleteHome(Player player, Home home) {
        homes.remove(player, home);
        String path = String.format("%s.homes.%s", player.getUniqueId().toString(), home.getName().toLowerCase());
        config.set(path, null);
        config.save();
    }

    public void deleteHome(Player player, String home) {
        Optional<Home> optional = homes.get(player).stream().filter(found -> found.getName().equalsIgnoreCase(home)).findFirst();
        if (!optional.isPresent()) player.sendMessage(ChatColor.RED + "Couldn't find home by that name.");
        deleteHome(player, optional.get());
    }

    public Home getHomeByName(Player player, String name) {
        return homes.get(player).stream()
                .filter(found -> found.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Collection<Home> getHomesOfPlayer(Player player) {
        return homes.get(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        homes.removeAll(e.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        UUID id = e.getPlayer().getUniqueId();
        config.getConfigurationSection(id.toString() + ".homes")
                .getKeys(false)
                .stream()
                .forEach(home -> {
                    String path = String.format("%s.homes.%s", id.toString(), home.toLowerCase());
                    Location location = LocationUtil.deserializeLiteLocation(config.getString(path + ".location"));
                    homes.put(e.getPlayer(), new Home(home, location));
                });
    }

}
