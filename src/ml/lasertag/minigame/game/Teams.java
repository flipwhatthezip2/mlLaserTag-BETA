package ml.lasertag.minigame.game;

import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.GameManager.Arena;
import org.bukkit.ChatColor;
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
        player.setPlayerListName((team == TEAM.YELLOW ? ChatColor.YELLOW : ChatColor.GREEN) + player.getName());
        player.sendMessage(Core.infoMessage + "You have joined" + ChatColor.translateAlternateColorCodes('&',(
                team == TEAM.YELLOW ? " &eYELLOW" : " &aGREEN") + " &7team!"));
    }

    public void removePlayer(Player player){
        this.teams.remove(player);
        player.setPlayerListName(player.getName());
    }

    public void resetTeams(){
        teams.clear();
    }

    public TEAM getTeam(Player player){
        return teams.get(player);
    }

}