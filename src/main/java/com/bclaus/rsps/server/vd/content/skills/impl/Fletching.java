package com.bclaus.rsps.server.vd.content.skills.impl;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.content.skills.impl.FletchingHandler.BoltTips;
import com.bclaus.rsps.server.vd.content.skills.impl.FletchingHandler.Bolts;
import com.bclaus.rsps.server.vd.content.skills.impl.FletchingHandler.Bows;
import com.bclaus.rsps.server.vd.content.skills.impl.FletchingHandler.CrossBow;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.PlayerAssistant;
import com.bclaus.rsps.server.task.ScheduledTask;

/**
 * @author Jesse Pinkman (Rune-Server.org)
 */

public class Fletching {
	/**
	 * ClickingButtons Data
	 */
	public static int[][] otherButtons = { { 34245, 0, 1 }, { 34244, 0, 5 }, { 34243, 0, 10 }, { 34242, 0, 28 }, // Far
																													// Left
																													// Picture,
																													// 1,5,10,X
			{ 34249, 1, 1 }, { 34248, 1, 5 }, { 34247, 1, 10 }, { 34246, 1, 28 }, // Left
																					// Picture,
																					// 1,5,10,X
			{ 34253, 2, 1 }, { 34252, 2, 5 }, { 34251, 2, 10 }, { 34250, 2, 28 }, // Middle
																					// Picture,
																					// 1,5,10,X
			{ 35001, 3, 1 }, { 35000, 3, 5 }, { 34255, 3, 10 }, { 34254, 3, 28 }, // Right
																					// Picture,
																					// 1,5,10,X
			{ 35005, 4, 1 }, { 35004, 4, 5 }, { 35003, 4, 10 }, { 35002, 4, 28 }, // Far
																					// Right
																					// Picture,
																					// 1,5,10,X
	};

	/**
	 * Reference Items (Ex: Feathers,Knife,Chisel)
	 */
	public static int[] refItems = {/* knife */946,/* headless */53,/* feather */
	314,/* bowString */1777,/* cBowString */9438,/* chisel */1755 };

	/**
	 * Returns Reference Item Ids
	 */
	public static int getKnife() {
		return refItems[0];
	}

	public static int getHeadless() {
		return refItems[1];
	}

	public static int getFeather() {
		return refItems[2];
	}

	public static int getBS() {
		return refItems[3];
	}

	public static int getCBS() {
		return refItems[4];
	}

	public static int getChisel() {
		return refItems[5];
	}

	/**
	 * Opens Fletching Dialogue Interface
	 */
	public static void openDialogue(Player c) {
		if (c.fletchSprites[2] <= 0) {
			resetFletching(c);
			return;
		}
		int i1 = c.fletchSprites[0], i2 = c.fletchSprites[1], i3 = c.fletchSprites[2], i4 = c.fletchSprites[3], i5 = c.fletchSprites[4];
		c.getPA().chooseItem5("What would you like to fletch?", name(c, i1), name(c, i2), name(c, i3), name(c, i4), name(c, i5), i1, i2, i3, i4, i5);
	}

	/**
	 * Gets the item name and trim's if needed
	 */
	public static String name(Player c, int item) {
		if (item <= 0)
			return "";
		String[] remove = { "Oak ", "Maple ", "Willow ", "Yew ", "Magic ", "Arrow ", "Crossbow ", "Bronze ", "Iron ", "Mithril ", "Adamant ", "Rune ", "Opal ", "Jade ", "Topaz ", "Sapphire ", "Emerald ", "Ruby ", "Diamond ", "Dragonstone ", "Dragon ", "Onyx " };
		String name = c.getItems().getItemName(item);
		for (String bad : remove) {
			if (name.contains(bad)) {
				name = name.replaceAll(bad, "");
			}
		}
		return name;
	}

	/**
	 * Handles Fletching Interface Clicks
	 */
	public static void handleFletchingClick(Player c, int actionID) {
		int fletchLevel = c.getPA().getLevelForXP(c.playerXP[Player.playerFletching]);
		for (int i = 0; i < otherButtons.length; i++) {
			if (otherButtons[i][0] == actionID) {
				c.fletchIndex = otherButtons[i][1];
				c.fletchAmount = otherButtons[i][2];
				if (c.fletchSprites[c.fletchIndex] <= 0)
					return;
				c.isFletching = true;
				if (c.fletchAmount > 1) {
					c.needsFletchDelay = true;
					startCycle(c);
				}
				c.fletchAmount--;
				if (c.fletchThis.equals("log")) {
					if (c.fletchIndex == 0) {
						c.fletchThis = "shaft";
						shaft(c);
						break;
					} else if (c.fletchIndex == 1) {
						c.fletchThis = "stock";
						stock(c, fletchLevel);
						break;
					} else if (c.fletchIndex == 2) {
						c.fletchThis = "short";
						bow(c, fletchLevel);
						break;
					} else if (c.fletchIndex == 3) {
						c.fletchThis = "long";
						bow(c, fletchLevel);
						break;
					}
				}
				if (c.fletchThis.equals("headlessarrow")) {
					headless(c);
					break;
				} else if (c.fletchThis.equals("arrow")) {
					arrows(c, fletchLevel);
					break;
				} else if (c.fletchThis.equals("bolt") || c.fletchThis.equals("boltGem")) {
					bolts(c, fletchLevel);
					break;
				} else if (c.fletchThis.equals("stringBow")) {
					stringBow(c, fletchLevel);
					break;
				} else if (c.fletchThis.equals("stringCross")) {
					stringCrossbow(c, fletchLevel);
					break;
				} else if (c.fletchThis.equals("tips")) {
					boltTips(c, fletchLevel);
					break;
				} else if (c.fletchThis.equals("limb")) {
					addLimbs(c, fletchLevel);
					break;
				}
				break;
			}
		}
	}

	/**
	 * Resets the Fletching Variables
	 */
	public static void resetFletching(Player c) {
		c.fletchDelay = c.fletchAmount = c.fletchItem = c.fletchIndex = -1;
		c.fletchThis = "";
		c.lastFletch = 0;
		c.isFletching = c.needsFletchDelay = false;
		for (int r = 0; r < c.fletchSprites.length; r++)
			c.fletchSprites[r] = -1;
	}

	/**
	 * Handles Timer
	 */
	public static void startCycle(final Player c) {
		Server.getTaskScheduler().schedule(new ScheduledTask(1) {
			@Override
			public void execute() {
				if (c.disconnected)
					this.stop();
				if (c.lastFletch <= 0 || System.currentTimeMillis() - c.lastFletch >= 1600) {
					if (c.fletchAmount > 0)
						appendDelay(c);
					else
						this.stop();
				}
			}

			@Override
			public void onStop() {
				resetFletching(c);
			}
		}.attach(c));
	}

	public static void headless(Player c) {
		if (c.getItems().playerHasItem(c.arrowShaft, 1)) {
			if (c.getItems().playerHasItem(getFeather(), 1)) {
				int Slot = c.getItems().getItemSlot(c.arrowShaft), amount = -1, Slot2 = c.getItems().getItemSlot(getFeather()), amount2 = -1;
				if (Slot != -1)
					amount = c.playerItemsN[Slot];
				if (Slot2 != -1)
					amount2 = c.playerItemsN[Slot2];
				if (amount >= 15 && amount2 >= 15) {
					c.getItems().deleteItem(c.arrowShaft, 15);
					c.getItems().deleteItem(getFeather(), 15);
					c.getItems().addItem(getHeadless(), 15);
					c.getPA().addSkillXP(15 * Constants.FLETCHING_EXPERIENCE, Player.playerFletching);
				} else {
					if (amount <= amount2) {
						c.getItems().deleteItem(c.arrowShaft, amount);
						c.getItems().deleteItem(getFeather(), amount);
						c.getItems().addItem(getHeadless(), amount);
						c.getPA().addSkillXP(15 * Constants.FLETCHING_EXPERIENCE, Player.playerFletching);
					} else {
						c.getItems().deleteItem(c.arrowShaft, amount2);
						c.getItems().deleteItem(getFeather(), amount2);
						c.getItems().addItem(getHeadless(), amount2);
						c.getPA().addSkillXP(15 * Constants.FLETCHING_EXPERIENCE, Player.playerFletching);
					}
				}
				c.lastFletch = System.currentTimeMillis();
			} else {
				c.getPA().removeAllWindows();
				PlayerAssistant.stopSkilling(c);
			}
		} else {
			c.getPA().removeAllWindows();
			PlayerAssistant.stopSkilling(c);
		}
		c.getPA().removeAllWindows();
	}

	public static void arrows(Player c, int fletchLevel) {
		if (FletchingHandler.Arrows.forId(c.fletchItem) != null) {
			FletchingHandler.Arrows arrow = FletchingHandler.Arrows.forId(c.fletchItem);
			if (fletchLevel >= arrow.getReq()) {
				if (c.getItems().playerHasItem(arrow.getTips(), 1)) {
					if (c.getItems().playerHasItem(getHeadless(), 1)) {
						int Slot = c.getItems().getItemSlot(arrow.getTips()), amount = -1, Slot2 = c.getItems().getItemSlot(getHeadless()), amount2 = -1;
						if (Slot != -1)
							amount = c.playerItemsN[Slot];
						if (Slot2 != -1)
							amount2 = c.playerItemsN[Slot2];
						if (amount >= 15 && amount2 >= 15) {
							c.getItems().deleteItem(arrow.getTips(), 15);
							c.getItems().deleteItem(getHeadless(), 15);
							c.getItems().addItem(arrow.getArrow(), 15);
							c.getPA().addSkillXP((arrow.getExp() * Constants.FLETCHING_EXPERIENCE) * 15, Player.playerFletching);
						} else {
							if (amount <= amount2) {
								c.getItems().deleteItem(arrow.getTips(), amount);
								c.getItems().deleteItem(getHeadless(), amount);
								c.getItems().addItem(arrow.getArrow(), amount);
								c.getPA().addSkillXP(arrow.getExp() * Constants.FLETCHING_EXPERIENCE, Player.playerFletching);
							} else {
								c.getItems().deleteItem(arrow.getTips(), amount2);
								c.getItems().deleteItem(getHeadless(), amount2);
								c.getItems().addItem(arrow.getArrow(), amount2);
								c.getPA().addSkillXP(arrow.getExp() * Constants.FLETCHING_EXPERIENCE, Player.playerFletching);
							}
						}
						c.lastFletch = System.currentTimeMillis();
					} else {
						c.getPA().removeAllWindows();
						PlayerAssistant.stopSkilling(c);
					}
				} else {
					c.getPA().removeAllWindows();
					PlayerAssistant.stopSkilling(c);
				}
			} else {
				c.getPA().removeAllWindows();
				PlayerAssistant.stopSkilling(c);
				c.sendMessage("You need a Fletching level of " + arrow.getReq() + " to fletch a " + c.getItems().getItemName(arrow.getArrow()));
			}
		} else {
			c.getPA().removeAllWindows();
			PlayerAssistant.stopSkilling(c);
		}
		c.getPA().removeAllWindows();
	}

	public static void boltTips(Player c, int fletchLevel) {
		for (final BoltTips tip : BoltTips.values())
			if (tip.getInput() == c.fletchItem)
				if (c.getItems().playerHasItem(tip.getInput(), 1)) {
					if (fletchLevel >= tip.getReq()) {
						c.getItems().deleteItem(tip.getInput(), 1);
						c.getItems().addItem(tip.getOutput(), tip.getAmt());
						c.getPA().addSkillXP(tip.getExp() * Constants.FLETCHING_EXPERIENCE, Player.playerFletching);
						c.getPA().removeAllWindows();
						c.lastFletch = System.currentTimeMillis();
					} else {
						c.getPA().removeAllWindows();
						PlayerAssistant.stopSkilling(c);
						c.sendMessage("You need a Fletching level of " + tip.getReq() + " to fletch " + c.getItems().getItemName(tip.getOutput()));
					}
				} else {
					c.getPA().removeAllWindows();
					PlayerAssistant.stopSkilling(c);
				}
	}

	public static void bolts(Player c, int fletchLevel) {
		int in1 = 0, in2 = 0, out = 0, req = 0, xp = 0;
		for (final Bolts bolt : Bolts.values()) {
			if (c.fletchThis.equals("bolt") && bolt.getType().equals("bolt") && c.fletchItem == bolt.getInput1()) {
				in1 = bolt.getInput1();
				in2 = getFeather();
				out = bolt.getOutput();
				req = bolt.getReq();
				xp = bolt.getExp();
				break;
			} else if (c.fletchThis.equals("boltGem") && bolt.getType().equals("boltGem") && c.fletchItem == bolt.getInput2()) {
				in1 = bolt.getInput1();
				in2 = bolt.getInput2();
				out = bolt.getOutput();
				req = bolt.getReq();
				xp = bolt.getExp();
				break;
			}
		}
		int Slot = c.getItems().getItemSlot(in1), amount = -1, Slot2 = c.getItems().getItemSlot(in2), amount2 = -1;
		if (Slot != -1)
			amount = c.playerItemsN[Slot];
		if (Slot2 != -1)
			amount2 = c.playerItemsN[Slot2];
		if (fletchLevel >= req) {
			if (c.getItems().playerHasItem(in1, 1) && c.getItems().playerHasItem(in2, 1)) {
				if (amount >= 15 && amount2 >= 15) {
					c.getItems().deleteItem(in1, 15);
					c.getItems().deleteItem(in2, 15);
					c.getItems().addItem(out, 15);
					c.getPA().addSkillXP((xp * Constants.FLETCHING_EXPERIENCE) * 15, Player.playerFletching);
				} else {
					if (amount <= amount2) {
						c.getItems().deleteItem(in1, amount);
						c.getItems().deleteItem(in2, amount);
						c.getItems().addItem(out, amount);
						c.getPA().addSkillXP(xp * Constants.FLETCHING_EXPERIENCE, Player.playerFletching);
					} else {
						c.getItems().deleteItem(in1, amount2);
						c.getItems().deleteItem(in2, amount2);
						c.getItems().addItem(out, amount2);
						c.getPA().addSkillXP(xp * Constants.FLETCHING_EXPERIENCE, Player.playerFletching);
					}
				}
				c.lastFletch = System.currentTimeMillis();
			}
		} else {
			c.getPA().removeAllWindows();
			PlayerAssistant.stopSkilling(c);
			c.sendMessage("You need a Fletching level of " + req + " to fletch " + c.getItems().getItemName(out));
		}
		c.getPA().removeAllWindows();
	}

	public static void shaft(Player c) {
		if (c.getItems().playerHasItem(c.fletchItem, 1)) {
			c.getItems().deleteItem(c.fletchItem, 1);
			c.getItems().addItem(c.arrowShaft, 15);
			c.getPA().addSkillXP(5 * Constants.FLETCHING_EXPERIENCE, Player.playerFletching);
			c.getPA().removeAllWindows();
			c.startAnimation(1248);
			c.lastFletch = System.currentTimeMillis();
		} else {
			c.getPA().removeAllWindows();
			PlayerAssistant.stopSkilling(c);
		}
	}

	public static void stock(Player c, int fletchLevel) {
		for (final CrossBow bow : CrossBow.values()) {
			if (c.fletchItem == bow.getLog()) {
				if (fletchLevel >= bow.getReq()) {
					if (c.getItems().playerHasItem(bow.getLog()) && c.getItems().playerHasItem(getKnife())) {
						c.getItems().deleteItem(bow.getLog(), 1);
						c.getItems().addItem(bow.getStock(), 1);
						c.getPA().addSkillXP(bow.getExp1() * Constants.FLETCHING_EXPERIENCE, Player.playerFletching);
						c.startAnimation(1248);
						c.lastFletch = System.currentTimeMillis();
					}
				} else {
					c.getPA().removeAllWindows();
					PlayerAssistant.stopSkilling(c);
					c.sendMessage("You need a Fletching level of " + bow.getReq() + " to fletch a " + c.getItems().getItemName(bow.getBowU()));
				}
			}
		}
		c.getPA().removeAllWindows();
	}

	public static void addLimbs(Player c, int fletchLevel) {
		for (final CrossBow bow : CrossBow.values()) {
			if (c.fletchItem == bow.getStock()) {
				if (fletchLevel >= bow.getReq()) {
					if (c.getItems().playerHasItem(bow.getStock()) && c.getItems().playerHasItem(bow.getLimbs())) {
						c.getItems().deleteItem(bow.getStock(), 1);
						c.getItems().deleteItem(bow.getLimbs(), 1);
						c.getItems().addItem(bow.getBowU(), 1);
						c.getPA().addSkillXP(bow.getExp1() * Constants.FLETCHING_EXPERIENCE, Player.playerFletching);
						c.lastFletch = System.currentTimeMillis();
					}
				} else {
					c.getPA().removeAllWindows();
					PlayerAssistant.stopSkilling(c);
					c.sendMessage("You need a Fletching level of " + bow.getReq() + " to fletch a " + c.getItems().getItemName(bow.getBowU()));
				}
			}
		}
		c.getPA().removeAllWindows();
	}

	public static void stringBow(Player c, int fletchLevel) {
		for (final Bows bow : Bows.values()) {
			if (c.fletchItem == bow.getBowU()) {
				if (fletchLevel >= bow.getReq()) {
					if (c.getItems().playerHasItem(getBS()) && c.getItems().playerHasItem(bow.getBowU())) {
						c.getItems().deleteItem(bow.getBowU(), 1);
						c.getItems().deleteItem(getBS(), 1);
						c.getItems().addItem(bow.getBow(), 1);
						c.getPA().addSkillXP(bow.getExp() * Constants.FLETCHING_EXPERIENCE, Player.playerFletching);
						c.startAnimation(bow.getEmote());
						c.lastFletch = System.currentTimeMillis();
					}
				} else {
					c.getPA().removeAllWindows();
					PlayerAssistant.stopSkilling(c);
					c.sendMessage("You need a Fletching level of " + bow.getReq() + " to string a " + c.getItems().getItemName(bow.getBow()));
				}
			}
		}
		c.getPA().removeAllWindows();
	}

	public static void stringCrossbow(Player c, int fletchLevel) {
		for (final CrossBow bow : CrossBow.values()) {
			if (c.fletchItem == bow.getBowU()) {
				if (fletchLevel >= bow.getReq()) {
					if (c.getItems().playerHasItem(getCBS()) && c.getItems().playerHasItem(bow.getBowU())) {
						c.getItems().deleteItem(bow.getBowU(), 1);
						c.getItems().deleteItem(getCBS(), 1);
						c.getItems().addItem(bow.getBow(), 1);
						c.getPA().addSkillXP(bow.getExp2() * Constants.FLETCHING_EXPERIENCE, Player.playerFletching);
						c.startAnimation(bow.getEmote());
						c.lastFletch = System.currentTimeMillis();
					}
				} else {
					c.getPA().removeAllWindows();
					PlayerAssistant.stopSkilling(c);
					c.sendMessage("You need a Fletching level of " + bow.getReq() + " to string a " + c.getItems().getItemName(bow.getBow()));
				}
			}
		}
		c.getPA().removeAllWindows();
	}

	public static void bow(Player c, int fletchLevel) {
		int in1 = 0, out = 0, req = 0, xp = 0;
		for (final Bows bow : Bows.values()) {
			if (c.fletchThis.equals("short") && bow.getBowType().equals("short") && c.fletchItem == bow.getLog()) {
				in1 = bow.getLog();
				out = bow.getBowU();
				req = bow.getReq();
				xp = bow.getExp();
				break;
			} else if (c.fletchThis.equals("long") && bow.getBowType().equals("long") && c.fletchItem == bow.getLog()) {
				in1 = bow.getLog();
				out = bow.getBowU();
				req = bow.getReq();
				xp = bow.getExp();
				break;
			}
		}
		if (fletchLevel >= req) {
			if (c.getItems().playerHasItem(in1, 1)) {
				c.getItems().deleteItem(in1, 1);
				c.getItems().addItem(out, 1);
				c.startAnimation(1248);
				c.lastFletch = System.currentTimeMillis();
				c.getPA().addSkillXP(xp * Constants.FLETCHING_EXPERIENCE, Player.playerFletching);
			} else {
				c.getPA().removeAllWindows();
				PlayerAssistant.stopSkilling(c);
			}
		} else {
			c.getPA().removeAllWindows();
			PlayerAssistant.stopSkilling(c);
			c.sendMessage("You need a Fletching level of " + req + " to fletch a " + c.getItems().getItemName(out));
		}
		c.getPA().removeAllWindows();
	}

	public static void appendDelay(Player c) {
		int fletchLevel = c.getPA().getLevelForXP(c.playerXP[Player.playerFletching]);
		if (c.fletchAmount > 0) {
			c.fletchAmount--;
			if (c.fletchThis.equals("log")) {
				if (c.fletchIndex == 0) {
					c.fletchThis = "shaft";
					shaft(c);
				} else if (c.fletchIndex == 1) {
					c.fletchThis = "stock";
					stock(c, fletchLevel);
				} else if (c.fletchIndex == 2) {
					c.fletchThis = "short";
					bow(c, fletchLevel);
				} else if (c.fletchIndex == 3) {
					c.fletchThis = "long";
					bow(c, fletchLevel);
				}
			}
			if (c.fletchThis.equals("headlessarrow"))
				headless(c);
			else if (c.fletchThis.equals("arrow"))
				arrows(c, fletchLevel);
			else if (c.fletchThis.equals("bolt") || c.fletchThis.equals("boltGem"))
				bolts(c, fletchLevel);
			else if (c.fletchThis.equals("stringBow"))
				stringBow(c, fletchLevel);
			else if (c.fletchThis.equals("stringCross"))
				stringCrossbow(c, fletchLevel);
			else if (c.fletchThis.equals("tips"))
				boltTips(c, fletchLevel);
			else if (c.fletchThis.equals("limb"))
				addLimbs(c, fletchLevel);
			else if (c.fletchThis.equals("shaft"))
				shaft(c);
			else if (c.fletchThis.equals("stock"))
				stock(c, fletchLevel);
			else if (c.fletchThis.equals("short") || c.fletchThis.equals("long"))
				bow(c, fletchLevel);
		}
	}
}