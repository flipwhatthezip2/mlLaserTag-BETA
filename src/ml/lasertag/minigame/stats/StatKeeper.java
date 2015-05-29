package ml.lasertag.minigame.stats;


import ml.lasertag.minigame.Core;
import ml.lasertag.minigame.events.PlayerStatUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class StatKeeper {

    Core core;

    private Player player;
    private int kills, deaths, wins, losses, draws, damageDealt, damageTaken, beaconDamageDealt;

    public StatKeeper(Core core, Player player, int kills, int deaths, int wins, int losses, int draws, int damageDealt, int damageTaken, int beaconDamageDealt){
        this.core = core; this.player = player;
        this.kills = kills; this.deaths = deaths; this.wins = wins; this.losses = losses; this.draws = draws;
        this.damageDealt = damageDealt; this.damageTaken = damageTaken; this.beaconDamageDealt = beaconDamageDealt;

        core.getStatsFile().addStatKeeper(this);
    }

    public Player getPlayer(){
        return this.player;
    }

    public double getKDR(){
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(this.deaths == 0 ? this.kills : ((double) this.kills / (double) this.deaths)));
    }

    public double getWLR(){
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(this.losses == 0 ? this.wins : ((double) this.wins / (double) this.losses)));
    }

    public int getKills(){
        return this.kills;
    }

    public int getDeaths(){
        return this.deaths;
    }

    public int getWins(){
        return this.wins;
    }

    public int getLosses(){
        return this.losses;
    }

    public int getDraws(){
        return this.draws;
    }

    public int getDamageDealt(){
        return this.damageDealt;
    }

    public int getDamageTaken(){
        return this.damageTaken;
    }

    public int getBeaconDamageDealt(){
        return this.beaconDamageDealt;
    }

    public void setKills(int kills){
        this.kills = kills;
        Bukkit.getPluginManager().callEvent(new PlayerStatUpdateEvent(player, PlayerStatUpdateEvent.PlayerStat.KILLS, kills));
    }

    public void setDeaths(int deaths){
        this.deaths = deaths;
        Bukkit.getPluginManager().callEvent(new PlayerStatUpdateEvent(player, PlayerStatUpdateEvent.PlayerStat.DEATHS, deaths));
    }

    public void setWins(int wins){
        this.wins = wins;
        Bukkit.getPluginManager().callEvent(new PlayerStatUpdateEvent(player, PlayerStatUpdateEvent.PlayerStat.WINS, wins));
    }

    public void setLosses(int losses){
        this.losses = losses;
        Bukkit.getPluginManager().callEvent(new PlayerStatUpdateEvent(player, PlayerStatUpdateEvent.PlayerStat.LOSSES, losses));
    }

    public void setDraws(int draws){
        this.draws = draws;
    }

    public void setDamageDealt(int damageDealt){
        this.damageDealt = damageDealt;
    }

    public void setDamageTaken(int damageTaken){
        this.damageTaken = damageTaken;
    }

    public void setBeaconDamageDealt(int beaconDamageDealt){
        this.beaconDamageDealt = beaconDamageDealt;
    }
    
    public void addKills(int kills){
        this.setKills(this.kills + kills);
    }

    public void addDeaths(int deaths){
        this.setDeaths(this.deaths + deaths);
    }

    public void addWins(int wins){
        this.setWins(this.wins + wins);
    }

    public void addLosses(int losses){
        this.setLosses(this.losses + losses);
    }

    public void addDraws(int draws){
        this.setDraws(this.draws + draws);
    }

    public void addDamageDealt(int damageDealt){
        this.setDamageDealt(this.damageDealt + damageDealt);
    }

    public void addDamageTaken(int damageTaken){
        this.setDamageTaken(this.damageTaken + damageTaken);
    }

    public void addBeaconDamageDealt(int beaconDamageDealt){
        this.setBeaconDamageDealt(this.beaconDamageDealt + beaconDamageDealt);
    }

}
