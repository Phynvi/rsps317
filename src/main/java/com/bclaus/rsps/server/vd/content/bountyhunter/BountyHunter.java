package com.bclaus.rsps.server.vd.content.bountyhunter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.util.NameUtils;

public final class BountyHunter {

	/**
	 * The player's hunter information ( current and highest killstreak ).
	 */
	private int[] hunter_info = new int[2];

	/**
	 * The player's 3 last killed targets.
	 */
	private List<String> last_targets = new CopyOnWriteArrayList<String>();

	/**
	 * The time the player left wilderness.
	 */
	private long left_wilderness;

	/**
	 * The bounty hunter.
	 */
	private Player player;

	/**
	 * The player's rogue information ( current and highest killstreak ).
	 */
	private int[] rogue_info = new int[2];
	/**
	 * The bounty to be hunted.
	 */
	private String target;

	/**
	 * The amount of targets the player avoided.
	 */
	private int targets_lost;

	/**
	 * Constructor of the bounty hunter.
	 *
	 * @param player the bounty hunter
	 */
	public BountyHunter(Player player) {
		this.player = player;
	}

	/**
	 * Adds a target to the killed list
	 *
	 * @param target the player to be added
	 */
	public void addTargetToList(String t) {
		if (t == null) {
			return;
		}

		for (int i = 0; i < last_targets.size(); i++) {
			String name = last_targets.get(i);

			if (name == null) {
				last_targets.remove(i);
				continue;
			}

			if (name.toLowerCase().equals(t.toLowerCase())) {
				return;
			}
		}

		last_targets.add(t);

		if (last_targets.size() > 3) {
			last_targets.remove(0);
		}
	}

	/**
	 * Updates the player and the targets info when the player leaves the wilderness ( for longer than 2 mins ) or logs
	 * out.
	 */
	public void avoidTarget() {
		if (target == null) {
			return;
		}

		targets_lost += 1;
		addTargetToList(target);

		if (hasTarget()) {
			getTarget().sendMessage("Your target is no longer avaliable.");
			getTarget().getBountyHunter().reset();
		}

		reset();
	}

	public int getRiskCarried(Player player) {
		int toReturn = 0;
		for (int i = 0; i < player.playerEquipment.length; i++) {
			toReturn += Item.getItemShopValue(player, player.playerEquipment[i]) * player.playerEquipmentN[i];
		}
		for (int i = 0; i < player.playerItems.length; i++) {
			toReturn += Item.getItemShopValue(player, player.playerItems[i]) * player.playerItemsN[i];
		}
		if (toReturn > Integer.MAX_VALUE)
			toReturn = Integer.MAX_VALUE;
		return toReturn;
	}

	/**
	 * Calculates the target's wealth and displays the interface config sprite.
	 *
	 * @return the wealth type ( low, medium, high, etc )
	 */
	public String calculateTargetWealth() {

		Player t = getTarget();
		player.getPA().sendFrame36(876, 0);

		for (int i = 0; i < 6; i++) {
			player.getPA().sendFrame36(877 + i, 1);
		}

		if (t == null) {
			player.getPA().sendFrame36(876, 1);
			return "---";
		}

		int carried_wealth = getRiskCarried(t);

		System.out.println(player.getName()+" Total wealth: "+carried_wealth);

		if (carried_wealth > 10_000_000) {
			player.getPA().sendFrame36(881, 0);
			return "Wealth: V. High";
		} else if ((carried_wealth >= 1_000_000) && (carried_wealth < 10_000_000)) {
			player.getPA().sendFrame36(880, 0);
			return "Wealth: High";
		} else if ((carried_wealth >= 250_000) && (carried_wealth < 1_000_000)) {
			player.getPA().sendFrame36(879, 0);
			return "Wealth: Medium";
		} else if ((carried_wealth >= 50_000) && (carried_wealth < 250_000)) {
			player.getPA().sendFrame36(878, 0);
			return "Wealth: Low";
		} else {
			player.getPA().sendFrame36(877, 0);
			return "Wealth: V. Low";
		}
	}

	/**
	 * Returns the player hunter information ( current and highest target killstreak )
	 *
	 * @return the information
	 */
	public int[] getHunterInfo() {
		return hunter_info;
	}

	/**
	 * Returns the last 3 targets killed by the player.
	 *
	 * @return the last 3 targets
	 */
	public List<String> getLastTargets() {
		return last_targets;
	}

	/**
	 * Returns the time the player left the wilderness.
	 *
	 * @return the time
	 */
	public long getLeftWildernessTime() {
		return left_wilderness;
	}

	/**
	 * Returns the player rogue information ( current and highest killstreak of players with targets that doesnt match
	 * with this player)
	 *
	 * @return the information
	 */
	public int[] getRogueInfo() {
		return rogue_info;
	}

	public static long usernameToHash(String username) {
		char[] chars = username.toCharArray(); // skip #charAt(int) bounds check
		long hash = 0L;

		for (int i = 0; i < username.length(); i++)
		{
			char c = chars[i];
			hash *= 37L;

			if ((c >= 'A') && (c <= 'Z'))
			{
				hash += (1 + c) - 65;
			}
			else if ((c >= 'a') && (c <= 'z'))
			{
				hash += (1 + c) - 97;
			}
			else if ((c >= '0') && (c <= '9'))
			{
				hash += (27 + c) - 48;
			}
		}

		while (((hash % 37L) == 0L) && (hash != 0L))
		{
			hash /= 37L;
		}

		return hash;
	}

	/**
	 * Returns an Optional containing an instance of {@link Player} representing
	 * the player with the given username, if they are logged in. The will not
	 * be present if the player is not logged in.
	 *
	 * @param username The username of the targeted player. Not null.
	 * @return An {@link Optional} player with the specified username. Not null.
	 * @throws NullPointerException If {@code username} is {@code null}.
	 */
	public static Optional<Player> getByUsername(String username) {
		Objects.requireNonNull(username, "username");
		return getByUsernameHash(usernameToHash(username));
	}

	/**
	 * Returns the first available {@link Player} who fulfills the contract of
	 * the supplied {@link Predicate}.
	 *
	 * @param filter The filter. Not null.
	 * @return A {@link Optional} containing the first match (if any). Not null.
	 * @throws NullPointerException If {@code filter} is {@code null}.
	 */
	public static Optional<Player> get(Predicate<Player> filter) {
		Objects.requireNonNull(filter, "null filter");
		for(Player player : World.PLAYERS) {
			if(player != null && filter.test(player)) {
				return Optional.of(player);
			}
		}
		return Optional.empty();
	}

	public static Optional<Player> getByUsernameHash(long hash) {
		return get(p -> NameUtils.nameToLong(p.playerName2) == hash);
	}

	/**
	 * Returns the target in player instance.
	 *
	 * @return the target
	 */
	public Player getTarget() {
		if (target == null) {
			return null;
		}

		Optional<Player> p =  getByUsername(target);
		return p.isPresent() ? p.get() : null;
	}

	/**
	 * Returns the target information ( location and combat level )
	 *
	 * @return the location and combat level
	 */
	public String getTargetInformation() {
		Player t = getTarget();

		if (t == null) {
			return "Level: -----";
		}

		String location = "Safe";
		String color = "@gr2@";

		if (t.inWild()) {
			int level = t.wildLevel;
			location = "Lvl " + (level <= 3 ? "1" : level - 3) + "-" + (level + 3);

			if ((t.combatLevel > (player.combatLevel + t.wildLevel))
					|| (player.combatLevel > (t.combatLevel + t.wildLevel))) {
				color = "@red@";
			}
		}

		location += ", Cmb " + t.combatLevel;
		return color + location;
	}

	/**
	 * Returns the target name.
	 *
	 * @return the name
	 */
	public String getTargetName() {
		return target;
	}

	/**
	 * Returns the amount of targets the player avoided.
	 *
	 * @return the amount
	 */
	public int getTargetsLost() {
		return targets_lost;
	}

	/**
	 * Checks if the player has a target.
	 *
	 * @return if he as a target
	 */
	public boolean hasTarget() {
		return (target != null) && getByUsername(target).isPresent();
	}

	/**
	 * Rewards the player after killing a player.
	 *
	 * @param player_name
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean killTarget(Player t) {
		if ((t == null) || (target == null) || t.playerName.equals(player.playerName)) {
			return false;
		}

		if (!target.equals(t.playerName)) {
			if (t.getBountyHunter().hasTarget() && !t.getBountyHunter().getTarget().equals(player.getName())) {
				rogue_info[0] += 1;

				if (rogue_info[0] > rogue_info[1]) {
					rogue_info[1] = rogue_info[0];
				}

				reset();
			}

			return false;
		}

		player.sendMessage("You've killed your target: " + t.playerName + "!");
		//GameEngine.itemHandler.createGroundItem(player, BountyHunterEmblem.getBest(player, true).getItemId(), t.getX(), t.getY(), 1, player.getId());
		addTargetToList(target);
		player.pkPoints += 50;
		hunter_info[0] += 1;

		if (hunter_info[0] > hunter_info[1]) {
			hunter_info[1] = hunter_info[0];
		}

		reset();
		t.getBountyHunter().reset();
		return true;
	}

	/**
	 * Resets the player's data.
	 */
	public void reset() {
		target = null;
		updateInterface();
		left_wilderness = 0;
		player.getPA().drawHeadicon(-1, player.getIndex(), 0, 0);
	}

	/**
	 * Searches for a new possible target inside the bounty hunters within his combat level and inside wilderness.
	 *
	 * @param possible_players the possible targets
	 */
	public void searchNextTarget(List<String> possible_players) {
		for (int i = 0; i < possible_players.size(); i++) {
			String name = possible_players.get(i);

			if (name == null) {
				possible_players.remove(i);
				continue;
			}

			Optional<Player> op = getByUsername(name);
			Player p = op.orElse(null);

			if ((p == null) || (p.playerId == player.playerId) || p.playerName.equals(player.playerName)
					|| last_targets.contains(name) || (p.getBountyHunter().hasTarget())
					|| (p.combatLevel > (player.combatLevel + 6))
					|| ((p.combatLevel + 6) < player.combatLevel)) {
				possible_players.remove(i);
				continue;
			}
		}

		if (possible_players.isEmpty() || (possible_players.size() == 0)) {
			//player.sendMessage("No targets avaliable.");
			return;
		}

		if ((targets_lost > 0) && (Misc.inclusiveRandom(targets_lost) > (targets_lost / 2))) {
			return;
		}

		Optional<Player> op = getByUsername(possible_players.get(Misc.inclusiveRandom(possible_players
				.size() - 1)));
		Player p = op.orElse(null);

		if ((p == null) || p.playerName.equals(player.playerName)) {
			//player.sendMessage("No targets avaliable.");
			return;
		}

		player.getBountyHunter().setTarget(p.playerName);
		p.getBountyHunter().setTarget(player.playerName);
		player.getPA().drawHeadicon(10, p.getIndex(), 0, 0);
		p.getPA().drawHeadicon(1, player.getIndex(), 0, 0);
	}

	/**
	 * Set the time the player left the wilderness.
	 *
	 * @param l
	 */
	public void setLeftWilderness(long l) {
		left_wilderness = l;
	}

	/**
	 * Sets the player new target.
	 *
	 * @param s the new target
	 */
	private void setTarget(String s) {
		target = s;
		player.sendMessage("You've been assigned a target: " + target);
		updateInterface();
	}

	/**
	 * Sets the amount of targets the player avoided.
	 *
	 * @param i the amount
	 */
	public void setTargetsLost(int i) {
		targets_lost = i;
	}

	/**
	 * Checks if the time the player left wilderness is over.
	 *
	 * @param l time to be checked ( seconds * 1000 )
	 * @return if it is over
	 */
	public boolean timeOver(long l) {
		return (left_wilderness - System.currentTimeMillis()) <= l;
	}

	public String calculateMyWealth() {
		return "Wealth: @gre@" + getRiskCarried(player);
	}

	/**
	 * Updates the bounty hunter interface.
	 */
	public void updateInterface() {
		player.getPA().sendFrame126("" + rogue_info[0], 23310);
		player.getPA().sendFrame126("" + rogue_info[1], 23311);
		player.getPA().sendFrame126("" + hunter_info[0], 23312);
		player.getPA().sendFrame126("" + hunter_info[1], 23313);
		player.getPA().sendFrame126(target == null ? "None" : target, 23307);
		player.getPA().sendFrame126(calculateTargetWealth(), 23305);
		player.getPA().sendFrame126(getTargetInformation(), 23308);
		player.getPA().sendFrame126(calculateMyWealth(), 23322);
	}
}