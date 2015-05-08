package ml.lasertag.minigame.game;

import org.bukkit.Color;
import org.bukkit.DyeColor;

public enum TEAM {
 YELLOW(Color.YELLOW, DyeColor.YELLOW), GREEN(Color.LIME, DyeColor.LIME);

 private Color laserColor;
 private DyeColor uniformColor;

 private TEAM(Color laserColor, DyeColor uniformColor){
  this.laserColor = laserColor;
  this.uniformColor = uniformColor;
 }

 public Color getLaserColor(){
  return this.laserColor;
 }
}