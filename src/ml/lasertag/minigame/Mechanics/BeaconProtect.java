package ml.lasertag.minigame.Mechanics;


import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.GameManager.Arena;
import ml.lasertag.minigame.game.TEAM;
import org.bukkit.Location;
import org.bukkit.block.Beacon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class BeaconProtect implements Listener{

    static Core core;


    public BeaconProtect(Core core){
        this.core = core;
    }

    public static boolean isInEnemyTerritory(Player player){
        Arena arena = Arena.getArena(core, player);
        TEAM enemyTeam = arena.getTeams().getTeam(player) == TEAM.YELLOW ? TEAM.GREEN : TEAM.YELLOW;
        Location loc = enemyTeam == TEAM.YELLOW ? arena.getYellowBeacon().getLocation() : arena.getGreenBeacon().getLocation();

        if (player.getLocation().distance(loc) <= 20) return true;
        return false;

    }

}
