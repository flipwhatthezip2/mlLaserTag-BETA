package ml.lasertag.minigame.GameManager;

import org.bukkit.World;

public class ArenaProperties {

    private World world;
    private int minimumPlayers;
    private int maximumPlayers;
    private String arenaName;

    public ArenaProperties(World world, int minimumPlayers, int maximumPlayers, String arenaName){
        this.world = world;
        this.minimumPlayers = minimumPlayers;
        this.maximumPlayers = maximumPlayers;
        this.arenaName = arenaName;
    }

    public World getWorld(){
        return this.world;
    }

    public int getMinimumPlayers(){
        return this.minimumPlayers;
    }

    public int getMaximumPlayers(){
        return this.maximumPlayers;
    }

    public String getArenaName(){
        return this.arenaName;
    }

    public void setMaximumPlayers(int maximumPlayers){
        this.maximumPlayers = maximumPlayers;
    }

    public void setMinimumPlayers(int minimumPlayers){
        this.minimumPlayers = minimumPlayers;
    }

    public void setArenaName(String arenaName){
        this.arenaName = arenaName;
    }

}