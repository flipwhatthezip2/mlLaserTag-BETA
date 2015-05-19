package ml.lasertag.minigame.game;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public enum TEAM {
    YELLOW(Color.YELLOW), GREEN(Color.LIME);

    private Color color;

    private TEAM(Color color){
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }

    public static void setUniform(Player player){
        TEAM team = Teams.getTeam(player);

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET); LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setColor(team.getColor()); helmet.setItemMeta(helmetMeta);

        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE); LeatherArmorMeta chestMeta = (LeatherArmorMeta) chest.getItemMeta();
        chestMeta.setColor(team.getColor()); chest.setItemMeta(chestMeta);

        ItemStack pants = new ItemStack(Material.LEATHER_LEGGINGS); LeatherArmorMeta pantsMeta = (LeatherArmorMeta) pants.getItemMeta();
        pantsMeta.setColor(team.getColor()); pants.setItemMeta(pantsMeta);

        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS); LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsMeta.setColor(team.getColor()); boots.setItemMeta(bootsMeta);

        org.bukkit.inventory.PlayerInventory inv = player.getInventory();
        inv.setBoots(boots); inv.setLeggings(pants); inv.setChestplate(chest); inv.setHelmet(helmet);

        player.updateInventory();

    }
}