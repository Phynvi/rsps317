package com.bclaus.rsps.server.database;

import java.sql.ResultSet;

import com.bclaus.rsps.server.vd.player.Player;



public class Donation implements Runnable {

	public static final String HOST_ADDRESS = "localhost";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "password";
	public static final String DATABASE = "donate";
	
	private Player player;
	
	@Override
	public void run() {
		try {
			Database db = new Database(HOST_ADDRESS, USERNAME, PASSWORD, DATABASE);
			
			if (!db.init()) {
				System.err.println("[Donation] Failed to connect to database!");
				return;
			}
			
			String name = player.playerName.replace("_", " ");
			ResultSet rs = db.executeQuery("SELECT * FROM payments WHERE player_name='"+name+"' AND claimed=0");
			
			while(rs.next()) {
				String item_name = rs.getString("item_name");
				int item_number = rs.getInt("item_number");
				double amount = rs.getDouble("amount");
				int quantity = rs.getInt("quantity");
				
				ResultSet result = db.executeQuery("SELECT * FROM products WHERE item_id="+item_number+" LIMIT 1");
				
				if (result == null || !result.next()
						|| !result.getString("item_name").equalsIgnoreCase(item_name)
						|| result.getDouble("item_price") != amount
						|| quantity < 1 || quantity > Integer.MAX_VALUE) {
					System.out.println("[Donation] Invalid purchase for "+name+" (item: "+item_name+", id: "+item_number+")");
					continue;
				}
				
				handleItems(item_number);
				rs.updateInt("claimed", 1);
				rs.updateRow();
			}
			
			db.destroyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleItems(int productId) {
		switch(productId) {
		case 0:
			player.sendMessage("<shad=cc0ff><img=1>You Have Recieved Your donation! Thank you for your support!");
            player.getItems().addItem(995, 100); 
			// handle item stuff, like adding items, points, etc.
			break;
		case 1:
			player.sendMessage("<shad=cc0ff><img=1>You Have Recieved Your donation! Thank you for your support!");
            player.getItems().addItem(9005, 1); 
			// handle item stuff, like adding items, points, etc.
			break;
		case 10:
			player.sendMessage("<shad=cc0ff><img=1>You Have Recieved Your donation! Thank you for your support!");
            player.getItems().addItem(1042, 1); 
			// handle item stuff, like adding items, points, etc.
			break;
		}
	}
	
	public Donation(Player player) {
		this.player = player;
	}
}
