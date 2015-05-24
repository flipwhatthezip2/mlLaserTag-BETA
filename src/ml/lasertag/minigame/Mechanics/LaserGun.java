package ml.lasertag.minigame.Mechanics;

import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.GameManager.Arena;
import ml.lasertag.minigame.GameManager.Gun;
import ml.lasertag.minigame.api.Feature;
import ml.lasertag.minigame.api.PacketSender;
import ml.lasertag.minigame.events.LaserDamageBeaconEvent;
import ml.lasertag.minigame.game.TEAM;
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
    public int livesRemaining;

    public LaserGun(Core core){
        this.core = core;
    }

    public void resetCooldowns(){
        cantShoot.clear();
    }

    @EventHandler
    public void onShoot(final PlayerInteractEvent e){

        final Player player = e.getPlayer();

        if (e.getAction().toString().contains("RIGHT") && player.getItemInHand().getType() == Material.IRON_BARDING
                && !cantShoot.contains(player) && Arena.getArena(core, player) != null &&
                Arena.getArena(core, e.getPlayer()).getArenaState() == Arena.ArenaState.IN_GAME &&
                player.getGameMode() == GameMode.ADVENTURE){
            cantShoot.add(player);
            player.getInventory().setItem(0, Feature.removeGlow(player.getInventory().getItem(0)));
            player.updateInventory();
            shootLaser(player);
            new BukkitRunnable(){

                @Override
                public void run(){
                    cantShoot.remove(player);

                    if (Arena.getArena(core, player) == null){
                        this.cancel();
                        return;
                    }

                    player.getInventory().setItem(0, Feature.addGlow(player.getInventory().getItem(0)));
                    player.updateInventory();
                }

            }.runTaskLater(core, Gun.getGun(player).getCooldown());
        }
    }

    public void shootLaser(Player player){

        Arena arena;

        if (Arena.getArena(core, player) == null){
            return;
        }

        arena = Arena.getArena(core, player);

        List<Player> list = player.getWorld().getPlayers();
        List<EnderCrystal> beacons = new ArrayList<EnderCrystal>();
        Location l = player.getEyeLocation();

        int range = Gun.getGun(player).getRange();

        for (org.bukkit.entity.Entity e : player.getWorld().getEntities()){
            if (e.getLocation().toVector().distance(l.toVector()) >= range) list.remove(e);
            else if (e instanceof EnderCrystal) beacons.add((EnderCrystal) e);

        }


        for (double a = 0; a < range; a+= 0.5){

            double r = arena.getTeams().getTeam(player).getColor().getRed();
            double g = arena.getTeams().getTeam(player).getColor().getGreen();
            double b = arena.getTeams().getTeam(player).getColor().getBlue();

            Location loc = l.clone().add(l.getDirection().multiply(a));

            if (loc.getBlock().getType().isSolid()) return;

            for (Player e : list){
                if (!e.isDead() && e != player && e.getGameMode() == GameMode.ADVENTURE && Arena.getArena(core, e) != null){
                    if (e.getLocation().toVector().distance(loc.toVector()) <= 2){
                        damage(arena, e, player);
                    }
                }
            }

            for (EnderCrystal e : beacons){
                for (Arena ar : core.getArenasFile().getArenas()){
                    if (arena.getTeams().getTeam(player) == TEAM.YELLOW){
                        if (arena.getGreenBeacon().getBeacon() == e && e.getLocation().toVector().distance(loc.toVector()) <= 2){
                            arena.getGreenBeacon().dealDamage(Gun.getGun(player).getCooldown() / 10);
                            Bukkit.getPluginManager().callEvent(new LaserDamageBeaconEvent(arena.getGreenBeacon(), player));
                            return;
                        }
                    }
                    else {
                        if (arena.getYellowBeacon().getBeacon() == e && e.getLocation().toVector().distance(loc.toVector()) <= 2){
                            arena.getYellowBeacon().dealDamage(Gun.getGun(player).getCooldown() / 10);
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

    public void damage(Arena arena, Player player, Player killer){

        if (arena.getTeams().getTeam(player) == arena.getTeams().getTeam(killer)) return;

        if (player.getHealth() <= Gun.getGun(killer).getDamage()) awardKill(arena, player, killer);

        player.damage(Gun.getGun(killer).getDamage());
        player.getWorld().playSound(player.getEyeLocation(), Sound.IRONGOLEM_HIT, 100L, 100L);

        Location loc = player.getEyeLocation();

        final Firework fw = player.getWorld().spawn(loc, Firework.class);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(
                arena.getTeams().getTeam(player).getColor().getRed(),
                arena.getTeams().getTeam(player).getColor().getGreen(),
                arena.getTeams().getTeam(player).getColor().getBlue())).build());

        fw.setFireworkMeta(fwm);

        Bukkit.getScheduler().scheduleSyncDelayedTask(core, new Runnable(){

            @Override
            public void run(){
                fw.detonate();
            }

        }, 2L);
    }

    public void awardKill(final Arena arena, final Player victim, Player killer){
        arena.broadcastMessage(Core.deathMessage + "§6" + victim.getName() + " §7has been vaporized by §6" + killer.getName() + "§7.");
        victim.setGameMode(GameMode.SPECTATOR);
        victim.setLevel(5);
        Feature.sendTitle(victim, 5, 100, 5, "§4§lYOU DIED!", "§cRespawning in §l" + victim.getLevel() + " seconds§c...");

        if (cantShoot.contains(victim)) cantShoot.remove(victim);

        new BukkitRunnable(){

            public void run(){
                Feature.sendSubtitle(victim, 5, 20, 5, "§cRespawning in §l" + victim.getLevel() + " seconds§c...");
                victim.setLevel(victim.getLevel() - 1);

                if (Arena.getArena(core, victim) == null){
                    victim.spigot().respawn();
                    victim.setGameMode(GameMode.ADVENTURE);
                    Feature.sendTitle(victim, 5, 100, 5, "", "");
                    this.cancel();
                    return;
                }

                if (Arena.getArena(core, victim).getArenaState() == Arena.ArenaState.RESTARTING){
                    victim.spigot().respawn();
                    victim.setGameMode(GameMode.ADVENTURE);
                    this.cancel();
                }

                if (victim.getLevel() <= 0){
                    victim.spigot().respawn();
                    victim.teleport(Arena.getArena(core, victim).getSpawn(arena.getTeams().getTeam(victim)));
                    victim.setGameMode(GameMode.ADVENTURE);
                    victim.setHealth(20D);
                    Feature.sendTitle(victim, 5, 200, 5, "", "");
                    this.cancel();
                }
            }

        }.runTaskTimer(core, 0, 20);
    }

}