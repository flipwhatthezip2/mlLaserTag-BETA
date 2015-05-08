package ml.lasertag.minigame.GameManager;

import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.Mechanics.LaserGun;
import ml.lasertag.minigame.game.Teams;
import org.bukkit.Bukkit;
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

 private int countDownTime = 5;
 private int countDown = 100;
 private int currentCountDownStage = 5;

 private boolean pvp = false;


 public Arena(ArenaProperties properties, LaserGun laserGun){
  this.properties = properties;
  this.arenaState = ArenaState.WAITING;
  this.laserGun = laserGun;
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

 public void startCountdown(){
  this.arenaState = ArenaState.COUNTDOWN;
  this.broadcastMessage("INSERT STARTING MESSAGE");

  countDownRunnable = new BukkitRunnable(){

   @Override
   public void run(){
    broadcastMessage(Core.info + "Game Starting In: §c§l" + currentCountDownStage);
    currentCountDownStage--;
   }


  }.runTaskTimer(core, 0L, countDown / countDownTime);
 }

 public void cancelCountdown(){
  Bukkit.getScheduler().cancelTask(countDownRunnable.getTaskId());
  currentCountDownStage = countDownTime;
  this.broadcastMessage("INSERT COUNTDOWN CANCELLED MESSAGE");
 }

 public void startGame(){
  this.pvp = true;
  this.arenaState = ArenaState.IN_GAME;
  this.broadcastMessage("INSERT START GAME MESSAGE");
  //TODO teleport all player's to their team's spawn
 }

 public void endGame(){
  this.pvp = false;
  this.arenaState = ArenaState.RESTARTING;
  this.laserGun.resetCooldowns();
  this.broadcastMessage("INSERT END GAME MESSAGE");
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