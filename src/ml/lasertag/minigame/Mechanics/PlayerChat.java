package ml.lasertag.minigame.Mechanics;

/*
 Project Info:
 Project Name: mlLaserTag
 Package: ml.LaserTag.minigame.Mechanics
 Created By: cfletcher
 Created On: 5/3/15
 All code is licenced to the following people:
 - Cameron Fletcher
 -
 */


import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.GameManager.Arena;
import ml.lasertag.minigame.game.TEAM;
import ml.lasertag.minigame.game.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener{

    Core core;

    public PlayerChat(Core core){
        this.core = core;
    }

    @EventHandler
    public void PlayerChat(AsyncPlayerChatEvent e){
        String message = e.getMessage();
        Player player = e.getPlayer();

        Arena arena;

        if (Arena.getArena(core, e.getPlayer()) == null){
            e.setFormat(ChatColor.RED + player.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + e.getMessage());
            return;
        }

        e.setCancelled(true);
        arena = Arena.getArena(core, e.getPlayer());

        if (arena.getTeams().getTeam(player) == TEAM.GREEN){
            for (Player p : Bukkit.getOnlinePlayers()){
                p.sendMessage("§2[GREEN] " + e.getPlayer().getName() + "§8: §f" + message);
            }
            return;
        } else if (arena.getTeams().getTeam(player) == TEAM.YELLOW){
            for (Player p : Bukkit.getOnlinePlayers()){
                p.sendMessage("§e[YELLOW] " + e.getPlayer().getName() + "§8: §f" + message);
            }
            return;
        } else {
            for (Player p : Bukkit.getOnlinePlayers()){
                p.sendMessage("§c" + e.getPlayer().getName() + "§8: §f" + message);
            }
            return;
        }
    }

}