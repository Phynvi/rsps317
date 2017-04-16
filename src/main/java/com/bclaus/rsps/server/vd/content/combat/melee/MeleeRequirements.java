package com.bclaus.rsps.server.vd.content.combat.melee;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.content.combat.magic.CastOnOther;
import com.bclaus.rsps.server.vd.player.Player;

public class MeleeRequirements {

	public static int getKillerId(Player c, int playerId) {
		int oldDamage = 0;
		int killerId = 0;
		for (int i = 1; i < World.PLAYERS.capacity(); i++) {
			if (World.PLAYERS.get(i) != null) {
				if (World.PLAYERS.get(i).killedBy == playerId) {
					if (World.PLAYERS.get(i).withinDistance(World.PLAYERS.get(playerId))) {
						if (World.PLAYERS.get(i).totalPlayerDamageDealt > oldDamage) {
							oldDamage = World.PLAYERS.get(i).totalPlayerDamageDealt;
							killerId = i;
						}
					}
					World.PLAYERS.get(i).totalPlayerDamageDealt = 0;
					World.PLAYERS.get(i).killedBy = 0;
				}
			}
		}
		return killerId;
	}

	public static int getCombatDifference(int combat1, int combat2) {
		if (combat1 > combat2) {
			return (combat1 - combat2);
		}
		if (combat2 > combat1) {
			return (combat2 - combat1);
		}
		return 0;
	}

	public static boolean checkReqs(Player c) {
		if (World.PLAYERS.get(c.playerIndex) == null) {
			return false;
		}
		if (c.playerIndex == c.getIndex())
			return false;
		if (c.inPits && World.PLAYERS.get(c.playerIndex).inPits)
			return true;
		if (World.PLAYERS.get(c.playerIndex).inDuelArena() && c.duelStatus != 5 && !c.usingMagic) {
			if (c.arenas() || c.duelStatus == 5) {
				c.sendMessage("You can't challenge inside the arena!");
				return false;
			}
			c.getTradeAndDuel().requestDuel(c.playerIndex);
			return false;
		}
		if (c.duelStatus == 5 && World.PLAYERS.get(c.playerIndex).duelStatus == 5) {
			if (World.PLAYERS.get(c.playerIndex).duelingWith == c.getIndex()) {
				return true;
			} else {
				c.sendMessage("This isn't your opponent!");
				return false;
			}
		}
		if (!World.PLAYERS.get(c.playerIndex).inWild() && !CastOnOther.castOnOtherSpells(c)) {
			c.sendMessage("Your opponent is not in the wilderness!");
			c.stopMovement();
			c.getCombat().resetPlayerAttack();
			return false;
		}
		if (!c.inWild() && !CastOnOther.castOnOtherSpells(c) && !c.inDuelArena() && !World.PLAYERS.get(c.playerIndex).inDuelArena()) {
			c.sendMessage("You are not in the wilderness.");
			c.stopMovement();
			c.getCombat().resetPlayerAttack();
			return false;
		}
		if (Constants.COMBAT_LEVEL_DIFFERENCE) {
			if (c.inWild()) {
				int combatDif1 = getCombatDifference(c.combatLevel, World.PLAYERS.get(c.playerIndex).combatLevel);
				if ((combatDif1 > c.wildLevel || combatDif1 > World.PLAYERS.get(c.playerIndex).wildLevel)) {
					c.sendMessage("Your combat level difference is too great to attack that player here.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return false;
				}
			} else {
				int myCB = c.combatLevel;
				int pCB = World.PLAYERS.get(c.playerIndex).combatLevel;
				if ((myCB > pCB + 12) || (myCB < pCB - 12)) {
					c.sendMessage("You can only fight players in your combat range!");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return false;
				}
			}
		}

		if (Constants.SINGLE_AND_MULTI_ZONES) {
			if (!World.PLAYERS.get(c.playerIndex).inMulti()) { // single
																	// combat
																	// zones
				if (World.PLAYERS.get(c.playerIndex).underAttackBy != c.getIndex() && World.PLAYERS.get(c.playerIndex).underAttackBy != 0) {
					c.sendMessage("That player is already in combat.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return false;
				}
				if (World.PLAYERS.get(c.playerIndex).getIndex() != c.underAttackBy && c.underAttackBy != 0 || c.underAttackBy2 > 0) {
					c.sendMessage("You are already in combat.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return false;
				}
			}
		}
		return true;
	}

	public static int getRequiredDistance(Player c) {
		if (c.followId > 0 && c.freezeTimer <= 0 && !c.isMoving)
			return 2;
		else if (c.followId > 0 && c.freezeTimer <= 0 && c.isMoving) {
			return 3;
		} else {
			return 1;
		}
	}
}