package com.bclaus.rsps.server.vd.content.cluescroll;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.items.IntervalItem;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.net.PlayerUpdating;
import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.items.ItemAssistant;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.util.Chance;
import com.bclaus.rsps.server.util.Misc;

import com.google.common.collect.Iterables;

/**
 * @author lare96 <http://github.com/lare96>
 */
public final class ClueScrollHandler {

	public static final int DIG_RADIUS = 3;
	public static final double CLUE_DROP_RATE = 0.025;

	public static final int[] ELITE_CLUE_DROPS = { 4186, 4972, 2881, 2882, 2883, 4172, 4173, 4174, 4175, 3847, 4291, 6222, 6247, 6260, 6203, 3340, 3200 };
	public static final IntervalItem[] EASY_CLUE_REWARDS = { new IntervalItem(1077), new IntervalItem(1167), new IntervalItem(1645), new IntervalItem(1621), new IntervalItem(1637), new IntervalItem(1718), new IntervalItem(1893), new IntervalItem(1511), new IntervalItem(1168), new IntervalItem(2631), new IntervalItem(2633), new IntervalItem(2635), new IntervalItem(2637), new IntervalItem(2583), new IntervalItem(2585), new IntervalItem(2587), new IntervalItem(2589), new IntervalItem(2591), new IntervalItem(2593), new IntervalItem(2595), new IntervalItem(2597), new IntervalItem(2579), new IntervalItem(2635), new IntervalItem(7329, 1, 17), new IntervalItem(7330, 1, 17), new IntervalItem(7331, 1, 17), new IntervalItem(7362), new IntervalItem(7364), new IntervalItem(7366), new IntervalItem(2631), new IntervalItem(7364), new IntervalItem(7362), new IntervalItem(7368), new IntervalItem(7366), new IntervalItem(7388), new IntervalItem(7370), new IntervalItem(7372), new IntervalItem(7374), new IntervalItem(7376), new IntervalItem(7378), new IntervalItem(7380), new IntervalItem(7382), new IntervalItem(7374), new IntervalItem(7376), new IntervalItem(7378), new IntervalItem(7380), new IntervalItem(7382), new IntervalItem(7384), new IntervalItem(7386), new IntervalItem(7388), new IntervalItem(7390), new IntervalItem(7392), new IntervalItem(7394), new IntervalItem(7396), new IntervalItem(7398), new IntervalItem(11710), new IntervalItem(11712), new IntervalItem(11714), new IntervalItem(10362), new IntervalItem(10364), new IntervalItem(10366), new IntervalItem(10034, 1, 15), new IntervalItem(803, 12, 15), new IntervalItem(802, 12, 19), new IntervalItem(813, 17, 23), new IntervalItem(814, 15, 22), new IntervalItem(815, 14, 25), new IntervalItem(10424), new IntervalItem(824, 1, 17), new IntervalItem(825, 1, 21), new IntervalItem(826, 1, 29), new IntervalItem(827, 12, 32), new IntervalItem(851), new IntervalItem(853), new IntervalItem(858), new IntervalItem(884, 1, 25), new IntervalItem(1033), new IntervalItem(1035), new IntervalItem(1119), new IntervalItem(1121), new IntervalItem(1129), new IntervalItem(1194), new IntervalItem(1191), new IntervalItem(1197), new IntervalItem(1211), new IntervalItem(1239), new IntervalItem(7366), new IntervalItem(7364), new IntervalItem(7356), new IntervalItem(10434), new IntervalItem(10412), new IntervalItem(10366), new IntervalItem(10392), new IntervalItem(2585), new IntervalItem(7392), new IntervalItem(7396), new IntervalItem(7331), new IntervalItem(2635), new IntervalItem(3472), new IntervalItem(7390), new IntervalItem(2633), new IntervalItem(2637) };
	public static final IntervalItem[] MEDIUM_CLUE_REWARDS = { new IntervalItem(2599), new IntervalItem(2601), new IntervalItem(2603), new IntervalItem(2605), new IntervalItem(2607), new IntervalItem(2609), new IntervalItem(2611), new IntervalItem(2613), new IntervalItem(7334), new IntervalItem(7340), new IntervalItem(7346), new IntervalItem(7352), new IntervalItem(7358), new IntervalItem(7319), new IntervalItem(7321), new IntervalItem(7323), new IntervalItem(7325), new IntervalItem(7327), new IntervalItem(7372), new IntervalItem(7370), new IntervalItem(7380), new IntervalItem(7378), new IntervalItem(2645), new IntervalItem(2647), new IntervalItem(2648), new IntervalItem(2577), new IntervalItem(2579), new IntervalItem(2613), new IntervalItem(2611), new IntervalItem(2609), new IntervalItem(2607), new IntervalItem(3475), new IntervalItem(2605), new IntervalItem(2603), new IntervalItem(2599), new IntervalItem(2601), new IntervalItem(3474), new IntervalItem(2580), new IntervalItem(2578), new IntervalItem(2645), new IntervalItem(2649), new IntervalItem(2647), new IntervalItem(10420), new IntervalItem(10422), new IntervalItem(10436), new IntervalItem(10438), new IntervalItem(10416), new IntervalItem(10418), new IntervalItem(10400), new IntervalItem(10402), new IntervalItem(10446), new IntervalItem(10448), new IntervalItem(10450), new IntervalItem(10452), new IntervalItem(10454), new IntervalItem(10456), new IntervalItem(7358), new IntervalItem(7352), new IntervalItem(7346), new IntervalItem(7340), new IntervalItem(7334), new IntervalItem(7319), new IntervalItem(7325), new IntervalItem(7327), new IntervalItem(7323), new IntervalItem(7321) };
	public static final IntervalItem[] HARD_CLUE_REWARDS = {  new IntervalItem(15589), new IntervalItem(2639), new IntervalItem(2641), new IntervalItem(2643), new IntervalItem(7336), new IntervalItem(7342), new IntervalItem(7348), new IntervalItem(7354), new IntervalItem(7360), new IntervalItem(2619), new IntervalItem(2621), new IntervalItem(2617), new IntervalItem(2615), new IntervalItem(3476), new IntervalItem(2627), new IntervalItem(2629), new IntervalItem(2625), new IntervalItem(2623), new IntervalItem(3477), new IntervalItem(2657), new IntervalItem(2659), new IntervalItem(2655), new IntervalItem(2653), new IntervalItem(3478), new IntervalItem(2665), new IntervalItem(2667), new IntervalItem(2577), new IntervalItem(2581), new IntervalItem(2663), new IntervalItem(2661), new IntervalItem(3479), new IntervalItem(2673), new IntervalItem(2675), new IntervalItem(2671), new IntervalItem(2669), new IntervalItem(3480), new IntervalItem(10374), new IntervalItem(10370), new IntervalItem(10372), new IntervalItem(10368), new IntervalItem(10382), new IntervalItem(10378), new IntervalItem(10380), new IntervalItem(10376), new IntervalItem(10390), new IntervalItem(10386), new IntervalItem(10388), new IntervalItem(10384), new IntervalItem(2581), new IntervalItem(2651), new IntervalItem(2639), new IntervalItem(2641), new IntervalItem(2643), new IntervalItem(7400), new IntervalItem(7399), new IntervalItem(7398), new IntervalItem(10440), new IntervalItem(10442), new IntervalItem(10444), new IntervalItem(10446), new IntervalItem(10448), new IntervalItem(10450), new IntervalItem(10362), new IntervalItem(3486), new IntervalItem(3488), new IntervalItem(3483), new IntervalItem(3481), new IntervalItem(3485), new IntervalItem(3831), new IntervalItem(3832), new IntervalItem(3833), new IntervalItem(3834), new IntervalItem(3827), new IntervalItem(3828), new IntervalItem(3829), new IntervalItem(3830), new IntervalItem(3835), new IntervalItem(3836), new IntervalItem(3837), new IntervalItem(3838), new IntervalItem(4561, 10, 50), new IntervalItem(7331), new IntervalItem(7330), new IntervalItem(7329), new IntervalItem(1163), new IntervalItem(1201), new IntervalItem(108), new IntervalItem(1127), new IntervalItem(1093), new IntervalItem(1645), new IntervalItem(1691), new IntervalItem(1700), new IntervalItem(1644), new IntervalItem(990), new IntervalItem(1631), new IntervalItem(892, 50, 200), new IntervalItem(1615), new IntervalItem(1319), new IntervalItem(1333), new IntervalItem(1290), new IntervalItem(1303), new IntervalItem(860), new IntervalItem(862), new IntervalItem(10284), new IntervalItem(565, 10, 200), new IntervalItem(563, 10, 200) };
	public static final IntervalItem[] ELITE_CLUE_REWARDS = { new IntervalItem(12471),  new IntervalItem(2577), new IntervalItem(1079), new IntervalItem(1093), new IntervalItem(1113), new IntervalItem(1333), new IntervalItem(1127), new IntervalItem(1359), new IntervalItem(1147), new IntervalItem(1373), new IntervalItem(1163), new IntervalItem(2491), new IntervalItem(1185), new IntervalItem(2497), new IntervalItem(1201), new IntervalItem(2503), new IntervalItem(1275), new IntervalItem(861), new IntervalItem(1303), new IntervalItem(859), new IntervalItem(1319), new IntervalItem(2581), new IntervalItem(2577), new IntervalItem(2651), new IntervalItem(3486), new IntervalItem(3488), new IntervalItem(3483), new IntervalItem(3481), new IntervalItem(3485), new IntervalItem(3831), new IntervalItem(3832), new IntervalItem(3833), new IntervalItem(3834), new IntervalItem(3827), new IntervalItem(3828), new IntervalItem(3829), new IntervalItem(3830), new IntervalItem(3835), new IntervalItem(3836), new IntervalItem(3837), new IntervalItem(3838), new IntervalItem(10374), new IntervalItem(10370), new IntervalItem(10372), new IntervalItem(10368), new IntervalItem(10382), new IntervalItem(10378), new IntervalItem(10380), new IntervalItem(10376), new IntervalItem(10390), new IntervalItem(10386), new IntervalItem(10388), new IntervalItem(10384), new IntervalItem(2581), new IntervalItem(4561, 10, 50), new IntervalItem(7331), new IntervalItem(7330), new IntervalItem(7329), new IntervalItem(1163), new IntervalItem(1201), new IntervalItem(1080), new IntervalItem(1127), new IntervalItem(1093), new IntervalItem(1645), new IntervalItem(1691), new IntervalItem(1700), new IntervalItem(1644), new IntervalItem(990), new IntervalItem(1631), new IntervalItem(1275), new IntervalItem(4587), new IntervalItem(892, 50, 200), new IntervalItem(1615), new IntervalItem(1319) };
	public static final IntervalItem[] ULTRA_RARE = { new IntervalItem(15389), new IntervalItem(10344), new IntervalItem(12356), new IntervalItem(12359), new IntervalItem(12358), new IntervalItem(12357), new IntervalItem(15586), new IntervalItem(10350), new IntervalItem(10348), new IntervalItem(10346), new IntervalItem(10352), new IntervalItem(10342), new IntervalItem(10338), new IntervalItem(10340), new IntervalItem(10344), new IntervalItem(10334), new IntervalItem(10330), new IntervalItem(10332), new IntervalItem(10336), };

	public static boolean calculateDrop(Player player, NPC npc, boolean always) {
		if (player.getItems().playerOwnsAnyItems(ClueDifficulty.getClueIds()) || player.bossDifficulty != null)
			return false;
		if ((Math.round(Misc.rand.nextDouble() * 100.0) / 100.0) <= CLUE_DROP_RATE || always) {
			Optional<ClueDifficulty> clueScroll = ClueDifficulty.determineClue(player, npc);
			if (!clueScroll.isPresent())
				return false;
			Item item = new Item(clueScroll.get().clueId);
			if (npc.npcType == 3847) {
				Server.itemHandler.createGroundItem(player, item.getId(), player.absX, player.absY + 1, player.heightLevel, item.getCount());
			} else {
				Server.itemHandler.createGroundItem(player, item.getId(), npc.absX, npc.absY, npc.heightLevel, item.getCount());
			}
			player.sendMessage("You have recieved a Clue scroll.");
			return true;
		}
		return false;
	}
	

	public static Item[] determineReward(Player p, ClueDifficulty c) {
		int amount = Misc.inclusiveRandom(c.minReward, c.maxReward);
		List<Item> items = new LinkedList<>();

		if (Chance.VERY_RARE.successful(Misc.RANDOM)) {
			IntervalItem item = Misc.randomElement(ULTRA_RARE).clone();
			items.add(new Item(item.id, item.count));
			amount--;
			PlayerUpdating.announce("<shad=000000><col=FF5E00>News: " + Misc.formatPlayerName(p.playerName) + " has just received " + ItemAssistant.getItemName(item.id) + "x" + item.count + " from a clue scroll!");
		}

		for (int i = 0; i < amount; i++) {
			if (Chance.UNCOMMON.successful(Misc.RANDOM)) {
				IntervalItem item = Misc.randomElement(c.rewards).clone();
				items.add(new Item(item.id, item.count));
			} else {
				IntervalItem item = Misc.randomElement(ClueScrollHandler.EASY_CLUE_REWARDS).clone();
				items.add(new Item(item.id, item.count));
			}
		}

		return Iterables.toArray(items, Item.class);
	}

	public static boolean npcDrop(Player player, NPC npc) {
		if (player.clueContainer != null && npc.spawnedBy == player.getIndex() && npc.forClue && player.bossDifficulty != null) {
			StringBuilder builder = new StringBuilder("The boss drops a casket, ");
			if (player.getItems().freeSlots() > 0) {
				player.getItems().addItem(2714, 1);
				builder.append("it is added to your inventory!");
			} else if (player.getItems().freeBankSlots() > 0) {
				player.getItems().sendItemToAnyTab(2714, 1);
				builder.append("it is added to your bank!");
			} else {
				Server.itemHandler.createGroundItem(player, 2714, npc.absX, npc.absY, npc.heightLevel, 1);
				builder.append("it is dropped on the floor!");
			}
			player.sendMessage(builder.toString());
			player.clueContainer = null;
			return true;
		}
		return false;
	}

	public static boolean giveReward(Player player) {
		if (player.clueContainer != null) {
			StringBuilder builder = new StringBuilder("You dig and find a casket, ");
			if (player.getItems().freeSlots() > 0) {
				player.getItems().addItem(2714, 1);
				builder.append("it is added to your inventory!");
			} else if (player.getItems().freeBankSlots() > 0) {
				player.getItems().sendItemToAnyTab(2714, 1);
				builder.append("it is added to your bank!");
			} else {
				Server.itemHandler.createGroundItem(player, 2714, player.absX, player.absY, player.heightLevel, 1);
				builder.append("it is dropped on the floor!");
			}
			player.sendMessage(builder.toString());
			player.clueContainer = null;
			return true;
		}
		return false;
	}

	public static ClueScroll[] getStages(ClueDifficulty c) {
		int amount = Misc.inclusiveRandom(c.minLeft, c.maxLeft);
		Set<ClueScroll> stages = new HashSet<>(amount);
		amount++;
		for (int i = 0; i < amount; i++)
			stages.add(Misc.randomElement(ClueScroll.values()));
		return Iterables.toArray(stages, ClueScroll.class);
	}
}
