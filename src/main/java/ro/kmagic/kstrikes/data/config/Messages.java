package ro.kmagic.kstrikes.data.config;

import org.bukkit.configuration.ConfigurationSection;
import ro.kmagic.kstrikes.Strikes;

public class Messages {

    private static final ConfigurationSection messagesSection = Strikes.getInstance().getConfig().getConfigurationSection("messages");

    public static String invalidCommand = messagesSection.getString("invalid-command");
    public static String noPermission = messagesSection.getString("no-permission");
    public static String prefix = messagesSection.getString("prefix");
    public static String noPlayer = messagesSection.getString("no-player");
    public static String tooManyArgs = messagesSection.getString("too-many-args");
    public static String playerDoesNotExist = messagesSection.getString("player-does-not-exist");
    public static String playerNotOnline = messagesSection.getString("player-is-not-online");
    public static String playerNoIsland = messagesSection.getString("player-no-island");
    public static String noAmount = messagesSection.getString("no-amount");
    public static String set = messagesSection.getString("set-points");
    public static String add = messagesSection.getString("add-points");
    public static String remove = messagesSection.getString("remove-points");
    public static String strikes = messagesSection.getString("strikes");

}
