package com.bclaus.rsps.server.task.impl;

import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;

public final class NPCMovementTask extends ScheduledTask {

	public NPCMovementTask() {
		super(15);
	}

	@Override
	public void execute() {
		for (int index = 0; index < NPCHandler.NPCS.capacity(); index++) {
			NPC npc = NPCHandler.NPCS.get(index);
			if (npc == null) {
				continue;
			}
			if(npc.npcType == 538) {
				//npc.forceChat("Spend your PK points here!");
			}
			if ((!npc.underAttack || npc.walkingHome) && npc.randomWalk && !npc.isDead) {
				npc.facePlayer(0);
				npc.killerId = 0;
				int walkingDistance = 5;
				if (npc.walkingType > 5) {
					walkingDistance = npc.walkingType;
				}
				if (npc.spawnedBy == 0) {
					if ((npc.getAbsX() > npc.makeX + walkingDistance) || (npc.getAbsX() < npc.makeX - walkingDistance) || (npc.getAbsY() > npc.makeY + walkingDistance) || (npc.getAbsY() < npc.makeY - walkingDistance)) {
						npc.walkingHome = true;
					}
				}
				if (npc.walkingHome && npc.getAbsX() == npc.makeX && npc.getAbsY() == npc.makeY) {
					npc.walkingHome = false;
				} else if (npc.walkingHome) {
					npc.moveX = NPCHandler.GetMove(npc.getAbsX(), npc.makeX);
					npc.moveY = NPCHandler.GetMove(npc.getAbsY(), npc.makeY);
					npc.getNextNPCMovement(index);
					npc.updateRequired = true;
				}
				if (npc.walkingType > 0) {
					if (Misc.random(3) == 1 && !npc.walkingHome) {
						int MoveX = 0;
						int MoveY = 0;
						int Rnd = Misc.random(9);
						switch (Rnd) {
						case 1:
							MoveX = npc.walkingType;
							MoveY = npc.walkingType;
							break;
						case 2:
							MoveX = -npc.walkingType;
							break;
						case 3:
							MoveY = -npc.walkingType;
							break;
						case 4:
							MoveX = npc.walkingType;
							break;
						case 5:
							MoveY = npc.walkingType;
							break;
						case 6:
							MoveX = -npc.walkingType;
							MoveY = -npc.walkingType;
							break;
						case 7:
							MoveX = -npc.walkingType;
							MoveY = npc.walkingType;
							break;
						case 8:
							MoveX = npc.walkingType;
							MoveY = -npc.walkingType;
							break;
						}

						if (MoveX == npc.walkingType) {
							if (npc.getAbsX() + MoveX < npc.makeX + 1) {
								npc.moveX = MoveX;
							} else {
								npc.moveX = 0;
							}

						}
						if (MoveX == -npc.walkingType) {
							if (npc.getAbsX() - MoveX > npc.makeX - 1) {
								npc.moveX = MoveX;
							} else {
								npc.moveX = 0;
							}

						}
						if (MoveY == npc.walkingType) {
							if (npc.getAbsY() + MoveY < npc.makeY + 1) {
								npc.moveY = MoveY;
							} else {
								npc.moveY = 0;
							}

						}
						if (MoveY == -npc.walkingType) {
							if (npc.getAbsY() - MoveY > npc.makeY - 1) {
								npc.moveY = MoveY;
							} else {
								npc.moveY = 0;
							}

						}
						npc.getNextNPCMovement(index);
						npc.updateRequired = true;
					}
				}
			}
		}
	}

}
