package ml.lasertag.minigame.GameManager;

import ml.lasertag.minigame.Core;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/*

------------------------
    mlLaserTag
------------------------
 Created by: cfletcher
 Created on: 5/16/15
------------------------
All code below is owned by
Cameron Fletcher and is not
to be edited/ copied without
my (Cameron Fletcher)'s permission.

*/
public class ArenaSelector implements org.bukkit.event.Listener{

    Core core;

    public ArenaSelector(Core core){
        this.core = core;
    }

    // FLIP's Area:
    public Inventory arenaSelectorMenu = Bukkit.createInventory(null, 27, "§4§lLASERTAG! §8Pick an Arena");
    {
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§cHow to play:");
        List<String> lore = new ArrayList<>();
        lore.add("§7When you first join a game you are put one of 2 teams");
        lore.add("§7Both teams have associated colors, §2§lGREEN §7and §e§lYELLOW§7");
        lore.add("§7The objective of the game is to destroy the other teams §bbeacon");
        lore.add("§7When you hit the other teams beacon it will disable all lasers.");
        lore.add("§7Hit the beacon §b4 times §7for it to loose one life");
        lore.add("§7Killing the other teams players will weaken that teams beacon.");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        arenaSelectorMenu.setItem(26, itemStack);
    }

    // READY's Area:
    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e){
        Player p = e.getPlayer();
        Entity entity = e.getRightClicked();

        e.setCancelled(true);

        if (entity.getCustomName().equalsIgnoreCase("§4§lLASERTAG §8- §cSelect an arena!")){
            if (p.getItemInHand() != null && p.getItemInHand().getType() == Material.BLAZE_ROD && p.isOp()){
                entity.remove();
                p.sendMessage(Core.success + "Removed ArenaSelector NPC");
                return;
            }
            p.openInventory(arenaSelectorMenu);
        }
    }


}
