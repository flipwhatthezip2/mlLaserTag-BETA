package ml.lasertag.minigame.GameManager;

import ml.lasertag.minigame.Core;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Gun {

    static Core core;

    private GunsFile gunsFile;

    private String name;
    private int cooldown;
    private int range;
    private int damage;

    private ArrayList<Player> users = new ArrayList<Player>();

    public Gun(Core core, GunsFile gunsFile, String name, int cooldown, int range, int damage){
        this.core = core; this.gunsFile = gunsFile;
        this.name = name; this.cooldown = cooldown; this.range = range; this.damage = damage;
    }

    public ItemStack getGun(){
        ItemStack gun = new ItemStack(Material.IRON_BARDING); ItemMeta meta = gun.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&l" + name));

        meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&cCooldown&f: " + ((double) cooldown/20) + " second(s)"),
                                    ChatColor.translateAlternateColorCodes('&', "&cRange&f: " + range + " block(s)"),
                                    ChatColor.translateAlternateColorCodes('&', "&cDamage&f: " + damage + " HP")));

        gun.setItemMeta(meta);

        return gun;
    }

    public void addUser(Player player){
        users.add(player);
    }

    public void removeUser(Player player){
        users.remove(player);
    }

    public String getName(){
        return this.name;
    }

    public int getCooldown(){
        return this.cooldown;
    }

    public int getRange(){
        return this.range;
    }

    public int getDamage(){
        return this.damage;
    }

    public List<Player> getUsers(){
        return this.users;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setCooldown(int cooldown){
        this.cooldown = cooldown;
    }

    public void setRange(int range){
        this.range = range;
    }

    public void setDamage(int damage){
        this.damage = damage;
    }

    public static Gun getGun(String name){
        for (Gun gun : core.getGunsFile().getGuns()) if (gun.getName().equalsIgnoreCase(ChatColor.stripColor(name))) return gun;
        return null;
    }

    public static Gun getGun(Player player){
        for (Gun gun : core.getGunsFile().getGuns()) if (gun.getUsers().contains(player)) return gun;
        return null;
    }

}
