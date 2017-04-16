package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.vd.player.Player;

public class StarterHandler {
	
	public enum ModeData {
		NORMAL(123016, "Normal", "-700 xp rate\\n-No increase drop rates.\\n\\nThis game mode is for those who want to get straight at \\nit, with fast level training you can start \\nSlaying/Bossing/Killing/Skilling fast."),
		LEGENDARY(123017, "Legendary", "-50 xp rate\\n-5% increase drop rates.\\n\\nThis game mode is for those who want to get straight at \\nit, with fast level training you can start \\nSlaying/Bossing/Killing/Skilling fast."),
		IRON_MAN(123018, "Iron Man", "-50 xp rate\\n-8% increase drop rates.\\n\\nThis game mode is for those who want to get straight at \\nit, with fast level training you can start \\nSlaying/Bossing/Killing/Skilling fast.");
		
		private final int button;
		
		private final String name;
		
		private final String description;
		
		private ModeData(int button, String name, String description) {
			this.button = button;
			this.name = name;
			this.description = description;
		}
		
		public int getButton() {
			return button;
		}
		
		public String getName() {
			return name;
		}
		
		public String getDescription() {
			return description;
		}
		
		public static ModeData forButton(int id) {
			for (ModeData data : ModeData.values())
				if (data.button == id) 
					return data;
			return null;
		}
		
	}
	
	public static boolean handleButton(Player player, int button) {
		ModeData data = ModeData.forButton(button);
		
		if (button == 123024) {
			confirm(player);
			return true;
		}
		
		if (data == null) {
			return false;
		}
		
		player.setMode(data);
		player.getPA().sendFrame126(data.getDescription(), 31511);
		player.sendMessage("You have selected " + data.getName() + " mode.");
		return true;
	}
	
	public static void confirm(Player player) {
		if (player.getMode() == null) {
			player.sendMessage("Please select a game mode first!");
			return;
		}
		
		player.setModeInt();
		player.getPA().removeAllWindows();
		player.sendMessage("Have fun on your adventure!");
	}

}
