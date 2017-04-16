package com.bclaus.rsps.server.vd.content.skills.impl;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.player.Player;

/**
 * 
 * @author Faris
 */
public class Skill {
	/**
	 * The constant skill ID's for each different skill
	 */
	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2, HITPOINTS = 3, RANGED = 4, PRAYER = 5, MAGIC = 6, COOKING = 7, WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11, CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15, AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19, RUNECRAFTING = 20, SUMMONING = 21, HUNTER = 22, CONSTRUCTION = 23, DUNGEONEERING = 24;

	public static final boolean CAN_HARVEST = true, CANNOT_HARVEST = false;

	/**
	 * Returns the clients skill level for the parsed skill id
	 * 
	 * @param client
	 * @param skillId
	 * @return
	 */
	public int getSkillLevel(Player client, int skillId) {
		return client.playerLevel[skillId];
	}

	/**
	 * Awards the client with the given amount of experience set
	 * 
	 * @param client
	 * @param exp
	 * @param skillId
	 */
	public void awardExp(Player client, int exp, int skillId) {
		client.getPA().addSkillXP(exp * Constants.GENERAL_EXPERIENCE, skillId);
	}

	/**
	 * Awards the client with the item given, after checking for inventory slots
	 * sends a post receive message to alert the user
	 * 
	 * @param client
	 * @param itemId
	 * @param amount
	 */
	public void awardItem(Player client, int itemId, int amount, boolean message) {
		if (!hasInventorySpace(client)) {
			return;
		}
		client.getItems().addItem(itemId, amount);
		if (message) {
			client.sendMessage("You get some " + client.getItems().getItemName(itemId) + ".");
		}
	}

	/**
	 * Deletes an item from a client
	 * 
	 * @param client
	 */
	public void deleteItem(Player client, int item, boolean message) {
		client.getItems().deleteItem(item, 1);
		if (message) {
			client.sendMessage("You consume the " + client.getItems().getItemName(item));
		}
	}

	/**
	 * Sends a message to the client
	 * 
	 * @param client
	 * @param message
	 */
	public void sendMessage(Player client, String message) {
		client.sendMessage(message);
	}

	/**
	 * Checks if the client has enough inventory space to receive one item
	 * 
	 * @param client
	 * @return
	 */
	public boolean hasInventorySpace(Player client) {
		if (client.getItems().freeSlots() < 1) {
			client.getDH().sendPlayerChat1("There is not enough space in my inventory.");
			if (client.nextChat > 0) {
				client.nextChat = -1;
			}
			return false;
		}
		return true;
	}

	/**
	 * Checks if the client's level in the given skill exceeds the given level
	 * requirement and displays a dialogue if not
	 * 
	 * @param client
	 * @param skillId
	 * @param levelRequirement
	 * @return
	 */
	public boolean playerHasRequiredLevel(Player client, int skillId, int levelRequirement) {
		if (getSkillLevel(client, skillId) < levelRequirement) {
			client.getDH().sendPlayerChat1("I do not have the required level for this action.");
			if (client.nextChat > 0) {
				client.nextChat = -1;
			}
			return false;
		}
		return true;
	}
	/*public long getTotalXp() {
		long totalxp = 0;
		for (double xp : getXp()) {
			totalxp += xp;
		}
		return totalxp;
	}*/
	/**
	 * Checks if the players equipment or inventory contains the given item, if
	 * not displays a dialogue alerting the user
	 * 
	 * @param client
	 * @param itemId
	 * @return
	 */
	public boolean playerHasItem(Player client, int itemId) {
		if (!client.getItems().playerHasItem(itemId) && !client.getItems().playerHasEquipped(itemId)) {
			client.getDH().sendPlayerChat1("I do not have the required equipment for this action.");
			if (client.nextChat > 0) {
				client.nextChat = -1;
			}
			return false;
		}
		return true;
	}

	/**
	 * Goes through all known resource implementations and returns the defined
	 * object associated with the implementation
	 * 
	 * @param objectId
	 * @return
	 */

	/**
	 * Begins the harvesting action for harvesting skills
	 * 
	 * @param harvest
	 *            is the type of action to be undertaken
	 * @param client
	 *            is the player undertaking the action
	 * @param resoruce
	 *            is what is being harvested
	 */

}