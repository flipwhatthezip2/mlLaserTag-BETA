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
import ml.lasertag.minigame.api.Feature;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

 int start = 1; // starting point in your phrase, word, or letter
 int spaces = 20; // for spaces


 @EventHandler
 public void PlayerJoin(PlayerJoinEvent e){
  final Player p = e.getPlayer();

  e.setJoinMessage(Core.success + "§l" + p.getName() + " §ahas joined the server.");

  Feature.sendTitle(p, 20, 200, 20, "§eWelcome to §c§lLASER TAG!", "§eA §6§lMineLegends §ecustom game!");
  Feature.sendActionBar(p, "§2§l>> §aJoined server: §l" + Bukkit.getServerName());

  Feature.sendTabTitle(p, "§6§lMineLegends! §8- §c§lLASERTAG!", "§eA §6§lMineLegends §eoriginal game!");

 }

}