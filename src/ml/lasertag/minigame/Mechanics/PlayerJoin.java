package ml.lasertag.minigame.Mechanics;

/*
 Project Info:
 Project Name: mlLaserTag
 Package: ml.LaserTag.minigame.events
 Created By: cfletcher
 Created On: 4/27/15
 All code is licenced to the following people:
 - Cameron Fletcher
 -
 */


import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.GameManager.Arena;
import ml.lasertag.minigame.api.Feature;
import ml.lasertag.minigame.events.PlayerStatUpdateEvent;
import ml.lasertag.minigame.stats.ScoreboardStatKeeper;
import ml.lasertag.minigame.stats.StatKeeper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerJoin implements Listener {

    Core core;

    public PlayerJoin(Core core){
        this.core = core;
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e){
        final Player p = e.getPlayer();

        e.setJoinMessage(Core.joinMessage + "§2" + p.getName() + " §7has joined the server.");

        Feature.sendTitle(p, 20, 200, 20, "§eWelcome to §c§lLASER TAG!", "§eA §6§lMineLegends §ecustom game!");

        Feature.sendTabTitle(p, "§cPlaying on §lLASERTAG! §8- §c§lLASERTAG!", "§eA §6§lMineLegends §eoriginal game!");

        p.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
        p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 5, 20));

        handleStatScoreboard(p);
    }

    public void handleStatScoreboard(final Player player){

        new BukkitRunnable(){

            @Override
            public void run(){
                core.getStatsFile().loadStats(player);
                core.getStatsFile().addScoreboardStatKeeper(new ScoreboardStatKeeper(core, player));
                player.setScoreboard(core.getStatsFile().getScoreboardStatKeeperFor(player).getScoreboard());
            }

        }.runTaskLater(core, 10L);

    }

    @EventHandler
    public void onStatUpdate(PlayerStatUpdateEvent e){
        StatKeeper sk = core.getStatKeeperFor(e.getPlayer());
        ScoreboardStatKeeper ssk = core.getStatsFile().getScoreboardStatKeeperFor(e.getPlayer());

        ssk.update();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        if (Arena.getArena(core, e.getPlayer()) != null){
            Arena.leaveArena(core, Arena.getArena(core, e.getPlayer()), e.getPlayer());
        }
        e.setQuitMessage(Core.quitMessage + "§c" + e.getPlayer().getName() + " §7has left the server.");
    }

}