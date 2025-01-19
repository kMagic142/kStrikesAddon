package ro.kmagic.kstrikes.commands;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;
import ro.kmagic.kstrikes.Strikes;
import ro.kmagic.kstrikes.commands.handler.CommandInterface;
import ro.kmagic.kstrikes.data.config.Messages;
import ro.kmagic.kstrikes.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements CommandInterface {

    @Override
    public void onCommand(CommandSender sender, String[] args) {

        if(!sender.hasPermission("strikes.help")) {
            sender.sendMessage(Utils.color(Messages.noPermission));
            return;
        }

        List<String> message = new ArrayList<>();
        message.add("&r");
        message.add("&r       " + Messages.prefix + " &7| &fVersion " + Strikes.getInstance().getPluginMeta().getVersion());
        message.add("&r");

        for(CommandInterface cmd : Strikes.getInstance().getCommandHandler().getCommands().values()) {
            message.add("&8* &7/strikes " + cmd.getName() + " &8- &7" + cmd.getDescription());
        }

        message.add("&r");

        List<TextComponent> finalList = Utils.colorList(message);

        for(TextComponent component : finalList) {
            sender.sendMessage(component);
        }

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
