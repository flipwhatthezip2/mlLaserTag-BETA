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


import gameAPI.Flipwhatthezip2.GameAPI;
import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.GameManager.*;
import ml.lasertag.minigame.api.PacketSender;
import ml.lasertag.minigame.events.GunStatUpdate;
import ml.lasertag.minigame.mobCreator.ArenaSelectorVillager;
import ml.lasertag.minigame.mobCreator.EntityTypes;
import ml.lasertag.minigame.mobCreator.GunSelectorVillager;
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
        if (cmd.getName().equalsIgnoreCase("lasertag") && args.length == 0) {
            lasertag(sender);
        } else if (args[0].equalsIgnoreCase("debug")) tryDebug(sender);
        else if (args[0].equalsIgnoreCase("help")) lasertaghelp(sender);
        else if (args[0].equalsIgnoreCase("arena")) lasertagarena(sender, args);
        else if (args[0].equalsIgnoreCase("gun")) lasertaggun(sender, args);
        else if (args[0].equalsIgnoreCase("announce")) lasertagAnnounce(sender);
        else if (args[0].equalsIgnoreCase("join")) tryJoining(sender, args);
        else if (args[0].equalsIgnoreCase("leave")) tryLeaving(sender, args);
        else if (args[0].equalsIgnoreCase("selectGun")) trySelectingGun(sender, args);
        else if (args[0].equalsIgnoreCase("spawnArenaSelector")) trySpawningArenaSelector(sender, args);
        else if (args[0].equalsIgnoreCase("spawnGunSelector")) trySpawningGunSelector(sender, args);
        return false;
    }

    public void tryDebug(CommandSender sender) {

        Player player = (Player) sender;

        player.sendMessage(GameAPI.getInfoMessage() + "Test");

    }

    public void trySelectingGun(CommandSender sender, String[] args){

        if (args.length != 2){
            sender.sendMessage(Core.infoMessage + "Invalid argument amount. /lasertag selectGun [gun]");
            return;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(Core.infoMessage + "Only players can select guns");
            return;
        }

        Player player = (Player) sender;

        if (Arena.getArena(core, player) != null){
            player.sendMessage(Core.infoMessage + "You can't select a gun while in an arena");
            return;
        }

        if (!core.getGunsFile().contains(args[1])){
            player.sendMessage(Core.infoMessage + "Gun does not exist");
            return;
        }

        Gun.getGun(args[1]).addUser(player);
        player.sendMessage(Core.kitMessage + "Successfully chose gun §a" + Gun.getGun(args[1]).getName());
        PacketSender.sendSound(player, "random.orb", 100F);

    }

    public void trySpawningGunSelector(CommandSender sender, String[] args){
        if (!sender.isOp()){
            sender.sendMessage(Core.infoMessage + "You don't have permission to do this");
            return;
        }

        if (!(sender instanceof Player)){
            sender.sendMessage(Core.infoMessage + "Only players can spawn GunSelector NPC's");
        }

        Player player = (Player) sender;

        EntityTypes.spawnEntity(new GunSelectorVillager(player.getWorld()), player.getLocation());
        player.sendMessage(Core.gameMessage + "You have spawned §aGun Selector");
    }

    public void trySpawningArenaSelector(CommandSender sender, String[] args){
        if (!sender.isOp()){
            sender.sendMessage(Core.infoMessage + "You don't have permission to do this");
            return;
        }

        if (!(sender instanceof Player)){
            sender.sendMessage(Core.infoMessage + "Only players can spawn ArenaSelector NPC's");
        }

        Player player = (Player) sender;

        EntityTypes.spawnEntity(new ArenaSelectorVillager(player.getWorld()), player.getLocation());
        player.sendMessage(Core.gameMessage + "You have spawned §aArena Selector");
    }

    public void tryJoining(CommandSender sender, String[] args){

        if (!(sender instanceof Player)){
            sender.sendMessage(Core.infoMessage + "Only players can join arenas");
            return;
        }

        if (args.length != 2){
            sender.sendMessage(Core.infoMessage + "Invalid argument amount");
            return;
        }

        if (!arenasFile.getArenaNames().contains(args[1])){
            sender.sendMessage(Core.infoMessage + "Specified arena does not exist");
            return;
        }

        if (Arena.getArena(core, (Player) sender) != null){
            sender.sendMessage(Core.infoMessage + "You're already in an arena");
            return;
        }

        if (!Arena.getArena(core, args[1]).getCanJoin()){
            sender.sendMessage(Core.infoMessage + "Specified arena is currently not joinable");
            return;
        }

        Arena.joinArena(core, Arena.getArena(core, args[1]), (Player) sender);
        sender.sendMessage(Core.gameMessage + "You have joined arena §a" + args[1]);

    }

    public void tryLeaving(CommandSender sender, String[] args){

        if (!(sender instanceof Player)){
            sender.sendMessage(Core.infoMessage + "Only players can leave arenas");
            return;
        }

        Player player = (Player) sender;

        if (args.length != 1){
            sender.sendMessage(Core.infoMessage + "Invalid argument amount");
            return;
        }

        if (Arena.getArena(core, player) == null){
            sender.sendMessage(Core.infoMessage + "You're not in an arena");
            return;
        }

        Arena.leaveArena(core, Arena.getArena(core, player), player);
        sender.sendMessage(Core.gameMessage + "You have left arena §a" + args[1]);

    }

    public void lasertaggun(CommandSender sender, String[] args){

        if (!sender.isOp()){
            sender.sendMessage(Core.infoMessage + "You don't have permission to execute this command");
            return;
        }

        if (args.length == 1) {
            sender.sendMessage(Core.infoMessage + "Commands for §9§lLASERTAG GUNS§e:");
            sender.sendMessage(Core.infoMessage + "/lasertag gun create [name] [cooldown] [range] [damage] [zoom] §8- §7Creates gun.");
            sender.sendMessage(Core.infoMessage + "/lasertag gun delete [gun] §8- §7Deletes gun.");
            sender.sendMessage(Core.infoMessage + "/lasertag gun set [gun] [gunStat] [value] §8- §7Sets a stat.");
            sender.sendMessage(Core.infoMessage + "/lasertag gun rename [gun] [newName] §8- §7Renames a gun.");
            return;
        }

        if (args.length < 2){
            sender.sendMessage(Core.infoMessage + "Invaild argument ammount.");
            return;
        }

        if (args[1].equalsIgnoreCase("create")){

            if (args.length != 7){
                sender.sendMessage(Core.infoMessage + "Invaild argument ammount.");
                return;
            }

            if (core.getGunsFile().contains(args[2])){
                sender.sendMessage(Core.infoMessage + "Gun with this name already exists");
                return;
            }

            try {
                Integer.parseInt(args[3]);
                Integer.parseInt(args[4]);
                Integer.parseInt(args[5]);
                Integer.parseInt(args[6]);
            } catch (NumberFormatException e){
                sender.sendMessage(Core.infoMessage + "Some values are not valid integers");
                return;
            }

            core.getGunsFile().createGun(args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));
            sender.sendMessage(Core.infoMessage + "Successfully created gun");
            Bukkit.getPluginManager().callEvent(new GunStatUpdate(Gun.getGun(args[2])));
        }

        else if (args[1].equalsIgnoreCase("delete")){

            if (args.length != 3){
                sender.sendMessage(Core.infoMessage + "Invalid argument amount");
                return;
            }

            if (!core.getGunsFile().contains(args[2])){
                sender.sendMessage(Core.infoMessage + "Gun does not exist");
                return;
            }

            core.getGunsFile().deleteGun(args[2]);
            sender.sendMessage(Core.infoMessage + "Successfully deleted gun");
            Bukkit.getPluginManager().callEvent(new GunStatUpdate(Gun.getGun(args[2])));

        }

        else if (args[1].equalsIgnoreCase("set")){

            if (args.length != 5){
                sender.sendMessage(Core.infoMessage + "Invalid argument amount");
                return;
            }

            if (!core.getGunsFile().contains(args[2])){
                sender.sendMessage(Core.infoMessage + "Gun does not exist");
                return;
            }

            boolean isValidStat = false;
            GunsFile.GunStat gunStat = null;

            for (GunsFile.GunStat stat : GunsFile.GunStat.values()){
                if (stat.toString().equalsIgnoreCase(args[3])){
                    isValidStat = true;
                    gunStat = stat;
                }
            }

            if (!isValidStat){
                sender.sendMessage(Core.infoMessage + "Invalid GunStat. GunStats are CoolDown, Range, Damage, and Zoom");
                return;
            }

            try {
                Integer.parseInt(args[4]);
            } catch (NumberFormatException e){
                sender.sendMessage(Core.infoMessage + "4th argument is an invalid integer");
                return;
            }

            core.getGunsFile().setGunStat(args[2], gunStat, Integer.parseInt(args[4]));
            sender.sendMessage(Core.infoMessage + "Successfully set GunStat");
            Bukkit.getPluginManager().callEvent(new GunStatUpdate(Gun.getGun(args[2])));

        }

        else if (args[1].equalsIgnoreCase("rename")){

            if (args.length != 4){
                sender.sendMessage(Core.infoMessage + "Invalid argument amount");
                return;
            }

            if (!core.getGunsFile().contains(args[2])){
                sender.sendMessage(Core.infoMessage + "Gun does not exist");
                return;
            }

            core.getGunsFile().setGunName(args[2], args[3]);
            sender.sendMessage(Core.infoMessage + "Successfully renamed gun");
            Bukkit.getPluginManager().callEvent(new GunStatUpdate(Gun.getGun(args[2])));

        }


    }

    public void lasertagAnnounce(CommandSender sender) {
        Bukkit.getServer().broadcastMessage("§e" + sender.getName() + "§8: §f§lStarting a new round! §fCome join me!");
    }
    public void lasertag(CommandSender sender){
        sender.sendMessage(Core.infoMessage + "Currently running §cLaser Tag §lBETA");
        sender.sendMessage(Core.infoMessage + "Developed by: §cFlipwhatthezip2 §eand §cReadySetPawn");
        sender.sendMessage(Core.infoMessage + "For a list of commands type: §c/lasertag §lhelp");
    }
    public void lasertagarena(CommandSender sender, String[] args){

        if (!sender.isOp()){
            sender.sendMessage(Core.infoMessage + "You don't have permission to execute this command");
            return;
        }

        if (args.length == 1) {
            sender.sendMessage(Core.infoMessage + "Commands for §c§lLASERTAG ARENAS§e:");
            sender.sendMessage(Core.infoMessage + "/lasertag arena create [arena] [world] §8- §7Creates arena.");
            sender.sendMessage(Core.infoMessage + "/lasertag arena delete [arena] §8- §7Deletes arena.");
            sender.sendMessage(Core.infoMessage + "/lasertag arena maxPlayer [arena] [maxPlayers] §8- §7Sets max players.");
            sender.sendMessage(Core.infoMessage + "/lasertag arena minPlayer [arena] [minPlayers] §8- §7Sets min players.");
            sender.sendMessage(Core.infoMessage + "/lasertag arena setYellowSpawn [arena] §8- §7Sets spawn.");
            sender.sendMessage(Core.infoMessage + "/lasertag arena setGreenSpawn [arena] §8- §7Sets spawn.");
            return;
        }

        else if (args.length == 3 || args.length == 4){
            if ((!(args[1].equalsIgnoreCase("create") || args[1].equalsIgnoreCase("maxPlayers") ||
                    args[1].equalsIgnoreCase("minPlayers"))) && args.length == 4) {
                sender.sendMessage(Core.infoMessage + "Invaild argument ammount.");
                return;
            }
        }

        if (args[1].equalsIgnoreCase("create")){

            if (args.length != 3){
                sender.sendMessage(Core.infoMessage + "Invalid argument amount");
                return;
            }

            if (arenasFile.getArenaNames().contains(args[2])){
                sender.sendMessage(Core.infoMessage + "Invaild arena name. Is it created?");
                return;
            }

            if (Bukkit.getWorld(args[2]) == null){
                sender.sendMessage(Core.infoMessage + "Invaild world name. Make sure you input the correct world name.");
                return;
            }

            World world = Bukkit.getWorld(args[2]);

            ArenaProperties ap = new ArenaProperties(core, world, 4, 12, args[2]);
            Arena arena = new Arena(core, ap, core.getLaserGun());
            core.getArenasFile().addArena(arena);

            sender.sendMessage(Core.gameMessage + "Successfully created the arena: §a§l" + args[2] + "§7!");
            sender.sendMessage(Core.gameMessage + "To make the arena valid please configure!");
        }

        else if (args[1].equalsIgnoreCase("delete")){

            if (Arena.getArena(core, args[2]) == null){
                sender.sendMessage(Core.infoMessage + "Specified arena does not exist");
                return;
            }

            core.getArenasFile().deleteArena(Arena.getArena(core, args[2]));
            sender.sendMessage(Core.infoMessage + "Successfully deleted the arena: §a§l" + args[2] + "§7!");
        }

        else if (args[1].equalsIgnoreCase("minPlayers") || args[1].equalsIgnoreCase("maxPlayers")){

            if (args.length != 4){
                sender.sendMessage(Core.infoMessage + "Invalid argument amount");
                return;
            }

            try {
                Integer.parseInt(args[3]);
            } catch (NumberFormatException e){
                sender.sendMessage(Core.infoMessage + "4th argument is an invaild integer.");
                return;
            }

            ArenaProperties ap = Arena.getArena(core, args[2]).getProperties();

            if (args[1].equalsIgnoreCase("minPlayers")){
                ap.setMinimumPlayers(Integer.parseInt(args[3]));
                sender.sendMessage(Core.infoMessage + "Successfully set the §a§lMIN §7players!");
            }

            if (args[1].equalsIgnoreCase("maxPlayers")){
                ap.setMaximumPlayers(Integer.parseInt(args[3]));
                sender.sendMessage(Core.infoMessage + "Successfully set the §a§lMAX §7players!");
            }

        }

        else if (args[1].equalsIgnoreCase("setYellowSpawn") || args[1].equalsIgnoreCase("setGreenSpawn")){

            if (!(sender instanceof Player)){
                sender.sendMessage(Core.infoMessage + "Spawn setting failed. You must be ingame to set a spawn!");
                return;
            }

            Player player = (Player) sender;

            if (args.length != 3){
                sender.sendMessage(Core.infoMessage + "Invalid argument amount");
                return;
            }

            if (!arenasFile.getArenaNames().contains(args[2])){
                sender.sendMessage(Core.infoMessage + "Specified arena does not exist");
                return;
            }

            if (args[1].equalsIgnoreCase("setYellowSpawn")){
                core.getArenasFile().setYellowSpawn(args[2], player.getLocation());
                sender.sendMessage(Core.infoMessage + "Successfully set the §e§lYELLOW §7spawn!");
            }

            if (args[1].equalsIgnoreCase("setGreenSpawn")){
                core.getArenasFile().setGreenSpawn(args[2], player.getLocation());
                sender.sendMessage(Core.infoMessage + "Successfully set the §2§lGREEN §7spawn!");
            }
        }

        else if (args[1].equalsIgnoreCase("setYellowBeacon") || args[1].equalsIgnoreCase("setGreenBeacon")){

            if (!(sender instanceof Player)){
                sender.sendMessage(Core.infoMessage + "Beacon setting failed. You must be ingame to set a spawn!");
                return;
            }

            Player player = (Player) sender;

            if (args.length != 3){
                sender.sendMessage(Core.infoMessage + "Invalid argument amount");
                return;
            }

            if (!arenasFile.getArenaNames().contains(args[2])){
                sender.sendMessage(Core.infoMessage + "Specified arena does not exist");
                return;
            }

            if (args[1].equalsIgnoreCase("setYellowBeacon")){
                core.getArenasFile().setYellowBeacon(args[2], player.getLocation());
                sender.sendMessage(Core.infoMessage + "Successfully set the §e§lYELLOW §7beacon!");
            }

            if (args[1].equalsIgnoreCase("setGreenBeacon")){
                core.getArenasFile().setGreenBeacon(args[2], player.getLocation());
                sender.sendMessage(Core.infoMessage + "Successfully set the §2§lGREEN §7beacon!");
            }
        }

    }
    public void lasertaghelp(CommandSender sender){
        sender.sendMessage(Core.infoMessage + "Commmands for §c§lLASERTAG§e:");
        sender.sendMessage(Core.infoMessage + "/lasertag debug §8- §7Displays the current builds debug info.");
        sender.sendMessage(Core.infoMessage + "/lasertag info §8- §7Displays the plugin info.");
        sender.sendMessage(Core.infoMessage + "/lasertag arena §8- §7Displays all arenas commands.");
        sender.sendMessage(Core.infoMessage + "/lasertag game §8- §7Displays the current games info.");
        sender.sendMessage(Core.infoMessage + "/lasertag start §8- §7Force starts the current game.");
        sender.sendMessage(Core.infoMessage + "/lasertag stop §8- §7Force stops the current game.");
    }
}
