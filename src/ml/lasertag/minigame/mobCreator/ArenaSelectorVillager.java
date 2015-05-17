package ml.lasertag.minigame.mobCreator;

import net.minecraft.server.v1_8_R2.DamageSource;
import net.minecraft.server.v1_8_R2.Entity;
import net.minecraft.server.v1_8_R2.EntityVillager;
import net.minecraft.server.v1_8_R2.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;

public class ArenaSelectorVillager extends EntityVillager{

    public ArenaSelectorVillager(org.bukkit.World world){
        super(((CraftWorld) world).getHandle());
        this.setCustomName("§4§lLASERTAG §8- §cSelect an arena!");
        this.setCustomNameVisible(true);
    }

    @Override
    public void collide(Entity entity){
        return;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f){
        return true;
    }

    public static void spawnEntity(Entity entity, Location loc){
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        (((CraftWorld) loc.getWorld())).getHandle().addEntity(entity);
    }

}
