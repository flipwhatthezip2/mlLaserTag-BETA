package ml.lasertag.minigame.Mechanics;

import ml.lasertag.minigame.api.Feature;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;

public class Restrictions implements Listener {


    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e){
        if(e.getEntity() instanceof Player){
            e.setCancelled(true);
            Player p = (Player)e.getEntity();
            p.setFoodLevel(20);
            p.setSaturation(20F);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        e.setCancelled(true);
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
        if (e.getCause() != EntityDamageEvent.DamageCause.VOID) e.setCancelled(true);
    }

    @EventHandler
    public void weatherChange (WeatherChangeEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e){
        Player player = e.getPlayer();

        Feature.sendTitle(player, 0, 0, 0, "", "");
    }

    @EventHandler
    public void onInventoryMove(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        Inventory inventory = e.getClickedInventory();
        if (inventory != null && inventory.getName() != null && inventory.getName().contains("LASERTAG")){
            if (e.getCurrentItem() != null){
                if (e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE){
                    p.sendMessage("§4§l>> §cHow to play §lLASERTAG!§c:");
                    p.sendMessage("§4§l> §7When you first join a game you are put one of §c2 teams");
                    p.sendMessage("§4§l> §7Both teams have associated colors, §2§lGREEN §7and §e§lYELLOW");
                    p.sendMessage("§4§l> §7You spawn in with 1 essential item, your §claser gun");
                    p.sendMessage("§4§l> §7The object of the game is destroy the other teams §cbeacon");
                    p.sendMessage("§4§l> §7To destroy the beacon you must shoot at it with your laser");
                    p.sendMessage("§4§l> §7Every §c4 hits §7the beacon takes causes it to loose 1 health");
                    p.sendMessage("§4§l> §7Each teams beacon has §c5 lives");
                    p.sendMessage("§4§l> §7Kill other players to weaken their teams beacon.");
                    p.sendMessage("§4§l> §7First team to destroy the other teams beacon in §c5 min §7wins!");
                    p.sendMessage("§4§l> Camping spawns and beacons result in debuffs, beware!");
                    e.setCancelled(true);
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