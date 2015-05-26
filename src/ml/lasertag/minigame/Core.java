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
import ml.lasertag.minigame.GameManager.GunSelector;
import ml.lasertag.minigame.GameManager.GunsFile;
import ml.lasertag.minigame.Mechanics.*;
import ml.lasertag.minigame.api.Feature;
import ml.lasertag.minigame.commands.LaserTag;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class Core extends JavaPlugin {

    public ArenasFile arenasFile;
    public GunsFile gunsFile;
    public LaserGun laserGun;

    public static String joinMessage = "§2§lJoin §8➤ §7";
    public static String quitMessage = "§c§lQuit §8➤ §7";

    public static String combatMessage = "§6§lCombat §8➤ §7";

    public static String teamMessage = "§6§lTeam §8➤ §7";
    public static String gameMessage = "§6§lGame §8➤ §7";
    public static String lootMessage = "§6§lLoot §8➤ §7";
    public static String statusMessage = "§6§lStatus §8➤ §7";
    public static String rechargingMessage = "§6§lRecharging §8➤ §7";
    public static String powerupMessage = "§6§lPowerup §8➤ §7";
    public static String kitMessage = "§6§lKit §8➤ §7";

    public static String infoMessage = "§6§lInfo §8➤ §7";
    public static String serverMessage = "§6§lServer §8➤ §7";


    public void onEnable(){

        Bukkit.getServer().getLogger().info("Server> Successfully initialized the minigame 'LaserTag' on server: " + Bukkit.getServerName());


        if (!getDataFolder().exists()) getDataFolder().mkdir();

        this.arenasFile = new ArenasFile(this); this.arenasFile.loadArenas();
        this.gunsFile = new GunsFile(this); this.gunsFile.loadGuns();

        for (World world : Bukkit.getWorlds()){
            world.setGameRuleValue("keepInventory", "true");
            world.setGameRuleValue("naturalRegeneration", "false");
        }

        for (Player player : Bukkit.getOnlinePlayers()){
            player.removePotionEffect(PotionEffectType.SLOW);
            player.setLevel(0);
            player.setPlayerListName(player.getName());
        }


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
        for (World w : Bukkit.getWorlds()) {
            for (Entity e : w.getEntities()){
                if (e instanceof EnderCrystal) e.remove();
            }
        }

        for (Player p : Bukkit.getOnlinePlayers()){
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            p.updateInventory();
            p.setWalkSpeed(0.2F);

            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            p.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
        }


    }

    public ArenasFile getArenasFile(){
        return this.arenasFile;
    }

    public GunsFile getGunsFile(){
        return this.gunsFile;
    }

    public LaserGun getLaserGun(){
        return this.laserGun;
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this.laserGun = new LaserGun(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChat(this), this);
        Bukkit.getPluginManager().registerEvents(new ArenaSelector(this), this);
        Bukkit.getPluginManager().registerEvents(new GunSelector(this), this);
        Bukkit.getPluginManager().registerEvents(new Restrictions(), this);
        Bukkit.getPluginManager().registerEvents(new AimDownSights(this), this);
        Bukkit.getPluginManager().registerEvents(new BeaconProtect(this), this);
    }
}