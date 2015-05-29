package ml.lasertag.minigame.GameManager;

import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.events.ArenaInteractEvent;
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
import org.bukkit.scheduler.BukkitRunnable;

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
public class ArenaSelector implements Listener {

    Core core;
    public Inventory arenaSelectorMenu = Bukkit.createInventory(null, 27, "§4§lLASERTAG! §8Pick an Arena");


    public ArenaSelector(Core core){
        this.core = core;

        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§cHow to play:");
        List<String> lore = new ArrayList<>();
        lore.add("§7Click me to learn §bhow to play§7!");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        arenaSelectorMenu.setItem(26, itemStack);

        new BukkitRunnable(){

            @Override
            public void run(){
                displayArenas();
            }

        }.runTaskLater(core, 10L);
    }

    public void displayArenas(){

        for (ItemStack item : arenaSelectorMenu.getContents()){
            if (item != null && item.getType() != Material.STAINED_GLASS_PANE) arenaSelectorMenu.remove(item);
        }

        int index = 0;

        for (Arena arena : core.getArenasFile().getArenas()){

            ItemStack item = new ItemStack(arena.getCanJoin() ? Material.SUGAR : Material.REDSTONE);
            ItemMeta im = item.getItemMeta();

            im.setDisplayName("§4§l"  + arena.getProperties().getArenaName());
            List<String> lore = new ArrayList<>();
            lore.add("§cPlayers: §f" + arena.getPlayers().size() + "§7/§f" + arena.getProperties().getMaximumPlayers());
            lore.add("§cArena Status: §f" + arena.getArenaState().toString());
            lore.add("§cMap Name: §f" + arena.getProperties().getWorld().getName());
            lore.add("§cPlayers Needed: §f" + (arena.getPlayers().size() <= arena.getProperties().getMinimumPlayers() ?
                    (arena.getProperties().getMinimumPlayers() - arena.getPlayers().size()) : "0"));
            im.setLore(lore);

            item.setItemMeta(im);
            arenaSelectorMenu.setItem(index, item);

            index++;
        }

    }

    @EventHandler
    public void onArenaInteract(ArenaInteractEvent e){
        this.displayArenas();
    }

    @EventHandler
    public void onArenaClick(InventoryClickEvent e){
        if (e.getWhoClicked() instanceof Player){
            Player player = (Player) e.getWhoClicked();

            if (Arena.getArena(core, player) == null){

                ItemStack item = e.getCurrentItem();

                if (item != null && item.getType() == Material.SUGAR){
                    Bukkit.dispatchCommand(player, "lasertag join " + ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                    e.setCancelled(true);
                }

            }
        }
    }

    // READY's Area:
    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e){
        Player p = e.getPlayer();
        Entity entity = e.getRightClicked();

        e.setCancelled(true);

        if (entity.getCustomName() != null && entity.getCustomName().equalsIgnoreCase("§4§lLASERTAG §8- §cSelect an arena!")){
            if (p.getItemInHand() != null && p.getItemInHand().getType() == Material.BLAZE_ROD && p.isOp()){
                entity.remove();
                p.sendMessage(Core.infoMessage + "Removed ArenaSelector NPC");
                return;
            }
            p.openInventory(arenaSelectorMenu);
        }
    }


}
