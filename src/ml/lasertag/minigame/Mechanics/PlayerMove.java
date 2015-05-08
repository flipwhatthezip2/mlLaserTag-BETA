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


import ml.lasertag.minigame.api.Feature;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener{

 @EventHandler
 public void PlayerMove(PlayerMoveEvent e){
  for (Player p : Bukkit.getOnlinePlayers()){
   Feature.sendActionBar(p, "§f§lCurrently playing §c§lLASERTAG §f§lon §6§lMineLegends");
  }
 }

}