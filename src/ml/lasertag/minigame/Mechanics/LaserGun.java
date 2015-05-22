package ml.lasertag.minigame.Mechanics;

import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.GameManager.Arena;
import ml.lasertag.minigame.api.Feature;
import ml.lasertag.minigame.events.LaserDamageBeaconEvent;
import ml.lasertag.minigame.game.TEAM;
import ml.lasertag.minigame.game.Teams;
import net.minecraft.server.v1_8_R2.EnumParticle;
import net.minecraft.server.v1_8_R2.PacketPlayOutWorldParticles;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

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
            new BukkitRunnable(){

                @Override
                public void run(){
                    cantShoot.remove(e.getPlayer());
                }

            }.runTaskLater(core, coolDown);
        }
    }

    public void shootLaser(Player player){

        List<Player> list = player.getWorld().getPlayers();
        List<EnderCrystal> beacons = new ArrayList<EnderCrystal>();
        Location l = player.getEyeLocation();

        int range = 25;

        for (org.bukkit.entity.Entity e : player.getWorld().getEntities()){
            if (e.getLocation().toVector().distance(l.toVector()) >= range) list.remove(e);
            else if (e instanceof EnderCrystal) beacons.add((EnderCrystal) e);

        }


        for (double a = 0; a < range; a+= 0.05){

            double r = Teams.getTeam(player).getColor().getRed();
            double g = Teams.getTeam(player).getColor().getGreen();
            double b = Teams.getTeam(player).getColor().getBlue();

            Location loc = l.add(l.getDirection().multiply(a));

            if (loc.getBlock().getType() != Material.AIR) return;

            for (Player e : list){
                if (!e.isDead() && e != player && e.getGameMode() == GameMode.ADVENTURE && Arena.getArena(core, e) != null){
                    if (e.getLocation().toVector().distance(loc.toVector()) <= 1.5){
                        damage(e, player);
                    }
                }
            }

            for (EnderCrystal e : beacons){
                for (Arena arena : core.getArenasFile().getArenas()){
                    if (Teams.getTeam(player) == TEAM.YELLOW){
                        if (arena.getGreenBeacon().getBeacon() == e && e.getLocation().toVector().distance(loc.toVector()) <= 1.5){
                            arena.getGreenBeacon().dealDamage(1);
                            Bukkit.getPluginManager().callEvent(new LaserDamageBeaconEvent(arena.getGreenBeacon(), player));
                            return;
                        }
                    }
                    else {
                        if (arena.getYellowBeacon().getBeacon() == e && e.getLocation().toVector().distance(loc.toVector()) <= 1.5){
                            arena.getYellowBeacon().dealDamage(1);
                            Bukkit.getPluginManager().callEvent(new LaserDamageBeaconEvent(arena.getYellowBeacon(), player));
                            return;
                        }
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

        if (Teams.getTeam(player) == Teams.getTeam(killer)) return;

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

    public void awardKill(final Player victim, Player killer){
        Bukkit.getServer().broadcastMessage(Core.warning + "§l" + victim.getName() + " §chas felt the deadly wrath of §l" + killer.getName() + "§c.");
        victim.setGameMode(GameMode.SPECTATOR);
        victim.setLevel(10);

        new BukkitRunnable(){

            public void run(){
                Feature.sendTitle(victim, 5, 200, 5, "§4§lYOU DIED!", "§cRespawning in §l" + victim.getLevel() + " seconds§c...");
                victim.setLevel(victim.getLevel() - 1);

                if (Arena.getArena(core, victim) == null){
                    victim.spigot().respawn();
                    victim.setGameMode(GameMode.ADVENTURE);
                    Feature.sendTitle(victim, 5, 200, 5, "", "");
                    this.cancel();
                }

                if (victim.getLevel() <= 0){
                    victim.setGameMode(GameMode.ADVENTURE);
                    victim.spigot().respawn();
                    victim.setHealth(20D);
                    victim.teleport(Arena.getArena(core, victim).getSpawn(Teams.getTeam(victim)));
                    Feature.sendTitle(victim, 5, 200, 5, "", "");
                    this.cancel();
                }
            }

        }.runTaskTimer(core, 0, 20);
    }

}