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

 public int lobbyCountdown = 30;

 public static void setScoreboard(final Player p){
  lobbyBoard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();

  ScoreboardManager manager = Bukkit.getScoreboardManager();
  Scoreboard board = manager.getNewScoreboard();

  Objective lobbyBoard = board.registerNewObjective("lobbyBoard", "dummy");

  lobbyBoard.setDisplayName("         §c§lLASER TAG         ");
  lobbyBoard.setDisplaySlot(DisplaySlot.SIDEBAR);

  Score divider1 = lobbyBoard.getScore(" ");
  Score playerOnlineText = lobbyBoard.getScore("§fReady you add text.");

  divider1.setScore(1);
  playerOnlineText.setScore(0);
 }

 public static void setGameStatusLobby(){
  // TODO: Add function to keep looking for minimum player requirements.
 }

}