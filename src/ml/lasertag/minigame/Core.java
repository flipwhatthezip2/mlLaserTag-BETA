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
import ml.lasertag.minigame.stats.ScoreboardStatKeeper;
import ml.lasertag.minigame.stats.StatKeeper;
import ml.lasertag.minigame.stats.StatsFile;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Core extends JavaPlugin {

    Core core;

    private ArenasFile arenasFile;
    private GunsFile gunsFile;
    private StatsFile statsFile;
    private LaserGun laserGun;

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

        this.core = this;

        Bukkit.getServer().getLogger().info("Server> Successfully initialized the minigame 'LaserTag' on server: " + Bukkit.getServerName());


        if (!getDataFolder().exists()) getDataFolder().mkdir();

        registerEvents();

        this.arenasFile = new ArenasFile(this); this.arenasFile.loadArenas();
        this.gunsFile = new GunsFile(this); this.gunsFile.loadGuns();
        this.statsFile = new StatsFile(this); this.statsFile.loadStats();


        for (World world : Bukkit.getWorlds()){
            world.setGameRuleValue("keepInventory", "true");
            world.setGameRuleValue("naturalRegeneration", "false");
        }

        for (Player p : Bukkit.getOnlinePlayers()){
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            p.updateInventory();
            p.setWalkSpeed(0.2F);
            p.setFoodLevel(20);
            p.setAllowFlight(false);
            p.setPlayerListName(p.getName());
            p.setLevel(0);
            p.setHealth(20D);
            p.removePotionEffect(PotionEffectType.SLOW);
            p.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
        }

        new BukkitRunnable(){

            @Override
            public void run(){
                for (Player p : Bukkit.getOnlinePlayers()){
                    statsFile.loadStats(p);
                    statsFile.addScoreboardStatKeeper(new ScoreboardStatKeeper(core, p));
                    p.setScoreboard(statsFile.getScoreboardStatKeeperFor(p).getScoreboard());
                }
            }

        }.runTaskLater(this, 10L);

        getCommand("lasertag").setExecutor(new LaserTag(this, arenasFile));
    }

    public void onDisable(){

        statsFile.saveStatsToConfig();

        for (World w : Bukkit.getWorlds()) {
            for (Entity e : w.getEntities()){
                if (e instanceof EnderCrystal) e.remove();
            }
        }
    }

    public ArenasFile getArenasFile(){
        return this.arenasFile;
    }

    public GunsFile getGunsFile(){
        return this.gunsFile;
    }

    public StatsFile getStatsFile(){
        return this.statsFile;
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

    public StatKeeper getStatKeeperFor(Player player){
        return statsFile.getStatKeeperFor(player);
    }

    public Player getPlayerFromUUID(String uuid){

        for (Player p :Bukkit.getOnlinePlayers()){
            if (p.getUniqueId().toString().equalsIgnoreCase(uuid)) return p;
        }
        return null;
    }
}