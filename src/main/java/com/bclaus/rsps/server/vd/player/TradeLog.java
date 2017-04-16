package com.bclaus.rsps.server.vd.player;

import java.util.Calendar;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.bclaus.rsps.server.vd.World;

/**
* TradeLog class
* @author Aintaro
*/

public class TradeLog {

	private Player c;
	
	
	
	public TradeLog(Player Client) {
		this.c = Client;
	}
	
	/**
	* Will write what kind of item a player has received.
	* MONTH = 0 = January
	* DAY OF MONTH = 30 || 31
	*/
	public void tradeReceived(String itemName, int itemAmount) {
		Player o = (Player) World.PLAYERS.get(c.tradeWith);
	Calendar C = Calendar.getInstance();
		try {
				BufferedWriter bItem = new BufferedWriter(new FileWriter("./data/trades/received/" + c.playerName + ".txt", true));
				try {			
					bItem.newLine();
					bItem.write("Year : " + C.get(Calendar.YEAR) + "\tMonth : " + C.get(Calendar.MONTH) + "\tDay : " + C.get(Calendar.DAY_OF_MONTH));
					bItem.newLine();
					bItem.write("Received " + itemAmount + " " + itemName + " From " + o.playerName);
					bItem.newLine();
					bItem.write("--------------------------------------------------");
					} finally {
						bItem.close();
					}
				} catch (IOException e) {
                    e.printStackTrace();
            }
	}
	
	/**
	* Will write what kind of item a player has traded with another player.
	* MONTH = 0 = January
	* DAY OF MONTH = 30 || 31
	*/
	public void tradeGive(String itemName, int itemAmount) {
		Player o = (Player) World.PLAYERS.get(c.tradeWith);
	Calendar C = Calendar.getInstance();
		 try {
				BufferedWriter bItem = new BufferedWriter(new FileWriter("./data/trades/gave/" + c.playerName + ".txt", true));
				try {			
					bItem.newLine();
					bItem.write("Year : " + C.get(Calendar.YEAR) + "\tMonth : " + C.get(Calendar.MONTH) + "\tDay : " + C.get(Calendar.DAY_OF_MONTH));
					bItem.newLine();
					bItem.write("Gave " + itemAmount + " " + itemName + " To " + o.playerName);
					bItem.newLine();
					bItem.write("--------------------------------------------------");
				} finally {
					bItem.close();
				}
			} catch (IOException e) {
			e.printStackTrace();
		}
	}
}