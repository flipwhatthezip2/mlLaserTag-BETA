package ml.lasertag.minigame.GameManager;

import ml.lasertag.minigame.Core;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ArenasFile {

 Core core;

 private File file;
 private FileConfiguration arenasFile;

 private ArrayList<Arena> arenas = new ArrayList<Arena>();
 private ArrayList<String> arenaNames = new ArrayList<String>();

 public ArenasFile(Core core){
  this.core = core;
  this.file = new File(core.getDataFolder(), "arenasFile.yml");

  if (!file.exists()){
   try {
    file.createNewFile();
   } catch (IOException e) {
    e.printStackTrace();
   }
  }

  this.arenasFile = YamlConfiguration.loadConfiguration(file);
 }

 public ArrayList<Arena> getArenas(){
  return this.arenas;
 }

 public ArrayList<String> getArenaNames(){
  return this.arenaNames;
 }

 public void addArena(Arena arena){
  String arenaName = "Arenas." + arena.getProperties().getArenaName();

  arenasFile.set(arenaName + ".World", arena.getProperties().getWorld().getName());
  arenasFile.set(arenaName + ".MinPlayers", arena.getProperties().getMinimumPlayers());
  arenasFile.set(arenaName + ".MaxPlayers", arena.getProperties().getMaximumPlayers());
  arenasFile.set(arenaName + ".GreenSpawn", null);
  arenasFile.set(arenaName + ".YellowSpawn", null);

  arenas.add(arena);
  arenaNames.add(arena.getProperties().getArenaName());

  this.setGreenSpawn(arena.getProperties().getArenaName(), arena.getProperties().getWorld().getSpawnLocation());
  this.setYellowSpawn(arena.getProperties().getArenaName(), arena.getProperties().getWorld().getSpawnLocation());

  this.save();
 }

 public void loadArenas(){
  Bukkit.getServer().broadcastMessage("I'M BEING CALLED");

  //if (arenasFile.getStringList("Arenas") == null) return;
  Bukkit.getServer().broadcastMessage(arenasFile.getStringList("Arenas").size() + "");
  for (String arenaName : arenasFile.getStringList("Arenas")){
   Bukkit.getServer().broadcastMessage(arenaName);
   ArenaProperties properties =
           new ArenaProperties(Bukkit.getWorld(arenasFile.getString("Arenas." + arenaName + ".World")),
                   arenasFile.getInt("Arenas." + arenaName + ".MinPlayers"),
                   arenasFile.getInt("Arenas." + arenaName + ".MaxPlayers"),
                   arenaName);

   arenas.add(new Arena(properties, core.getLaserGun()));
   arenaNames.add(arenaName);

   Bukkit.getServer().broadcastMessage(arenaName);
  }
 }

 public void deleteArena(Arena arena){
  arenasFile.set("Arenas." + arena.getProperties().getArenaName(), null);
  arenas.remove(arena);
  arenaNames.remove(arena.getProperties().getArenaName());

  this.save();
 }

 public int getMinimumPlayers(String arenaName){
  return arenasFile.getInt("Arenas." + arenaName + ".MinPlayers");
 }

 public int getMaximumPlayers(String arenaName){
  return arenasFile.getInt("Arenas." + arenaName + ".MaxPlayers");
 }

 public void setGreenSpawn(String arenaName, Location loc){
  arenasFile.set("Arenas." + arenaName + ".GreenSpawn", loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());

  this.save();
 }

 public void setYellowSpawn(String arenaName, Location loc){
  arenasFile.set("Arenas." + arenaName + ".YellowSpawn", loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());

  this.save();
 }

 public Location getGreenSpawn(String arenaName){

  World world = Bukkit.getWorld(arenasFile.getString(arenaName + ".World"));
  int x = Integer.parseInt(arenasFile.get("Arenas." + arenaName + ".GreenSpawn").toString().split(", ")[0]);
  int y = Integer.parseInt(arenasFile.get("Arenas." + arenaName + ".GreenSpawn").toString().split(", ")[1]);
  int z = Integer.parseInt(arenasFile.get("Arenas." + arenaName + ".GreenSpawn").toString().split(", ")[2]);

  return new Location(world, x, y, z);
 }

 public Location getYellowSpawn(String arenaName){

  World world = Bukkit.getWorld(arenasFile.getString(arenaName + ".World"));
  int x = Integer.parseInt(arenasFile.get("Arenas." + arenaName + ".YellowSpawn").toString().split(", ")[0]);
  int y = Integer.parseInt(arenasFile.get("Arenas." + arenaName + ".YellowSpawn").toString().split(", ")[1]);
  int z = Integer.parseInt(arenasFile.get("Arenas." + arenaName + ".YellowSpawn").toString().split(", ")[2]);

  return new Location(world, x, y, z);
 }

 public void save(){
  try {
   arenasFile.save(file);
  } catch (IOException e) {
   e.printStackTrace();
  }
 }

}