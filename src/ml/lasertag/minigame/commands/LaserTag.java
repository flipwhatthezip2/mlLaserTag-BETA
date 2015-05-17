package ml.lasertag.minigame.commands;


/* 
 Project Info:
 Project Name: mlLaserTag
 Package: ml.LaserTag.minigame.commands
 Created By: cfletcher
 Created On: 4/26/15
 All code is licenced to the following people:
 - Cameron Fletcher
 -
 */


import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.GameManager.Arena;
import ml.lasertag.minigame.GameManager.ArenaProperties;
import ml.lasertag.minigame.GameManager.ArenasFile;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LaserTag implements CommandExecutor {

 Core core;
 private ArenasFile arenasFile;

 public LaserTag(Core core, ArenasFile arenasFile){
  this.core = core;
  this.arenasFile = arenasFile;
 }

 @Override
 public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
  if (cmd.getName().equalsIgnoreCase("lasertag") && args.length == 0){
   lasertag(sender);
  }
  else if (args[0].equalsIgnoreCase("help")){lasertaghelp(sender); return false;}
  else if (args[0].equalsIgnoreCase("debug")){lasertagdebug(sender); return false;}
  else if (args[0].equalsIgnoreCase("arena")){lasertagarena(sender, args); return false;}
  else if (args[0].equalsIgnoreCase("announce")){lasertagAnnounce(sender); return false;}
  else if (args[0].equalsIgnoreCase("join")){tryJoining(sender, args);}
  else if (args[0].equalsIgnoreCase("leave")){tryLeaving(sender, args);}
  return false;
 }

 public void tryJoining(CommandSender sender, String[] args){

  if (!(sender instanceof Player)){
   sender.sendMessage(Core.warning + "Only players can join arenas");
   return;
  }

  if (args.length != 2){
   sender.sendMessage(Core.warning + "Invalid argument amount");
   return;
  }

  for (String string : arenasFile.getArenaNames()){
   sender.sendMessage(string);
  }

  if (!arenasFile.getArenaNames().contains(args[1])){
   sender.sendMessage(Core.warning + "Specified arena does not exist");
   return;
  }

  if (!Arena.getArena(args[1]).getCanJoin()){
   sender.sendMessage(Core.warning + "Specified arena is currently not joinable");
   return;
  }

  Arena.joinArena(Arena.getArena(args[1]), (Player) sender);

 }

 public void tryLeaving(CommandSender sender, String[] args){

  if (!(sender instanceof Player)){
   sender.sendMessage(Core.warning + "Only players can leave arenas");
   return;
  }

  Player player = (Player) sender;

  if (args.length != 2){
   sender.sendMessage(Core.warning + "Invalid argument amount");
   return;
  }

  if (Arena.getArena(player) == null){
   sender.sendMessage(Core.warning + "You're not in an arena");
   return;
  }

  Arena.leaveArena(Arena.getArena(args[1]), (Player) sender);

 }

 public void lasertagAnnounce(CommandSender sender) {
  Bukkit.getServer().broadcastMessage("§e" + sender.getName() + "§8: §f§lStarting a new round! §fCome join me!");
 }
 public void lasertag(CommandSender sender){
  sender.sendMessage(Core.info + "Currently running §cLaser Tag §lBETA");
  sender.sendMessage(Core.info + "Developed by: §cFlipwhatthezip2 §eand §cReadySetPawn");
  sender.sendMessage(Core.info + "For a list of commands type: §c/lasertag §lhelp");
 }
 public void lasertagdebug(CommandSender sender){
  sender.sendMessage(Core.info + "Displaying debug info:");
  sender.sendMessage(Core.info + "Currently running build §c§lLS.b.01");
  sender.sendMessage(Core.info + "Report reference #: §c1.b.a");
  sender.sendMessage(Core.info + "Current game status: §c§lN/A");
 }
 public void lasertagarena(CommandSender sender, String[] args){
  if (args.length == 1) {
   sender.sendMessage(Core.info + "Commands for §c§lLASERTAG ARENAS§e:");
   sender.sendMessage(Core.infoList + "/lasertag arena create [arena] [world] §8- §7Creates arena.");
   sender.sendMessage(Core.infoList + "/lasertag arena delete [arena] §8- §7Deletes arena.");
   sender.sendMessage(Core.infoList + "/lasertag arena maxPlayer [arena] [maxPlayers] §8- §7Sets max players.");
   sender.sendMessage(Core.infoList + "/lasertag arena minPlayer [arena] [minPlayers] §8- §7Sets min players.");
   sender.sendMessage(Core.infoList + "/lasertag arena setYellowSpawn [arena] §8- §7Sets spawn.");
   sender.sendMessage(Core.infoList + "/lasertag arena setGreenSpawn [arena] §8- §7Sets spawn.");
   return;
  }

  else if (args.length == 3 || args.length == 4){
   if ((!(args[1].equalsIgnoreCase("create") || args[1].equalsIgnoreCase("maxPlayers") ||
           args[1].equalsIgnoreCase("minPlayers"))) && args.length == 4) {
    sender.sendMessage(Core.warning + "Invaild argument ammount.");
    return;
   }
  }

  if (args[1].equalsIgnoreCase("create")){

   if (args.length != 3){
    sender.sendMessage(Core.warning + "Invalid argument amount");
    return;
   }

   if (arenasFile.getArenaNames().contains(args[2])){
    sender.sendMessage(Core.warning + "Invaild arena name. Is it created?");
    return;
   }

   if (Bukkit.getWorld(args[2]) == null){
    sender.sendMessage(Core.warning + "Invaild world name. Make sure you input the correct world name.");
    return;
   }

   World world = Bukkit.getWorld(args[2]);

   ArenaProperties ap = new ArenaProperties(world, 4, 12, args[2]);
   Arena arena = new Arena(ap, core.getLaserGun());
   core.getArenasFile().addArena(arena);

   sender.sendMessage(Core.success + "Successfully created the arena: §l" + args[2] + "§a!");
   sender.sendMessage(Core.success + "To make the arena valid please configure!");
  }

  else if (args[1].equalsIgnoreCase("delete")){
   core.getArenasFile().deleteArena(Arena.getArena(args[2]));
   sender.sendMessage(Core.success + "Successfully deleted the arena: §l" + args[2] + "§a!");
  }

  else if (args[1].equalsIgnoreCase("minPlayers") || args[1].equalsIgnoreCase("maxPlayers")){

   try {
    Integer.parseInt(args[3]);
   } catch (NumberFormatException e){
    sender.sendMessage(Core.warning + "3rd argument is an invaild integer.");
    return;
   }

   ArenaProperties ap = Arena.getArena(args[2]).getProperties();

   if (args[1].equalsIgnoreCase("minPlayers")){
    ap.setMinimumPlayers(Integer.parseInt(args[3]));
    sender.sendMessage(Core.success + "Successfully set the §lMIN §aplayers!");
   }

   if (args[1].equalsIgnoreCase("maxPlayers")){
    ap.setMaximumPlayers(Integer.parseInt(args[3]));
    sender.sendMessage(Core.success + "Successfully set the §lMAX §aplayers!");
   }

  }

  else if (args[1].equalsIgnoreCase("setYellowSpawn") || args[1].equalsIgnoreCase("setGreenSpawn")){
   if (!(sender instanceof Player)){
    sender.sendMessage(Core.warning + "Spawn setting failed. You must be ingame to set a spawn!");
    return;
   }

   Player player = (Player) sender;

   if (args[1].equalsIgnoreCase("setYellowSpawn")){
    core.getArenasFile().setYellowSpawn(args[2], player.getLocation());
    sender.sendMessage(Core.success + "Successfully set the §e§lYELLOW §aspawn!");
   }

   if (args[1].equalsIgnoreCase("setGreenSpawn")){
    core.getArenasFile().setGreenSpawn(args[2], player.getLocation());
    sender.sendMessage(Core.success + "Successfully set the §2§lGREEN §aspawn!");
   }
  }

 }
 public void lasertaghelp(CommandSender sender){
  sender.sendMessage(Core.info + "Commands for §c§lLASERTAG§e:");
  sender.sendMessage(Core.infoList + "/lasertag debug §8- §7Displays the current builds debug info.");
  sender.sendMessage(Core.infoList + "/lasertag info §8- §7Displays the plugin info.");
  sender.sendMessage(Core.infoList + "/lasertag arena §8- §7Displays all arenas commands.");
  sender.sendMessage(Core.infoList + "/lasertag game §8- §7Displays the current games info.");
  sender.sendMessage(Core.infoList + "/lasertag start §8- §7Force starts the current game.");
  sender.sendMessage(Core.infoList + "/lasertag stop §8- §7Force stops the current game.");
 }
}
