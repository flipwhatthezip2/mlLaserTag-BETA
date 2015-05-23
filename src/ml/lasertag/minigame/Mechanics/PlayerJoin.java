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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoin implements Listener {

    Core core;

    public PlayerJoin(Core core){
        this.core = core;
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e){
        final Player p = e.getPlayer();

        e.setJoinMessage(Core.infoMessage + "§6" + p.getName() + " §7has joined the server.");

        Feature.sendTitle(p, 20, 200, 20, "§eWelcome to §c§lLASER TAG!", "§eA §6§lMineLegends §ecustom game!");

        Feature.sendTabTitle(p, "§cPlaying on §lLASERTAG! §8- §c§lLASERTAG!", "§eA §6§lMineLegends §eoriginal game!");

        e.getPlayer().teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        if (Arena.getArena(core, e.getPlayer()) != null){
            Arena.leaveArena(core, Arena.getArena(core, e.getPlayer()), e.getPlayer());
        }
        e.setQuitMessage(Core.infoMessage + "§6" + e.getPlayer().getName() + " §7has left the server.");
    }

}