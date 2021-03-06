package ml.lasertag.minigame.GameManager;


import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.events.ArenaInteractEvent;
import ml.lasertag.minigame.events.GunStatUpdate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GunSelector implements Listener{

    Core core;
    public Inventory gunSelectorMenu = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&1&lLASERTAG! &9Pick a Gun"));


    public GunSelector(Core core){
        this.core = core;

        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cHow to play:"));
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click me to learn &bhow to play&7!"));
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        gunSelectorMenu.setItem(26, itemStack);

        new BukkitRunnable(){

            @Override
            public void run(){
                displayGuns();
            }

        }.runTaskLater(core, 10L);
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

        for (Player p : Bukkit.getOnlinePlayers()){
            if (Arena.getArena(core, p) != null){
                if (Gun.getGun(p) == e.getGun()){
                    if (p.getInventory().getItem(0).getType() == Material.IRON_BARDING){
                        ItemMeta meta = p.getInventory().getItem(0).getItemMeta();

                        meta.setDisplayName(e.getGun().getGun().getItemMeta().getDisplayName());
                        meta.setLore(e.getGun().getGun().getItemMeta().getLore());

                        p.getInventory().getItem(0).setItemMeta(meta);

                        p.updateInventory();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onGunClick(InventoryClickEvent e){
        if (e.getWhoClicked() instanceof Player){
            Player player = (Player) e.getWhoClicked();

            if ((Arena.getArena(core, player) == null) || (Arena.getArena(core, player) != null && player.getGameMode() == GameMode.CREATIVE)){

                ItemStack item = e.getCurrentItem();

                if (item != null && item.getType() == Material.IRON_BARDING){
                    Bukkit.dispatchCommand(player, "lasertag selectGun " + ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                    player.closeInventory();
                    e.setCancelled(true);

                    if (Arena.getArena(core, player) != null){
                        player.getInventory().setItem(0, Gun.getGun(player).getGun());
                    }

                }

            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e){
        Player player = e.getPlayer();
        ItemStack item = player.getItemInHand();

        if (item != null && item.getType() == Material.NETHER_STAR){
            player.openInventory(gunSelectorMenu);
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e){
        Player p = e.getPlayer();
        Entity entity = e.getRightClicked();

        e.setCancelled(true);

        if (entity.getCustomName() != null && entity.getCustomName().equalsIgnoreCase(org.bukkit.ChatColor.BLUE +"Select a Gun!")){
            if (p.getItemInHand() != null && p.getItemInHand().getType() == Material.BLAZE_ROD && p.isOp()){
                entity.remove();
                p.sendMessage(Core.infoMessage + "Removed GunSelector NPC");
                return;
            }
            p.openInventory(gunSelectorMenu);
        }
    }

}
