package ml.lasertag.minigame.GameManager;


import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.events.ArenaInteractEvent;
import ml.lasertag.minigame.events.GunStatUpdate;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GunSelector implements Listener{

    Core core;
    public Inventory gunSelectorMenu = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&1&lLASERTAG! &9Pick a Gun"));


    public GunSelector(Core core){
        this.core = core;
        this.displayGuns();

        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cHow to play:"));
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click me to learn &bhow to play&7!"));
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        gunSelectorMenu.setItem(26, itemStack);
    }

    public void displayGuns(){

        for (ItemStack item : gunSelectorMenu.getContents()){
            if (item != null && item.getType() != Material.STAINED_GLASS_PANE) gunSelectorMenu.remove(item);
        }

        int index = 0;

        for (Gun gun : core.getGunsFile().getGuns()){

            gunSelectorMenu.setItem(index, gun.getGun());

            index = index + 2;
        }

    }

    @EventHandler
    public void onGunStatUpdate(GunStatUpdate e){
        this.displayGuns();
    }

    @EventHandler
    public void onGunClick(InventoryClickEvent e){
        if (e.getWhoClicked() instanceof Player){
            Player player = (Player) e.getWhoClicked();

            if (Arena.getArena(core, player) == null){

                ItemStack item = e.getCurrentItem();

                if (item != null && item.getType() == Material.IRON_BARDING){
                    Bukkit.dispatchCommand(player, "lasertag selectGun " + ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                    e.setCancelled(true);
                }

            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e){
        Player p = e.getPlayer();
        Entity entity = e.getRightClicked();

        e.setCancelled(true);

        if (entity.getCustomName() != null && entity.getCustomName().equalsIgnoreCase(ChatColor.DARK_BLUE.toString() + org.bukkit.ChatColor.BOLD + "LASERTAG" + org.bukkit.ChatColor.BLUE +" - Select a Gun!")){
            if (p.getItemInHand() != null && p.getItemInHand().getType() == Material.BLAZE_ROD && p.isOp()){
                entity.remove();
                p.sendMessage(Core.success + "Removed GunSelector NPC");
                return;
            }
            p.openInventory(gunSelectorMenu);
        }
    }

}
