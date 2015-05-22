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


import gameAPI.Flipwhatthezip2.chat.ChatStrings;
import ml.lasertag.minigame.GameManager.ArenaSelector;
import ml.lasertag.minigame.GameManager.ArenasFile;
import ml.lasertag.minigame.Mechanics.LaserGun;
import ml.lasertag.minigame.Mechanics.PlayerChat;
import ml.lasertag.minigame.Mechanics.PlayerJoin;
import ml.lasertag.minigame.Mechanics.Restrictions;
import ml.lasertag.minigame.api.Feature;
import ml.lasertag.minigame.commands.LaserTag;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {

    public ArenasFile arenasFile;
    public LaserGun laserGun;

    public static String info = "§7§l>> §e";
    public static String infoList = "§7§l> §e";
    public static String success = "§2§l>> §a";
    public static String successList = "§2§l> §a";
    public static String warning = "§4§l>> §c";
    public static String warningList = "§4§l> §c";

    public static String joinMessage = ChatStrings.joinMessage;
    public static String quitMessage = ChatStrings.quitMessage;

    public static String deathMessage = ChatStrings.deathMessage;
    public static String killMessage = ChatStrings.killMessage;

    public static String teamMessage = ChatStrings.teamMessage;
    public static String gameMessage = ChatStrings.gameMessage;
    public static String statusMessage = ChatStrings.statusMessage;
    public static String rechargingMessage = ChatStrings.rechargingMessage;
    public static String powerupMessage = ChatStrings.powerupMessage;

    public static String infoMessage = ChatStrings.infoMessage;
    public static String serverMessage = ChatStrings.serverMessage;
    public static String portalMessage = ChatStrings.portalMessage;

    public void onEnable(){

        ChatStrings.setJoinMessage("§3Join> §7");
        ChatStrings.setQuitMessage("§3Quit> §7");
        ChatStrings.setDeathMessage("§3Death> §7");
        ChatStrings.setKillMessage("§3Kill> §7");
        ChatStrings.setTeamMessage("§3Team> §7");
        ChatStrings.setGameMessage("§3Game> §7");
        ChatStrings.setStatusMessage("§3Status> §7");
        ChatStrings.setRechargingMessage("§3Recharging> §7");
        ChatStrings.setPowerupMessage("§3Powerup> §7");
        ChatStrings.setInfoMessage("§3Info> §7");
        ChatStrings.setServerMessage("§3Server> §7");
        ChatStrings.setPortalMessage("§3Portal> §7");

        Bukkit.getServer().getLogger().info(ChatStrings.serverMessage() + "Successfully initialized the minigame 'LaserTag' on server: " + Bukkit.getServerName());

        this.arenasFile = new ArenasFile(this);
        this.arenasFile.loadArenas();

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

        for (Player p : Bukkit.getOnlinePlayers()){
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            p.updateInventory();

            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            p.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
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
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChat(this), this);
        Bukkit.getPluginManager().registerEvents(new ArenaSelector(this), this);
        Bukkit.getPluginManager().registerEvents(new Restrictions(), this);
    }
}