package ro.kmagic.kstrikes;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblock;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ro.kmagic.kstrikes.commands.*;
import ro.kmagic.kstrikes.commands.handler.CommandHandler;
import ro.kmagic.kstrikes.data.DataListener;
import ro.kmagic.kstrikes.data.Placeholders;
import ro.kmagic.kstrikes.data.config.Options;
import ro.kmagic.kstrikes.data.database.DBConnection;
import ro.kmagic.kstrikes.data.database.DBManager;
import ro.kmagic.kstrikes.utils.Utils;

import java.sql.SQLException;

import static ro.kmagic.kstrikes.data.database.DataType.MYSQL;
import static ro.kmagic.kstrikes.data.database.DataType.SQLITE;


public final class Strikes extends JavaPlugin {

    private static Strikes instance;
    private CommandHandler commandHandler;
    private DBManager dbManager;

    private SuperiorSkyblock plugin;

    @Override
    public void onEnable() {
        instance = this;

        if(getServer().getPluginManager().getPlugin("SuperiorSkyBlock2") != null) {
            plugin = (SuperiorSkyblock) getServer().getPluginManager().getPlugin("SuperiorSkyBlock2");
            Utils.info("Hooked successfully into SuperiorSkyBlock2!");
        } else {
            getServer().getPluginManager().disablePlugin(this);
            Utils.info("SuperiorSkyBlock2 is missing, disabling plugin.");
        }

        initData();
        registerCommands();

        getServer().getPluginManager().registerEvents(new DataListener(), this);

        new Placeholders().register();

        Utils.info("Plugin enabled successfully.");
    }

    @Override
    public void onDisable() {
        try {
            dbManager.getDbConnection().getConnection().close();
        } catch (SQLException e) {
            Utils.info("Error when trying to disconnect from database.");
        }

        Utils.info("Plugin is disabling. Goodbye!");
    }

    private void initData() {
        Utils.info("Initializing data...");

        try {
            saveDefaultConfig();
            getConfig().options().copyDefaults(true);
        } catch(Exception e) {
            e.printStackTrace();
            Utils.error("Error while loading config! Send this to the dev.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Utils.info("Config loaded successfully.");

        switch(Options.dataType) {
            case SQLITE:
                dbManager = new DBManager(new DBConnection(SQLITE));
                dbManager.createTable(Options.tablePrefix + "users", new String[]{"uuid", "name", "strikes"});
                dbManager.createTable(Options.tablePrefix + "islands", new String[]{"uuid", "strikes"});
                break;
            case MYSQL:
                dbManager = new DBManager(new DBConnection(MYSQL,
                        Options.host,
                        Options.database,
                        Options.username,
                        Options.password,
                        Options.port
                ));

                dbManager.createTable(Options.tablePrefix + "users", new String[]{"uuid", "name", "strikes"});
                dbManager.createTable(Options.tablePrefix + "islands", new String[]{"uuid", "strikes"});
                break;
            default:
                Utils.info("Initialized database successfully.");
        }

    }

    private void registerCommands() {
        Utils.info("Loading commands...");

        commandHandler = new CommandHandler();
        commandHandler.register("strikes", new StrikesCommand());
        commandHandler.register("help", new HelpCommand());
        commandHandler.register("add", new AddCommand());
        commandHandler.register("remove", new RemoveCommand());
        commandHandler.register("reload", new ReloadCommand());
        commandHandler.register("set", new SetCommand());

        getCommand("strike").setExecutor(commandHandler);
        getCommand("strike").setTabCompleter(commandHandler);

        Utils.info("Loaded commands successfully.");
    }

    public static Strikes getInstance() {
        return instance;
    }

    public int getPlayerStrikes(final Player player) {
        try {
            return dbManager.getInt(Options.tablePrefix + "users", "strikes", "name", player.getName());
        } catch (Exception e) {
            Utils.error("Problem retrieving player points from database!");
            e.printStackTrace();
        }

        return 0;
    }

    public int getPlayerStrikes(final String player) {
        try {
            return dbManager.getInt(Options.tablePrefix + "users", "strikes", "name", player);
        } catch (Exception e) {
            Utils.error("Problem retrieving player points from database!");
            e.printStackTrace();
        }

        return 0;
    }

    public int getIslandStrikes(final Player player) {
        try {
            Island island = plugin.getPlayers().getSuperiorPlayer(player).getIsland();
            if(island == null) return 0;
            return dbManager.getInt(Options.tablePrefix + "islands", "strikes", "uuid", island.getUniqueId().toString());
        } catch (Exception e) {
            Utils.error("Problem retrieving player points from database!");
            e.printStackTrace();
        }

        return 0;
    }

    public int getIslandStrikes(final Island island) {
        try {
            return dbManager.getInt(Options.tablePrefix + "islands", "strikes", "uuid", island.getUniqueId().toString());
        } catch (Exception e) {
            Utils.error("Problem retrieving player points from database!");
            e.printStackTrace();
        }

        return 0;
    }

    public int getIslandStrikes(final String p) {
        try {
            SuperiorPlayer player = plugin.getPlayers().getSuperiorPlayer(p);
            if(player.getIsland() == null) return 0;
            return dbManager.getInt(Options.tablePrefix + "islands", "strikes", "uuid", player.getIsland().getUniqueId().toString());
        } catch (Exception e) {
            Utils.error("Problem retrieving player points from database!");
            e.printStackTrace();
        }

        return 0;
    }

    public DBManager getDBManager() {
        return dbManager;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public SuperiorSkyblock getPlugin() {
        return plugin;
    }
}
