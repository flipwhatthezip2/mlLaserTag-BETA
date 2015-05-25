package ml.lasertag.minigame.GameManager;

import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.Mechanics.LaserGun;
import ml.lasertag.minigame.Mechanics.LaserTagBeacon;
import ml.lasertag.minigame.api.Feature;
import ml.lasertag.minigame.events.ArenaInteractEvent;
import ml.lasertag.minigame.game.StatsScoreboard;
import ml.lasertag.minigame.game.TEAM;
import ml.lasertag.minigame.game.Teams;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;

public class Arena {

 Core core;

 private LaserGun laserGun;
 private Arena arena;

 private ArenaProperties properties;
 private StatsScoreboard scoreboard;
 private ArrayList<Player> players = new ArrayList<Player>();
 private ArenaState arenaState;
 private Teams teams;
 private BukkitTask countDownRunnable;
 private boolean canJoin = true;

 private int countDownTime = 5;
 private int countDown = 100;
 private int currentCountDownStage = 5;

 private LaserTagBeacon yellowBeacon;
 private LaserTagBeacon greenBeacon;

 private boolean pvp = false;


 public Arena(Core core, ArenaProperties properties, LaserGun laserGun){
  this.core = core;
  this.arena = this;
  this.properties = properties;
  this.arenaState = ArenaState.WAITING;
  this.laserGun = laserGun;
  this.teams = new Teams(this);

  this.intializeBeacons();
  this.scoreboard = new StatsScoreboard(core, this);

  Bukkit.getPluginManager().callEvent(new ArenaInteractEvent(ArenaInteractEvent.ArenaAction.CREATE, this));
 }

 public Teams getTeams(){
  return this.teams;
 }

 public StatsScoreboard getScoreboard(){
  return this.scoreboard;
 }

 public LaserTagBeacon getYellowBeacon(){
  return this.yellowBeacon;
 }

 public LaserTagBeacon getGreenBeacon(){
  return this.greenBeacon;
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

 public void intializeBeacons(){
  if (core.getArenasFile().getYellowBeacon(properties.getArenaName()) == null ||
          core.getArenasFile().getGreenBeacon(properties.getArenaName()) == null){
   Bukkit.getServer().getLogger().info("PLEASE CONFIGURE BEACONS FOR " + properties.getArenaName());
   return;
  }

  this.yellowBeacon = new LaserTagBeacon(core, this, TEAM.YELLOW);
  this.greenBeacon = new LaserTagBeacon(core, this, TEAM.GREEN);
 }

 public void resetBeacons(){
  greenBeacon.reset();
  yellowBeacon.reset();
 }

 public void addPlayer(Player player){
  players.add(player);
  teams.addPlayer(player, teams.pickTeam(player));
  player.teleport(getSpawn(this.teams.getTeam(player)));
  if (players.size() == properties.getMaximumPlayers()) this.canJoin = false;
  if (players.size() == properties.getMinimumPlayers()) this.startCountdown();
 }

 public void removePlayer(Player player){

  players.remove(player);
  teams.removePlayer(player);

  player.getInventory().clear();
  player.getInventory().setArmorContents(null);
  player.setGameMode(GameMode.ADVENTURE);
  player.setWalkSpeed(0.2F);
  player.setHealth(20D);
  player.setLevel(0);

  Gun.getGun(player).removeUser(player);

  if (arenaState == ArenaState.WAITING || arenaState == ArenaState.COUNTDOWN) this.canJoin = true;
  if (players.size() < properties.getMinimumPlayers() && arenaState == ArenaState.COUNTDOWN) this.cancelCountdown();
  if (players.size() < properties.getMinimumPlayers() && arenaState == ArenaState.IN_GAME) this.endGameAsDraw();

 }

 public ArenaState getArenaState(){
  return this.arenaState;
 }

 public Location getSpawn(TEAM team){
  if (team == TEAM.GREEN) return core.getArenasFile().getGreenSpawn(properties.getArenaName());
  return core.getArenasFile().getYellowSpawn(properties.getArenaName());
 }

 public void spawnPlayers(){
  for (Player p : players){
   p.teleport(Arena.getArena(core, p).getSpawn(this.teams.getTeam(p)));
  }
 }

 public void startCountdown(){
  this.arenaState = ArenaState.COUNTDOWN;
  Bukkit.getPluginManager().callEvent(new ArenaInteractEvent(ArenaInteractEvent.ArenaAction.UPDATE_STAT, this));
  this.broadcastMessage(Core.success + "The game will begin shortly!");

  countDownRunnable = new BukkitRunnable(){

   @Override
   public void run(){
    if (currentCountDownStage == 0){
     currentCountDownStage = 5; startGame(); this.cancel(); return;
    }

    broadcastMessage(Core.warning + "Game starting in: §l" + currentCountDownStage);
    currentCountDownStage--;
   }


  }.runTaskTimer(core, 0L, countDown / countDownTime);
 }

 public void cancelCountdown(){
  Bukkit.getScheduler().cancelTask(countDownRunnable.getTaskId());
  currentCountDownStage = countDownTime;
  this.arenaState = ArenaState.WAITING;
  this.broadcastMessage(Core.warning + "Game countdown has been halted.");
  Bukkit.getPluginManager().callEvent(new ArenaInteractEvent(ArenaInteractEvent.ArenaAction.UPDATE_STAT, this));
 }

 public void startGame(){
  this.pvp = true;
  this.arenaState = ArenaState.IN_GAME;
  this.broadcastMessage(Core.success + "The game has be§lgun§a!");
  this.spawnPlayers();
  this.canJoin = false;
  this.scoreboard.showScoreboard();
  this.distributeKits();
  Bukkit.getPluginManager().callEvent(new ArenaInteractEvent(ArenaInteractEvent.ArenaAction.UPDATE_STAT, this));

  for (Player p : players){
   p.setGameMode(GameMode.ADVENTURE);
   p.setWalkSpeed(0.28F);
  }
 }

 public void endGameAsDraw(){
  this.pvp = false;
  this.broadcastTitle(ChatColor.BLUE + "Draw", ChatColor.GRAY + "The game has ended!");
  this.arenaState = ArenaState.RESTARTING;
  this.teams.resetTeams();
  this.handlePlayerLeave();


  new BukkitRunnable(){

   @Override
   public void run(){
    resetBeacons();
    for (Player p : players) p.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
    emptyArena();
    arenaState = ArenaState.WAITING;
    canJoin = true;
    scoreboard.reset();
    Bukkit.getPluginManager().callEvent(new ArenaInteractEvent(ArenaInteractEvent.ArenaAction.UPDATE_STAT, arena));
   }

  }.runTaskLater(core, 160L);
 }

 public void endGame(){
  this.pvp = false;
  this.broadcastTitle((greenBeacon.getHealth() > yellowBeacon.getHealth() ? ChatColor.DARK_GREEN + "Green " : ChatColor.YELLOW + "Yellow ") + "Team Won!",
          ChatColor.GRAY + "The game has ended!");
  this.arenaState = ArenaState.RESTARTING;
  this.teams.resetTeams();
  this.handlePlayerLeave();


  new BukkitRunnable(){

   @Override
  public void run(){
    resetBeacons();
    for (Player p : players) p.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
    emptyArena();
    arenaState = ArenaState.WAITING;
    canJoin = true;
    scoreboard.reset();
    Bukkit.getPluginManager().callEvent(new ArenaInteractEvent(ArenaInteractEvent.ArenaAction.UPDATE_STAT, arena));
   }

  }.runTaskLater(core, 160L);

 }

 public void emptyArena() {
  for (Player player : players) {
   player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
  }
  players.clear();
 }

  public void handlePlayerLeave(){
  for (Player player : players){
   player.getInventory().clear();
   player.getInventory().setArmorContents(null);
   player.updateInventory();

   player.setWalkSpeed(0.2F);
   player.setHealth(20D);
   player.removePotionEffect(PotionEffectType.SLOW);
   teams.removePlayer(player);

   Gun.getGun(player).removeUser(player);
  }
 }


 public void distributeKits(){

  for (Player p : players){
   TEAM.setUniform(core, p);
   p.getInventory().setItem(0, Gun.getGun(p).getGun());
   p.updateInventory();
  }
 }

 public void broadcastMessage(String message){
  for (Player p : players){
   p.sendMessage(message);
  }
 }

 public void broadcastTitle(String message, String subtitle){
  for (Player p : players){
   Feature.sendTitle(p, 20, 120, 20, message, subtitle);
  }
 }

 public void broadcastActionBar(String message){
  for (Player p : players){
   Feature.sendActionBar(p, message);
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

 public static void joinArena(Core core, Arena arena, Player player) {
  arena.addPlayer(player);
  arena.broadcastMessage(Core.joinMessage + "§6" + player.getName() + " §7has joined the game!");
  Bukkit.getPluginManager().callEvent(new ArenaInteractEvent(ArenaInteractEvent.ArenaAction.JOIN, arena));
 }

 public static void leaveArena(Core core, Arena arena, Player player){
  arena.broadcastMessage(Core.quitMessage + "§6" + player.getName() + " §7has left the game!");
  arena.removePlayer(player);
  player.setWalkSpeed(0.2F);
  player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
  player.setLevel(0);
  player.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
  Bukkit.getPluginManager().callEvent(new ArenaInteractEvent(ArenaInteractEvent.ArenaAction.LEAVE, arena));
 }

 public enum ArenaState {
  WAITING, COUNTDOWN, IN_GAME, RESTARTING;
 }
}