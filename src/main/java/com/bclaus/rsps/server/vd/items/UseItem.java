
package com.bclaus.rsps.server.vd.items;

import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.content.FlaxSpinning;
import com.bclaus.rsps.server.vd.content.PotionMixing;
import com.bclaus.rsps.server.vd.content.ResourceBag;
import com.bclaus.rsps.server.vd.content.minigames.warriorsguild.WarriorsGuild;
import com.bclaus.rsps.server.vd.content.skills.impl.Firemaking;
import com.bclaus.rsps.server.vd.content.skills.impl.Fletching;
import com.bclaus.rsps.server.vd.content.skills.impl.FletchingHandler;
import com.bclaus.rsps.server.vd.content.skills.impl.Woodcutting;
import com.bclaus.rsps.server.vd.content.skills.impl.crafting.GemCutting;
import com.bclaus.rsps.server.vd.content.skills.impl.herblore.Herblore;
import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.world.Position;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.content.Chest;
import com.bclaus.rsps.server.vd.content.PVPAssistant;
import com.bclaus.rsps.server.vd.content.Spinning;
import com.bclaus.rsps.server.vd.content.skills.impl.Cooking;
import com.bclaus.rsps.server.vd.content.skills.impl.Prayer;
import com.bclaus.rsps.server.vd.content.skills.impl.SmithingInterface;
import com.bclaus.rsps.server.vd.content.skills.impl.crafting.JewelleryMaking;
import com.bclaus.rsps.server.vd.content.skills.impl.crafting.LeatherMaking;
import com.bclaus.rsps.server.vd.objects.cannon.CannonManager;

/**
 * 
 * @author Ryan / Lmctruck30
 * 
 */

public class UseItem {

	public static void ItemonObject(Player c, int objectID, int objectX, int objectY, int itemId) {
		if (!c.getItems().playerHasItem(itemId, 1))
			return;
		c.getFarming().patchObjectInteraction(objectID, itemId, objectX, objectY);
		switch (objectID) {
		
		case 6:
			if(itemId == 2)
				CannonManager.fireCannon(c, new Position(c.objectX, c.objectY, c.heightLevel));
			break;
		case 2644:
			if (itemId == 1737)
				Spinning.itemOnObject(c, itemId);
			else
				FlaxSpinning.itemOnObject(c, itemId);
			break;
		case 3044:
		case 2643:
			if (itemId == 2357)
				JewelleryMaking.jewelryInterface(c);
			break;
			
		case 5574:
			if (itemId == 1931) {
				c.getItems().deleteItem(1931, 1);
				c.getItems().addItem(1933, 1);
				c.sendMessage("You collect some flour from the sack and place it in your pot.");
			}
		case 3359:
			if (itemId == 1492) {
				if (c.getItems().playerHasItem(1496, 1) && c.getItems().playerHasItem(1500, 1) && c.getItems().playerHasItem(1502, 1) && c.getItems().playerHasItem(1492, 1)) {
				c.getItems().deleteItem(1496, 1);
				c.getItems().deleteItem(1500, 1);
				c.getItems().deleteItem(1502, 1);
				c.getItems().deleteItem(1492, 1);
				c.getItems().addItem(1410, 1);
				c.getItems().addItem(554, 1000);
				c.getItems().addItem(560, 200);
				c.undergroundQP += 1;
				c.sendMessage("You use Iban's Doll on the well, You search more and find the Iban's staff!");
				c.sendMessage("You are teleported out of the temple.");
				TeleportExecutor.teleport(c, new Position(2494, 9715, 0));
				}
			}
		case 4587:
			if (itemId == 2347) {
				if (c.getItems().playerHasItem(960, 2) && c.getItems().playerHasItem(2353, 2) && (c.playerLevel[Player.playerSmithing] >= 64)) {
				c.getItems().deleteItem(960, 2);
				c.getItems().deleteItem(2353, 2);
				c.startAnimation(898);
				c.getPA().addSkillXP(25000, Player.playerSmithing);
				c.horrorQP += 1;
				c.sendMessage("You use your steel bars and planks to fix the lighthouse.");
			} else if (c.playerLevel[Player.playerSmithing] < 64) {
				c.sendMessage("You need a level of 64 Smithing to fix the lighthouse.");
				}
			}
		case 3294:
			if (itemId == 1410) {
				if (c.getItems().playerHasItem(1410, 1)) {
				c.getItems().deleteItem(1410, 1);
				c.startAnimation(899);
				c.getItems().addItem(1409, 1);
				c.sendMessage("You use the heat from the furnace to bind the staff together.");
				}
			}
		case 4272:
			if (itemId == 960) {
			c.getItems().deleteItem(960, 1);
			c.startAnimation(898);
			c.getPA().addSkillXP(900, Player.playerConstruction);
			c.sendMessage("You add a plank to the table.");
			//oak plank
			} else if (itemId == 8778) {
			c.getItems().deleteItem(8778, 1);
			c.startAnimation(898);
			c.getPA().addSkillXP(1400, Player.playerConstruction);
			c.sendMessage("You add a oak plank to the table.");
				//teak plank
			} else if (itemId == 8780) {
			c.getItems().deleteItem(8780, 1);
			c.startAnimation(898);
			c.getPA().addSkillXP(1850, Player.playerConstruction);
			c.sendMessage("You add a teak plank to the table.");
			} else if (itemId == 8782) {
			c.getItems().deleteItem(8782, 1);
			c.startAnimation(898);
			c.getPA().addSkillXP(2250, Player.playerConstruction);
			c.sendMessage("You add a mahogany plank to the table.");
			}
			break;
		case 15621: /* Magical Animator */
			WarriorsGuild.handleAnimator(c, itemId, objectX, objectY);
			break;
		case 12269:
		case 2732:
		case 114:
		case 2728:
			if (c.getItems().playerHasItem(itemId))
				Cooking.cookThisFood(c, itemId, objectID);
			break;
		case 409:
			Prayer.bonesOnAltar(c, itemId);
			break;
		case 2783:
			SmithingInterface.showSmithInterface(c, itemId);
			break;
		case 8689:
			if (c.getItems().playerHasItem(1925, 1)) {
				c.startAnimation(2305);
				c.getItems().deleteItem(1925, 1);
				c.getItems().addItem(1927, 1);
				c.sendMessage("You milked the Cow to get a fresh bucket of milk.");
			} else {
				c.sendMessage("You need a bucket you milk these cows!");
				break;
			}
		default:
			if (c.playerRights == 5)
				Misc.println("Player At Object id: " + objectID + " with Item id: " + itemId);
			break;
		}

	}

	public static void ItemonItem(Player c, int itemUsed, int useWith) {
		if (itemUsed == 1755 && useWith == 451) {
			c.getItems().deleteItem(451, 1);
			c.getItems().addItem(824, 10);
			c.sendMessage("You make some runite tips.");
		}
		if (useWith == 10834) {
        	if (!c.inWild()) {
        		c.sendMessage("You can only use this bag in the wilderness");
        		return;
        	}
        	if (c.bagItems.length > 27) {//c.getPA().getTotalBagItems... here not .length > 27
        		c.sendMessage("Your looting bag is full");
        		return;
        	}
        	if (c.getItems().playerHasItem(10834) && c.getItems().playerHasItem(itemUsed)) {
        		if (itemUsed != 10834) {
	        		if (!c.getItems().isStackable(itemUsed)) {
	        			if (c.getShops().getItemShopValue(itemUsed) < 150000) {
	        				if (c.bagItems[26] == 0) {
	        					c.isUsingBag= true;
		        				for (int i = 0; i < c.bagItems.length; i++) {
		        					if (c.getItems().getItemAmount(itemUsed) > 1) {
		        						if (c.bagItems[i] == 0) {
		        							c.outStream.createFrame(27);
		        							c.tempIdHolder = itemUsed;
		        						}
		        					} else {
			        					if (c.bagItems[i] == 0) {
			        						c.bagItems[i] = itemUsed;
			        						c.sendMessage("You have stored the item: @red@"+c.getItems().getItemName(itemUsed)+"@bla@ amount: @red@1X");
			        						c.getItems().deleteItem(itemUsed, 1);
			        						return;
			        					}
		        					}
		        				}
	        				} else {
	        					c.sendMessage("Your looting bag is full");
	        				}
	        			} else {
	        				c.sendMessage("You cannot store this rare items");
	        			}
	        		} else {
	        			c.sendMessage("You cannot store stacked items");
	        		}
	        	} else {
	        		c.sendMessage("You may be surprised to learn that bagception is not permitted");
	        	}
        	}
        }
		if ((itemUsed == 824 && useWith == 314) || (itemUsed == 314 && useWith == 824)) {
			int amt = 10;
			if (!c.getItems().playerHasItem(824, 10))
				amt = c.getItems().getItemAmount(824);
			if (c.getItems().getItemAmount(314) < amt)
				amt = c.getItems().getItemAmount(314);
			c.getItems().deleteItem2(314, amt);
			c.getItems().deleteItem2(824, amt);
			c.getItems().addItem(811, amt);
			c.sendMessage("You make some rune darts.");
		}
		
		if (itemUsed == 227 || useWith == 227) {
			Herblore.handlePotMaking(c, itemUsed, useWith);
			return;
		}
		if(itemUsed == 15372 || useWith == 4151) {
			if(c.getItems().playerHasItem(4151, 1) && c.getItems().playerHasItem(15372, 1)) {
				c.getItems().deleteItem(4151, 1);
				c.getItems().deleteItem(15372, 1);
				c.getItems().addItem(15052, 1);
			}
			return;
		}
		if(useWith == 10525) {
			c.usedItemOnBag = true;
			ResourceBag.add(c, itemUsed, 100);
			return;
		}
		if (itemUsed == 1759 || useWith == 1759) {
			JewelleryMaking.stringAmulet(c, itemUsed, useWith);
			return;
		}
		if (Firemaking.playerLogs(itemUsed, useWith)) {
			Firemaking.grabData(c, itemUsed, useWith);
			return;
		}
		if (itemUsed == Chest.toothHalf() && useWith == Chest.loopHalf() || itemUsed == Chest.loopHalf() && useWith == Chest.toothHalf()) {
			Chest.makeKey(c);
			return;
		}
		
		if (itemUsed == 11230 && useWith == 14614 || itemUsed == 14614 && useWith == 11230) {
			int dart = 11230;
			int amount = c.getItems().getItemAmount(dart);
			c.getItems().deleteItem(dart, amount);
			c.dartsLoaded += amount;
			c.sendMessage("You load <col=ff0000>" + amount + " </col> dragons dart(s) into your blowpipe.");
		}
		if (itemUsed == 811 && useWith == 14614 || itemUsed == 14614 && useWith == 811) {
			int dart = 811;
			int amount = c.getItems().getItemAmount(dart);
			c.getItems().deleteItem(dart, amount);
			c.dartsLoaded += amount;
			c.sendMessage("You load <col=ff0000>" + amount + " </col> Rune dart(s) into your blowpipe.");
		}
		
		/*if (itemUsed == 811 && useWith == 14614 || itemUsed == 14614 && useWith == 811 && c.dartsLoaded == 0) {
			int darts = 811;
			int amounts = c.getItems().getItemAmount(darts);
			c.getItems().deleteItem(darts, amounts);
			c.runedartsLoaded += amounts;
			c.sendMessage("You load <col=ff0000>" + amounts + " </col> rune dart(s) into your blowpipe.");
		}
		if (itemUsed == 811 && useWith == 14614 || itemUsed == 14614 && useWith == 811 && c.dartsLoaded > 0) {
			c.sendMessage("You already have darts in your Blowpipe!");
		}*/
		
		
		if ((itemUsed == 1540 && useWith == 11286) || (itemUsed == 11286 && useWith == 1540)) {
			if (c.playerLevel[Player.playerSmithing] >= 95) {
				c.getItems().deleteItem(1540, c.getItems().getItemSlot(1540), 1);
				c.getItems().deleteItem(11286, c.getItems().getItemSlot(11286), 1);
				c.getItems().addItem(11283, 1);
				c.sendMessage("You combine the two materials to create a dragonfire shield.");
				if (c.diffLevel != 100)
					c.getPA().addSkillXP(500 * (Constants.SMITHING_EXPERIENCE / c.diffLevel), Player.playerSmithing);
				else
					c.getPA().addSkillXP(500, Player.playerSmithing);
			} else {
				c.sendMessage("You need a smithing level of 95 to create a dragonfire shield.");
			}
		}
		//Use saw on a normal log to make a regular plank (basic construction) till more is added :)
		if ((itemUsed == 8794 && useWith == 1511)) {
			if (c.playerLevel[Player.playerConstruction] >=1) {
				c.getItems().deleteItem(1511, c.getItems().getItemSlot(1511), 1);
				c.getItems().addItem(960, 1);
				c.sendMessage("You carefully craft a plank out of the logs.");
				if (c.diffLevel != 1)
					c.getPA().addSkillXP(20 * (Constants.CONSTRUCTION_EXPERIENCE / c.diffLevel), Player.playerConstruction);
				else
					c.getPA().addSkillXP(20, Player.playerConstruction);
			} else { 
				c.sendMessage("You do not have the required items to make a plank.");
				
			}
			}
		
		if ((itemUsed == 8794 && useWith == 1521)) {
			if (c.playerLevel[Player.playerConstruction] >=15) {
			c.getItems().deleteItem(1521, c.getItems().getItemSlot(1521), 1);
			c.getItems().addItem(8778, 1);
			c.sendMessage("You carefully craft a plank out of the Oak logs.");
			if (c.diffLevel !=15)
				c.getPA().addSkillXP(45 * (Constants.CONSTRUCTION_EXPERIENCE / c.diffLevel), Player.playerConstruction);
			else
				c.getPA().addSkillXP(45, Player.playerConstruction);
		} else { 
			c.sendMessage("You do not have the required items to make a plank.");
			
		}
		}
		if ((itemUsed == 8794 && useWith == 6333)) {
			if (c.playerLevel[Player.playerConstruction] >=34) {
			c.getItems().deleteItem(6333, c.getItems().getItemSlot(6333), 1);
			c.getItems().addItem(8780, 1);
			c.sendMessage("You carefully craft a plank out of the Teak logs.");
			if (c.diffLevel !=34)
				c.getPA().addSkillXP(55 * (Constants.CONSTRUCTION_EXPERIENCE / c.diffLevel), Player.playerConstruction);
			else
				c.getPA().addSkillXP(55, Player.playerConstruction);
		} else { 
			c.sendMessage("You do not have the required items to make a plank.");
			
		}
		}
		if ((itemUsed == 8794 && useWith == 6332)) {
			if (c.playerLevel[Player.playerConstruction] >=55) {
			c.getItems().deleteItem(6332, c.getItems().getItemSlot(6332), 1);
			c.getItems().addItem(8782, 1);
			c.sendMessage("You carefully craft a plank out of the Mahogany logs.");
			if (c.diffLevel !=55)
				c.getPA().addSkillXP(125 * (Constants.CONSTRUCTION_EXPERIENCE / c.diffLevel), Player.playerConstruction);
			else
				c.getPA().addSkillXP(125, Player.playerConstruction);
		} else { 
			c.sendMessage("You do not have the required items to make a plank.");
			
		}
		}
			
			
			
		
		if ((itemUsed == 573 && useWith == 1391)) {
		if (c.playerLevel[Player.playerCrafting] >=50) {
			c.getItems().deleteItem(573, c.getItems().getItemSlot(573), 1);
			c.getItems().deleteItem(1391, c.getItems().getItemSlot(1391), 1);
			c.getItems().addItem(1397, 1);
			c.sendMessage("You combine the Air orb and the Battlestaff to creat a Air Battlestaff");
			if (c.diffLevel != 55)
				c.getPA().addSkillXP(1000 * (Constants.CRAFTING_EXPERIENCE / c.diffLevel), Player.playerCrafting);
			else
				c.getPA().addSkillXP(1000, Player.playerCrafting);
		} else { 
			c.sendMessage("You need a Crafting level of 50 to create a Air battlestaff.");
			
		}
		}

		if (itemUsed == 1929 && useWith == 1933 || itemUsed == 1933 && useWith == 1929) {
			c.getDH().sendDialogues(1354, -1);
		}

		/**
		 * Flour Making
		 */
		if (itemUsed == 1929 && useWith == 1933 || itemUsed == 1933 && useWith == 1929) {
			c.getDH().sendDialogues(1354, -1);
		}

		/** Lighters **/

		if (itemUsed == 7331 && useWith == 1511 || itemUsed == 1511 && useWith == 7331) {
			c.getItems().deleteItem(1511, 1);
			c.getItems().deleteItem(7331, c.getItems().getItemSlot(7331), 1);
			c.getItems().addItem(7406, 1);
		} else if (itemUsed == 7329 && useWith == 1511 || itemUsed == 1511 && useWith == 7329) {
			c.getItems().deleteItem(1511, 1);
			c.getItems().deleteItem(7329, c.getItems().getItemSlot(7329), 1);
			c.getItems().addItem(7404, 1);
		} else if (itemUsed == 7330 && useWith == 1511 || itemUsed == 1511 && useWith == 7330) {
			c.getItems().deleteItem(1511, 1);
			c.getItems().deleteItem(7330, c.getItems().getItemSlot(7330), 1);
			c.getItems().addItem(7405, 1);
		} else if (itemUsed == 10327 && useWith == 1511 || itemUsed == 1511 && useWith == 10327) {
			c.getItems().deleteItem(1511, 1);
			c.getItems().deleteItem(10327, c.getItems().getItemSlot(10327), 1);
			c.getItems().addItem(10328, 1);
		} else if (itemUsed == 10326 && useWith == 1511 || itemUsed == 1511 && useWith == 10326) {
			c.getItems().deleteItem(1511, 1);
			c.getItems().deleteItem(10326, c.getItems().getItemSlot(10326), 1);
			c.getItems().addItem(10329, 1);
		}
		/**
		 * Fletching
		 */
		if (itemUsed == 1733 || useWith == 1733) {
			LeatherMaking.craftLeatherDialogue(c, itemUsed, useWith);
		}
		if (itemUsed == 1759 || useWith == 1759) {
			JewelleryMaking.stringAmulet(c, itemUsed, useWith);
		}
		for (int ref : Fletching.refItems) {
			if (itemUsed == ref || useWith == ref) {
				if (c.playerIsWoodcutting) {
					c.playerIsWoodcutting = false;
					Woodcutting.resetWoodcutting(c);
					return;
				}
				FletchingHandler.appendType(c, itemUsed, useWith);

			}
		}
		for (final FletchingHandler.Bolts bolt : FletchingHandler.Bolts.values()) {
			if (itemUsed == bolt.getInput1() || useWith == bolt.getInput1()) {
				FletchingHandler.appendType(c, itemUsed, useWith);
				return;
			}
		}
		if (itemUsed == 1755 || useWith == 1755) {
			GemCutting.cutGem(c, itemUsed, useWith);
			return;
		}
		if (itemUsed == 8794 && useWith == 1515 || itemUsed == 1515 && useWith == 8794) {// christmas
			if (c.getItems().playerHasItem(1515, 10)) {
				c.getItems().deleteItem(1515, 10);
				c.getItems().addItem(6864, 1);
				c.sendMessage("Now all you need is a Marionette Handle and some Strings!");
			} else {
				c.sendMessage("You need 10 Yew Logs to make a Marionette Handle!");
			}
		}
		if (c.getItems().getItemName(itemUsed).contains("(") && c.getItems().getItemName(useWith).contains("(") && c.getItems().getUnnotedItem(itemUsed) == itemUsed && c.getItems().getUnnotedItem(useWith) == useWith)
			PotionMixing.mixPotion2(c, itemUsed, useWith);

		if (itemUsed == 2368 && useWith == 2366 || itemUsed == 2366 && useWith == 2368) {
			c.getItems().deleteItem(2368, c.getItems().getItemSlot(2368), 1);
			c.getItems().deleteItem(2366, c.getItems().getItemSlot(2366), 1);
			c.getItems().addItem(1187, 1);
		}

		if (c.getItems().isHilt(itemUsed) || c.getItems().isHilt(useWith)) {
			int hilt = c.getItems().isHilt(itemUsed) ? itemUsed : useWith;
			int blade = c.getItems().isHilt(itemUsed) ? useWith : itemUsed;
			if (blade == 11690) {
				c.getItems().makeGodsword(hilt);
			}
		}

		switch (itemUsed) {

		default:
			if (c.playerRights == 5)
				Misc.println("Player used Item id: " + itemUsed + " with Item id: " + useWith);
			break;
		}
	}

	public static void ItemonNpc(Player c, int itemId, int npcId, int slot) {
		switch (npcId) {
		case 538:
			PVPAssistant.tradeSingleArtefact(c, itemId);
			break;
		default:
			break;
		}
		switch (itemId) {

		default:
			if (c.playerRights == 5)
				Misc.println("Player used Item id: " + itemId + " with Npc id: " + npcId + " With Slot : " + slot);
			break;
		}

	}

}