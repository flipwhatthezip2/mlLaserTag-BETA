package ml.lasertag.minigame.game;

import ml.lasertag.minigame.GameManager.Arena;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Teams {

 private Arena arena;

 private static ArrayList<Player> yellowTeam = new ArrayList<Player>();
 private static ArrayList<Player> greenTeam = new ArrayList<Player>();

 public Teams(Arena arena){
  this.arena = arena;
 }

 public void addPlayer(Player player, TEAM team){
  if (team == TEAM.YELLOW) yellowTeam.add(player);
  else greenTeam.add(player);
 }

 public void removePlayer(Player player, TEAM team){
  if (team == TEAM.YELLOW) yellowTeam.remove(player);
  else greenTeam.remove(player);
 }

 public static TEAM getTeam(Player player){
  if (yellowTeam.contains(player)) return TEAM.YELLOW;
  return TEAM.GREEN;
 }

}