package com.bclaus.rsps.server.vd.content.skills.impl.farming;

import java.util.ArrayList;
import java.util.List;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.player.DialogueHandler;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;

/**
 * 
 * @author Jason http://www.rune-server.org/members/jason
 * @date Oct 27, 2013
 */

public class Farming {

	static final int MAX_PATCHES = 3;
	private Player player;
	private int weeds;

	public Farming(Player player) {
		this.player = player;
	}

	public void patchObjectInteraction(final int objectId, final int itemId, final int x, final int y) {
		Patch patch = Patch.get(x, y);
		if (patch == null)
			return;
		final int id = patch.getId();
		if (objectId == FarmingConstants.GRASS_OBJECT || objectId == FarmingConstants.HERB_PATCH_DEPLETED) {
			if (player.getFarmingState(id) < State.RAKED.getId()) {
				if (!player.getItems().playerHasItem(FarmingConstants.RAKE, 1))
					player.sendMessage("You need to rake this patch to remove all the weeds.");
				else if (itemId == FarmingConstants.RAKE || player.getItems().playerHasItem(FarmingConstants.RAKE)) {
					player.startAnimation(FarmingConstants.RAKING_ANIM);
					player.turnPlayerTo(x, y);
					if (weeds <= 0)
						weeds = 3;
					Server.getTaskScheduler().schedule(new ScheduledTask(3) {
						@Override
						public void execute() {
							if (weeds > 0) {
								weeds--;
								player.turnPlayerTo(x, y);
								player.getItems().addItem(6055, 1);
								player.startAnimation(FarmingConstants.RAKING_ANIM);
							} else if (weeds == 0) {
								player.setFarmingState(id, State.RAKED.getId());
								player.sendMessage("You raked the patch of all it's weeds, now the patch is ready for compost.");
								player.getPA().resetAnimation();
								updateObjects();
								this.stop();
							}
						}

					}.attach(player));
				}
			} else if (player.getFarmingState(id) >= State.RAKED.getId() && player.getFarmingState(id) < State.COMPOST.getId()) {
				if (!player.getItems().playerHasItem(FarmingConstants.COMPOST, 1))
					player.sendMessage("You need to put compost on this to enrich the soil.");
				else if (itemId == FarmingConstants.COMPOST || player.getItems().playerHasItem(FarmingConstants.COMPOST) && itemId == -1) {
					player.turnPlayerTo(x, y);
					player.startAnimation(FarmingConstants.PUTTING_COMPOST);
					player.getItems().deleteItem2(FarmingConstants.COMPOST, 1);
					player.getItems().addItem(3727, 1);
					player.setFarmingState(id, State.COMPOST.getId());
					player.sendMessage("You put compost on the soil, it is now time to seed it.");
				}
			} else if (player.getFarmingState(id) >= State.COMPOST.getId() && player.getFarmingState(id) < State.SEEDED.getId()) {
				if (!player.getItems().playerHasItem(FarmingConstants.SEED_DIBBER, 1)) {
					player.sendMessage("You need to use a seed dibber with a seed on this patch.");
					return;
				}
				final FarmingHerb.Herb herb = FarmingHerb.getHerbForSeed(itemId);
				if (herb == null) {
					player.sendMessage("You must use an appropriate seed on the patch at this stage.");
					return;
				}
				if (Player.getLevelForXP(player.playerXP[19]) < herb.getLevelRequired()) {
					player.sendMessage("You need a farming level of " + herb.getLevelRequired() + " to grow " + herb.getSeedName().replaceAll(" seed", "") + ".");
					return;
				}
				if (itemId == herb.getSeedId() && player.getItems().playerHasItem(FarmingConstants.SEED_DIBBER)) {
					player.turnPlayerTo(x, y);
					player.startAnimation(FarmingConstants.SEED_DIBBING);
					Server.getTaskScheduler().schedule(new ScheduledTask(3) {
						@Override
						public void execute() {
							if (!player.getItems().playerHasItem(herb.getSeedId()))
								return;
							player.getItems().deleteItem2(herb.getSeedId(), 1);
							player.setFarmingState(id, State.SEEDED.getId());
							player.setFarmingSeedId(id, herb.getSeedId());
							if (player.playerEquipment[Player.playerWeapon] == 7409)
								player.setFarmingTime(id, herb.getGrowthTime() / 2);
							else
								player.setFarmingTime(id, herb.getGrowthTime());
							player.setFarmingHarvest(id, 3 + Misc.random(4));
							player.getPA().addSkillXP(herb.getPlantingXp() * Constants.FARMING_EXPERIENCE, Player.playerFarming);
							player.sendMessage("You dib a seed into the soil, it is now time to water it.");
							updateObjects();
							this.stop();
						}
					}.attach(player));
				}
			}
		} else if (objectId == FarmingConstants.HERB_OBJECT) {
			if (player.getFarmingState(id) >= State.SEEDED.getId() && player.getFarmingState(id) < State.GROWTH.getId()) {
				if (!player.getItems().playerHasItem(FarmingConstants.WATERING_CAN, 1))
					player.sendMessage("You need to water the herb before you can harvest it.");
				else if (itemId == FarmingConstants.WATERING_CAN || player.getItems().playerHasItem(FarmingConstants.WATERING_CAN) && itemId == -1) {
					player.turnPlayerTo(x, y);
					player.startAnimation(FarmingConstants.WATERING_CAN_ANIM);
					player.setFarmingState(id, State.GROWTH.getId());
					player.sendMessage("You water the herb, wait " + (player.getFarmingTime(id) * .6) + " for the herb to mature.");
					return;
				}
			}
			if (player.getFarmingState(id) == State.GROWTH.getId()) {
				if (player.getFarmingTime(id) > 0) {
					player.sendMessage("You need to wait another " + ((int) (player.getFarmingTime(id) * .6)) + " seconds until the herb is mature.");
					return;
				}
			}
			if (player.getFarmingState(id) == State.HARVEST.getId()) {
				if (player.getItems().freeSlots() < 1) {
					DialogueHandler.sendStatement(player, "You need atleast 1 free space to harvest some herbs.");
					player.nextChat = -1;
					return;
				}
				if (System.currentTimeMillis() - player.lastAction < 8000) {
					return;
				}
				if (player.getFarmingHarvest(id) == 0 || player.getFarmingState(id) != State.HARVEST.getId()) {
					resetValues(id);
					updateObjects();
					return;
				}
				final FarmingHerb.Herb herb = FarmingHerb.getHerbForSeed(player.getFarmingSeedId(id));
				if (herb != null) {
					Server.getTaskScheduler().schedule(new ScheduledTask(3) {

						@Override
						public void execute() {
							player.lastAction = System.currentTimeMillis();
							if (player.getItems().freeSlots() < 1) {
								DialogueHandler.sendStatement(player, "You need atleast 1 free space to harvest some herbs.");
								player.nextChat = -1;
								player.getPA().resetAnimation();
								this.stop();
								return;
							}
							if (player.getFarmingHarvest(id) <= 0) {
								player.sendMessage("The herb patch has completely depleted...");
								player.getPA().resetAnimation();
								resetValues(id);
								updateObjects();
								stop();
								return;
							}
							player.startAnimation(FarmingConstants.PICKING_HERB_ANIM);
							player.setFarmingHarvest(id, player.getFarmingHarvest(id) - 1);
							player.getItems().addItem(herb.getGrimyId(), 1);
							player.getPA().addSkillXP(herb.getHarvestingXp() * Constants.FARMING_EXPERIENCE, Player.playerFarming);
						}

					}.attach(player));
				}
			}
		}
	}

	public void farmingProcess() {
		for (int i = 0; i < Farming.MAX_PATCHES; i++) {
			if (player.getFarmingTime(i) > 0 && player.getFarmingState(i) == Farming.State.GROWTH.getId()) {
				player.setFarmingTime(i, player.getFarmingTime(i) - 1);
				if (player.getFarmingTime(i) == 0) {
					FarmingHerb.Herb herb = FarmingHerb.getHerbForSeed(player.getFarmingSeedId(i));
					if (herb != null)
						player.sendMessage("Your farming patch of " + herb.getSeedName().replaceAll(" seed", "") + " is ready to be harvested.");
					player.setFarmingState(i, Farming.State.HARVEST.getId());
				}
			}
		}
	}

	public void resetValues(int id) {
		player.setFarmingHarvest(id, 0);
		player.setFarmingSeedId(id, 0);
		player.setFarmingState(id, 0);
		player.setFarmingTime(id, 0);
	}

	public void updateObjects() {
		for (int i = 0; i < Farming.MAX_PATCHES; i++) {
			Patch patch = Patch.get(i);
			if (patch == null)
				continue;
			if (player.distanceToPoint(patch.getX(), patch.getY()) > 60)
				continue;
			if (player.getFarmingState(i) < State.RAKED.getId())
				player.getPA().object(FarmingConstants.GRASS_OBJECT, patch.getX(), patch.getY(), 0, 10);
			else if (player.getFarmingState(i) >= State.RAKED.getId() && player.getFarmingState(i) < State.SEEDED.getId())
				player.getPA().object(FarmingConstants.HERB_PATCH_DEPLETED, patch.getX(), patch.getY(), 0, 10);
			else if (player.getFarmingState(i) >= State.SEEDED.getId())
				player.getPA().object(FarmingConstants.HERB_OBJECT, patch.getX(), patch.getY(), 0, 10);

		}
	}

	public boolean isHarvestable(int id) {
		return player.getFarmingState(id) == State.HARVEST.getId();
	}

	public enum State {
		NONE(0), RAKED(1), COMPOST(2), SEEDED(3), WATERED(4), GROWTH(5), HARVEST(6);

		private int id;

		State(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	static enum Patch {
		CAMELOT_AREA(0, 2813, 3463),
		CAMELOT_AREA2(1, 3055, 3308);
		
		int id, x, y;

		Patch(int id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}

		public int getId() {
			return this.id;
		}

		public int getX() {
			return this.x;
		}

		public int getY() {
			return this.y;
		}

		static List<Patch> patches = new ArrayList<Patch>();

		static {
			for (Patch patch : Patch.values())
				patches.add(patch);
		}

		public static Patch get(int x, int y) {

			for (Patch patch : patches)

				if (patch.getX() == x && patch.getY() == y)
					return patch;
			return null;
		}

		public static Patch get(int id) {
			for (Patch patch : patches)
				if (patch.getId() == id)
					return patch;
			return null;
		}
	}
}
