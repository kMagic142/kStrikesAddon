package ro.kmagic.kstrikes.commands.handler;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ro.kmagic.kstrikes.data.config.Messages;
import ro.kmagic.kstrikes.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {

    private static final HashMap<String, CommandInterface> commands = new HashMap<>();


    public void register(String name, CommandInterface cmd) {
        commands.put(name, cmd);
    }

    public HashMap<String, CommandInterface> getCommands() {
        return commands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(args.length == 0) {
            commands.get("strikes").onCommand(sender, new String[]{});
            return true;
        }

        if(args[0].equalsIgnoreCase("check")) {
            commands.get("strikes").onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
            return true;
        }

        if(commands.containsKey(args[0])) {
            commands.get(args[0]).onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
            return true;
        } else {
            sender.sendMessage(Utils.color(Messages.invalidCommand));
            return false;
        }

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> numberCompletions = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9");

        if(args.length == 2) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }

        if(args.length == 3 && (
                args[0].equalsIgnoreCase("give") ||
                args[0].equalsIgnoreCase("remove") ||
                args[0].equalsIgnoreCase("set") ||
                args[0].equalsIgnoreCase("add"))) return numberCompletions;

        if(args.length == 1) {
            StringUtil.copyPartialMatches(args[0], commands.keySet(), completions);
            return completions;
        }

        return completions;

    }
}
