package ml.lasertag.minigame.game;


import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.GameManager.Arena;
import ml.lasertag.minigame.Mechanics.LaserTagBeacon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class StatsScoreboard {

    Core core;

    private Arena arena;
    private Scoreboard scoreboard;
    private Objective obj;
    private Objective health;

    private double greenBeaconHealth;
    private double yellowBeaconHealth;

    public StatsScoreboard(Core core, Arena arena){
        this.core = core;
        this.arena = arena;

        this.greenBeaconHealth = arena.getGreenBeacon().getHealth();
        this.yellowBeaconHealth = arena.getYellowBeacon().getHealth();

        this.initializeScoreboard();
    }

    public void initializeScoreboard(){
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.obj = scoreboard.registerNewObjective("Gameboard", "dummy");

        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + arena.getProperties().getArenaName());

        obj.getScore("  ").setScore(10);

        obj.getScore(ChatColor.YELLOW + "Yellow Beacon: ").setScore(9);
        obj.getScore(yellowBeaconHealth + "").setScore(8);

        obj.getScore(" ").setScore(7);

        obj.getScore(ChatColor.DARK_GREEN + "Green Beacon: ").setScore(6);
        obj.getScore(greenBeaconHealth + " ").setScore(5);
    }

    public void updateHealth(LaserTagBeacon beacon){
        if (beacon.getTeam() == TEAM.YELLOW){
            scoreboard.resetScores(yellowBeaconHealth + "");
            obj.getScore(beacon.getHealth() + "").setScore(8);
            this.yellowBeaconHealth = beacon.getHealth();
        }
        else {
            scoreboard.resetScores(greenBeaconHealth + " ");
            obj.getScore(beacon.getHealth() + " ").setScore(5);
            this.greenBeaconHealth = beacon.getHealth();
        }
    }

    public void showScoreboard(){
        for (Player p : arena.getPlayers()){
            p.setScoreboard(scoreboard);
        }
    }

    public void reset(){
        this.greenBeaconHealth = 50;
        this.yellowBeaconHealth = 50;
        this.initializeScoreboard();
    }

    public void set(TEAM team, int health){
        if (team == TEAM.YELLOW){
            scoreboard.resetScores(yellowBeaconHealth + "");
            obj.getScore(health + "").setScore(8);
            this.yellowBeaconHealth = health;
        }
        else {
            scoreboard.resetScores(greenBeaconHealth + " ");
            obj.getScore(health + " ").setScore(5);
            this.greenBeaconHealth = health;
        }
    }

}
