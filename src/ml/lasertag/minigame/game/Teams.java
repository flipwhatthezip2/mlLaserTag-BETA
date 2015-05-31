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

    private ArrayList<Player> yellowTeam = new ArrayList<>();
    private ArrayList<Player> greenTeam = new ArrayList<>();

    public Teams(Arena arena){
        this.arena = arena;
    }

    public void addPlayer(Player player, TEAM team){
        this.teams.put(player, team);
        player.setPlayerListName((team == TEAM.YELLOW ? ChatColor.YELLOW : ChatColor.GREEN) + player.getName());
        player.sendMessage(Core.infoMessage + "You have joined" + ChatColor.translateAlternateColorCodes('&',(
                team == TEAM.YELLOW ? " &eYELLOW" : " &aGREEN") + " &7team!"));

        if (team == TEAM.YELLOW) yellowTeam.add(player); else greenTeam.add(player);
    }

    public void removePlayer(Player player){
        this.teams.remove(player);
        player.setPlayerListName(player.getName());

        if (yellowTeam.contains(player)) yellowTeam.remove(player); else greenTeam.remove(player);
    }

    public void resetTeams(){
        teams.clear();
    }

    public boolean isYellowTeamEmpty(){
        return yellowTeam.size() == 0;
    }

    public boolean isGreenTeamEmpty(){
        return greenTeam.size() == 0;
    }

    public TEAM getTeam(Player player){
        return teams.get(player);
    }

}