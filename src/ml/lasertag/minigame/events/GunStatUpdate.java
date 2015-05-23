package ml.lasertag.minigame.events;


import ml.lasertag.minigame.GameManager.Gun;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GunStatUpdate extends Event {

    public static final HandlerList handlerList = new HandlerList();

    private Gun gun;

    public GunStatUpdate(Gun gun){
        this.gun = gun;
    }

    public Gun getGun(){
        return this.gun;
    }

    public static HandlerList getHandlerList(){
        return handlerList;
    }

    public HandlerList getHandlers(){
        return handlerList;
    }


}
