package com.bclaus.rsps.server.vd.content.skills.impl.hunter;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.content.achievements.AchievementType;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;
import org.omicron.jagex.runescape.CollisionMap;

import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.content.achievements.Achievements;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.objects.Objects;
import com.bclaus.rsps.server.vd.world.ObjectHandler;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;

public class HunterHandler {
	public static final int NPCID = 0, OBJECTID = 1, CATCHID = 2, ANIM = 3, ITEMID = 4, LAYANIM = 5, XP = 6, LOOT = 7, REQ = 8, BAIT = 9, ORIG = 10, TREE = 11;
	public static int[][] trapData = { { 5117, 19651, 19675, 5262, -1, -1, 700, 10149, 21, 1939, 19652, 19662 }, { 5087, 19206, 19216, 5272, -1, -1, 500, 9953, 20, 1739, 19205 }, { 5073, 19175, 19180, 6779, 10006, 5208, 800, 10088, 1 }, { 5072, 19175, 19178, 6779, 10006, 5208, 790, 10087, 1 }, { 5074, 19175, 19182, 6779, 10006, 5208, 900, 10089, 1 }, { 5079, 19187, 19192, 5184, 10008, 5208, 10000, 10033, 53, 1982 }, { 5080, 19187, 19192, 5184, 10008, 5208, 15000, 10034, 64, 1982 } };
	public static final int JAR = 1;
	public static final int LEVEL_REQ = 2;
	public static int[][] implingData = { { 1028, 11238, 1, 100 }, { 5083, 10016, 35, 150 }, { 6064, 11256, 83, 390 }, { 5085, 10020, 15, 325 }, { 5082, 10014, 45, 600 }, { 5084, 10018, 25, 450 }, { 6053, 11254, 74, 1000 }, { 6062, 11252, 65, 700 } };
	public static int[][] lootData = { { 995, 1000, 995, 10000, 995, 500 }, { 10016, 1 }, { 11212, 10, 11212, 20, 9341, 10, 9341, 25, 5699, 3, 1305, 1, 537, 25, 534, 40 }, { 3385, 1, 3391, 1, 1113, 1, 4097, 1, 4095, 1, 1333, 1, 1347, 1, 1747, 1, 2363, 1 }, // Ninja
																																																																	// implings
			{ 1681, 1, 2568, 1, 3391, 1, 4097, 1, 4095, 1, 1185, 1, 5541, 1, 2364, 2, 1747, 1, 1603, 1 } }; // Magpie
																											// Implings

	public static int getData(int[][] from, int data, int inputData, int returningData) {
		for (int[] aFrom : from) {
			if (aFrom[inputData] == data) {
				return aFrom[returningData];
			}
		}
		return -1;
	}

	public static int getSize(int[][] from, int data, int inputData) {
		for (int[] aFrom : from) {
			if (aFrom[inputData] == data) {
				return aFrom.length;
			}
		}
		return -1;
	}

	public static int[] getLoot(int whose) {
		int loot = (int) (java.lang.Math.random() * (lootData[whose].length - 1));
		if (loot % 2 == 0) {
			return new int[] { lootData[whose][loot], lootData[whose][loot + 1] };
		} else {
			return new int[] { lootData[whose][loot + 1], lootData[whose][loot + 2] };
		}
	}

	public static int getLootId(int data, int inputData) {
		for (int x = 0; x < implingData.length; x++) {
			if (implingData[x][inputData] == data) {
				return x;
			}
		}
		return -1;
	}

	public static boolean bait(Player c, int id, Objects o) {
		if (c == null) {
			return false;
		}
		if (c.getItems().playerHasItem(id)) {
			if (id == getData(trapData, o.objectId, OBJECTID, BAIT) && getData(trapData, o.objectId, OBJECTID, OBJECTID) > 0) {
				c.getItems().deleteItem(id, 1);
				o.bait = true;
				c.turnPlayerTo(o.objectX, o.objectY);
				c.startAnimation(5208);
				c.sendMessage("You succesfully bait the trap.");
				return true;
			} else if (getData(trapData, o.objectId, OBJECTID, BAIT) > 0 && id != getData(trapData, o.objectId, OBJECTID, BAIT)) {
				c.sendMessage("You're trying to use wrong bait on this trap. Right bait would be " + c.getItems().getItemName(getData(trapData, o.objectId, OBJECTID, BAIT)) + ".");
			}
		}
		return false;
	}

	public static boolean netTrap(final Player c, final Objects o) {
		if (o == null) {
			return false;
		}
		if (c == null)
			return false;
		if (o.objectId == 19652) {
			if (c.getItems().playerHasItem(954) && c.getItems().playerHasItem(303)) {
				c.turnPlayerTo(o.objectX, o.objectY);
				c.startAnimation(5208);
				final Objects setObject = new Objects(getData(trapData, o.objectId, ORIG, TREE), o.objectX, o.objectY, 0, 0, 10, 0);
				final Objects net = new Objects(getData(trapData, o.objectId, ORIG, OBJECTID), o.objectX, o.objectY + 1, 0, 0, 10, 0);
				setObject.owner = c.getIndex();
				Server.objectHandler.addObject(net);
				Server.objectHandler.placeObject(net);
				net.owner = c.getIndex();
				Server.objectHandler.addObject(setObject);
				Server.objectHandler.placeObject(setObject);
				Server.getTaskScheduler().schedule(new ScheduledTask(20 * 1000) {

					@Override
					public void execute() {
						try {
							if (net.owner == -1) {
								return;
							}
							Server.objectHandler.addObject(o);
							Server.objectHandler.placeObject(o);
							Server.objectHandler.removeObject(net);
							Server.objectHandler.removeObject(setObject);
							setObject.owner = -1;
							setObject.objectX = 0;
							setObject.objectY = 0;
							net.owner = -1;
							net.objectX = 0;
							net.objectY = 0;
							c.sendMessage("Your trap has dissapeared");
						} finally {
							super.stop();
						}
					}
				}.attach(c));
				Server.objectHandler.removeObject(o);
				return true;
			} else {
				c.sendMessage("You need to have small fishing net and rope in order to set-up net trap.");
			}
		} else if (o.objectId == 19205) {
			if (c.getItems().playerHasItem(1511) && c.getItems().playerHasItem(946)) {
				c.turnPlayerTo(o.objectX, o.objectY);
				c.getItems().deleteItem(1511, 1);
				c.startAnimation(5208);
				final Objects setObject = new Objects(19206, o.objectX, o.objectY, 0, 0, 10, 0);
				setObject.owner = c.getIndex();
				Server.objectHandler.addObject(setObject);
				Server.objectHandler.placeObject(setObject);
				Server.getTaskScheduler().schedule(new ScheduledTask(20 * 1000) {

					@Override
					public void execute() {
						try {
							if (setObject.owner == -1) {
								return;
							}
							Server.objectHandler.addObject(o);
							Server.objectHandler.placeObject(o);
							Server.objectHandler.removeObject(setObject);
							final Objects setObject = new Objects(19205, o.objectX, o.objectY, 0, 0, 10, 0);
							setObject.owner = -1;
							setObject.objectX = 0;
							setObject.objectY = 0;
							c.sendMessage("2Your trap has dissapeared");
						} finally {
							super.stop();
						}
					}
				}.attach(c));
				Server.objectHandler.removeObject(o);
				return true;
			} else {
				c.sendMessage("You need knife and logs to setup this trap.");
			}
		}
		return false;
	}

	public static boolean tryCatch(Player c, int id) { // DEATHEMOTES
		if (c == null)
			return false;
		if (NPCHandler.NPCS.get(id).isDead)
			return true;
		int npcId = id;
		id = NPCHandler.NPCS.get(id).npcType;
		if (getData(implingData, id, NPCID, NPCID) < 0) {
			return false;
		}
		if (c.playerEquipment[Player.playerWeapon] != 10010) {
			c.sendMessage("You need a butterfly net to catch this.");
			return true;
		}
		if (c.playerLevel[22] < getData(implingData, id, NPCID, LEVEL_REQ)) {
			c.sendMessage("You need hunter level of " + getData(implingData, id, NPCID, LEVEL_REQ) + " to catch this.");
			return true;
		}
		if (Misc.random(10) >= ((c.playerLevel[22] - 10) / 10) + 1) {
			c.sendMessage("You fail catching it.");
			return true;
		}
		c.turnPlayerTo(NPCHandler.NPCS.get(npcId).getAbsX(), NPCHandler.NPCS.get(npcId).getAbsY());
		c.startAnimation(5209);
		NPCHandler.NPCS.get(npcId).isDead = true;
		NPCHandler.NPCS.get(npcId).noDeathEmote = true;
		NPCHandler.NPCS.get(npcId).updateRequired = true;
		c.getItems().addItem(getData(implingData, id, NPCID, JAR), 1);
		return true;
	}

	public static boolean lootImp(Player c, int id) {
		if (getData(implingData, id, JAR, JAR) < 0) {
			return false;
		}
		if (c.getItems().playerHasItem(id, 1)) {
			c.getItems().deleteItem(id, 1);
			int[] loot = getLoot(getLootId(id, JAR));
			c.getItems().addItem(loot[0], loot[1]);
			// c.sendMessage(loot[0]+":"+loot[1]);
			c.sendMessage("You loot impling.");
			return true;
		}
		return false;
	}

	public static void checkTrap(final NPC n) { // add only specified NPCs only
		if (n == null)
			return;
		if (getData(trapData, n.npcType, 0, 1) > -1) {
			final Objects o = checkClosestObjects(n, getData(trapData, n.npcType, 0, 1), n.getIndex());
			if ((o != null && !n.isDead && distSquare(n.getAbsX(), n.getAbsY(), o.objectX, o.objectY) > 0) && (((int) (Math.random() * (getData(trapData, n.npcType, NPCID, REQ) * (o.bait ? 10 : 20) + 50)) < (World.PLAYERS.get(o.owner) == null ? 0 : World.PLAYERS.get(o.owner).playerLevel[22]) && o.delay < System.currentTimeMillis() && n.delay < System.currentTimeMillis()) || n.getIndex() == o.target)) {
				if (World.PLAYERS.get(o.owner) != null && World.PLAYERS.get(o.owner).playerLevel[22] < getData(trapData, n.npcType, NPCID, REQ)) {
					return;
				}
				if (o.target != n.getIndex())
					o.target = n.getIndex();
				n.moveX = NPCHandler.GetMove(n.getAbsX(), o.getObjectX());
				n.moveY = NPCHandler.GetMove(n.getAbsY(), o.getObjectY());
				n.getNextNPCMovement(8);
				o.delay = System.currentTimeMillis() + 2000;
				n.delay = System.currentTimeMillis() + 2000;
			} else if (o != null && distSquare(n.getAbsX(), n.getAbsY(), o.objectX, o.objectY) <= 0 && o.oDelay < System.currentTimeMillis() && !n.isDead) {
				if (World.PLAYERS.get(o.owner) != null && World.PLAYERS.get(o.owner).playerLevel[22] < getData(trapData, n.npcType, NPCID, REQ)) {
					return;
				}
				n.noDeathEmote = true;
				n.forceAnim(getData(trapData, n.npcType, NPCID, ANIM));
				o.oDelay = System.currentTimeMillis() + 6000;
				final Objects catchObject = new Objects(getData(trapData, n.npcType, NPCID, CATCHID), o.objectX, o.objectY, 0, 0, 10, 0);
				catchObject.owner = o.owner;
				if (getSize(trapData, o.objectId, OBJECTID) < 12) {
					Server.objectHandler.addObject(catchObject);
					Server.objectHandler.placeObject(catchObject);
					Server.objectHandler.removeObject(o);
					o.owner = -1;
				} else {
					catchObject.objectY -= 1;
					Objects empty = new Objects(100, o.objectX, o.objectY, 0, 0, 10, 0);
					Server.objectHandler.addObject(empty);
					Server.objectHandler.placeObject(empty);
					Server.objectHandler.removeObject(empty);
					empty.owner = -1;
					o.owner = -1;
				}
				Server.objectHandler.addObject(catchObject);
				Server.objectHandler.placeObject(catchObject);
				Server.objectHandler.removeObject(o);
				o.owner = -1;
				o.objectX = 0;
				o.objectY = 0;
				catchObject.item = getData(trapData, n.npcType, NPCID, LOOT);
				catchObject.xp = getData(trapData, n.npcType, NPCID, XP);
				Server.getTaskScheduler().schedule(new ScheduledTask(60) {

					@Override
					public void execute() {
						try {
							if (catchObject.owner == -1) {
								return;
							}
							Server.itemHandler.createGroundItem((World.PLAYERS.get(catchObject.owner)), getData(trapData, n.npcType, NPCID, ITEMID), catchObject.objectX, 1, catchObject.objectY, 1);
							Server.objectHandler.removeObject(catchObject);
							Objects empty = new Objects(100, catchObject.objectX, catchObject.objectY, 0, 0, 10, 0);
							Server.objectHandler.placeObject(empty);
							World.PLAYERS.get(catchObject.owner).sendMessage("Your trap has dismantled.");
							catchObject.owner = -1;
							catchObject.objectX = 0;
							catchObject.objectY = 0;
						} finally {
							super.stop();
						}
					}
				}.attach(catchObject.owner));
				n.animUpdateRequired = true;
				n.isDead = true;
				n.updateRequired = true;
				n.delay = System.currentTimeMillis() + 2000;
			}
		}

	}

	public static boolean layTrap(final Player c, final int id) {
		if (c == null)
			return false;
		if (getData(trapData, id, 4, 1) <= -1) {
			return false;
		}
		if (!canTrap(c)) {
			return false;
		}
		if (c.inProcess) {
			c.sendMessage("You're already setting up a trap!");
			return false;
		}
		c.startAnimation(5208);
		c.getItems().deleteItem(id, 1);
		c.inProcess = true;
		final Objects o = new Objects(getData(trapData, id, 4, 1), c.getAbsX(), c.getAbsY(), 0, 0, 10, 0);
		Server.getTaskScheduler().schedule(new ScheduledTask(4) {
			@Override
			public void execute() {
				if (c.disconnected) {
					this.stop();
				}
				o.owner = c.getIndex();
				Server.objectHandler.addObject(o);
				Server.objectHandler.placeObject(o);
				c.inProcess = false;
				Achievements.increase(c, AchievementType.HUNTER, 1);
				super.stop();
			}

		});
		if (!CollisionMap.isEastBlocked(c.heightLevel, c.absX - 1, c.absY)) {
			c.getPA().walkTo(-1, 0);
		} else if (!CollisionMap.isWestBlocked(c.heightLevel, c.absX + 1, c.absY)) {
			c.getPA().walkTo(1, 0);
		}
		Server.getTaskScheduler().schedule(new ScheduledTask(55) {

			@Override
			public void execute() {
				try {
					if (o.owner == -1) {
						return;
					}
					if (c.getOutStream() != null) {
						Server.itemHandler.createGroundItem(c, getData(trapData, o.objectId, OBJECTID, ITEMID), o.objectX, o.objectY, o.getObjectHeight(), 1);
					}
					Objects empty = new Objects(100, o.objectX, o.objectY, 0, 0, 10, 0);
					Server.objectHandler.placeObject(empty);
					Server.objectHandler.removeObject(o);
					Server.objectHandler.placeObject(empty);
					Server.objectHandler.removeObject(o);
					o.owner = -1;
					o.objectX = 0;
					o.objectY = 0;
					c.sendMessage("Your trap has dismantled and has broken..");
				} finally {
					super.stop();
				}
			}
		}.attach(c));
		return true;
	}

	public static boolean checkTrap(Player c, Objects o) {
		if (c == null || o == null)
			return false;
		if (o.owner == c.getIndex() && o.item > 0) {
			c.getPA().addSkillXP(o.xp, 22);
			c.getItems().addItem(o.item, 1);
			if (getData(trapData, o.objectId, CATCHID, ITEMID) > 0) {
				c.getItems().addItem(getData(trapData, o.objectId, CATCHID, ITEMID), 1);
				Objects empty = new Objects(100, o.objectX, o.objectY, 0, 0, 10, 0);
				Server.objectHandler.addObject(empty);
				Server.objectHandler.placeObject(empty);
				Server.objectHandler.removeObject(empty);
			} else {
				Objects deadfall = new Objects(getData(trapData, o.objectId, CATCHID, ORIG), o.objectX, o.objectY, 0, 0, 10, 0);
				Server.objectHandler.addObject(deadfall);
				Server.objectHandler.placeObject(deadfall);
			}
			c.turnPlayerTo(o.objectX, o.objectY);
			c.startAnimation(5208);
			o.owner = -1;
			o.objectX = 0;
			o.objectY = 0;
			c.sendMessage("You collect from your trap");
			return true;
		} else if (o.owner != c.getIndex()) {
			// c.sendMessage("You can only collect from your own trap!");
		} else if (o.owner == c.getIndex() && o.item <= 0) {
			if (o.objectId == 19662 || o.objectId == 19651 || o.objectId == 19206) {
				return false;
			}
			c.getItems().addItem(getData(trapData, o.objectId, OBJECTID, ITEMID), 1);
			Objects empty = new Objects(100, o.objectX, o.objectY, 0, 0, 10, 0);
			Server.objectHandler.placeObject(empty);
			o.owner = -1;
			o.objectX = 0;
			o.objectY = 0;
			c.sendMessage("You pickup your own trap.");
		}
		return false;
	}

	public static int distSquare(int x, int y, int tx, int ty) {
		return (int) Math.sqrt((Math.abs(x - tx) + Math.abs(y - ty)));
	}

	public static Objects checkClosestObjects(NPC n, int objectId, int npcId) {
		Objects closest = null;
		for (Objects o : ObjectHandler.globalObjects) {
			if (distSquare(n.getAbsX(), n.getAbsY(), o.objectX, o.objectY) < 5 && o.objectId == objectId) { // trapcheck
				if (closest == null) {
					closest = o;
				} else if (npcId == o.target) {
					return o;
				} else if (distSquare(n.getAbsX(), n.getAbsY(), closest.objectX, closest.objectY) > distSquare(n.getAbsX(), n.getAbsY(), o.objectX, o.objectY)) {
					closest = o;
				}
			}
		}
		return closest;
	}

	public static boolean canTrap(Player c) {

		if (c == null)
			return false;
		if (!c.inHunterZone()) {
			c.sendMessage("You can only lay traps in the hunter area");
			return false;
		}
		int amt = 0;

		Objects objects[] = new Objects[6];
		for (Objects o : ObjectHandler.globalObjects) {
			if (o.owner == c.getIndex()) {
				boolean same = false;
				for (Objects o2 : objects) {
					if (o2 != null && o2.objectX == o.objectX && o2.objectY == o.objectY) {
						same = true;
					}
				}
				if (!same)
					objects[amt++] = o;

			}
			if (o.objectId == c.objectId && o.getObjectX() == c.getAbsX() && o.getObjectY() == c.getAbsY()) {
				c.sendMessage("You cannot lay a trap on another trap.");
				return false;
			}
		}

		if (amt > maxTraps(c.getPA().getLevelForXP(c.playerXP[22]))) {
			c.sendMessage("You cannot lay that many traps at your level.");
			return false;
		}
		return true;
	}

	private static int maxTraps(int levelForXP) {
		return levelForXP / 20 + 1;
	}

}
