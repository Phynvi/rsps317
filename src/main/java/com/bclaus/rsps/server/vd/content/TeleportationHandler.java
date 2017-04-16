package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.world.Position;

public class TeleportationHandler {
	
	public enum TeleportData {


		;
		private final int button;
		
		private final String name;
		
		private final Position position;
		
		private TeleportData(int button, String name, Position position) {
			this.button = button;
			this.name = name;
			this.position = position;
		}
		
		public int getButton() {
			return button;
		}
		
		public String getName() {
			return name;
		}
		
		public Position getPosition() {
			return position;
		}
		
		public static TeleportData forButton(int id) {
			for (TeleportData data : TeleportData.values())
				if (data.button == id)
					return data;
			return null;
		}
		
	}
	
	public static boolean handle(Player player, int button) {
		TeleportData data = TeleportData.forButton(button);
		
		if (data == null) {
			return false;
		}
		
		TeleportExecutor.teleport(player, data.getPosition());
		player.sendMessage("You have teleported to " + data.getName() + ".");
		return true;
	}

}
