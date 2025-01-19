package ro.kmagic.kstrikes.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import ro.kmagic.kstrikes.Strikes;
import ro.kmagic.kstrikes.data.config.Messages;
import ro.kmagic.kstrikes.data.config.Options;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Utils {

    public static String DEFAULT_DB_URI_SQLITE = "jdbc:sqlite:";
    public static String DEFAULT_DB_URI_MYSQL = "jdbc:mysql://";

    private static final Logger logger = Strikes.getInstance().getLogger();

    public static TextComponent color(String str) {
        if(Options.prefixEnabled) return LegacyComponentSerializer.legacyAmpersand().deserialize(Messages.prefix + str);
        return LegacyComponentSerializer.legacyAmpersand().deserialize(str);
    }

    public static List<TextComponent> colorList(List<String> messages) {
        List<TextComponent> newList = new ArrayList<>();

        for (String str : messages) {
            newList.add(Utils.color(str));
        }

        return newList;
    }

    public static List<TextComponent> colorList(Player player, List<String> messages) {
        List<TextComponent> newList = new ArrayList<>();

        for (String str : messages) {
            newList.add(Utils.color(PlaceholderAPI.setPlaceholders(player, str)));
        }

        return newList;
    }

    public static TextComponent color(Player player, String msg) {
        return color(PlaceholderAPI.setPlaceholders(player, msg));
    }

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void error(String msg) {
        logger.severe(msg);
    }

}
