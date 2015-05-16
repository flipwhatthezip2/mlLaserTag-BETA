package ml.lasertag.minigame.GameManager;

import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.Mechanics.LaserGun;
import ml.lasertag.minigame.game.TEAM;
import ml.lasertag.minigame.game.Teams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class Arena {

 static Core core;
 private LaserGun laserGun;

 private ArenaProperties properties;
 private ArrayList<Player> players = new ArrayList<Player>();
 private ArenaState arenaState;
 private Teams teams;
 private BukkitTask countDownRunnable;
 private ItemStack gun = new ItemStack(Material.IRON_BARDING);
 private Teams teamManager;

 private int countDownTime = 5;
 private int countDown = 100;
 private int currentCountDownStage = 5;

 private boolean pvp = false;


 public Arena(ArenaProperties properties, LaserGun laserGun){
  this.properties = properties;
  this.arenaState = ArenaState.WAITING;
  this.laserGun = laserGun;
  this.teamManager = new Teams(this);
  this.teams = new Teams(this);
 }

 public ArenaProperties getProperties(){
  return this.properties;
 }

 public ArrayList<Player> getPlayers(){
  return this.players;
 }

 public boolean isPvpEnabled(){
  return this.pvp;
 }

 public void addPlayer(Player player){
  players.add(player);
 }

 public void removePlayer(Player player){
  players.remove(player);
 }

 public Location getSpawn(TEAM team){
  if (team == TEAM.GREEN) return core.getArenasFile().getGreenSpawn(properties.getArenaName());
  return core.getArenasFile().getYellowSpawn(properties.getArenaName());
 }

 public void spawnPlayers(){
  for (Player p : players){
   p.teleport(Arena.getArena(p).getSpawn(Teams.getTeam(p)));
  }
 }

 public void startCountdown(){
  this.arenaState = ArenaState.COUNTDOWN;
  this.broadcastMessage(Core.success + "The game will begin shortly!");

  countDownRunnable = new BukkitRunnable(){

   @Override
   public void run(){
    broadcastMessage(Core.warning + "Game starting in: §l" + currentCountDownStage);
    currentCountDownStage--;
   }


  }.runTaskTimer(core, 0L, countDown / countDownTime);
 }

 public void cancelCountdown(){
  Bukkit.getScheduler().cancelTask(countDownRunnable.getTaskId());
  currentCountDownStage = countDownTime;
  this.broadcastMessage(Core.warning + "Game countdown has been halted.");
 }

 public void startGame(){
  this.pvp = true;
  this.arenaState = ArenaState.IN_GAME;
  this.broadcastMessage(Core.success + "The game has be§lgun§a!");
  this.spawnPlayers();
 }

 public void endGame(){
  this.pvp = false;
  this.arenaState = ArenaState.RESTARTING;
  this.laserGun.resetCooldowns();
  this.broadcastMessage(Core.success + "The game has ended!");
  for (Player p : players) p.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
  this.players.clear();
  this.arenaState = ArenaState.WAITING;
 }

 public void distributeKits(){
  for (Player p : players){
   p.getInventory().setItem(0, gun); p.updateInventory();
  }
 }

 public void broadcastMessage(String message){
  for (Player p : players){
   p.sendMessage(message);
  }
 }

 public static Arena getArena(Player player){
  for (Arena a : core.getArenasFile().getArenas()) if (a.getPlayers().contains(player)) return a;
  return null;
 }

 public static Arena getArena(String string){
  for (Arena a : core.getArenasFile().getArenas())
   if (a.getProperties().getArenaName().equalsIgnoreCase(string)) return a;
  return null;
 }

 public enum ArenaState {
  WAITING, COUNTDOWN, IN_GAME, RESTARTING;
 }
}