package ro.kmagic.kstrikes.data.config;

import org.bukkit.configuration.ConfigurationSection;
import ro.kmagic.kstrikes.Strikes;
import ro.kmagic.kstrikes.data.database.DataType;

import java.util.Map;

public class Options {

    private final static ConfigurationSection optionsSection = Strikes.getInstance().getConfig().getConfigurationSection("options");

    public static boolean prefixEnabled = optionsSection.getBoolean("prefix-enabled");

    private final static ConfigurationSection databaseSection = Strikes.getInstance().getConfig().getConfigurationSection("database");

    public static DataType dataType = DataType.valueOf(databaseSection.getString("type").toUpperCase());
    public static String host = databaseSection.getString("host");
    public static String port = databaseSection.getString("port");
    public static String username = databaseSection.getString("username");
    public static String database = databaseSection.getString("database");
    public static String password = databaseSection.getString("password");
    public static String tablePrefix = databaseSection.getString("table-prefix");

    public static Map<String, Object> actions = Strikes.getInstance().getConfig().getConfigurationSection("actions").getValues(true);

}
