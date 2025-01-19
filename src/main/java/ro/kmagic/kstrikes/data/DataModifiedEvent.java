package ro.kmagic.kstrikes.data;

import com.bgsoftware.superiorskyblock.api.island.Island;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ro.kmagic.kstrikes.Strikes;

public class DataModifiedEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final String player;
    private final Island island;

    public DataModifiedEvent(String player) {
        this.player = player;
        this.island = Strikes.getInstance().getPlugin().getPlayers().getSuperiorPlayer(player).getIsland();
    }

    public DataModifiedEvent(String player, Island island) {
        this.player = player;
        this.island = island;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Island getIsland() {
        return island;
    }

    public String getPlayer() {
        return player;
    }
}
