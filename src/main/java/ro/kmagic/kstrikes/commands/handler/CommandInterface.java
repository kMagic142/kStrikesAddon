package ro.kmagic.kstrikes.commands.handler;

import org.bukkit.command.CommandSender;

public interface CommandInterface {

    void onCommand(CommandSender sender, String[] args);

    String getName();

    String getDescription();

}
