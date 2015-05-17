package ml.lasertag.minigame.Mechanics;

import org.bukkit.GameMode;
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
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemGrab(PlayerPickupItemEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.FALL) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryMove(InventoryClickEvent e){
        e.setCancelled(true);
        Inventory inventory = e.getClickedInventory();

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