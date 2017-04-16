package com.bclaus.rsps.server.vd.npc.pets;

import java.util.HashMap;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;

/**
 * 
 * @author Jason MacKeigan
 * @date Aug 20, 2014, 11:53:02 AM
 */
public class PlayerPet implements PetIntel {
	/**
	 * The player that owns the pet
	 */
	Player player;
	/**
	 * The pet owned by the player
	 */
	private Pet pet;

	/**
	 * The npc assigned to the pet
	 */
	private NPC npc;

	/**
	 * The time in milleseconds of the last spawn
	 */
	private long lastSpawn;

	/**
	 * Stores a small map of all the pets owned by the player. If they own it
	 * the state of it will be true. The Integer is the npc's item id.
	 */
	public HashMap<Integer, Boolean> ownedPets = new HashMap<>();

	/**
	 * Constructs a new class to handle the players pet
	 * 
	 * @param player
	 */
	public PlayerPet(Player player) {
		this.player = player;
		for (Pet pet : Pet.values()) {
			if (ownedPets.get(pet.getItemId()) == null) {
				ownedPets.put(pet.getItemId(), false);
			}
		}
	}

	/**
	 * Processes critial pet actions such as following and more if needed
	 */
	@Override
	public void process() {
		if (npc != null) {
			if (player.distanceToPoint(npc.getX(), npc.getY()) < 25) {
				Server.npcHandler.followPlayer(npc.getIndex(), player.getIndex());
			} else {
				npc.moveX = player.getX();
				npc.moveY = player.getY();
				npc.absX = player.getX();
				npc.absY = player.getY() - 1;
				npc.heightLevel = player.heightLevel;
			}
		}
	}

	/**
	 * Creates a new pet spawn
	 * 
	 * @param itemId
	 *            the itemId used to spawn
	 */
	public boolean spawn(int itemId) {
		/*if (System.currentTimeMillis() - lastSpawn < 5000) {
			player.sendMessage("You can only spawn a pet once every 5 seconds.");
			return false;
		}*/
		if (this.npc != null) {
			player.sendMessage("You still have a pet spawned, pickup your pet to spawn another.");
			return false;
		}
		if(player.isInGWD()) {
			player.sendMessage("You currently can't use pets in GWD");
			return false;
		}
		/*if (!this.isPetOwner(itemId)) {
			player.sendMessage("You cannot drop this pet, you do not have the privilege of owning it.");
			return false;
		}*/
		lastSpawn = System.currentTimeMillis();
		setPet(Pet.get(itemId));
		NPCHandler.spawnNpc(player, pet.getPetId(), player.getX() + 1, player.getY(), player.heightLevel, 0, 0, 0, 0, 0, false, false);
		return true;
	}

	/**
	 * Disposes the npc, pet information, and adds the item back to your bank
	 */
	public void dispose() {
		if (player == null || pet == null || npc == null) {
			return;
		}
		if(pet.getItemId() == 10729) {
			NPCHandler.NPCS.get(npc.getIndex()).absX = -1;
			NPCHandler.NPCS.get(npc.getIndex()).absY = -1;
			NPCHandler.NPCS.remove(npc);
			pet = null;
			npc = null;
			return;
		}
		if (player.getItems().freeSlots() <= 0) {
			player.sendMessage("The " + NPCHandler.getNpcListName(npc.npcType) + " has been sent to your bank.");
			player.getItems().sendItemToAnyTab(pet.getItemId(), 1);
		} else {
			player.getItems().addItem(pet.getItemId(), 1);
		}
		NPCHandler.NPCS.get(npc.getIndex()).absX = -1;
		NPCHandler.NPCS.get(npc.getIndex()).absY = -1;
		NPCHandler.NPCS.remove(npc);
		pet = null;
		npc = null;
	}

	/**
	 * Returns the pet associated with this player
	 * 
	 * @return the pet
	 */
	public Pet getPet() {
		return this.pet;
	}

	/**
	 * Sets the owners pet to a new or existing one
	 * 
	 * @param pet
	 *            the pet
	 */
	public void setPet(Pet pet) {
		this.pet = pet;
	}

	/**
	 * Returns the player object associated with the pet
	 * 
	 * @return the player
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Returns the npc
	 * 
	 * @return the npc
	 */
	public NPC getNpc() {
		return this.npc;
	}

	/**
	 * Sets the NPC object associated with the pet
	 * 
	 * @param npc
	 *            the npc
	 */
	public void setNpc(NPC npc) {
		this.npc = npc;
	}

	/**
	 * Sets the time in milleseconds of the spawn
	 * 
	 * @param spawn
	 *            the time
	 */
	public void setLastSpawn(Long spawn) {
		this.lastSpawn = spawn;
	}

	/**
	 * Returns the time of the last spawn
	 * 
	 * @return the time
	 */
	public long getLastSpawn() {
		return this.lastSpawn;
	}

	public void setOwnedPet(int id, boolean state) {
		this.ownedPets.put(id, state);
	}

	public boolean isPetOwner(int id) {
		if (this.ownedPets.get(id) == null)
			return false;
		return this.ownedPets.get(id);
	}

}
