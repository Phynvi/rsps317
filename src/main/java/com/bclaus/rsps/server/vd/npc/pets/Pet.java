package com.bclaus.rsps.server.vd.npc.pets;

import java.util.EnumSet;
import java.util.Set;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.net.PlayerUpdating;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.util.Misc;

/**
 * The order of this enum is CRUCIAL. Each element is
 * 
 * @author Jason MacKeigan
 * @date Aug 20, 2014, 11:50:11 AM
 */
public enum Pet {
	KALPHITE_JR(15563, 4162, new int[] { 1158 }, 825), 
	KALPHITE_JR2(15560, 4161, new int[] { 1160 }, 825), 
	KBD(15559, 4163, new int[] { 50 }, 825), 
	ZILYANA(15561, 4166, new int[] { 6247 }, 825), 
	CHAOS_ELEMENTAL(15568, 4170, new int[] { 3200 }, 825), 
	ZAMORAK(15562, 4164, new int[] { 8253 }, 825), 
	KREE(15558, 4165, new int[] { 6222 }, 825), 
	BANDOS(15555, 4159, new int[] { 6260 }, 825),
	KRAKEN(15556, 4157, new int[] { 3847 }, 825),
	PRIME(15557, 4160, new int[] { 2882 }, 825), 
	MOLE(15564, 4171, new int[] { 3340 }, 500),
	REX(15566, 4168, new int[] { 2883 }, 825), 
	HELL_CAT(7582, 3506, new int[] { -1 }, -1), 
	BARRELCHEST_JR(15567, 4169, new int[] { 5666 }, 825), 
	JAD(15569, 4176, new int[] { 2745 }, 25), 
	VERAC_JR(15570, 4177, new int[] { -1 }, -1), 
	RABBIT(10729, 1404, new int[] { -1 }, -1), 
	HELL_KITTEN(7583, 3505, new int[] { -1 }, -1), 
	VENENATIS(15571, 4190, new int[] { 4173 }, 825),
	SCORPIA(15572, 4194, new int[] { 4172 }, 825), 
	VETION(15574, 4192, new int[] { 4175 }, 825), 
	CAVE_KRAKEN(15556, 4157, new int[] { 3848 }, 825), 
	ZULRAH(15554, 4187, new int[] { 2042 }, 1000), 
	CALLISTO(15573, 4191, new int[] { 4174 }, 825), 
	JUNGLE_DEMON(15575, 4178, new int[] { 1472 }, 825), 
	PET_ROCK(15576, 4179, new int[] { 2452 }, 825),
	SUPREME(15565, 4167, new int[] { 2881 }, 825);
	/**
	 * KALPHITE_JR(15563, 4162), KALPHITE_JR2(15560, 4161), KBD(15559, 4163),
	 * ZILYANA(15561, 4166), SUPREME(15565, 4167), BARRELCHEST(15567, 4169),
	 * CHAOS_ELEMENTAL(15568, 4170), ZAMORAK(15562, 4164), KRAKEN(15556, 4157),
	 * PRIME(15557, 4160), MOLE(15564, 4171), REX(15566, 4168);
	 */
	/**
	 * The item id associated with the npc
	 */
	private int itemId;
	/**
	 * The npc id for the character associated with the pet
	 */
	private int petId;

	/**
	 * The ids of the boss npcs that drop this pet
	 */
	private int[] bossIds;

	/**
	 * The absolute maximum probability of receiving an item from the boss
	 */
	private int dropRate;

	/**
	 * Constructs a new Pet object with an npc id
	 * 
	 * @param slot
	 *            the npc id
	 */
	Pet(int itemId, int petId, int[] bossIds, int dropRate) {
		this.itemId = itemId;
		this.petId = petId;
		this.bossIds = bossIds;
		this.dropRate = dropRate;
	}


	/**
	 * Returns the item id used to spawn the pet
	 * 
	 * @return the item id
	 */
	public int getItemId() {
		return this.itemId;
	}

	/**
	 * Returns the npcid associated with the pet
	 * 
	 * @return the npc id
	 */
	public int getPetId() {
		return this.petId;
	}

	/**
	 * Returns the bosses associated with this pet
	 * 
	 * @return the boss ids
	 */
	public int[] getBossIds() {
		return this.bossIds;
	}

	/**
	 * Returns the drop rate associated with this pet
	 * 
	 * @return the drop rate
	 */
	public int getDropRate() {
		return this.dropRate;
	}

	/**
	 * A set of the elements from the Pet emum
	 */
	public static Set<Pet> PETS = EnumSet.allOf(Pet.class);

	/**
	 * Gets the Pet object associated with the item id
	 * 
	 * @param itemId
	 *            the item id
	 * @return the pet
	 */
	public static final Pet get(int itemId) {
		for (Pet pet : PETS) {
			if (pet.getItemId() == itemId)
				return pet;
		}
		return null;
	}

	/**
	 * Returns true if the npc id passed is the id of a pet, false if it is not.
	 * 
	 * @param npcId
	 *            the npcid
	 * @return true or false
	 */
	public static boolean isPet(int npcId) {
		for (Pet pet : PETS) {
			if (pet.getPetId() == npcId)
				return true;
		}
		return false;
	}

	public static void drop(Player player, int bossId) {
		Pet pet = null;
		for (Pet p : values()) {
			for (int boss : p.bossIds) {
				if (boss == bossId) {
					pet = p;
					break;
				}
			}
		}
		if (pet == null) {
			return;
		}
		if (pet.getDropRate() == -1) {
			return;
		}
		if (player.getItems().bankContains(pet.getItemId()) || player.getItems().playerHasItem(pet.getItemId())) {
			return;
		}
		if (Misc.random(pet.dropRate) == 0) {
			String name = NPCHandler.getNpcListName(bossId);
			player.getPet().setOwnedPet(pet.getItemId(), true);
			player.sendMessage("Shortly after the " + name + " fell, you found a Pet " + name + ".");
			if (player.getPet().getPet() == null && player.getPet().getNpc() == null) {
				player.getPet().spawn(pet.getItemId());
			} else {
				player.getItems().sendItemToAnyTab(pet.getItemId(), 1);
				player.sendMessage("The npc has been sent to an open spot in your bank.");
			}
			PlayerUpdating.announce("<shad=000000><col=FF5E00>News: " + Misc.formatPlayerName(player.playerName) + " has just received a Pet " + name.toLowerCase() + " from killing the boss.");
		}
	}

}
