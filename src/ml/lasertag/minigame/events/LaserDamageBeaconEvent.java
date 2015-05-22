package ml.lasertag.minigame.events;


        import ml.lasertag.minigame.Mechanics.LaserTagBeacon;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LaserDamageBeaconEvent extends Event{

    public static final HandlerList handlers = new HandlerList();

    private LaserTagBeacon beacon;
    private Player damager;

    public LaserDamageBeaconEvent(LaserTagBeacon beacon, Player damager){
        this.beacon = beacon;
        this.damager = damager;
    }

    public LaserTagBeacon getBeacon(){
        return this.beacon;
    }

    public Player getDamager(){
        return this.damager;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public HandlerList getHandlers(){
        return handlers;
    }

}
