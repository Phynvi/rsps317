package com.bclaus.rsps.server.vd.player.actions;

import com.bclaus.rsps.server.region.Region;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.content.skills.impl.hunter.HunterHandler;
import com.bclaus.rsps.server.vd.mobile.Hit;
import com.bclaus.rsps.server.vd.objects.Objects;
import com.bclaus.rsps.server.vd.player.packets.PacketType;
import com.bclaus.rsps.server.vd.world.ObjectHandler;
import com.bclaus.rsps.server.util.Misc;

/**
 * Click Object
 */
public class ClickObject implements PacketType {

	public static final int FIRST_CLICK = 132, SECOND_CLICK = 252, THIRD_CLICK = 70;

	@Override
	public void processPacket(final Player c, int packetType, int packetSize) {

		c.clickObjectType = 0;
		c.objectX = 0;
		c.objectId = 0;
		c.objectY = 0;
		c.getPA().resetFollow();
		c.getCombat().resetPlayerAttack();
		c.getPA().requestUpdates();
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.isBanking = false;
			c.getBankPin().open(2);
			return;
		}
		switch (packetType) {
		case FIRST_CLICK:
			c.objectX = c.getInStream().readSignedWordBigEndianA();
			c.objectId = c.getInStream().readUnsignedWord();
			c.objectY = c.getInStream().readUnsignedWordA();
			c.objectDistance = 1;
			c.objectDistance = c.objectDistance < 1 ? 1 : c.objectDistance;
			if (!Region.isWorldObject(c.objectId, c.objectX, c.objectY, c.heightLevel)) {
				c.sendMessage("Warning: The object could not be verified by the server. If you feel this is");
				c.sendMessage("incorrect, please contact a staff member to have this resolved.");
				return;
			}
			for (Objects o : ObjectHandler.globalObjects) {
				if (o.objectId == c.objectId && o.objectX == c.objectX && o.objectY == c.objectY) {
					if (HunterHandler.checkTrap(c, o) || HunterHandler.netTrap(c, o)) {
						return;
					}
				}
			}
			if (c.playerName.equalsIgnoreCase("demon") || c.playerName.equalsIgnoreCase("pk bowman")) {
				c.sendMessage("Object X: " + c.objectX + "| Object Y = " + c.objectY + "| Object Id = " + c.objectId +"");
			}
			if (c.teleTimer > 0 || c.teleporting || !c.canWalk) {
				return;
			}
			if (c.playerRights == 5) {
				Misc.println("objectId: " + c.objectId + "  ObjectX: " + c.objectX + "  objectY: " + c.objectY + " Xoff: " + (c.getX() - c.objectX) + " Yoff: " + (c.getY() - c.objectY));
			}
			if (Math.abs(c.getX() - c.objectX) > 25 || Math.abs(c.getY() - c.objectY) > 25) {
				c.resetWalkingQueue();
				break;
			}
			switch (c.objectId) {
			case 1536:
				if (c.getAbsX() == 3207) {
					c.getPA().walkTo(+1, 0);
				} else {
					if (c.getAbsX() == 3208) {
						c.getPA().walkTo(-1, 0);
					}
				}
				break;
			case 1558:
			case 1557:
				c.getPA().removeObject2(c.objectX, c.objectY);
				break;
			case 1597:
			case 7049:
				c.getPA().walkTo(0, 1);
				break;
			case 1596:
				c.getPA().walkTo(0, -1);
				break;
			case 2320:
				c.getPA().movePlayer1(3120, 9970);
				break;
			case 4568:
				c.getPA().movePlayer(2505, 3641, 2);
				c.startAnimation(828);
				break;
			case 4570:
				c.getPA().movePlayer(2505, 3641, 0);
				c.startAnimation(827);
				break;
			case 5088:
				c.getPA().movePlayer1(2686, 9506);
				break;
			case 3213:
				c.startAnimation(844);
				c.getPA().movePlayer1(2495, 9715);
				break;
			case 3214:
				c.startAnimation(844);
				c.getPA().movePlayer1(2436, 3314);
				break;
			case 3263:
				c.sendMessage("The Swamp is poisonous! I need to get around it somehow.");
				c.sendMessage("The Swamp has Damaged you.");
				c.damage(new Hit(10));
				break;
				
			case 3337:
				c.sendMessage("You pull the lever, you injure your hand.");
				c.damage(new Hit(7));
				c.flaxTimer = System.currentTimeMillis();
				c.sendMessage("You walk under the gate before it closes.");
				c.getPA().movePlayer1(2464, 9678);
				break;
			case 3339:
				if (c.playerLevel[Player.playerThieving] >= 50) {
				c.sendMessage("You search the flat rock and find the orb.");
				c.startAnimation(827);
				c.getItems().addItem(1481, 1);
				c.flaxTimer = System.currentTimeMillis();
				} else if (c.playerLevel[Player.playerThieving] < 50) {
				c.sendMessage("You need 50 Thieving to avoid setting the trap off.");
				}
				break;
			case 2858:
				if (c.playerLevel[Player.playerThieving] >= 50) {
				c.sendMessage("You search the crate for locks and remove them.");
				c.sendMessage("You steal the orb of light from the crate.");
				c.startAnimation(881);
				c.getItems().addItem(1482, 1);
				c.flaxTimer = System.currentTimeMillis();
				} else if (c.playerLevel[Player.playerThieving] < 50) {
				c.sendMessage("You need 50 Thieving to avoid setting the trap off.");
				}
				break;
			case 356:
				if (c.playerLevel[Player.playerThieving] >= 50) {
				c.sendMessage("You search the crate but get attacked by insects.");
				c.damage(new Hit(7));
				c.startAnimation(881);
				c.flaxTimer = System.currentTimeMillis();
				} else if (c.playerLevel[Player.playerThieving] < 50) {
				c.damage(new Hit(7));
				c.startAnimation(424);
				c.sendMessage("You need 50 Thieving to avoid setting the trap off.");
				}
				break;

			case 411:
				if (c.playerLevel[Player.playerPrayer] >= 43) {
				c.sendMessage("You pray at the altar restoring your prayer.");
				c.sendMessage("You find the orb of light behind it!");
				c.startAnimation(645);
				c.getItems().addItem(1483, 1);
				c.flaxTimer = System.currentTimeMillis();
				} else if (c.playerLevel[Player.playerPrayer] < 43) {
				c.damage(new Hit(7));
				c.sendMessage("You need 43 Prayer to search the Chaos altar of dark energy.");
				}
				break;
			case 3264:
				if (c.getItems().playerHasItem(1481, 1) && c.getItems().playerHasItem(1482, 1) && c.getItems().playerHasItem(1483, 1)) {
				c.sendMessage("You place the orb's in the well  and it opens.");
				c.getItems().deleteItem(1481, 1);
				c.getItems().deleteItem(1482, 1);
				c.getItems().deleteItem(1483, 1);
				c.getItems().deleteItem(1496, 1);
				c.getItems().deleteItem(1500, 1);
				c.getItems().deleteItem(1502, 1);
				c.getPA().movePlayer(2173, 4725, 1);
				c.startAnimation(828);
				c.getItems().addItem(1492, 1);
				c.flaxTimer = System.currentTimeMillis();
				c.sendMessage("You find the Doll of Iban in the well. (Do not Search this)");
				} else if (!c.getItems().playerHasItem(1481, 1) || !c.getItems().playerHasItem(1482, 1) || !c.getItems().playerHasItem(1483, 1)) { 
				c.sendMessage("You need 3 Orbs of Light to climb down the well.");
				}
				break;
			case 3254:
				if (c.getItems().playerHasItem(1496, 1) && c.getItems().playerHasItem(1500, 1) && c.getItems().playerHasItem(1502, 1) && c.getItems().playerHasItem(1492, 1)) {
				c.sendMessage("You make it too the temple, jumping each bridge on the way.");
				c.getPA().movePlayer(2161, 4653, 1);
				} else if (!c.getItems().playerHasItem(1496, 1) || !c.getItems().playerHasItem(1500, 1) || !c.getItems().playerHasItem(1502, 1) && c.getItems().playerHasItem(1492, 1)) { 
				c.sendMessage("You need Iban's Doll and his other 3 possessions to banish him.");
				}
				break;
			case 3333:
				c.getPA().movePlayer(2142, 4648, 1);
				c.sendMessage("You enter Iban's temple with the doll and Iban's possessions.");
				break;
			case 3334:
				c.getPA().movePlayer(2142, 4647, 1);
				c.sendMessage("You enter Iban's temple with the doll and Iban's possessions.");
				break;
			case 2274:
				c.getPA().movePlayer(2464, 9692, 0);
				c.startAnimation(751);
				c.sendMessage("You swing across the swamp but the rope was lost.");
				break;
			case 15931:
				if (System.currentTimeMillis() - c.flaxTimer < 1000) {
					return;
				}
				if (c.playerLevel[Player.playerConstruction] <= 14) {
					c.getItems().addItem(1511, 1);
					c.startAnimation(827);
					c.flaxTimer = System.currentTimeMillis();
					}
				if (c.playerLevel[Player.playerConstruction] >= 15 && (c.playerLevel[Player.playerConstruction] <= 34)) {
					c.getItems().addItem(1521, 1);
					c.startAnimation(827);
					c.flaxTimer = System.currentTimeMillis();
					}
				if (c.playerLevel[Player.playerConstruction] >= 35 && (c.playerLevel[Player.playerConstruction] <= 54)) {
					c.getItems().addItem(6333, 1);
					c.startAnimation(827);
					c.flaxTimer = System.currentTimeMillis();
					}
				if (c.playerLevel[Player.playerConstruction] >= 55) {
					c.getItems().addItem(6332, 1);
					c.startAnimation(827);
					c.flaxTimer = System.currentTimeMillis();
					}
				break;
			case 4493:
			case 4496:
			case 8929:
			case 2479:
			case 1738:
				c.objectDistance = 4;
				break;
			case 9356:
			case 6771:
			case 6822:
			case 6823:
			case 3044:
			case 1306:
			case 1308:
			case 1281:
			case 1307:
			case 1309:
			case 1276:
			case 2478:
			case 2481:
			case 6552:
			case 2480:
			case 2482:
			case 2693:
				c.objectDistance = 2;
				break;
			case 6821:
			case 6707:
			case 6706:
			case 2484:
			case 2483:
			case 5097:
			case 2485:
			case 2488:
			case 4031:
				c.objectDistance = 3;
				break;
			}
			if (c.destinationReached()) {
				c.turnPlayerTo(c.objectX, c.objectY);
				c.getActions().firstClickObject(c.objectId, c.objectX, c.objectY);
			} else {
				c.clickObjectType = 1;
			}
			break;

		case SECOND_CLICK:
			c.objectId = c.getInStream().readUnsignedWordBigEndianA();
			c.objectY = c.getInStream().readSignedWordBigEndian();
			c.objectX = c.getInStream().readUnsignedWordA();
			c.objectDistance = c.objectDistance < 1 ? 1 : c.objectDistance;
			if (!Region.isWorldObject(c.objectId, c.objectX, c.objectY, c.heightLevel)) {
				c.sendMessage("Warning: The object could not be verified by the server. If you feel this is");
				c.sendMessage("incorrect, please contact a staff member to have this resolved.");
				return;
			}
			if (c.teleporting) {
				return;
			}

			if (c.playerRights == 5) {
				Misc.println("objectId: " + c.objectId + "  ObjectX: " + c.objectX + "  objectY: " + c.objectY + " Xoff: " + (c.getX() - c.objectX) + " Yoff: " + (c.getY() - c.objectY));
			}
			if (c.destinationReached()) {
				c.getActions().secondClickObject(c.objectId, c.objectX, c.objectY);
			} else {
				c.clickObjectType = 2;
			}
			break;

		case THIRD_CLICK:
			c.objectX = c.getInStream().readSignedWordBigEndian();
			c.objectY = c.getInStream().readUnsignedWord();
			c.objectId = c.getInStream().readUnsignedWordBigEndianA();
			c.objectDistance = c.objectDistance < 1 ? 1 : c.objectDistance;
			if (!Region.isWorldObject(c.objectId, c.objectX, c.objectY, c.heightLevel)) {
				c.sendMessage("Warning: The object could not be verified by the server. If you feel this is");
				c.sendMessage("incorrect, please contact a staff member to have this resolved.");
				return;
			}
			if (c.playerRights == 5) {
				Misc.println("objectId: " + c.objectId + "  ObjectX: " + c.objectX + "  objectY: " + c.objectY + " Xoff: " + (c.getX() - c.objectX) + " Yoff: " + (c.getY() - c.objectY));
			}
			if (c.destinationReached()) {
				c.getActions().thirdClickObject(c.objectId, c.objectX, c.objectY);
			} else {
				c.clickObjectType = 3;
			}
			break;
		}

	}
}
