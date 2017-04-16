package com.bclaus.rsps.server.vd.content.skills.impl;

import java.util.HashMap;
import java.util.Map;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.vd.player.Player;

/**
 * 
 * @author Tim http://rune-server.org/members/Someone
 *
 */
public class Prayer {

	private enum Bones {

		BONE(526, 5), WOLF_BONES(2859, 5), BURNT_BONES(528, 5), MONKEY_BONES(3179, 5), BAT_BONES(530, 5), BIG_BONES(532, 15), JOGRE_BONES(3125, 15), ZOGRE_BONES(4812, 23), SHAIKAHAN_BONES(3123, 25), BABYDRAGON_BONES(534, 30), WYVERN_BONES(6812, 180), DRAGON_BONES(536, 98), FAYRG_BONES(4830, 84), RAURG_BONES(4832, 96), DAGANNOTH_BONES(6729, 125), OURG_BONES(4834, 140), FROST_DRAGON_BONES(18830, 180), ANCIENT_BONES(15410, 200), IMPIOUS_ASHES(20264, 4), ACCURSED_ASHES(20266, 13), INFERNAL_ASHES(20268, 63);

		/**
		 * The items representing the bones. The experience given when bury.
		 */
		private int itemId, experience;

		/**
		 * 
		 * @param itemId
		 *            The item being buried.
		 * @param experience
		 *            The experience given by each bone.
		 */
		private Bones(int itemId, int experience) {
			this.itemId = itemId;
			this.experience = experience;
		}

		/**
		 * A map containing all of the valid bone item id's.
		 */
		public static Map<Integer, Bones> bonesMap = new HashMap<Integer, Bones>();

		/**
		 * Populates the bones map.
		 */
		static {
			for (Bones bones : values())
				bonesMap.put(bones.getItemId(), bones);
		}

		/**
		 * The item being buried.
		 */
		public int getItemId() {
			return itemId;
		}

		/**
		 * The experience given by each bone.
		 */
		public int getExperience() {
			return experience;
		}

		/**
		 * Populates and stores the bone data.
		 */
		public static Bones forId(int id) {
			return bonesMap.get(id);
		}

	}

	/**
	 * Animation for burying bones.
	 */
	private static int BURY_ANIMATION = 827;
	private static int ALTAR_ANIMATION = 896;

	/**
	 * The method that buries the bones.
	 */
	public static boolean buryBones(Player client, int itemId) {
		Bones bones = Bones.forId(itemId);

		if (bones == null)
			return false;
		if (bones.getItemId() == itemId) {
			client.startAnimation(BURY_ANIMATION);
			client.getItems().deleteItem2(bones.getItemId(), 1);
			client.getPA().addSkillXP((bones.getExperience()) * 10, Player.playerPrayer);
			client.sendMessage("You dig a hole into the ground - and bury the bones...");
		}
		return true;
	}

	/**
	 * Bones interacting with altar(s).
	 */
	public static boolean bonesOnAltar(final Player client, final int itemId) {
		final Bones bones = Bones.forId(itemId);
		if (bones == null)
			return false;
		if (client.doingBone) {
			return false;
		}
		client.playerStun = true;
		client.doingBone = true;
		if (System.currentTimeMillis() - client.foodDelay >= 1500) {
			if (bones.getItemId() == itemId) {
				client.startAnimation(ALTAR_ANIMATION);
				Server.getTaskScheduler().schedule(new ScheduledTask(4) {
					@Override
					public void execute() {
						client.startAnimation(ALTAR_ANIMATION);
						client.getItems().deleteItem2(itemId, 1);
						client.getPA().addSkillXP(bones.getExperience() * 40, Player.playerPrayer);
						client.getPA().createPlayersStillGfx(624, 3091, 3506, 0, 0);
						client.sendMessage("The gods are pleased with your offering!");
						if (!client.getItems().playerHasItem(bones.getItemId())) {
							client.sendMessage("You ran out of bones.");
							this.stop();
						}
					}

					@Override
					public void onStop() {
						client.startAnimation(65535);
						client.getPA().createPlayersStillGfx(65535, -1, -1, -1, -1);
						client.playerStun = false;
						client.doingBone = false;

					}

				}.attach(client));
			}
			client.foodDelay = System.currentTimeMillis();
		}
		return true;
	}

	/**
	 * @param Is
	 *            a bone check requirement
	 * @return
	 */
	public static boolean isBone(int itemId) {
		return Bones.bonesMap.containsKey(itemId);
	}

}
