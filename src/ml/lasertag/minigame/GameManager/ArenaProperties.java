package ml.lasertag.minigame.GameManager;

import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.events.ArenaInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class ArenaProperties {

 Core core;

 private World world;
 private int minimumPlayers;
 private int maximumPlayers;
 private String arenaName;

 public ArenaProperties(Core core, World world, int minimumPlayers, int maximumPlayers, String arenaName){
  this.core = core;
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
  core.getArenasFile().setMaximumPlayers(arenaName, maximumPlayers);
  Bukkit.getPluginManager().callEvent(new ArenaInteractEvent(ArenaInteractEvent.ArenaAction.UPDATE_STAT, core.getArenasFile().getArena(arenaName)));
 }

 public void setMinimumPlayers(int minimumPlayers){
  this.minimumPlayers = minimumPlayers;
  core.getArenasFile().setMinimumPlayers(arenaName, minimumPlayers);
  Bukkit.getPluginManager().callEvent(new ArenaInteractEvent(ArenaInteractEvent.ArenaAction.UPDATE_STAT, core.getArenasFile().getArena(arenaName)));
 }

 public void setArenaName(String arenaName){
  this.arenaName = arenaName;
  Bukkit.getPluginManager().callEvent(new ArenaInteractEvent(ArenaInteractEvent.ArenaAction.UPDATE_STAT, core.getArenasFile().getArena(arenaName)));
 }

}