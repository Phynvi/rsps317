/**
 * 
 */
package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.content.teleport.TeleportType;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.world.Position;
import com.bclaus.rsps.server.vd.content.teleport.Teleport;
import com.bclaus.rsps.server.task.ScheduledTask;

/**
 * @author Tim http://rune-server.org/members/Someone
 *
 */

public class TeleTabs {

	final static int ANIM = 4731, GFX = 678;

	public enum TabData {
		VARROCK(8007, 3212, 3423), LUMBRIDGE(8008, 3222, 3218), FALADOR(8009, 2965, 3385), CAMELOT(8010, 2757, 3478), ARDOUGNE(8011, 2662, 3305), WATCHTOWER(8012, 2549, 3112);

		private int itemId, posX, posY;

		TabData(int itemId, int posX, int posY) {
			this.itemId = itemId;
			this.posX = posX;
			this.posY = posY;
		}

		public int getItemId() {
			return itemId;
		}

		public int getPosX() {
			return posX;
		}

		public int getPosY() {
			return posY;
		}

		public static TabData forId(int itemId) {
			for (TabData data : TabData.values()) {
				if (data.itemId == itemId)
					return data;
			}
			return null;
		}
	}

	public static void useTeleTab(final Player c, int slot, final TabData data) {

		if (TeleportExecutor.canTeleport(c)) {
			c.isTeleporting = true;
			c.startAnimation(4069);
			c.getItems().deleteItem(data.getItemId(), slot, 1);
			Server.getTaskScheduler().schedule(new ScheduledTask(2) {
				@Override
				public void execute() {
					TeleportExecutor.teleport(c, new Teleport(new Position(data.posX, data.posY, 0), TeleportType.TABLET), false);
					this.stop();
				}

			}.attach(c));
		}
	}

}
