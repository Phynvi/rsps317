package com.bclaus.rsps.server.vd.player.actions;

import java.util.Arrays;
import java.util.Optional;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.content.RandomSelection;
import com.bclaus.rsps.server.vd.content.TeleTabs;
import com.bclaus.rsps.server.vd.content.VoteBox;
import com.bclaus.rsps.server.vd.content.achievements.AchievementType;
import com.bclaus.rsps.server.vd.content.achievements.Achievements;
import com.bclaus.rsps.server.vd.content.cluescroll.ClueDifficulty;
import com.bclaus.rsps.server.vd.content.cluescroll.ClueScrollContainer;
import com.bclaus.rsps.server.vd.content.cluescroll.ClueScrollHandler;
import com.bclaus.rsps.server.vd.content.consumables.Food;
import com.bclaus.rsps.server.vd.content.consumables.Potions;
import com.bclaus.rsps.server.vd.content.minigames.Barrows;
import com.bclaus.rsps.server.vd.content.skills.impl.MysteryBox;
import com.bclaus.rsps.server.vd.content.skills.impl.Prayer;
import com.bclaus.rsps.server.vd.content.skills.impl.herblore.Herblore;
import com.bclaus.rsps.server.vd.content.skills.impl.hunter.HunterHandler;
import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.mobile.Hit;
import com.bclaus.rsps.server.vd.objects.cannon.CannonManager;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.PlayerAssistant;
import com.bclaus.rsps.server.vd.player.packets.PacketType;
import com.bclaus.rsps.server.vd.world.Location;
import com.bclaus.rsps.server.vd.world.Position;

/**
 * Clicking an item, bury bone, eat food etc
 */
public class ClickItem implements PacketType {

	private static long clickDelay;

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int junk = c.getInStream().readSignedWordBigEndianA();
		int itemSlot = c.getInStream().readUnsignedWordA();
		int itemId = c.getInStream().readUnsignedWordBigEndian();
		if (itemId != c.playerItems[itemSlot] - 1 || c.teleporting || HunterHandler.layTrap(c, itemId)) {
			return;
		}
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.isBanking = false;
			c.getBankPin().open(2);
			return;
		}
		TeleTabs.TabData tabData = TeleTabs.TabData.forId(itemId);
		if (tabData != null) {
			TeleTabs.useTeleTab(c, itemSlot, tabData);
		}
		if (itemId == 6099 || itemId == 6100 || itemId == 6101 || itemId == 6102) {
			PlayerAssistant.activateTeleportCrystal(c, itemId);

			return;
		}
		Food.eat(c, Food.FoodToEat.forId(itemId), itemSlot);
		if (Prayer.isBone(itemId)) {
			if (System.currentTimeMillis() - clickDelay >= 1375)
				Prayer.buryBones(c, itemId);
			clickDelay = System.currentTimeMillis();
			return;
		}
		if (Herblore.isUnidHerb(itemId)) {
			Herblore.handleHerbClick(c, itemId);
			return;
		}
		if (Potions.isPotion(c, itemId)) {
			Potions.handlePotion(c, itemId, itemSlot);
			return;
		}
		boolean found = true;
		switch (itemId) {
		case 7269:
			c.getItems().deleteItem(7269);
			c.votePoints += 5;
			c.sendMessage("You redeem the scroll for 5 vote points.");
			break;
		case 10525:
			//c.canDeposit = false;
			//ResourceBag.refreshInterface(c);
			c.sendMessage("Resource bags have been disabled.");
			break;
		case 10834:
			if (c.getPA().getTotalBagItems() > 0) {
				for (int i = 0; i < c.bagItems.length; i ++) {
					int modifiedSlot = i + 1;
					
					if (c.bagItems[i] == 0)
						return;
					c.sendMessage("Slot: @red@"+modifiedSlot+"@bla@ - Item name: @red@"+c.getItems().getItemName(c.bagItems[i]));
				}
			} else {
				c.sendMessage("You don't have any items stored to view");
			}
			break;
		case 6:
		case 8:
		case 10:
		case 12:
			CannonManager.makeCannon(c);
			break;
		//potato	
		case 5733:
			c.getDH().sendDialogues(1656, 0);
			break;
			
		case 2677:
		case 2678:
		case 2679:
		case 2680:
			if (c.clueContainer == null) {
				Optional<ClueDifficulty> cd = ClueDifficulty.getDifficulty(itemId);
				if (!cd.isPresent())
					return;
				c.clueContainer = new ClueScrollContainer(c, ClueScrollHandler.getStages(cd.get()));
			}
			c.clueContainer.current(itemId);
			break;

		case 2714:
			
			
			if (c.bossDifficulty == null) {
				c.sendMessage("You have not completed a clue scroll!");
				return;
			}
			
			Item[] items = ClueScrollHandler.determineReward(c, c.bossDifficulty);

			if (c.getItems().freeSlots() < items.length + 1) {
				c.sendMessage("You do not have enough space in your inventory!");
				return;
			}
			if (c.bossDifficulty.equals(ClueDifficulty.EASY)) {
				c.easyClue += 1;
				c.sendMessage("<col=009900> You have now completed " + c.easyClue + " easy clues");
			}
			if (c.bossDifficulty.equals(ClueDifficulty.MEDIUM)) {
				c.mediumClue += 1;
				c.sendMessage("<col=FF5050> You have now completed " + c.mediumClue + " medium clues");
				Achievements.increase(c, AchievementType.MEDIUM_CLUE, 1);
			}
			if (c.bossDifficulty.equals(ClueDifficulty.HARD)) {
				c.hardClue += 1;
				Achievements.increase(c, AchievementType.HARD_CLUE, 1);
				c.sendMessage("<col=CC3300> You have now completed " + c.hardClue + " hard clues");
			}
			if (c.bossDifficulty.equals(ClueDifficulty.ELITE)) {
				c.eliteClue += 1;
				Achievements.increase(c, AchievementType.ELITE_CLUE, 1);
				c.sendMessage("<col=5A0000> You have now completed " + c.eliteClue + " elite clues");
			}
			Achievements.increase(c, AchievementType.CLUE_SCROLL, 1);
			c.getItems().deleteItem(2714);
			c.getPA().displayReward(items);
			Arrays.stream(items).forEach(c.getItems()::addItem);
			c.sendMessage("You open the casket and obtain your reward!");
			c.bossDifficulty = null;
			break;
		default:
			found = false;
		}

		if (found) {
			return;
		}
		if (itemId == 4079) {
			c.startAnimation(1457);
		}
		if (itemId == 15576) {
			c.startAnimation(1333);
			return;
		}
		if (itemId == 7509) {
			if (System.currentTimeMillis() - c.foodDelay >= 600 && c.playerLevel[3] > 1) {
				c.getCombat().resetPlayerAttack();
				c.attackTimer += 2;
				c.startAnimation(829);
				c.damage(new Hit(10));
				c.updateRequired = true;
				c.getPA().refreshSkill(3);
				c.sendMessage("Ow! I nearly broke a tooth.");
				c.foodDelay = System.currentTimeMillis();
			}
		}

		if (itemId == 8013) {
			c.teleTab = true;
			c.getItems().deleteItem(itemId, c.getItems().getItemSlot(itemId), 1);
			TeleportExecutor.teleport(c, new Position(3087, 3504, 0));
		}
		if (itemId == 9044) {
			if (c.inWild() || c.duelStatus == 5) {
				return;
			}
			c.getPA().movePlayer(3046, 4969, 1);
			c.sendMessage("The item's magic teleports you to Rogues Den.");
		}

		if (itemId == 4204) {
			c.sendMessage("The King has asked for your help to save his Thanksgiving. He's requested:");
			c.sendMessage("----------------------------------------------------------------------");
			c.sendMessage("* I speak with the Bartender in Blue Moon's Bar located in Varrock");
			c.sendMessage("* I speak to the man in charge of Varrock's General Store");
			c.sendMessage("* I collect 6 Raw Chickens");
		}
		if (itemId == 1492) {
			TeleportExecutor.teleport(c, new Position(2435, 3314, 0));
		}
		if (itemId == 4079) {
			c.startAnimation(1457);

		}
		if (itemId == 6199) {
			new RandomSelection(c, RandomSelection.RewardType.MYSTERY_BOX, new RandomSelection.Item(6199, 1), 1).create();
		}
		if (itemId == 3062)
        	if (c.getItems().playerHasItem(3062)) {
        	VoteBox.Open(itemId, c);
        	return;
}
		if (itemId == 2717) {
			MysteryBox.addItem(c);
		}
		if (itemId == 15345) {
			if (c.getItems().playerHasItem(6831)) {
				c.getItems().deleteItem(6831, 1);
				c.getItems().addItem(4151, 1);
				c.isDonator = true;
				c.donatorRights = 2;
				c.playerRights = 7;
				c.disconnected = true;
			c.sendMessage("<col=255>You are now a DemonRsps Regular Donator!</col>");
		}
		}
		
		if (itemId == 2746) {
			if (c.getItems().freeSlots() != 0) {
				c.getItems().addItem(995, 100000);
			} else {
				Server.itemHandler.createGroundItem(c, 995, c.absX, c.absY, c.heightLevel, 100000);
			}
			c.getItems().deleteItem(2746, 1);
			return;
		}
		if (itemId == 2748) {
			if (c.getItems().freeSlots() != 0) {
				c.getItems().addItem(995, 250000);
			} else {
				Server.itemHandler.createGroundItem(c, 995, c.absX, c.absY, c.heightLevel, 250000);
			}
			c.getItems().deleteItem(2748, 1);
			return;
		}
		if (itemId == 4155) {
			c.getDH().sendDialogues(346, 1596);
			return;
		}
		if (itemId == 2528) {
			c.sendMessage("You can only instant level combat skills");
			c.sendMessage("<col=255>Make sure you make your selection, otherwise it will auto default to Attack!");
			c.getPA().showInterface(2808);
		}
		if (itemId == 4251) {
			c.getPA().movePlayer(3565, 3316, 0);
			c.sendMessage("You empty the ectophial.");
			c.getItems().deleteItem(4251, c.getItems().getItemSlot(4251), 1);
			c.getItems().addItem(4252, 1);
		}

		if (itemId == 6542) {
			c.getItems().deleteItem(6542, c.getItems().getItemSlot(6542), 1);
			c.getItems().addItem(6335 + Misc.random(5), 1);
			c.sendMessage("You have received a random Tribal Mask.");
		}

		if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			int a;
			a = itemId;
			if (a == 5509)
				pouch = 0;
			if (a == 5510)
				pouch = 1;
			if (a == 5512)
				pouch = 2;
			if (a == 5514)
				pouch = 3;
			c.getPA().fillPouch(pouch);
			return;
		}
		
		if (itemId == 952) {
			handleShovel(c);
		}
		
		if (itemId == 8015) {
			handleBones(c);
		}
		
		}
	
	private void handleShovel(final Player c) {
		c.startAnimation(830);
		Server.getTaskScheduler().schedule(new ScheduledTask(1) {

			@Override
			public void execute() {
				stop();
			}

			@Override
			public void onStop() {
				doShovelActions(c);

			}
		});
	}
	
	private void handleBones(final Player c) {
		if (c.getItems().playerHasItem(532, c.getItems().getItemAmount(532))) {
			c.getItems().deleteItem(8015, 1);
			c.getItems().replaceItem(c, 532, 6883);
			c.startAnimation(722);
			c.gfx100(141);
		} else if (c.getItems().playerHasItem(532, 1)) {
			c.sendMessage("You need bones to cast bones to peaches.");
		}
	}

	private void sendBarrowsLocation(Player c) {
		c.teleTimer = 3;
		PlayerAssistant.changeLocation(c);
	}

	private boolean sendBarrows(Player c) {
		if (c.inArea3(3553, 3301, 3561, 3294)) {
			c.newLocation = 1;
			sendBarrowsLocation(c);
		} else if (c.inArea3(3550, 3287, 3557, 3278)) {
			c.newLocation = 2;
			sendBarrowsLocation(c);
		} else if (c.inArea3(3561, 3292, 3568, 3285)) {
			c.newLocation = 3;
			sendBarrowsLocation(c);
		} else if (c.inArea3(3570, 3302, 3579, 3293)) {
			c.newLocation = 4;
			sendBarrowsLocation(c);
		} else if (c.inArea3(3571, 3285, 3582, 3278)) {
			c.newLocation = 5;
			sendBarrowsLocation(c);
		} else if (c.inArea3(3562, 3279, 3569, 3273)) {
			c.newLocation = 6;
			sendBarrowsLocation(c);
		} else {
			return false;
		}
		return true;
	}

	private boolean sendClue(Player player, int id) {
		if (player.clueContainer == null || id == -1) {
			return false;
		}
		Location l = player.clueContainer.stages.peek().getLocation();
		if (player.getPosition().inLocation(l)) {
			player.clueContainer.next(id);
			return true;
		}
		return false;
	}

	private void doShovelActions(Player c) {

		if (!Barrows.digToBrother(c) && !sendClue(c, c.getItems().search(ClueDifficulty.getClueIds()))) {
			c.sendMessage("Nothing interesting happens.");
		}
	}
}
