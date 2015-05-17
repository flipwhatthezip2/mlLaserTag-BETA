package ml.lasertag.minigame.GameManager;

import ml.lasertag.minigame.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

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
    // TODO: Flip add code here:

    // READY's Area:
    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e){

    }


}
