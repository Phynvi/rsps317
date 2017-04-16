package com.bclaus.rsps.server.vd.objects.cannon;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.region.RegionObject;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Boundary;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.world.ObjectManager;
import com.bclaus.rsps.server.vd.world.Position;
import com.bclaus.rsps.server.vd.items.Item;

/**
 * @author lare96
 */
public final class CannonManager {

	public static final Misc.Interval DAMAGE_INTERVAL = new Misc.Interval().inclusiveInterval(0, 25);
	public static final int FIRE_RADIUS = 7;
	public static final int BUILD_RADIUS = 10;
	public static final Set<Position> POSITION_SET = new HashSet<>();

	public static void makeCannon(final Player player) {
		
		if (player.getCC().hasCannon()) {
			player.sendMessage("You can only own one built cannon at a time!");
			return;
		}
		/*if (!player.hasBoughtCannon) {
			player.sendMessage("You do not hold the cannon-status, therefore you cannot place a cannon down.");
			return;
		}*/
		if (player.inDuelArena() || player.inPcGame() || player.inFightCaves() || cantPlace(player.absX, player.absY, player.heightLevel)) {
			player.sendMessage("You cannot setup your cannon here.");
			return;
		}
		if(Boundary.isInBounds(player, Boundary.ZULRAH)) {
			player.sendMessage("You cannot setup a cannon here");
			return;
		}
		if (player.getCC().getSetupStage() != Setup.NOTHING) {
			player.sendMessage("You have already started setting up a cannon!");
			return;
		}
		for (Position position : POSITION_SET) {
			if (player.getPosition().withinDistance(position, BUILD_RADIUS)) {
				player.sendMessage("You must build your cannon at least " + BUILD_RADIUS + " squares away from other cannons!");
				return;
			}
		}
		if (!player.getItems().contains(new int[] { 6, 8, 10, 12 })) {
			player.sendMessage("You need a base, stand, barrels, and a furnace in order to build a cannon!");
			return;
		}

		player.resetWalkingQueue();
		player.playerStun = true;

		Server.getTaskScheduler().schedule(new ScheduledTask(2, true) {
			@Override
			public void execute() {
				player.getCC().getSetupStage().setup(player);

				if (player.getCC().getSetupStage() == Setup.CANNON) {
					POSITION_SET.add(player.getPosition());
					player.playerStun = false;
					this.stop();
				}
			}

			@Override
			public void onStop() {
				if (player.disconnected)
					POSITION_SET.remove(player.getPosition());
			}
		}.attach(player));
	}

	public static void retrieveCannon(Player player, Position cannonPosition, boolean forcePickup) {

		if (player.getItems().freeSlots() < 1 && player.getItems().freeBankSlots() < 1 && forcePickup) {
		}
		if (!player.getCC().hasCannon()) {
			if (!forcePickup)
				player.sendMessage("This is not your cannon to pick up!");
			return;
		}

		if (!player.getCC().getCannon().getPosition().equals(cannonPosition)) {
			if (forcePickup) {
				throw new IllegalStateException("actual cannon position: " + player.getCC().getCannon().getPosition() + ", picking up cannon on: " + cannonPosition);
			}
			player.sendMessage("This is not your cannon to pick up!");
			return;
		}

		Item[] returnItems = { new Item(6), new Item(8), new Item(10), new Item(12), new Item(2, player.getCC().getCannon().getAmmunition()) };

		for (Item item : returnItems) {
			if (item.getCount() < 1) {
				continue;
			}

			if (player.getItems().freeSlots() > 3 || item.getId() == 2 && player.getItems().contains(2)) {
				player.getItems().addItem(item);
			} else {
				player.getItems().addItemToBank(item);
			}
		}

		if (!forcePickup)
			player.startAnimation(827);
		remove(cannonPosition);
		ObjectManager.removeObjectDatabase(cannonPosition);
		player.getCC().getCannon().setCurrentlyFiring(false);
		player.getCC().setCannon(null);
		player.getCC().setSetupStage(Setup.NOTHING);
	}

	private static void remove(Position p) {
		for (Iterator<Position> it = POSITION_SET.iterator(); it.hasNext();) {
			Position o = it.next();
			if (o == null)
				continue;
			if (o.equals(p)) {
				it.remove();
			}
		}
	}

	public static boolean cantPlace(int x, int y, int z) {
		return RegionObject.objectExists(x, y, z) || RegionObject.objectExists(x + 1, y, z) || RegionObject.objectExists(x, y + 1, z) || RegionObject.objectExists(x + 1, y + 1, z) || RegionObject.objectExists(x + 1, y - 1, z) || RegionObject.objectExists(x - 1, y + 1, z) || RegionObject.objectExists(x - 1, y - 1, z) || RegionObject.objectExists(x, y - 1, z) || RegionObject.objectExists(x - 1, y, z);
	}

	public static void fireCannon(final Player player, Position cannonPosition) {
		if (!player.getCC().hasCannon() || !player.getCC().getCannon().getPosition().equals(cannonPosition)) {
			player.sendMessage("This is not your cannon to fire!");
			return;
		}

		if (player.getCC().getCannon().isCurrentlyFiring()) {
			player.sendMessage("Your cannon is already firing!");
			return;
		}

		int count = player.getItems().getItemAmount(2);

		if (count >= 30) {
			player.getCC().getCannon().setAmmunition(30);
			player.getItems().deleteItem(2, 30);
			player.sendMessage("You load 30 of your " + count + " cannon balls and begin firing.");
		} else if (count > 0 && count < 30) {
			player.getCC().getCannon().setAmmunition(count);
			player.getItems().deleteItem(2, count);
			player.sendMessage("You load " + count + " of your cannon balls and begin firing.");
		} else if (count == 0) {
			player.sendMessage("You need cannon balls in order to fire this cannon!");
			return;
		}

		player.getCC().getCannon().setCurrentlyFiring(true);
		final Cannon cannon = player.getCC().getCannon();

		Server.getTaskScheduler().schedule(new ScheduledTask(1, false) {
			@Override
			public void execute() {
				if (!cannon.isCurrentlyFiring()) {
					this.stop();
					return;
				}

				if (cannon.getAmmunition() == 0) {
					player.sendMessage("Your cannon has run out of ammo.");
					player.getCC().getCannon().setCurrentlyFiring(false);
					this.stop();
					return;
				}
				player.getPA().sendAllObjectAnimation(cannon.getPosition(), cannon.getDirection().getObjectAnimation(), 10, -1);

				if (player.underAttackBy2 > 0) {
					NPC n = NPCHandler.NPCS.get(player.underAttackBy2);
					if (n != null && !(n.MaxHP < 1) && !n.isDead) {
						if (n.getPosition().withinDistance(cannon.getPosition(), FIRE_RADIUS) && cannon.getDirection().canTarget(cannon.getPosition(), n.getPosition())) {
							cannon.fireProjectile(n);
							Server.getTaskScheduler().schedule(new FireWorker(n, player));
							cannon.decrementAmmunition();
						}
					}
				} else {
					for (NPC npc : NPCHandler.NPCS) {
						if (npc == null)
							continue;
						if (npc.MaxHP < 1 || npc.isDead || !npc.getPosition().withinDistance(cannon.getPosition(), FIRE_RADIUS)) {
							continue;
						}

						if (cannon.getDirection().canTarget(cannon.getPosition(), npc.getPosition())) {
							if (!npc.inMulti() && npc.underAttack || npc.killerId > 0 && npc.killerId != player.getIndex()) {
								continue;
							}

							cannon.fireProjectile(npc);
							Server.getTaskScheduler().schedule(new FireWorker(npc, player));
							cannon.decrementAmmunition();
							break;
						}
					}
				}

				cannon.fireRotation();
			}
		}.attach(player));
	}
}
