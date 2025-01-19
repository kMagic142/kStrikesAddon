package ro.kmagic.kstrikes.commands;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblock;
import com.bgsoftware.superiorskyblock.api.island.Island;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.kmagic.kstrikes.Strikes;
import ro.kmagic.kstrikes.commands.handler.CommandInterface;
import ro.kmagic.kstrikes.data.DataModifiedEvent;
import ro.kmagic.kstrikes.data.config.Messages;
import ro.kmagic.kstrikes.data.config.Options;
import ro.kmagic.kstrikes.utils.Utils;

import java.sql.SQLException;

public class RemoveCommand implements CommandInterface {

    @Override
    public void onCommand(CommandSender sender, String[] args) {

        if(!sender.hasPermission("strikes.admin.remove")) {
            sender.sendMessage(Utils.color(Messages.noPermission));
            return;
        }

        if(args.length < 1) {
            sender.sendMessage(Utils.color(Messages.noPlayer));
            return;
        }

        int points;

        if(args.length < 2) {
            points = 1;
            Strikes instance = Strikes.getInstance();

            if(Bukkit.getPlayer(args[0]) != null) {
                Player player = Bukkit.getPlayer(args[0]);

                SuperiorSkyblock plugin = Strikes.getInstance().getPlugin();
                Island island = plugin.getPlayers().getSuperiorPlayer(player).getIsland();

                if(island == null) {
                    sender.sendMessage(Utils.color(Messages.playerNoIsland));
                    return;
                }

                sender.sendMessage(Utils.color(player, Messages.remove.replace("{strikes}", String.valueOf((points))).replace("{player}", player.getName())));

                instance.getDBManager().set(Options.tablePrefix + "islands", "strikes", "uuid", instance.getIslandStrikes(player) - points, island.getUniqueId());
                instance.getDBManager().set(Options.tablePrefix + "users", "strikes", "uuid", instance.getPlayerStrikes(player) - points, player.getUniqueId());

                Bukkit.getPluginManager().callEvent(new DataModifiedEvent(player.getName(), island));
            } else {
                boolean playerExists = instance.getDBManager().exists(Options.tablePrefix + "users", "name", args[0]);

                if(!playerExists) {
                    sender.sendMessage(Utils.color(Messages.playerDoesNotExist));
                    return;
                }

                int defaultIslandStrikes;
                int defaultPlayerStrikes;

                SuperiorSkyblock plugin = Strikes.getInstance().getPlugin();
                Island island = plugin.getPlayers().getSuperiorPlayer(args[0]).getIsland();

                if(island == null) {
                    sender.sendMessage(Utils.color(Messages.playerNoIsland));
                    return;
                }

                try {
                    defaultIslandStrikes = Integer.parseInt(instance.getDBManager().getString(Options.tablePrefix + "islands", "strikes", "uuid", island.getUniqueId()));
                    defaultPlayerStrikes = Integer.parseInt(instance.getDBManager().getString(Options.tablePrefix + "users", "strikes", "name", args[0]));
                } catch (SQLException e) {
                    e.printStackTrace();
                    Utils.error("Error when getting island strikes from database! Report to dev ASAP!");
                    return;
                }

                sender.sendMessage(Utils.color(Messages.remove.replace("{strikes}", String.valueOf(points)).replace("{player}", args[0])));

                instance.getDBManager().set(Options.tablePrefix + "islands", "strikes", "uuid", defaultIslandStrikes - points, island.getUniqueId());
                instance.getDBManager().set(Options.tablePrefix + "users", "strikes", "name", defaultPlayerStrikes - points, args[0]);

                Bukkit.getPluginManager().callEvent(new DataModifiedEvent(args[0], island));
            }

        } else if(args.length == 2) {
            points = Integer.parseInt(args[1]);
            Strikes instance = Strikes.getInstance();

            if (Bukkit.getPlayer(args[0]) != null) {
                Player player = Bukkit.getPlayer(args[0]);

                SuperiorSkyblock plugin = Strikes.getInstance().getPlugin();
                Island island = plugin.getPlayers().getSuperiorPlayer(player).getIsland();

                if(island == null) {
                    sender.sendMessage(Utils.color(Messages.playerNoIsland));
                    return;
                }

                sender.sendMessage(Utils.color(player, Messages.remove.replace("{strikes}", String.valueOf((points))).replace("{player}", player.getName())));

                instance.getDBManager().set(Options.tablePrefix + "islands", "strikes", "uuid", instance.getIslandStrikes(player) - points, island.getUniqueId());
                instance.getDBManager().set(Options.tablePrefix + "users", "strikes", "uuid", instance.getPlayerStrikes(player) - points, player.getUniqueId());

                Bukkit.getPluginManager().callEvent(new DataModifiedEvent(player.getName(), island));
            } else {
                boolean playerExists = instance.getDBManager().exists(Options.tablePrefix + "users", "name", args[0]);

                if (!playerExists) {
                    sender.sendMessage(Utils.color(Messages.playerDoesNotExist));
                    return;
                }

                int defaultIslandStrikes;
                int defaultPlayerStrikes;

                SuperiorSkyblock plugin = Strikes.getInstance().getPlugin();
                Island island = plugin.getPlayers().getSuperiorPlayer(args[0]).getIsland();

                if(island == null) {
                    sender.sendMessage(Utils.color(Messages.playerNoIsland));
                    return;
                }

                try {
                    defaultIslandStrikes = Integer.parseInt(instance.getDBManager().getString(Options.tablePrefix + "islands", "strikes", "uuid", island.getUniqueId()));
                    defaultPlayerStrikes = Integer.parseInt(instance.getDBManager().getString(Options.tablePrefix + "users", "strikes", "name", args[0]));
                } catch (SQLException e) {
                    e.printStackTrace();
                    Utils.error("Error when getting player hearts from database! Report to dev ASAP!");
                    return;
                }

                sender.sendMessage(Utils.color(Messages.remove.replace("{strikes}", String.valueOf(points)).replace("{player}", args[0])));

                instance.getDBManager().set(Options.tablePrefix + "users", "strikes", "name", defaultPlayerStrikes - points, args[0]);
                instance.getDBManager().set(Options.tablePrefix + "islands", "strikes", "uuid", defaultIslandStrikes - points, island.getUniqueId());

                Bukkit.getPluginManager().callEvent(new DataModifiedEvent(args[0], island));
            }
        }
    }

    @Override
    public String getName() {
        return "remove <player> [strikes]";
    }

    @Override
    public String getDescription() {
        return "Remove strike(s) from a player";
    }

}
