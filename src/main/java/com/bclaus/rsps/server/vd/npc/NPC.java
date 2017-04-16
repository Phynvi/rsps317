package com.bclaus.rsps.server.vd.npc;

import com.bclaus.rsps.server.region.Region;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.mobile.Hit;
import com.bclaus.rsps.server.vd.mobile.MobileCharacterType;
import com.bclaus.rsps.server.vd.world.Position;
import com.bclaus.rsps.server.vd.mobile.EntityType;
import com.bclaus.rsps.server.vd.mobile.MobileCharacter;

public class NPC extends MobileCharacter {

	public int getId() {
		return npcType;
	}

	public int stage = 0;
	public int chance = 0;

	public boolean transformUpdateRequired = false;
	public int transformId;

	public void requestTransform(int Id) {
		transformId = Id;
		transformUpdateRequired = true;
		updateRequired = true;
	}
	
	public int npcType, combatLevel, makeX, makeY, maxHit, defence, attack, moveX, moveY, direction, walkingType, itemId;

	public int whose, attackType, projectileId, endGfx, spawnedBy, hitDelayTimer, HP, MaxHP, animNumber, actionTimer, freezeTimer, attackTimer, killerId, killedBy, oldIndex, underAttackBy, npcIndex;
	public boolean noDeathEmote, applyDead, isDead, needRespawn, walkingHome, underAttack, randomWalk, dirUpdateRequired, animUpdateRequired, forcedChatRequired;

	public boolean aggressive;
	public long lastDamageTaken;

	/**
	 * attackType: 0 = melee, 1 = range, 2 = mage
	 */
	private String forcedText;
	public void turnNpc(int i, int j) {
        FocusPointX = 2 * i + 1;
        FocusPointY = 2 * j + 1;
        updateRequired = true;

    }
	public NPC(int _npcType) {
		super(MobileCharacterType.NPC);
		npcType = _npcType;
		direction = -1;
		isDead = false;
		applyDead = false;
		actionTimer = 0;
		randomWalk = true;
		combatLevel = NPCDefinitions.getDefinitions()[npcType] == null ? 1 : NPCDefinitions.getDefinitions()[npcType].getNpcCombat();
	}

	@Override
	public Hit decrementHP(Hit hit) {
		int damage = hit.getDamage();
		if (damage > this.HP)
			damage = this.HP;
		this.HP -= damage;
		return new Hit(damage, hit.getType());
	}

	public Position getPosition() {
		return new Position(absX, absY, heightLevel);
	}

	/**
	 * Text update
	 **/

	public void forceChat(String text) {
		setForcedText(text);
		forcedChatRequired = true;
		updateRequired = true;
	}

	/**
	 * Graphics
	 **/

	public int mask80var1 = 0;
	public int mask80var2 = 0;
	private boolean mask80update = false;
	
	public void gfx100(int gfx) {
		mask80var1 = gfx;
		mask80var2 = 6553600;
		setMask80update(true);
		updateRequired = true;
	}

	public void gfx0(int gfx) {
		mask80var1 = gfx;
		mask80var2 = 65536;
		setMask80update(true);
		updateRequired = true;
	}
	
	/**
	 * 
	 Face
	 * 
	 **/

	public int FocusPointX = -1, FocusPointY = -1;
	public int face = 0;
	
	public void faceLocation(int x, int y) {
		FocusPointX = x;
		FocusPointY = y;
		updateRequired = true;
	}
	
	public void facePlayer(int player) {
		if (npcType > 1531 && npcType < 1536) {
			return;
		}
		face = player + 32768;
		dirUpdateRequired = true;
		updateRequired = true;
	}

	public void forceAnim(int number) {
		animNumber = number;
		animUpdateRequired = true;
		updateRequired = true;
	}


	public int getSize() {
		return NPCSize.getSize(npcType);// kk try
	}

	public void clearUpdateFlags() {
		updateRequired = false;
		forcedChatRequired = false;
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		animUpdateRequired = false;// that should be it for the npc sizes
		dirUpdateRequired = false;
		transformUpdateRequired = false;
		setMask80update(false);
		setForcedText(null);
		moveX = 0;
		moveY = 0;
		direction = -1;
		FocusPointX = -1;
		FocusPointY = -1;
		super.clear();
	}

	public int getNextWalkingDirection() {
		int dir;
		dir = Misc.direction(absX, absY, (absX + moveX), (absY + moveY));
		if (!Region.canMove(absX, absY, (absX + moveX), (absY + moveY), heightLevel, getSize(), getSize()))
			return -1;
		if (dir == -1)
			return -1;
		dir >>= 1;
		absX += moveX;
		absY += moveY;
		return dir;
	}

	public void getNextNPCMovement(int i) {
		direction = -1;
		if (NPCHandler.NPCS.get(i).freezeTimer == 0) {
			direction = getNextWalkingDirection();
		}
	}

	public long delay;

	public boolean summoner;
	public int summonedBy;

	public boolean forClue;
	

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	public boolean inMulti() {
		return (absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607) || (absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839) || (absX >= 2625 && absX <= 2685 && absY >= 2550 && absY <= 2620) || // Pest
				(absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967) || (absX >= 2700 && absX <= 2790 && absY >= 9000 && absY <= 9200) || (absX >= 2864 && absX <= 2877 && absY >= 5348 && absY <= 5374) || // bandos
				(absX >= 2917 && absX <= 2937 && absY >= 5315 && absY <= 5332) || // zammy
				(absX >= 2884 && absX <= 2991 && absY >= 5255 && absY <= 5278) || // sara
				(absX >= 2821 && absX <= 2844 && absY >= 5292 && absY <= 5311) || // armadyl
				(absX >= 2968 && absX <= 2988 && absY >= 9512 && absY <= 9523) || // barrelchest
				(absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967) || (absX >= 2315 && absX <= 2354 && absY >= 3693 && absY <= 3716) || (absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831) || (absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903) || (absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711) || (absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647) || (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619) || (absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117) || (absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630) || (absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464) || (absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711) || (absX >= 3157 && absX <= 3191 && absY >= 2965 && absY <= 2995) || (absX >= 2512 && absX <= 2540 && absY >= 4633 && absY <= 4659) || (absX >= 2770 && absX <= 2798 && absY >= 9321 && absY <= 9340) || (absX >= 3461 && absX <= 3494 && absY >= 9476 && absY <= 9506) || (absX >= 2331 && absX <= 2354 && absY >= 3693 && absY <= 3716) || (absX >= 3357 && absX <= 3383 && absY >= 3721 && absY <= 3749) || (absX >= 2785 && absX <= 2809 && absY >= 2775 && absY <= 2795) || (absX >= 3093 && absX <= 3118 && absY >= 3922 && absY <= 3947) || absX >= 2894 && absX <= 2924 && absY >= 3596 && absY <= 3630 || (absX >= 2660 && absX <= 2730 && absY >= 3707 && absY <= 3737) ||
				(absX >= 2880 && absX <= 3005 && absY >= 4360 && absY <= 4415);
	}

	public boolean inWild() {
		return absX > 2941 && absX < 3392 && absY > 3518 && absY < 3966 || absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.NPC;
	}

	public boolean isMask80update() {
		return mask80update;
	}

	public void setMask80update(boolean mask80update) {
		this.mask80update = mask80update;
	}

	public String getForcedText() {
		return forcedText;
	}

	public void setForcedText(String forcedText) {
		this.forcedText = forcedText;
	}

}
