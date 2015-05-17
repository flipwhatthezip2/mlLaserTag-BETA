package ml.lasertag.minigame.Mechanics;

import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.GameManager.Arena;
import ml.lasertag.minigame.api.Feature;
import ml.lasertag.minigame.game.Teams;
import net.minecraft.server.v1_8_R2.EnumParticle;
import net.minecraft.server.v1_8_R2.PacketPlayOutWorldParticles;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LaserGun implements Listener {

 Core core;

 public ArrayList<Player> cantShoot = new ArrayList<Player>();
 public int coolDown = 20;
 public int livesRemaining;

 public LaserGun(Core core){
  this.core = core;
 }

 public void resetCooldowns(){
  cantShoot.clear();
 }

 @EventHandler
 public void onShoot(final PlayerInteractEvent e){
  if (e.getAction() == Action.RIGHT_CLICK_AIR && e.getPlayer().getItemInHand().getType() == Material.IRON_BARDING
          && !cantShoot.contains(e.getPlayer()) && Arena.getArena(core, e.getPlayer()) != null &&
          Arena.getArena(core, e.getPlayer()).isPvpEnabled()){
   shootLaser(e.getPlayer());
   cantShoot.add(e.getPlayer());
   e.getPlayer().setLevel(0);
   new BukkitRunnable(){

    @Override
    public void run(){
     cantShoot.remove(e.getPlayer());
     e.getPlayer().setLevel(1);
    }

   }.runTaskLater(core, coolDown);
  }
 }

 public void shootLaser(Player player){

  List<Player> list = player.getWorld().getPlayers();
  Location l = player.getEyeLocation();

  int range = 50;

  for (org.bukkit.entity.Entity e : player.getWorld().getLivingEntities()){
   if (e.getLocation().toVector().distance(l.toVector()) >= range) list.remove(e);
  }


  for (double a = 0; a < range; a+= 0.1){

   double r = Teams.getTeam(player).getColor().getRed();
   double g = Teams.getTeam(player).getColor().getGreen();
   double b = Teams.getTeam(player).getColor().getBlue();

   Location loc = l.add(l.getDirection().multiply(a));

   for (Player e : list){
    if (!e.isDead() && e != player){
     if (e.getLocation().toVector().distance(loc.toVector()) <= 1.5){
      damage(e, player);
     }
    }
   }

   PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true,
           (float) (loc.getX()),
           (float) (loc.getY()),
           (float) (loc.getZ()),
           (float) r,
           (float) g,
           (float) b,
           1, 0);

   for (Player online : player.getWorld().getPlayers()){
    ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
   }
  }
 }

 public void damage(Player player, Player killer){

  if (player.getHealth() <= 8) awardKill(player, killer);

  player.damage(8);
  player.getWorld().playSound(player.getEyeLocation(), Sound.IRONGOLEM_HIT, 100L, 100L);

  Location loc = player.getEyeLocation();

  final Firework fw = player.getWorld().spawn(loc, Firework.class);
  FireworkMeta fwm = fw.getFireworkMeta();

  fwm.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(
          Teams.getTeam(player).getColor().getRed(),
          Teams.getTeam(player).getColor().getGreen(),
          Teams.getTeam(player).getColor().getBlue())).build());

  fw.setFireworkMeta(fwm);

  Bukkit.getScheduler().scheduleSyncDelayedTask(core, new Runnable(){

   @Override
   public void run(){
    fw.detonate();
   }

  }, 2L);
 }

 public void awardKill(Player victim, Player killer){
  livesRemaining = victim.getLevel();
  Bukkit.getServer().broadcastMessage(Core.warning + "§l" +victim.getName() + " §chas felt the deadly wrath of §l" + killer.getName() + "§c.");
  victim.sendMessage(Core.warning + "You have §l" + livesRemaining + " §clives remaining.");
  Feature.sendTitle(victim, 5, 200, 5, "§4§lYOU DIED!", "§cYou have §l" + livesRemaining + " §cremaining lives.");


  if (victim.getLevel() != 0){
   victim.spigot().respawn();
   victim.setLevel(victim.getLevel() - 1);
  }

  else {
   victim.sendMessage(Core.warning + "You have §lNO §clives remaining. You are out for this game.");
   Feature.sendTitle(victim, 5, 200, 5, "§4§lYOUR OUT!", "§cYou do not have any lives left.");
  }
 }

}