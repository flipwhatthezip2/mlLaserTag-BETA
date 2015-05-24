package ml.lasertag.minigame.Mechanics;


import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.GameManager.Arena;
import ml.lasertag.minigame.GameManager.Gun;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AimDownSights implements Listener{

    Core core;

    public AimDownSights(Core core){
        this.core = core;
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e){

        Player player = e.getPlayer();

        if (Arena.getArena(core, player) != null) {
            if (player.getItemInHand() != null && player.getItemInHand().getType() == Material.IRON_BARDING) {
                if (!player.isSneaking()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000, Gun.getGun(player).getZoom()));
                }
                else {
                    player.removePotionEffect(PotionEffectType.SLOW);
                }
            }
        }

    }

    @EventHandler
    public void onSlotChange(PlayerItemHeldEvent e){

        Player player = e.getPlayer();

        if (Arena.getArena(core, player) != null && player.getInventory().contains(Material.IRON_BARDING) && Arena.getArena(core, player).getArenaState() == Arena.ArenaState.IN_GAME){

            if (e.getNewSlot() != 0){
                player.removePotionEffect(PotionEffectType.SLOW);
            }
            else {
                if (player.isSneaking()){
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000, Gun.getGun(player).getZoom()));
                }
            }

        }

    }

}
