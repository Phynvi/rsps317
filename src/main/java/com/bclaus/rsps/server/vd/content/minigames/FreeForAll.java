package com.bclaus.rsps.server.vd.content.minigames;
/*package server.game.content.minigames;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import server.game.content.achievements.AchievementType;
import server.game.content.achievements.Achievements;
import server.game.player.Client;
import server.game.player.PlayerAssistant;
import server.game.player.PlayerHandler;
import Misc;


 * @author Balla_ <http://www.rune-server.org/members/balla/>
 * Rewritten a bit rushed this but had to be on the safe side
 
public class FreeForAll {

 *//** 
 * Stores the player's inside of the waiting room. 
 */
/*

private static CopyOnWriteArrayList<Client> lobbyPlayers = new CopyOnWriteArrayList<Client>(); 

 *//** 
 * Stores the players inside of the free for all participating. 
 */
/*

private static CopyOnWriteArrayList<Client> insidePlayers = new CopyOnWriteArrayList<Client>(); 

 *//** 
 * Pot Size
 */
/*
public static int POT_SIZE;

 *//** 
 * The wait lobby timer for the users waiting for the next game. 
 */
/*

private static int lobbyTimer = 50; 

 *//** 
 * The timer remaining for the users fighting amongst each other. 
 */
/*

private static int insideTimer = -1; 

 *//** 
 * The amount of required players needed to start the game. 
 */
/*

private static final int requiredPlayers = 2; 

 *//** 
 * Represents whether there is a game in progress already. 
 */
/*

private static boolean gameProgress = false; 

 *//** 
 * The abs-x coordinate of the outside of the waiting room. 
 */
/*

public static final int OUTSIDE_X = 2399; 

 *//** 
 * The abs-y coordinate of the outside of the waiting room. 
 */
/*

public static final int OUTSIDE_Y = 5177; 

 *//** 
 * The abs-x coordinate of the waiting room. 
 */
/*

private static final int waitingRoomX = 2399; 

 *//** 
 * The abs-y coordinate of the waiting room. 
 */
/*

private static final int waitingRoomY = 5175; 

 *//** 
 * The abs-x coordinate inside of the free for all. 
 */
/*

private static final int insideGameX = 2398; 

 *//** 
 * The abs-y coordinate inside of the free for all. 
 */
/*

private static final int insideGameY = 5155; 

 *//** 
 * The amount of tokens required to participate in this minigame. 
 */
/*

private static final int goldRequired = 100000; //100k


 *//** 
 * The free for all game status. 
 */
/*

private enum GameStatus { 
START_GAME, END_GAME, RESET_GAME, DRAW_GAME; 
} 

 *//** 
 * The player entering a lobby requirements. 
 * @param player The player entering the lobby. 
 */
/*


public static void enterLobby(Client player) { 

if(!player.firstGame) {
	player.sendMessage("<col=255>This is your first time playing FFA, here are some guidelines!</col>");
	player.sendMessage("To participate in this minigame you must enter a standard fee of 100k");
	player.sendMessage("This fee adds up, with all players in the Lobby, and the reward is the Fees entered");
	player.sendMessage("Any FFA game with 8 or more players activates an extra reward bonus of Tokkul");
	player.sendMessage("Please report any bugs with the new minigame, and Enjoy!");
	player.firstGame = true;
	return;
}
if (!player.getItems().playerHasItem(995, goldRequired)) { 
	player.sendMessage("You must have "+goldRequired+" gold to participate in this minigame.."); 
	return; 
} 
player.getItems().deleteItem2(995, goldRequired);
POT_SIZE += goldRequired;
PlayerAssistant.movePlayer(player, waitingRoomX, waitingRoomY, 0); 
addToLobby(player);
} 

 *//** 
 * Handles the start of a game for free for all. 
 * @param player The player's waiting in the lobby. 
 */
/*
private static boolean tokkulReward;
private static void startGame(Client player) { 
if(player == null) {
	return;
}
addToGame(player);
lobbyPlayers.clear();
PlayerAssistant.refreshSkill(player, 3);
PlayerAssistant.refreshSkill(player, 5);
PlayerAssistant.movePlayer(player, insideGameX + Misc.random(10), insideGameY, 0); 
if(insidePlayers.size() > 7) {
	PlayerHandler.announce("[FFA] Randomized Reward activated for the winner!");
	tokkulReward = true;
}
} 

 *//** 
 * The end of a game when the last user remaining dies. 
 * @param player The player's inside of the arena 
 */
/*

private static void endGame(Client player) { 
if(player == null) {
	endGame(null);
}
int tokkul = Misc.random(15000);
if(tokkulReward) {
	player.getItems().addItem(6529, tokkul);
	PlayerHandler.announce("<col=ff0033>[FFA] </col> The winner has been awarded "+ tokkul+ " Tokkul as an extra award!");
	tokkulReward = false;
}
if (insidePlayers.size() == 1) { 
	player.sendMessage("You win a total of " + Misc.getNumberFormat(POT_SIZE) + " coins!"); 
	PlayerHandler.announce(Misc.formatPlayerName("<col=ff0033>["+ Misc.formatPlayerName(player.playerName) + "] </col>won the free for all minigame!"));
	player.getItems().addItem(995, POT_SIZE); 
}
POT_SIZE = 0;
Achievements.increase(player, AchievementType.FFA, 1);
PlayerAssistant.movePlayer(player, OUTSIDE_X, OUTSIDE_Y, 0); 
insidePlayers.clear();

} 

 *//** 
 * The end of a game when the last user remaining dies. 
 * @param player The player's inside of the arena 
 */
/*

private static void drawGame(Client player) { 
if(player == null) {
	
}
player.sendMessage("Game ended in a draw since the timer reached 0!");
PlayerAssistant.movePlayer(player, OUTSIDE_X, OUTSIDE_Y, 0); 
PlayerAssistant.refreshSkill(player, 3);
PlayerAssistant.refreshSkill(player, 5);
insidePlayers.clear();

} 

 *//** 
 * Removes the player from the minigame. 
 * @param player The player participating in the minigame. 
 * @param forceTeleport Symbolises if the user needs to be teleported from the minigame. 
 */
/*

public static void removePlayer(Client player, boolean forceTeleport) { 
if (forceTeleport) { 
	PlayerAssistant.movePlayer(player, OUTSIDE_X, OUTSIDE_Y, 0); 
} 
if(inLobby(player)) {
	player.getItems().addItem(995, goldRequired); 
	POT_SIZE -= goldRequired;
	player.sendMessage("You receive your gold back."); 
}
removeFromGame(player);
removeFromLobby(player);

} 

 *//** 
 * Updates the interface for every player inside of the minigame. 
 * @param player The player participating in the free for all minigame. 
 */
/*

public static void updateInterfaces(Client player) { 
if (inLobby(player)) { 
	player.getPA().walkableInterface(2804); 
	player.getPA().sendFrame126(!gameProgress ? "@gre@Time till next game: " + lobbyTimer : "@gre@Game in progress", 2805); 
	player.getPA().sendFrame126("Players required to start: " + requiredPlayers, 2806); 
	player.getPA().sendFrame36(560, 1); 
} 
if (inGame(player)) {  
	player.getPA().walkableInterface(2804); 
	player.getPA().sendFrame126("Time until the game ends: " + insideTimer, 2805); 
	player.getPA().sendFrame126("Total Remaining: " + insidePlayers.size(), 2806); 
	player.getPA().sendFrame36(560, 1); 
} 
} 

 *//** 
 * Handles the process of moving the player's from one location to another. 
 * @status The current status of a game. 
 */
/*

private static void movePlayers(GameStatus status, List<Client> list) { 
Iterator<Client> itr = list.iterator(); 
switch(status) { 
case START_GAME: 
	while(itr.hasNext()) { 
		Client p = itr.next(); 
		startGame(p); 
	} 
	break; 
case END_GAME: 
	while(itr.hasNext()) { 
		Client p = itr.next(); 
		endGame(p); 
	} 
	break; 
case RESET_GAME: 
	break;
case DRAW_GAME:
	while(itr.hasNext()) { 
		Client p = itr.next(); 
		drawGame(p); 
	} 
	break;
default:
	break; 
} 
setVariables(status); 
} 

 *//** 
 * Changes all of the variables when the game starts or ends. 
 * @param status The game status. 
 */
/*

private static void setVariables(GameStatus status) { 
switch(status) { 
case START_GAME: 
	gameProgress = true; 
	insideTimer = 400; 
	lobbyTimer = -1; 
	break; 
case END_GAME: 
	gameProgress = false; 
	lobbyTimer = 400; 
	insideTimer = -1; 
	break; 
case RESET_GAME: 
	lobbyTimer = 150; 
	insideTimer = -1; 
	break;
case DRAW_GAME:
	gameProgress = false; 
	lobbyTimer = 400; 
	insideTimer = -1; 
	break;
default:
	break; 
} 
} 

 *//** 
 * Handles the objects related to the free for all minigame. 
 * @param player The player clicking the object. 
 * @param objectId The object id. 
 */
/*

public static void handleObjects(Client player, int objectId) { 
boolean testingMode = false;
if(testingMode) {
	player.sendMessage("Currently in testing mode out soon!");
	return;
}
if (System.currentTimeMillis() - player.lastRequest < 1000) {
	return;
}
player.lastRequest = System.currentTimeMillis();
switch(objectId) { 

case 9369: 
	if (player.getY() > 5175) { 
		enterLobby(player); 
	} else { 
		removePlayer(player, true); 
	} 
	break; 

} 
} 

 *//** 
 * The timers which execute upon every second. 
 */
/*
 * 
 * public static void process() { if (!gameProgress) { if (lobbyTimer > 0) {
 * lobbyTimer--; } if (lobbyTimer == 0) { if (lobbyPlayers.size() >=
 * requiredPlayers) { movePlayers(GameStatus.START_GAME, lobbyPlayers); } else {
 * movePlayers(GameStatus.RESET_GAME, lobbyPlayers); } } } if (insideTimer > 0)
 * { insideTimer--; } if (insideTimer == 0) { movePlayers(GameStatus.DRAW_GAME,
 * insidePlayers); } if (insidePlayers.size() == 1) {
 * movePlayers(GameStatus.END_GAME, insidePlayers); } } public static void
 * addToLobby(Client c) { if(!lobbyPlayers.contains(c)) lobbyPlayers.add(c); }
 * 
 * public static void addToGame(Client c) { if(!insidePlayers.contains(c))
 * insidePlayers.add(c); }
 * 
 * public static void removeFromGame(Client c) { if(insidePlayers.contains(c))
 * insidePlayers.remove(c); }
 * 
 * public static void removeFromLobby(Client c) { if(lobbyPlayers.contains(c))
 * lobbyPlayers.remove(c); }
 * 
 * public static boolean inLobby(Client c) { return lobbyPlayers.contains(c); }
 * 
 * public static boolean inGame(Client c) { return insidePlayers.contains(c); }
 * }
 */