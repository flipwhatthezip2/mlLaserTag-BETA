package ml.lasertag.minigame.events;


import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerStatUpdateEvent extends Event {

    public static final HandlerList handlers = new HandlerList();

    private Player player;
    private PlayerStat playerStat;
    private int newValue;

    public PlayerStatUpdateEvent(Player player, PlayerStat playerStat, int newValue){
        this.player = player;
        this.playerStat = playerStat;
    }

    public Player getPlayer(){
        return this.player;
    }

    public PlayerStat getPlayerStat(){
        return this.playerStat;
    }

    public int getNewValue(){
        return this.newValue;
    }

    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public enum PlayerStat{
        KILLS, DEATHS, WINS, LOSSES;
    }

}
