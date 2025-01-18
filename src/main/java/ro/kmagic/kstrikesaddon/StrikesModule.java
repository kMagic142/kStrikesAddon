package ro.kmagic.kstrikesaddon;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblock;
import com.bgsoftware.superiorskyblock.api.commands.SuperiorCommand;
import com.bgsoftware.superiorskyblock.api.modules.PluginModule;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class StrikesModule extends PluginModule {

    private StrikesModule instance;
    private SuperiorSkyblock plugin;

    public StrikesModule(String moduleName, String authorName) {
        super("kStrikes", "kMagic");
        this.instance = this;
    }

    @Override
    public void onEnable(SuperiorSkyblock plugin) {
        this.plugin = plugin;

    }

    @Override
    public void onReload(SuperiorSkyblock superiorSkyblock) {

    }

    @Override
    public void onDisable(SuperiorSkyblock superiorSkyblock) {

    }

    @Override
    public Listener[] getModuleListeners(SuperiorSkyblock superiorSkyblock) {
        return new Listener[0];
    }

    @Override
    public SuperiorCommand[] getSuperiorCommands(SuperiorSkyblock superiorSkyblock) {
        return new SuperiorCommand[0];
    }

    @Override
    public SuperiorCommand[] getSuperiorAdminCommands(SuperiorSkyblock superiorSkyblock) {
        return new SuperiorCommand[0];
    }
}
