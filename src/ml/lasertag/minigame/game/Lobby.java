package ml.lasertag.minigame.game;



/*
 Project Info:
 Project Name: mlLaserTag
 Package: ml.LaserTag.minigame.game
 Created By: cfletcher
 Created On: 4/27/15
 All code is licenced to the following people:
 - Cameron Fletcher
 -
 */


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class Lobby {

    public static Scoreboard lobbyBoard;

    public static void setScoreboard(final Player p){
        lobbyBoard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        Objective lobbyBoard = board.registerNewObjective("lobbyBoard", "dummy");

        lobbyBoard.setDisplayName("         §c§lLASER TAG         ");
        lobbyBoard.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score divider1 = lobbyBoard.getScore(" ");
        Score divider2 = lobbyBoard.getScore("  ");
        Score divider3 = lobbyBoard.getScore("   ");
        Score divider4 = lobbyBoard.getScore("    ");
        Score divider5 = lobbyBoard.getScore("     ");
        Score divider6 = lobbyBoard.getScore("      ");
        Score divider7 = lobbyBoard.getScore("       ");

        Score line1 = lobbyBoard.getScore("§4-----------------");

        Score arenaNameText = lobbyBoard.getScore("§cArena Name:");
        Score arenaName = lobbyBoard.getScore("§7*GET NAME*");
        Score arenaPlayersText = lobbyBoard.getScore("§cPlayers:");
        Score arenaPlayers = lobbyBoard.getScore("§7*GET PLAYERS*");
        Score greenTeamText = lobbyBoard.getScore("§2Green Players:");
        Score greenTeam = lobbyBoard.getScore("§7*GET GREEN*");
        Score yellowTeamText = lobbyBoard.getScore("§eYellow Players:");
        Score yellowTeam = lobbyBoard.getScore("§7*GET YELLOW*");

        divider1.setScore(10);
        arenaNameText.setScore(9);
        arenaName.setScore(8);
        divider2.setScore(7);
        arenaPlayersText.setScore(6);
        arenaPlayers.setScore(5);
        divider3.setScore(4);
        greenTeamText.setScore(3);
        greenTeam.setScore(2);
        divider4.setScore(1);
        yellowTeamText.setScore(0);
        yellowTeam.setScore(-1);
        line1.setScore(-2);
    }

    public static void setGameStatusLobby(){
        // TODO: Add function to keep looking for minimum player requirements.
    }

}