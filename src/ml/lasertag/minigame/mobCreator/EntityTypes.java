package ml.lasertag.minigame.mobCreator;

import net.minecraft.server.v1_8_R2.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;

import java.lang.reflect.Field;
import java.util.Map;

public enum EntityTypes {

    AS_VILLAGER("Villager", 120, ArenaSelectorVillager.class);

    private EntityTypes(String name, int id, Class<? extends Entity> custom){
        addToMaps(custom, name, id);
    }

    public static Entity spawnEntity(Entity entity, Location loc){
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        (((CraftWorld) loc.getWorld())).getHandle().addEntity(entity);
        return entity;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void addToMaps(Class clazz, String name, int id){
        ((Map)getPrivateField("c", net.minecraft.server.v1_8_R2.EntityTypes.class, null)).remove(clazz);
        ((Map)getPrivateField("d", net.minecraft.server.v1_8_R2.EntityTypes.class, null)).remove(clazz);
        ((Map)getPrivateField("e", net.minecraft.server.v1_8_R2.EntityTypes.class, null)).remove(clazz);
        ((Map)getPrivateField("f", net.minecraft.server.v1_8_R2.EntityTypes.class, null)).remove(clazz);
        ((Map)getPrivateField("g", net.minecraft.server.v1_8_R2.EntityTypes.class, null)).remove(clazz);

        ((Map)getPrivateField("c", net.minecraft.server.v1_8_R2.EntityTypes.class, null)).put(name, clazz);
        ((Map)getPrivateField("d", net.minecraft.server.v1_8_R2.EntityTypes.class, null)).put(clazz, name);
        ((Map)getPrivateField("e", net.minecraft.server.v1_8_R2.EntityTypes.class, null)).put(Integer.valueOf(id), clazz);
        ((Map)getPrivateField("f", net.minecraft.server.v1_8_R2.EntityTypes.class, null)).put(clazz, Integer.valueOf(id));
        ((Map)getPrivateField("g", net.minecraft.server.v1_8_R2.EntityTypes.class, null)).put(name, Integer.valueOf(id));
    }

    public static Object getPrivateField(String fieldName, Class<net.minecraft.server.v1_8_R2.EntityTypes> class1, Object object){
        Field field;
        Object o = null;

        try
        {
            field = class1.getDeclaredField(fieldName);

            field.setAccessible(true);

            o = field.get(object);
        }
        catch(NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return o;
    }

}
