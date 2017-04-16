package com.bclaus.rsps.server.vd.objects.cannon;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.world.Object;
import com.bclaus.rsps.server.vd.world.ObjectManager;

/**
 * @author lare96
 */
public enum Setup {

	NOTHING {
		@Override
		public void setup(Player player) {
			player.getCC().setSetupStage(Setup.BASE);
		}
	},
	BASE {
		@Override
		public void setup(Player player) {
			player.startAnimation(827);
			player.getItems().deleteItem(6);
			ObjectManager.placeObjectDatabase(new Object(7, player.getPosition()));
			player.getCC().setSetupStage(Setup.STAND);
		}
	},
	STAND {
		@Override
		public void setup(Player player) {
			player.startAnimation(827);
			player.getItems().deleteItem(8);
			ObjectManager.removeObjectDatabase(player.getPosition());
			ObjectManager.placeObjectDatabase(new Object(8, player.getPosition()));
			player.getCC().setSetupStage(Setup.BARRELS);
		}
	},
	BARRELS {
		@Override
		public void setup(Player player) {
			player.startAnimation(827);
			player.getItems().deleteItem(10);
			ObjectManager.removeObjectDatabase(player.getPosition());
			ObjectManager.placeObjectDatabase(new Object(9, player.getPosition()));
			player.getCC().setSetupStage(Setup.FURNACE);
		}
	},
	FURNACE {
		@Override
		public void setup(Player player) {
			player.startAnimation(827);
			player.getItems().deleteItem(12);
			player.getCC().setCannon(new Cannon(player));
			player.getCC().setSetupStage(Setup.CANNON);
			ObjectManager.removeObjectDatabase(player.getPosition());
			ObjectManager.placeObjectDatabase(player.getCC().getCannon());
		}
	},
	CANNON {
		@Override
		public void setup(Player player) {

		}
	};

	public abstract void setup(Player player);
}
