package ml.lasertag.minigame.Mechanics;

import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.GameManager.Arena;
import ml.lasertag.minigame.GameManager.Gun;
import ml.lasertag.minigame.api.Feature;
import ml.lasertag.minigame.api.PacketSender;
import ml.lasertag.minigame.events.LaserDamageBeaconEvent;
import ml.lasertag.minigame.game.TEAM;
import ml.lasertag.minigame.stats.StatKeeper;
import net.minecraft.server.v1_8_R2.EnumParticle;
import net.minecraft.server.v1_8_R2.PacketPlayOutWorldParticles;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class LaserGun implements Listener {

    Core core;

    public ArrayList<Player> cantShoot = new ArrayList<Player>();
    public ArrayList<Player> cantJump = new ArrayList<Player>();

    public LaserGun(Core core){
        this.core = core;
    }

    public void resetCooldowns(Arena arena){
        for (Player p : arena.getPlayers()){
            if (cantShoot.contains(p)) cantShoot.remove(p);
        }
    }

    public void resetJumps(Arena arena){
        for (Player p : arena.getPlayers()){
            if (cantJump.contains(p)) cantJump.remove(p);
        }
    }

    @EventHandler
    public void onBeaconDamage(LaserDamageBeaconEvent e){
        StatKeeper sk = core.getStatKeeperFor(e.getDamager());

        sk.addBeaconDamageDealt(e.getDamage());
    }

    @EventHandler
    public void onJump(PlayerToggleFlightEvent e){

        Player player = e.getPlayer();

        if (player.getGameMode() != GameMode.CREATIVE){
            e.setCancelled(true);
        }

        if ((!cantJump.contains(player)) && Arena.getArena(core, player) != null && Arena.getArena(core, player).getArenaState() == Arena.ArenaState.IN_GAME &&
                player.getGameMode() == GameMode.ADVENTURE && !player.isFlying()){
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setVelocity(player.getLocation().getDirection().multiply(4).setY(2));
            cantJump.add(player);
        }
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

        arena.getProperties().getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 20, 60);

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
                    if (e.getLocation().getBlock().getLocation().toVector().distance(loc.getBlock().getLocation().toVector()) <= 2){
                        damage(arena, e, player);
                        return;
                    }
                }
            }

            for (EnderCrystal e : beacons){
                for (Arena ar : core.getArenasFile().getArenas()){
                    if (arena.getTeams().getTeam(player) == TEAM.YELLOW){
                        if (arena.getGreenBeacon().getBeacon() == e && e.getLocation().toVector().distance(loc.toVector()) <= 2){
                            arena.getGreenBeacon().dealDamage(player, Gun.getGun(player).getCooldown() / 10);
                            Bukkit.getPluginManager().callEvent(new LaserDamageBeaconEvent(arena.getGreenBeacon(), player, Gun.getGun(player).getCooldown() / 10));
                            return;
                        }
                    }
                    else {
                        if (arena.getYellowBeacon().getBeacon() == e && e.getLocation().toVector().distance(loc.toVector()) <= 2){
                            arena.getYellowBeacon().dealDamage(player, Gun.getGun(player).getCooldown() / 10);
                            Bukkit.getPluginManager().callEvent(new LaserDamageBeaconEvent(arena.getYellowBeacon(), player, Gun.getGun(player).getCooldown() / 10));
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

        int damage = BeaconProtect.isInEnemyTerritory(killer) ? Gun.getGun(killer).getDamage() / 2 : Gun.getGun(killer).getDamage();

        core.getStatKeeperFor(killer).addDamageDealt(damage);
        core.getStatKeeperFor(player).addDamageTaken(damage);

        if (player.getHealth() <= damage) awardKill(arena, player, killer);

        long shotDistance = Math.round(player.getLocation().distance(killer.getLocation()));

        player.damage(damage);
        this.hitIndicator(killer);
    }

    public void awardKill(final Arena arena, final Player victim, Player killer){

        core.getStatKeeperFor(victim).addDeaths(1);
        core.getStatKeeperFor(killer).addKills(1);

        arena.broadcastMessage(Core.combatMessage + "§6" + victim.getName() + " §7has been vaporized by §6" +
                killer.getName() + "§7 " + "using §c§l" + Gun.getGun(killer).getName());
        victim.setLevel(6);
        victim.removePotionEffect(PotionEffectType.SLOW);
        this.setSpectator(victim);
        Feature.sendTitle(victim, 5, 100, 5, "§4§lYOU DIED!", "§cRespawning in §l" + victim.getLevel() + " seconds§c...");

        final Firework fw = victim.getWorld().spawn(victim.getEyeLocation(), Firework.class);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(
                arena.getTeams().getTeam(victim).getColor().getRed(),
                arena.getTeams().getTeam(victim).getColor().getGreen(),
                arena.getTeams().getTeam(victim).getColor().getBlue())).build());

        fw.setFireworkMeta(fwm);

        Bukkit.getScheduler().scheduleSyncDelayedTask(core, new Runnable() {

            @Override
            public void run() {
                fw.detonate();
            }

        }, 2L);

        if (cantShoot.contains(victim)) cantShoot.remove(victim);

        new BukkitRunnable(){

            public void run(){
                Feature.sendSubtitle(victim, 5, 20, 5, "§cRespawning in §l" + victim.getLevel() + " seconds§c...");

                if (Arena.getArena(core, victim) == null){
                    victim.spigot().respawn();
                    removeSpectator(victim);
                    Feature.sendTitle(victim, 5, 100, 5, "", "");
                    this.cancel();
                    return;
                }

                if (Arena.getArena(core, victim).getArenaState() == Arena.ArenaState.RESTARTING){
                    victim.spigot().respawn();
                    removeSpectator(victim);
                    this.cancel();
                }

                if (victim.getLevel() <= 0){
                    victim.spigot().respawn();
                    victim.teleport(Arena.getArena(core, victim).getSpawn(arena.getTeams().getTeam(victim)));
                    removeSpectator(victim);
                    victim.setHealth(20D);
                    Feature.sendTitle(victim, 5, 200, 5, "", "");
                    this.cancel();
                }

                victim.setLevel(victim.getLevel() - 1);

            }

        }.runTaskTimer(core, 0, 20);
    }

    public void setSpectator(Player player){

        ItemStack gunSelector = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = gunSelector.getItemMeta();

        meta.setDisplayName("§a§lGun Selector");

        gunSelector.setItemMeta(meta);

        player.setGameMode(GameMode.CREATIVE);
        player.setFlying(true);
        player.teleport(player.getLocation().add(0, 0.5, 0));
        player.getInventory().setItem(8, gunSelector);
        player.updateInventory();

        for (Player p : player.getWorld().getPlayers()){
            p.hidePlayer(player);
        }
    }

    public void removeSpectator(Player player){
        player.setGameMode(GameMode.ADVENTURE);
        player.setFlying(false);
        player.closeInventory();
        player.getInventory().setItem(8, null);
        player.updateInventory();
        player.setAllowFlight(true);
        cantJump.remove(player);

        for (Player p : player.getWorld().getPlayers()){
            p.showPlayer(player);
        }

        if (Arena.getArena(core, player) == null){
            player.setPlayerListName(player.getName());
            return;
        }
        else {
            TEAM team = Arena.getArena(core, player).getTeams().getTeam(player);
            player.setPlayerListName((team == TEAM.YELLOW ? ChatColor.YELLOW : ChatColor.GREEN) + player.getName());
        }
    }

    public void hitIndicator(Player player){
        PacketSender.sendSound(player, "random.break", 40);
    }

}