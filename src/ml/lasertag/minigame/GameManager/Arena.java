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
 private boolean canJoin = true;

 private int countDownTime = 5;
 private int countDown = 100;
 private int currentCountDownStage = 5;

 private boolean pvp = true;


 public Arena(Core core, ArenaProperties properties, LaserGun laserGun){
  this.core = core;
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

 public void setCanJoin(boolean canJoin){
  this.canJoin = canJoin;
 }

 public boolean getCanJoin(){
  return this.canJoin;
 }

 public void addPlayer(Player player){
  players.add(player);
  teams.addPlayer(player, teams.pickTeam(player));
  player.teleport(getSpawn(Teams.getTeam(player)));
  TEAM.setUniform(player);
 }

 public void removePlayer(Player player){
  players.remove(player);
  teams.removePlayer(player, Teams.getTeam(player));
  player.getInventory().clear();
  player.getInventory().setArmorContents(null);
 }

 public Location getSpawn(TEAM team){
  if (team == TEAM.GREEN) return core.getArenasFile().getGreenSpawn(properties.getArenaName());
  return core.getArenasFile().getYellowSpawn(properties.getArenaName());
 }

 public void spawnPlayers(){
  for (Player p : players){
   p.teleport(Arena.getArena(core, p).getSpawn(Teams.getTeam(p)));
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
  this.emptyInventories();
  this.arenaState = ArenaState.RESTARTING;
  this.laserGun.resetCooldowns();
  this.broadcastMessage(Core.success + "The game has ended!");
  this.emptyPlayerList();
  for (Player p : players) p.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
  this.arenaState = ArenaState.WAITING;
 }

 public void emptyPlayerList() {
  for (Player player : players) {
   this.removePlayer(player);
  }
 }

  public void emptyInventories(){
  for (Player player : players){
   player.getInventory().clear();
   player.updateInventory();
  }
 }


 public void distributeKits(){
  for (Player p : players){
   TEAM.setUniform(p);
   p.getInventory().setItem(0, gun);
   p.updateInventory();
  }
 }

 public void broadcastMessage(String message){
  for (Player p : players){
   p.sendMessage(message);
  }
 }

 public static Arena getArena(Core core, Player player){
  for (Arena a : core.getArenasFile().getArenas()) if (a.getPlayers().contains(player)) return a;
  return null;
 }

 public static Arena getArena(Core core, String string){
  for (Arena a : core.getArenasFile().getArenas())
   if (a.getProperties().getArenaName().equalsIgnoreCase(string)) return a;
  return null;
 }

 public static void joinArena(Core core, Arena arena, Player player){
  arena.addPlayer(player);
  arena.broadcastMessage(Core.success + "§l" + player.getName() + " §ahas joined the game!");
 }

 public static void leaveArena(Core core, Arena arena, Player player){
     arena.broadcastMessage(Core.warning + "§l" + player.getName() + " §chas left the game!");
  arena.removePlayer(player);
 }

 public enum ArenaState {
  WAITING, COUNTDOWN, IN_GAME, RESTARTING;
 }
}