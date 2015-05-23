package ml.lasertag.minigame.mobCreator;


import net.minecraft.server.v1_8_R2.DamageSource;
import net.minecraft.server.v1_8_R2.Entity;
import net.minecraft.server.v1_8_R2.EntityVillager;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;


public class GunSelectorVillager extends EntityVillager {

    public GunSelectorVillager(org.bukkit.World world){
        super(((CraftWorld) world).getHandle());
        this.setCustomName(ChatColor.DARK_BLUE.toString() + ChatColor.BOLD + "LASERTAG" + ChatColor.BLUE +" - Select a Gun!");
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

    @Override
    public void move(double d0, double d1, double d2){
        return;
    }

}
