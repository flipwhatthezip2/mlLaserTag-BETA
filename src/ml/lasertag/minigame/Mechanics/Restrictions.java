package ml.lasertag.minigame.Mechanics;

import ml.lasertag.minigame.Core;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;

public class Restrictions implements Listener {


    @EventHandler
    public void onFoodDrain(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        if (player.getGameMode() != GameMode.CREATIVE && !player.isOp()) e.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE){
            e.setCancelled(false);
            return;
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e){
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE){
            e.setCancelled(false);
            return;
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemGrab(PlayerPickupItemEvent e){
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE){
            e.setCancelled(false);
            return;
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.FALL) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e){
        e.setCancelled(true);
    }

    // TODO: NPE @ ml.lasertag.minigame.GameManager.ArenaSelector.onRightClick(ArenaSelector.java:60)

    @EventHandler
    public void onInventoryMove(InventoryClickEvent e){
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        Inventory inventory = e.getClickedInventory();
        if (inventory.getName().equalsIgnoreCase("§4§lLASERTAG! §8Pick an Arena")){
            if (e.getCurrentItem() != null){
                if (e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE){
                    p.sendMessage(Core.warning + "How to play §lLASERTAG!§c:");
                    p.sendMessage(Core.warningList + "§7When you first join a game you are put one of §c2 teams");
                    p.sendMessage(Core.warningList + "§7Both teams have associated colors, §2§lGREEN §7and §e§lYELLOW");
                    p.sendMessage(Core.warningList + "§7You spawn in with 1 essential item, your §claser gun");
                    p.sendMessage(Core.warningList + "§7The object of the game is destroy the other teams §cbeacon");
                    p.sendMessage(Core.warningList + "§7To destroy the beacon you must shoot at it with your laser");
                    p.sendMessage(Core.warningList + "§7Every §c4 hits §7the beacon takes causes it to loose 1 health");
                    p.sendMessage(Core.warningList + "§7Each teams beacon has §c5 lives");
                    p.sendMessage(Core.warningList + "§7Kill other players to weaken their teams beacon.");
                    p.sendMessage(Core.warningList + "§7First team to destroy the other teams beacon in §c5 min §7wins!");
                    p.sendMessage(Core.warningList + "Camping spawns and beacons result in debuffs, beware!");
                }
            }
        }

        /*

        lore.add("§7When you first join a game you are put one of 2 teams");
        lore.add("§7Both teams have associated colors, §2§lGREEN §7and §e§lYELLOW§7");
        lore.add("§7The objective of the game is to destroy the other teams §bbeacon");
        lore.add("§7When you hit the other teams beacon it will disable all lasers.");
        lore.add("§7Hit the beacon §b4 times §7for it to loose one life");
        lore.add("§7Killing the other teams players will weaken that teams beacon.");

         */

    }

}