package ml.lasertag.minigame.GameManager;

import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.events.ArenaInteractEvent;
import ml.lasertag.minigame.game.TEAM;
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
        arenas.add(arena);
        arenaNames.add(arena.getProperties().getArenaName());

        arenasFile.set("Arenas", arenaNames);

        String arenaName = arena.getProperties().getArenaName();

        arenasFile.set(arenaName + ".World", arena.getProperties().getWorld().getName());
        arenasFile.set(arenaName + ".MinPlayers", arena.getProperties().getMinimumPlayers());
        arenasFile.set(arenaName + ".MaxPlayers", arena.getProperties().getMaximumPlayers());
        arenasFile.set(arenaName + ".GreenSpawn", null);
        arenasFile.set(arenaName + ".YellowSpawn", null);
        arenasFile.set(arenaName + ".GreenBeacon", null);
        arenasFile.set(arenaName + ".YellowBeacon", null);

        this.setGreenSpawn(arena.getProperties().getArenaName(), arena.getProperties().getWorld().getSpawnLocation());
        this.setYellowSpawn(arena.getProperties().getArenaName(), arena.getProperties().getWorld().getSpawnLocation());

        this.setGreenBeacon(arena.getProperties().getArenaName(), arena.getProperties().getWorld().getSpawnLocation());
        this.setYellowBeacon(arena.getProperties().getArenaName(), arena.getProperties().getWorld().getSpawnLocation());

        this.save();
    }

    public void loadArenas(){
        for (String arenaName : arenasFile.getStringList("Arenas")){
            ArenaProperties properties =
                    new ArenaProperties(Bukkit.getWorld(arenasFile.getString(arenaName + ".World")),
                            arenasFile.getInt(arenaName + ".MinPlayers"),
                            arenasFile.getInt(arenaName + ".MaxPlayers"),
                            arenaName);

            arenas.add(new Arena(core, properties, core.getLaserGun()));
            arenaNames.add(arenaName);
        }
    }

    public void deleteArena(Arena arena){
        Bukkit.getPluginManager().callEvent(new ArenaInteractEvent(ArenaInteractEvent.ArenaAction.DELETE, arena));

        arenas.remove(arena);
        arenaNames.remove(arena.getProperties().getArenaName());

        arenasFile.set("Arenas." + arena.getProperties().getArenaName(), arenaNames);
        arenasFile.set(arena.getProperties().getArenaName(), null);

        this.save();
    }

    public int getMinimumPlayers(String arenaName){
        return arenasFile.getInt(arenaName + ".MinPlayers");
    }

    public int getMaximumPlayers(String arenaName){
        return arenasFile.getInt(arenaName + ".MaxPlayers");
    }

    public void setGreenSpawn(String arenaName, Location loc){
        arenasFile.set(arenaName + ".GreenSpawn", loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());

        this.save();
    }

    public void setYellowSpawn(String arenaName, Location loc){
        arenasFile.set(arenaName + ".YellowSpawn", loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());

        this.save();
    }

    public void setGreenBeacon(String arenaName, Location loc){
        arenasFile.set(arenaName + ".GreenBeacon", loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());

        this.save();
    }

    public void setYellowBeacon(String arenaName, Location loc){
        arenasFile.set(arenaName + ".YellowBeacon", loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());

        this.save();
    }

    public Location getGreenSpawn(String arenaName){

        World world = Bukkit.getWorld(arenaName);
        int x = Integer.parseInt(arenasFile.get(arenaName + ".GreenSpawn").toString().split(", ")[0]);
        int y = Integer.parseInt(arenasFile.get(arenaName + ".GreenSpawn").toString().split(", ")[1]);
        int z = Integer.parseInt(arenasFile.get(arenaName + ".GreenSpawn").toString().split(", ")[2]);

        return new Location(world, x, y, z);
    }

    public Location getYellowSpawn(String arenaName){

        World world = Bukkit.getWorld(arenaName);
        int x = Integer.parseInt(arenasFile.get(arenaName + ".YellowSpawn").toString().split(", ")[0]);
        int y = Integer.parseInt(arenasFile.get(arenaName + ".YellowSpawn").toString().split(", ")[1]);
        int z = Integer.parseInt(arenasFile.get(arenaName + ".YellowSpawn").toString().split(", ")[2]);

        return new Location(world, x, y, z);
    }

    public Location getGreenBeacon(String arenaName){

        World world = Bukkit.getWorld(arenaName);

        try {
            int x = Integer.parseInt(arenasFile.get(arenaName + ".GreenBeacon").toString().split(", ")[0]);
            int y = Integer.parseInt(arenasFile.get(arenaName + ".GreenBeacon").toString().split(", ")[1]);
            int z = Integer.parseInt(arenasFile.get(arenaName + ".GreenBeacon").toString().split(", ")[2]);

            return new Location(world, x, y, z);
        } catch (NullPointerException e){
            Bukkit.getLogger().info("FAILED TO GET GREEN BEACON LOCATION FOR " + arenaName);
            return world.getSpawnLocation();
        }
    }

    public Location getYellowBeacon(String arenaName){

        World world = Bukkit.getWorld(arenaName);

        try {
            int x = Integer.parseInt(arenasFile.get(arenaName + ".YellowBeacon").toString().split(", ")[0]);
            int y = Integer.parseInt(arenasFile.get(arenaName + ".YellowBeacon").toString().split(", ")[1]);
            int z = Integer.parseInt(arenasFile.get(arenaName + ".YellowBeacon").toString().split(", ")[2]);

            return new Location(world, x, y, z);
        } catch (NullPointerException e){
            Bukkit.getLogger().info("FAILED TO GET YELLOW BEACON LOCATION FOR " + arenaName);
            return world.getSpawnLocation();
        }
    }

    public Location getBeacon(String arenaName, TEAM team){
        if (team == TEAM.YELLOW) return this.getYellowBeacon(arenaName);
        return this.getGreenBeacon(arenaName);
    }

    public void save(){
        try {
            arenasFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}