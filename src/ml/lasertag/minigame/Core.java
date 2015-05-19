package ml.lasertag.minigame;


/* 
 Project Info:
 Project Name: mlLaserTag
 Package: ml.LaserTag.minigame
 Created By: cfletcher
 Created On: 4/26/15
 All code is licenced to the following people:
 - Cameron Fletcher 
 - 
 */


import ml.lasertag.minigame.GameManager.ArenaSelector;
import ml.lasertag.minigame.GameManager.ArenasFile;
import ml.lasertag.minigame.Mechanics.LaserGun;
import ml.lasertag.minigame.Mechanics.PlayerChat;
import ml.lasertag.minigame.Mechanics.PlayerJoin;
import ml.lasertag.minigame.Mechanics.Restrictions;
import ml.lasertag.minigame.api.Feature;
import ml.lasertag.minigame.commands.LaserTag;
import ml.lasertag.minigame.game.GameStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Core extends JavaPlugin {

    public ArenasFile arenasFile;
    public LaserGun laserGun;

    public static String info = "§7§l>> §e";
    public static String infoList = "§7§l> §e";
    public static String success = "§2§l>> §a";
    public static String successList = "§2§l> §a";
    public static String warning = "§4§l>> §c";
    public static String warningList = "§4§l> §c";

    public static GameStatus currentState;

    public void onEnable(){
        Bukkit.getServer().getLogger().info(">> Minigame 'LaserTag' is successfully running on: " + Bukkit.getServerName());

        this.arenasFile = new ArenasFile(this);
        this.arenasFile.loadArenas();

        Bukkit.getServer().getLogger().info(ChatColor.GREEN + "-Successfully initialize arenas-");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (Player all : Bukkit.getServer().getOnlinePlayers()){
                    Feature.sendActionBar(all, "§cPlaying §lLASERTAG §con §6§lMineLegends§c!");
                }
            }
        }, 20, 20);

        registerEvents();

        getCommand("lasertag").setExecutor(new LaserTag(this, arenasFile));
    }

    public void onDisable(){
        for (World w : Bukkit.getWorlds()){
            for (Entity e : w.getEntities()){
                if (e instanceof EnderCrystal) e.remove();
            }
        }


    }

    public ArenasFile getArenasFile(){
        return this.arenasFile;
    }

    public LaserGun getLaserGun(){
        return this.laserGun;
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this.laserGun = new LaserGun(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChat(), this);
        Bukkit.getPluginManager().registerEvents(new ArenaSelector(this), this);
        Bukkit.getPluginManager().registerEvents(new Restrictions(), this);
    }
}