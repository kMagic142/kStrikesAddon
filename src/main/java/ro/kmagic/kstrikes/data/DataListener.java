package ro.kmagic.kstrikes.data;

import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandJoinEvent;
import com.bgsoftware.superiorskyblock.api.island.Island;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ro.kmagic.kstrikes.Strikes;
import ro.kmagic.kstrikes.data.config.Options;
import ro.kmagic.kstrikes.utils.Utils;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class DataListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Strikes instance = Strikes.getInstance();

        boolean playerExists = false;
        try {
            ResultSet result = instance.getDBManager().get(Options.tablePrefix + "users", "uuid", player.getUniqueId());
            playerExists = result.next();
        } catch (Exception ignored) {}

        if(!playerExists) {
            instance.getDBManager().insert(Options.tablePrefix + "users",
                    new String[]{"uuid", "name", "strikes"},
                    new String[]{player.getUniqueId().toString(), player.getName(), "0"});
        }

    }

    @EventHandler
    public void onIslandCreated(IslandCreateEvent event) {
        Player player = event.getPlayer().asPlayer();
        Island island = event.getIsland();
        Strikes instance = Strikes.getInstance();

        boolean islandExists = false;
        try {
            ResultSet result = instance.getDBManager().get(Options.tablePrefix + "islands", "uuid", island.getUniqueId());
            islandExists = result.next();
        } catch (Exception ignored) {}

        if(!islandExists) {
            instance.getDBManager().insert(Options.tablePrefix + "islands",
                    new String[]{"uuid", "strikes"},
                    new String[]{island.getUniqueId().toString(), "0"});
        }

    }

    @EventHandler
    public void onIslandJoin(IslandJoinEvent event) {
        Player player = event.getPlayer().asPlayer();
        Island island = event.getIsland();
        Strikes instance = Strikes.getInstance();

        boolean islandExists = false;
        int strikes = 0;

        try {
            ResultSet result = instance.getDBManager().get(Options.tablePrefix + "islands", "uuid", island.getUniqueId());
            while(result.next()) {
                strikes = result.getInt("strikes");
            }
        } catch (Exception ignored) {}

        try {
            ResultSet result = instance.getDBManager().get(Options.tablePrefix + "users", "uuid", player.getUniqueId());
            while(result.next()) {
                strikes += result.getInt("strikes");
            }
        } catch (Exception ignored) {}

        if(!islandExists) {
            instance.getDBManager().insert(Options.tablePrefix + "islands",
                    new String[]{"uuid", "strikes"},
                    new String[]{player.getUniqueId().toString(), island.getUniqueId().toString(), "0"});
        }

        if(strikes == 0) return;

        instance.getDBManager().set(Options.tablePrefix + "islands", "strikes", "uuid", strikes, island.getUniqueId());
        Bukkit.getPluginManager().callEvent(new DataModifiedEvent(player.getName(), island));
    }

    @EventHandler
    public void onDataModified(DataModifiedEvent event) {
        Player player = Bukkit.getPlayerExact(event.getPlayer());
        Island island = event.getIsland();
        Strikes instance = Strikes.getInstance();

        Map<String, Object> actions = Options.actions;
        int playerStrikes;
        int islandStrikes;

        if(player != null) playerStrikes = Strikes.getInstance().getPlayerStrikes(player);
        else {
            playerStrikes = 0;
        }

        if(island != null) islandStrikes = Strikes.getInstance().getIslandStrikes(island);
        else {
            islandStrikes = 0;
        }

        actions.forEach((i, c) -> {
            int strikes = Integer.parseInt(i);
            List<String> commands = (List<String>) c;

            if(playerStrikes == strikes && player != null) {
                for(String command : commands) {
                    if(command.startsWith("[msg]")) {
                        player.sendMessage(Utils.color(player, command.replace("[msg]", "")));
                        continue;
                    }

                    if(command.startsWith("[message]")) {
                        player.sendMessage(Utils.color(player, command.replace("[message]", "")));
                        continue;
                    }

                    if(command.startsWith("[reset-island]")) {
                        instance.getDBManager().set(Options.tablePrefix + "islands", "strikes", "uuid", 0, island.getUniqueId());
                        continue;
                    }

                    if(command.startsWith("[reset-player]")) {
                        instance.getDBManager().set(Options.tablePrefix + "users", "strikes", "name", 0, event.getPlayer());
                        continue;
                    }

                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, command));
                }
            } else if(islandStrikes == strikes && island != null) {
                for(String command : commands) {
                    if(command.startsWith("[msg]")) {
                        island.getIslandMembers(true).forEach(p -> p.asPlayer().sendMessage(Utils.color(p.asPlayer(), command.replace("[msg]", "").replace("{island}", island.getName()))));
                        continue;
                    }

                    if(command.startsWith("[message]")) {
                        island.getIslandMembers(true).forEach(p -> p.asPlayer().sendMessage(Utils.color(p.asPlayer(), command.replace("[message]", "").replace("{island}", island.getName()))));
                        continue;
                    }

                    if(command.startsWith("[reset-island]")) {
                        instance.getDBManager().set(Options.tablePrefix + "islands", "strikes", "uuid", 0, island.getUniqueId());
                        continue;
                    }

                    if(command.startsWith("[reset-player]")) {
                        instance.getDBManager().set(Options.tablePrefix + "users", "strikes", "name", 0, event.getPlayer());
                        continue;
                    }

                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("{island}", island.getName()).replace("{player}", event.getPlayer()));
                }
            }

        });
    }

}
