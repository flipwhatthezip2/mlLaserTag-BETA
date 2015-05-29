package ml.lasertag.minigame.stats;


import ml.lasertag.minigame.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardStatKeeper {

    Core core;

    private Player player;
    private StatsFile statsFile;
    private StatKeeper sk;
    
    private Scoreboard scoreboard;
    private Objective obj;
    
    private int kills, deaths, wins, losses;
    private double kdr, wlr;

    public ScoreboardStatKeeper(Core core, Player player){
        this.core = core;

        this.player = player;
        this.statsFile = core.getStatsFile();
        this.sk = statsFile.getStatKeeperFor(player);

        if (!statsFile.contains(player)){
            statsFile.newPlayer(player);
        }

        this.kills = sk.getKills();
        this.deaths = sk.getDeaths();
        this.kdr = sk.getKDR();
        this.wins = sk.getWins();
        this.losses = sk.getLosses();
        this.wlr = sk.getWLR();

        this.initializeScoreboard();

    }
    
    public void initializeScoreboard(){
        
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.obj = scoreboard.registerNewObjective("stats", "dummy");
        
        obj.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + "Stats");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        obj.getScore("            ").setScore(15);

        obj.getScore(ChatColor.translateAlternateColorCodes('&', "&bKills &7- &bDeaths &7- &bKDR")).setScore(14);
        obj.getScore(sk.getKills() + " - " + sk.getDeaths() + " - " + sk.getKDR()).setScore(13);

        obj.getScore("         ").setScore(12);

        obj.getScore(ChatColor.translateAlternateColorCodes('&', "&bWins &7- &bLosses &7- &bWLR")).setScore(11);
        obj.getScore(sk.getWins() + " - " + sk.getLosses() + " - " + sk.getWLR() + " ").setScore(10);
        
    }

    public void update(){
        scoreboard.resetScores(this.kills + " - " + this.deaths + " - " + this.kdr);
        scoreboard.resetScores(this.wins + " - " + this.losses + " - " + this.wlr + " ");

        obj.getScore(sk.getKills() + " - " + sk.getDeaths() + " - " + sk.getKDR()).setScore(13);
        obj.getScore(sk.getWins() + " - " + sk.getLosses() + " - " + sk.getWLR() + " ").setScore(10);

        this.kills = sk.getKills(); this.deaths = sk.getDeaths(); this.kdr = sk.getKDR();
        this.wins = sk.getWins(); this.losses = sk.getLosses(); this.wlr = sk.getWLR();
    }

    public Player getPlayer(){
        return this.player;
    }

    public Scoreboard getScoreboard(){
        return this.scoreboard;
    }
}
