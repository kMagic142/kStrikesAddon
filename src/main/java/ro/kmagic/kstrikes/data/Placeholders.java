package ro.kmagic.kstrikes.data;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import ro.kmagic.kstrikes.Strikes;

public class Placeholders extends PlaceholderExpansion {

    private Strikes instance;

    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin(getRequiredPlugin()) != null;
    }

    @Override
    public boolean register() {
        if (!canRegister()) return false;

        instance = (Strikes) Bukkit.getPluginManager().getPlugin(getRequiredPlugin());
        if (instance == null) {
            return false;
        }

        return super.register();
    }

    @Override
    public String getRequiredPlugin() {
        return "kPoints";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "points";
    }

    @Override
    public @NotNull String getAuthor() {
        return instance.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return instance.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        String[] args = identifier.split("_");

        if(args.length < 2) return null;

        if(args[0].equalsIgnoreCase("amount")) {
            if(args[1].equalsIgnoreCase("player")) {
                if (Bukkit.getPlayerExact(player.getName()) == null) return "0";
                return "" + instance.getPlayerStrikes(player.getName());
            } else if(args[1].equalsIgnoreCase("island")) {
                if (Bukkit.getPlayerExact(player.getName()) == null) return "0";
                return "" + instance.getIslandStrikes(Bukkit.getPlayerExact(player.getName()));
            }
        }

        return null;
    }

}




















