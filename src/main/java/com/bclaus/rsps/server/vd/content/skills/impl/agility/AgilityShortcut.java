package com.bclaus.rsps.server.vd.content.skills.impl.agility;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.vd.player.DialogueHandler;
import com.bclaus.rsps.server.vd.player.Player;

/**
 * @author Tim http://rune-server.org/members/Someone
 *
 */
public class AgilityShortcut {
	private static final int PIPES_EMOTE = 844;
	private static final int WALK = 1, MOVE = 2, AGILITY = 3;

	public static void agilityWalk(final Player c, final int walkAnimation, final int x, final int y) {
		c.isRunning2 = false;
		c.getPA().sendFrame36(173, 0);
		c.playerWalkIndex = walkAnimation;
		c.getPA().requestUpdates();
		c.getPA().walkTo(x, y);
	}
	public static void wildernessEntrance(Player c, String Object, int level, int x, int y, int a, int b, int xp) {
		if (c.playerLevel[Player.playerAgility] < level) {
			c.sendMessage("You need a Agility level of " + level + " to pass this " + Object + ".");
			return;
		}
		c.getPA().walkTo3(x, y);
		c.getPA().addSkillXP(xp, Player.playerAgility);
		c.getPA().refreshSkill(Player.playerAgility);
	}
	public static void doWildernessEntrance(final Player c) {
		if (System.currentTimeMillis() - c.foodDelay < 2000) {
			return;
		}
		c.stopMovement();
		c.freezeTimer = 16;
		c.playerWalkIndex = 762;
		c.updateRequired = true;
		c.appearanceUpdateRequired = true;
		if (c.getX() == 2998 && c.getY() == 3931) {
			wildernessEntrance(c, "Door", 1, 0, -15, 2998, 3917, 40 * Constants.AGILITY_EXPERIENCE);
		} else {
			wildernessEntrance(c, "Door", 1, 0, +15, 2998, 3917, 40 * Constants.AGILITY_EXPERIENCE);
		}

		c.foodDelay = System.currentTimeMillis();
		Server.getTaskScheduler().schedule(new ScheduledTask(14) {
			@Override
			public void execute() {
				c.playerStandIndex = 0x328;
				c.playerTurnIndex = 0x337;
				c.playerWalkIndex = 0x333;
				c.playerTurn180Index = 0x334;
				c.playerTurn90CWIndex = 0x335;
				c.playerTurn90CCWIndex = 0x336;
				c.playerRunIndex = 0x338;
				c.updateRequired = true;
				c.appearanceUpdateRequired = true;
				this.stop();
			}
		});
	}

	private static void handleAgility(Player player, int x, int y, int levelReq, int anim, int walk, String message) {
		if (player.playerLevel[Player.playerAgility] < levelReq) {
			player.sendMessage("You need " + levelReq + " agility to use this shortcut.");
			return;
		}

		switch (walk) {
		case 1:
			player.getPA().walkTo(x, y);
			break;
		case 2:
			player.getPA().movePlayer(x, y, player.heightLevel);
			break;
		case 3:
			agilityWalk(player, x, y, anim);
			break;
		}
		if (anim != 0 && anim != -1) {
			player.startAnimation(anim);
		}
		player.sendMessage(message);
	}

	public static void processAgilityShortcut(Player client) {
		switch (client.objectId) {
		case 18411:
			if (client.absY == 3491) {
				handleAgility(client, 2, 0, 1, 3067, WALK, "You jump over the broken fence.");
			} else if (client.absY == 3493) {
				handleAgility(client, -2, 0, 1, 3067, WALK, "You jump over the broken fence.");
			}
			break;
		case 993:
			if (client.absY == 3435) {
				handleAgility(client, 2761, 3438, 1, 3067, MOVE, "You jump over the stile.");
			} else if (client.absY == 3438) {
				handleAgility(client, 2761, 3435, 1, 3067, MOVE, "You jump over the stile.");
			}
			break;
		case 8739:
			if (client.absY == 3299) {
			handleAgility(client, -2, 0, 1, 3067, WALK, "You jump over the Rock-slide.");
			} else if (client.absY == 3300) {
			handleAgility(client, -2, 0, 1, 3067, WALK, "You jump over the Rock-slide.");
			}
			break;
		case 3309:
			if (client.absX == 2479 && client.absY == 9721) {
			handleAgility(client, -2, 0, 1, 839, WALK, "You jump over the Rock-slide.");
			} else if (client.absX == 2477 && client.absY == 9721) {
			handleAgility(client, 2, 0, 1, 839, WALK, "You jump over the Rock-slide.");
			} else if (client.absX == 2466 && client.absY == 9723) {
			handleAgility(client, 2, 0, 1, 839, WALK, "You jump over the Rock-slide.");
			} else if (client.absX == 2468 && client.absY == 9723) {
			handleAgility(client, -2, 0, 1, 839, WALK, "You jump over the Rock-slide.");
			} else if (client.absX == 2481 && client.absY == 9679) {
			handleAgility(client, 2, 0, 1, 839, WALK, "You jump over the Rock-slide.");
			} else if (client.absX == 2483 && client.absY == 9679) {
			handleAgility(client, -2, 0, 1, 839, WALK, "You jump over the Rock-slide.");
			} else if (client.absX == 2470 && client.absY == 9706) {
			handleAgility(client, 2, 0, 1, 839, WALK, "You jump over the Rock-slide.");
			} else if (client.absX == 2472 && client.absY == 9706) {
			handleAgility(client, -2, 0, 1, 839, WALK, "You jump over the Rock-slide.");
			} else if (client.absY == 9721 && client.absX == 2460) {
			handleAgility(client, 0, -2, 1, 839, WALK, "You jump over the Rock-slide.");
			} else if (client.absY == 9719 && client.absX== 2460) {
			handleAgility(client, 0, 2, 1, 839, WALK, "You jump over the Rock-slide.");
			} else if (client.absY == 9692 && client.absX == 2491) {
			handleAgility(client, 0, -2, 1, 839, WALK, "You jump over the Rock-slide.");
			} else if (client.absY == 9690 && client.absX== 2491) {
			handleAgility(client, 0, 2, 1, 839, WALK, "You jump over the Rock-slide.");
			} else if (client.absY == 9714 && client.absX == 2480) {
			handleAgility(client, 0, -2, 1, 839, WALK, "You jump over the Rock-slide.");
			} else if (client.absY == 9712 && client.absX== 2480) {
			handleAgility(client, 0, 2, 1, 839, WALK, "You jump over the Rock-slide.");
			} else if (client.absY == 9713 && client.absX == 2458) {
			handleAgility(client, 0, -2, 1, 839, WALK, "You jump over the Rock-slide.");
			} else if (client.absY == 9711 && client.absX== 2458) {
			handleAgility(client, 0, 2, 1, 839, WALK, "You jump over the Rock-slide.");
			}
			break;
		case 8738:
			handleAgility(client, 2, 0, 1, 3067, WALK, "You jump over the strange floor.");
			break;
		case 51:
			handleAgility(client, 1, 0, 66, 2240, WALK, "You squeeze through the railings");
			break;
		case 9326:
			if (client.absX == 2773) {
				handleAgility(client, 2, 0, 81, 3067, WALK, "You jump over the strange floor.");
			} else if (client.absX == 2775) {
				handleAgility(client, -2, 0, 81, 3067, WALK, "You jump over the strange floor.");
			} else if (client.absX == 2770) {
				handleAgility(client, -2, 0, 81, 3067, WALK, "You jump over the strange floor.");
			}
			break;
		case 9321:
			if (client.absX == 2735) {
				handleAgility(client, -5, 0, 62, 2240, WALK, "You squeeze through the crevice.");
			} else if (client.absX == 2730) {
				handleAgility(client, 5, 0, 62, 2240, WALK, "You squeeze through the crevice.");
			}
			break;
		case 12127:
			if (client.absY == 4403) {
				handleAgility(client, 0, -2, 66, 2240, WALK, "You squeeze past the jutted wall.");
			} else if (client.absY == 4401) {
				handleAgility(client, 0, 2, 66, 2240, WALK, "You squeeze past the jutted wall.");
			} else if (client.absY == 4404) {
				handleAgility(client, 0, -2, 46, 2240, WALK, "You squeeze past the jutted wall.");
			} else if (client.absY == 4402) {
				handleAgility(client, 0, 2, 46, 2240, WALK, "You squeeze past the jutted wall.");
			}
			break;
		case 3933:
			if (client.absY == 3232) {
				handleAgility(client, 0, 7, 85, 762, WALK, "You pass through the agility shortcut.");
			} else if (client.absY == 3239) {
				handleAgility(client, 0, -7, 85, 762, WALK, "You pass through the agility shortcut.");
			}
			break;
		case 4615:
		case 4616:
			if (client.absX == 2595) {
				handleAgility(client, 2599, client.absY, 1, 3067, MOVE, "You pass through the agility shortcut.");
			} else if (client.absX == 2599) {
				handleAgility(client, 2595, client.absY, 1, 3067, MOVE, "You pass through the agility shortcut.");
			}
			break;
		case 11844:
			if (client.absX == 2936) {
				handleAgility(client, -2, 0, 5, -1, WALK, "You pass through the agility shortcut.");
			} else if (client.absX == 2934) {
				handleAgility(client, 2, 0, 5, -1, WALK, "You pass through the agility shortcut.");
			}
			break;
		case 5090:
			if (client.absX == 2687) {// 2682, 9506
				handleAgility(client, -5, 0, 5, 762, WALK, "You walk across the log balance.");
			}
			break;
		case 5088:
			if (client.absX == 2682) {// 2867, 9506
				handleAgility(client, 5, 0, 5, 762, WALK, "You walk across the log balance.");
			}
			break;
		case 14922:
			if (client.objectX == 2344 && client.objectY == 3651) {
				handleAgility(client, 2344, 3655, 1, 762, MOVE, "You crawl through the hole.");
			} else if (client.objectX == 2344 && client.objectY == 3654) {
				handleAgility(client, 2344, 3650, 1, 762, MOVE, "You crawl through the hole.");
			}
			break;
		case 9330:
			if (client.objectX == 2601 && client.objectY == 3336) {
				handleAgility(client, -4, 0, 33, getAnimation(PIPES_EMOTE), AGILITY, "You pass through the agility shortcut.");
			}
		case 5100:
			if (client.absY == 9566) {
				handleAgility(client, 2655, 9573, 17, 762, MOVE, "You pass through the agility shortcut.");
			} else if (client.absY == 9573) {
				handleAgility(client, 2655, 9573, 17, 762, MOVE, "You pass through the agility shortcut.");
			}
			break;
		case 9328:
			if (client.objectX == 2599 && client.objectY == 3336) {
				handleAgility(client, 4, 0, 33, getAnimation(PIPES_EMOTE), AGILITY, "You pass through the agility shortcut.");
			}
			break;

		case 9293:
			if (client.absX < client.objectX) {
				handleAgility(client, 2892, 9799, 70, getAnimation(PIPES_EMOTE), MOVE, "You pass through the agility shortcut.");
			} else {
				handleAgility(client, 2886, 9799, 70, getAnimation(PIPES_EMOTE), MOVE, "You pass through the agility shortcut.");
			}
			break;

		case 9294:
			if (client.absX == 2880) {
				handleAgility(client, -2, 0, 81, 3067, WALK, "You jump over the strange floor.");
				;
			} else {
				handleAgility(client, -2, 0, 81, 3067, WALK, "You jump over the strange floor.");
			}
			break;

		case 9302:
			if (client.absY == 3112) {
				handleAgility(client, 2575, 3107, 16, 844, MOVE, "You pass through the agility shortcut.");
			}
			break;

		case 9301:
			if (client.absY == 3107) {
				handleAgility(client, 2575, 3112, 16, 844, MOVE, "You pass through the agility shortcut.");
			}
			break;
		case 9309:
			if (client.absY == 3309) {
				handleAgility(client, 2948, 3313, 26, 844, MOVE, "You pass through the agility shortcut.");
			}
			break;
		case 9310:
			if (client.absY == 3313) {
				handleAgility(client, 2948, 3309, 26, 844, MOVE, "You pass through the agility shortcut.");
			}
			break;
		case 2322:
			if (client.absX == 2709) {
				handleAgility(client, 2704, 3209, 10, 3067, MOVE, "You pass through the agility shortcut.");
			}
			break;
		case 2323:
			if (client.absX == 2705) {
				handleAgility(client, 2709, 3205, 10, 3067, MOVE, "You pass through the agility shortcut.");
			}
			break;
		case 2332:
			if (client.absX == 2906) {
				handleAgility(client, 4, 0, 1, 762, WALK, "You pass through the agility shortcut.");
			} else if (client.absX == 2910) {
				handleAgility(client, -4, 0, 1, 762, WALK, "You pass through the agility shortcut.");
			}
			break;
		case 3067:
			if (client.absX == 2639) {
				handleAgility(client, -1, 0, 1, 3067, WALK, "You pass through the agility shortcut.");
			} else if (client.absX == 2638) {
				handleAgility(client, -1, 0, 1, 3067, WALK, "You pass through the agility shortcut.");
			}
			break;
		case 2618:
			if (client.absY == 3492) {
				handleAgility(client, 0, +2, 1, 3067, WALK, "You jump over the broken fence.");
			} else if (client.absY == 3494) {
				handleAgility(client, -0, -2, 1, 3067, WALK, "You jump over the broken fence.");
			}
			break;
		case 5110:
			brimhavenSkippingStone(client);
			break;
		case 5111:
			brimhavenSkippingStone(client);
			break;
		case 2296:
			if (client.absX == 2603) {
				handleAgility(client, -5, 0, 1, -1, WALK, "You pass through the agility shortcut.");
			} else if (client.absX == 2598) {
				handleAgility(client, 5, 0, 1, -1, WALK, "You pass through the agility shortcut.");
			}
			break;
		}
	}

	public static int getAnimation(int objectId) {
		switch (objectId) {
		case 154:
		case 4084:
		case 9330:
		case 9228:
		case 5100:
			return PIPES_EMOTE;
		}
		return -1;
	}

	public static void brimhavenSkippingStone(final Player c) {
		if (c.stopPlayerPacket) {
			return;
		}
		if (c.playerLevel[Player.playerAgility] < 12) {
			DialogueHandler.sendStatement(c, "You need 12 agility to use these stepping stones");
			c.nextChat = 0;
			return;
		}
		c.stopPlayerPacket = true;
		Server.getTaskScheduler().schedule(new ScheduledTask(1) {
			@Override
			public void execute() {
				c.startAnimation(769);
				if (c.absX <= 2997) {
					stop();
				}
			}
		});
		Server.getTaskScheduler().schedule(new ScheduledTask(3) {

			@Override
			public void execute() {
				if (c.absX >= 2648) {
					c.teleportToX = c.absX - 2;
					c.teleportToY = c.absY - 5;
					if (c.absX <= 2997) {
						stop();
					}
				} else if (c.absX <= 2648) {
					c.teleportToX = c.absX + 2;
					c.teleportToY = c.absY + 5;
					if (c.absX >= 2645) {
						stop();
					}
				}

			}

			@Override
			public void onStop() {
				c.getPA().addSkillXP(300, Player.playerAgility);
				setAnimationBack(c);
				c.stopPlayerPacket = false;
			}
		});
	}

	private static void setAnimationBack(Player c) {
		c.isRunning2 = true;
		c.getPA().sendFrame36(173, 1);
		c.playerWalkIndex = 0x333;
		c.getPA().requestUpdates();
	}
}
