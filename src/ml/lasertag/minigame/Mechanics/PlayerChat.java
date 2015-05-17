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


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener{

    @EventHandler
    public void PlayerChat(AsyncPlayerChatEvent e){
        String message = e.getMessage();
        for (Player p : Bukkit.getOnlinePlayers()){
            p.sendMessage("§e" + e.getPlayer() + "§8: §7" + message);
        }
    }

}