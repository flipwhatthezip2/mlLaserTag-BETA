package ml.lasertag.minigame.events;


import ml.lasertag.minigame.GameManager.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaInteractEvent extends Event {

    public static final HandlerList handlerList = new HandlerList();

    private ArenaAction arenaAction;
    private Arena arena;

    public ArenaInteractEvent(ArenaAction arenaAction, Arena arena){
        this.arenaAction = arenaAction;
        this.arena = arena;
    }

    public ArenaAction getArenaAction(){
        return this.arenaAction;
    }

    public Arena getArena(){
        return this.arena;
    }

    public static HandlerList getHandlerList(){
        return handlerList;
    }

    public HandlerList getHandlers(){
        return handlerList;
    }

    public enum ArenaAction {
        JOIN, LEAVE, CREATE, DELETE, UPDATE_STAT;
    }
}
