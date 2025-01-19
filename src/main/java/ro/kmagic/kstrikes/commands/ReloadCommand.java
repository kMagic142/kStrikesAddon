package ro.kmagic.kstrikes.commands;

import org.bukkit.command.CommandSender;
import ro.kmagic.kstrikes.Strikes;
import ro.kmagic.kstrikes.commands.handler.CommandInterface;
import ro.kmagic.kstrikes.data.config.Messages;
import ro.kmagic.kstrikes.utils.Utils;

public class ReloadCommand implements CommandInterface {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("strikes.admin.reload")) {
            sender.sendMessage(Utils.color(Messages.noPermission));
            return;
        }

        Strikes.getInstance().reloadConfig();
        sender.sendMessage(Utils.color("&aReloaded config!"));
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads the plugin.";
    }
}
