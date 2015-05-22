package ml.lasertag.minigame.game;

import ml.lasertag.minigame.GameManager.Arena;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Teams {

    private Arena arena;

    private HashMap<Player, TEAM> teams = new HashMap<Player, TEAM>();
    private int yellowTeamCount = 0;
    private int greenTeamCount = 0;

    public Teams(Arena arena){
        this.arena = arena;
    }

    public void addPlayer(Player player, TEAM team){
        this.teams.put(player, team);
    }

    public void removePlayer(Player player){
        this.teams.remove(player);
    }

    public TEAM pickTeam(Player player){
        if (this.yellowTeamCount > this.greenTeamCount){
            greenTeamCount++;
            return TEAM.GREEN;
        }
        yellowTeamCount++;
        return TEAM.YELLOW;
    }

    public void resetTeams(){
        teams.clear();
    }

    public TEAM getTeam(Player player){
        return teams.get(player);
    }

}