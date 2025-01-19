package ro.kmagic.kstrikes.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.kmagic.kstrikes.Strikes;
import ro.kmagic.kstrikes.commands.handler.CommandInterface;
import ro.kmagic.kstrikes.data.config.Messages;
import ro.kmagic.kstrikes.utils.Utils;

public class StrikesCommand implements CommandInterface {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("strikes.strikes")) {
            sender.sendMessage(Utils.color(Messages.noPermission));
            return;
        }

        if (!(sender instanceof Player player)) return;

        if(args.length < 1) {
            int playerStrikes = 0;
            int islandStrikes = 0;

            try {
                playerStrikes = Strikes.getInstance().getPlayerStrikes(player);
                islandStrikes = Strikes.getInstance().getIslandStrikes(player);
            } catch(Exception ignored) {}

            player.sendMessage(Utils.color(player, Messages.strikes.replace("{player}", playerStrikes + "").replace("{island}", islandStrikes + "")));
            return;
        }

        if(!sender.hasPermission("strikes.strikes.others")) {
            sender.sendMessage(Utils.color(Messages.noPermission));
            return;
        }

        int playerStrikes = 0;
        int islandStrikes = 0;

        try {
            playerStrikes = Strikes.getInstance().getPlayerStrikes(args[0]);
            islandStrikes = Strikes.getInstance().getIslandStrikes(args[0]);
        } catch(Exception ignored) {}

        player.sendMessage(Utils.color(player, Messages.strikes.replace("{player}", playerStrikes + "").replace("{island}", islandStrikes + "")));
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Get this message";
    }
}
