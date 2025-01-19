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

public class SetCommand implements CommandInterface {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("strikes.admin.set")) {
            sender.sendMessage(Utils.color(Messages.noPermission));
            return;
        }

        if(args.length < 1) {
            sender.sendMessage(Utils.color(Messages.noPlayer));
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if(args.length < 2) {
            sender.sendMessage(Utils.color(Messages.noAmount));
            return;
        }

        Strikes instance = Strikes.getInstance();
        int amount = Integer.parseInt(args[1]);

        SuperiorSkyblock plugin = Strikes.getInstance().getPlugin();
        Island island = plugin.getPlayers().getSuperiorPlayer(player).getIsland();

        if(island == null) {
            sender.sendMessage(Utils.color(Messages.playerNoIsland));
            return;
        }

        if(player == null) {
            boolean playerExists = instance.getDBManager().exists(Options.tablePrefix + "users", "name", args[0]);

            if(!playerExists) {
                sender.sendMessage(Utils.color(Messages.playerDoesNotExist));
                return;
            }

            sender.sendMessage(Utils.color(Messages.set.replace("{strikes}", String.valueOf(amount)).replace("{player}", args[0])));

            instance.getDBManager().set(Options.tablePrefix + "islands", "strikes", "uuid", amount, island.getUniqueId());
            instance.getDBManager().set(Options.tablePrefix + "users", "strikes", "name", amount, args[0]);

            Bukkit.getPluginManager().callEvent(new DataModifiedEvent(args[0], island));

            return;
        }

        sender.sendMessage(Utils.color(player, Messages.set.replace("{strikes}", String.valueOf(amount)).replace("{player}", player.getName())));

        instance.getDBManager().set(Options.tablePrefix + "islands", "strikes", "uuid", amount, island.getUniqueId());
        instance.getDBManager().set(Options.tablePrefix + "users", "strikes", "uuid", amount, player.getUniqueId());

        Bukkit.getPluginManager().callEvent(new DataModifiedEvent(player.getName(), island));
    }

    @Override
    public String getName() {
        return "set <player> <amount>";
    }

    @Override
    public String getDescription() {
        return "Set a player's strikes amount";
    }
}
