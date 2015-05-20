package ml.lasertag.minigame.api;


import net.minecraft.server.v1_8_R2.EnumParticle;
import net.minecraft.server.v1_8_R2.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_8_R2.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;


public class PacketSender {

    public static void playParticle(EnumParticle particle, Location loc, int amount){

        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true,
                                                loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 0, 0, 0, 0, amount, 0);

        for (Player p : loc.getWorld().getPlayers()){
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }

    }

    public static void playSound(String sound, Location loc, float volume){
        PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(sound,
                                            loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), volume, 1);

        for (Player p : loc.getWorld().getPlayers()){
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public static void broadcastSound(String sound, World world, float volume){

        for (Player player : world.getPlayers()){
            playSound(sound, player.getLocation(), volume);
        }

    }

}
