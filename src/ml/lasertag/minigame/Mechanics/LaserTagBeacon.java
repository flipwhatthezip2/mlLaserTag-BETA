package ml.lasertag.minigame.Mechanics;


import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.GameManager.Arena;
import ml.lasertag.minigame.api.PacketSender;
import ml.lasertag.minigame.game.TEAM;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R2.EnumParticle;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;

public class LaserTagBeacon {

    Core core;

    private Arena arena;
    private TEAM team;

    private int initialHealth = 20;
    private int health = 20;
    private int life = 5;
    private Location beaconLocation;
    private EnderCrystal beacon;
    private ArrayList<Block> indicatorRing = new ArrayList<Block>();

    public LaserTagBeacon(Core core, Arena arena, TEAM team){
        this.core = core;
        this.arena = arena;
        this.team = team;

        this.beaconLocation = core.getArenasFile().getBeacon(arena.getProperties().getArenaName(), team).getBlock().getLocation().add(0.5, 0, 0.5);
        this.beacon = (EnderCrystal) arena.getProperties().getWorld().spawnEntity(beaconLocation, EntityType.ENDER_CRYSTAL);

        this.establishIndicatorRing();
        this.setRing(BeaconLife.GREEN);

        beacon.setCustomName((team == TEAM.YELLOW ? ChatColor.YELLOW + "Yellow" : ChatColor.DARK_GREEN + "Green") + " Team's Beacon");
        beacon.setCustomNameVisible(true);
    }

    public void establishIndicatorRing(){
        indicatorRing.clear();

        indicatorRing.add(beaconLocation.getBlock().getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN));
        indicatorRing.add(beaconLocation.getBlock().getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN));
        indicatorRing.add(beaconLocation.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN));
        indicatorRing.add(beaconLocation.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN));

        indicatorRing.add(beaconLocation.getBlock().getRelative(BlockFace.NORTH_EAST).getRelative(BlockFace.DOWN));
        indicatorRing.add(beaconLocation.getBlock().getRelative(BlockFace.NORTH_WEST).getRelative(BlockFace.DOWN));
        indicatorRing.add(beaconLocation.getBlock().getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.DOWN));
        indicatorRing.add(beaconLocation.getBlock().getRelative(BlockFace.SOUTH_WEST).getRelative(BlockFace.DOWN));
    }

    public void setLife(int life){
        switch (life){
            case 5:
                setRing(BeaconLife.GREEN);
                break;
            case 4:
                setRing(BeaconLife.YELLOW);
                break;
            case 3:
                setRing(BeaconLife.ORANGE);
                break;
            case 2:
                setRing(BeaconLife.RED);
                break;
            case 1:
                setRing(BeaconLife.BLACK);
                break;
            case 0:
                setRing(BeaconLife.BEDROCK);
                this.death();
                break;
        }
    }

    public void setRing(BeaconLife beaconLife){
        Material material = Material.WOOL;
        byte id = 0;

        if (beaconLife == BeaconLife.GREEN) id = 5;
        else if (beaconLife == BeaconLife.YELLOW) id = 4;
        else if (beaconLife == BeaconLife.ORANGE) id = 1;
        else if (beaconLife == BeaconLife.RED) id = 14;
        else if (beaconLife == BeaconLife.BLACK) id = 15;
        else if (beaconLife == BeaconLife.BEDROCK){ id = 0; material = Material.BEDROCK;}

        for (Block b : indicatorRing){
            b.setType(material);
            b.setData(id);

            arena.getProperties().getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getTypeId(), 20);
        }
    }

    public void death(){
        arena.getProperties().getWorld().playEffect(beaconLocation, Effect.EXPLOSION_LARGE, 20, 20);
        PacketSender.broadcastSound("mob.enderdragon.death", arena.getProperties().getWorld(), 100);

        arena.endGame();
    }

    public void reset(){
        health = initialHealth;
        setLife(5);
    }

    public void dealDamage(int damage){
        PacketSender.playParticle(EnumParticle.EXPLOSION_HUGE, beaconLocation.clone().add(0.5, 2, 0.5), 1);
        arena.getProperties().getWorld().playSound(beaconLocation, Sound.IRONGOLEM_HIT, 100, 1);

        health = health - damage;

        if (health >= 16) this.setLife(5);
        else if (health >= 12) this.setLife(4);
        else if (health >= 8) this.setLife(3);
        else if (health >= 4) this.setLife(2);
        else if (health > 0) this.setLife(1);
        else if (health == 0) this.setLife(0);

        arena.getScoreboard().updateHealth(this);

    }

    public void resetHealth(){
        health = initialHealth;
        this.setLife(5);
    }

    public void setDeathRing(){
        for (Block b : indicatorRing){
            b.setType(Material.BEDROCK);
            b.setData((byte) 0);
        }
    }

    public Arena getArena(){
        return this.arena;
    }

    public TEAM getTeam(){
        return this.team;
    }

    public EnderCrystal getBeacon(){
        return this.beacon;
    }

    public int getHealth(){
        return this.health;
    }

    public enum BeaconLife {
        GREEN, YELLOW, ORANGE, RED, BLACK, BEDROCK;
    }

}
