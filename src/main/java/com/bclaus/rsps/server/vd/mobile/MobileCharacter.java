package com.bclaus.rsps.server.vd.mobile;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.player.Player;

/**
 * @author Omicron
 */
public abstract class MobileCharacter extends Entity {
	public int absX;
	public int absY;
	private int lastX, lastY;
	public int heightLevel;
	public transient Object distanceEvent;
	private final MobileCharacterType type;
	private boolean registered;
	public int poisonDamage;
	public Hit primary;
	public Hit secondary;
	public boolean updateRequired = true;
	public boolean hitUpdateRequired;
	public boolean hitUpdateRequired2;
	private Random random = new Random();

	public MobileCharacter(MobileCharacterType type) {
		this.type = type;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getIndex();
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MobileCharacter))
			return false;
		MobileCharacter other = (MobileCharacter) obj;
		if (getIndex() != other.getIndex())
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	public abstract Hit decrementHP(Hit hit);

	public void clear() {
		primary = null;
		secondary = null;
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		updateRequired = false;
	}

	public boolean poison(PoisonType poisonType) {
		if (poisonDamage > 0 || poisonType == null)
			return false;
		if (random.nextInt(3) == 0) {
			if (type == MobileCharacterType.PLAYER) {
				if (!asPlayer().poisonImmune.elapsed(2, TimeUnit.MINUTES))
					return false;
				if (asPlayer().playerEquipment[Player.playerHat] == 14613)
					return false;
				asPlayer().sendMessage("You have been poisoned!");
			}
			poisonDamage = poisonType.getDamage();
			Server.getTaskScheduler().schedule(new PoisonCombatTask(this));
			return true;
		}
		return false;
	}

	public void damage(Hit... hits) {
		assert (hits.length >= 1 && hits.length <= 4);

		switch (hits.length) {
		case 1:
			sendDamage(hits[0]);
			break;
		case 2:
			sendDamage(hits[0], hits[1]);
			break;
		case 3:
			sendDamage(hits[0], hits[1], hits[2]);
			break;
		case 4:
			sendDamage(hits[0], hits[1], hits[2], hits[3]);
			break;
		}
	}

	private void primaryDamage(Hit hit) {
		primary = decrementHP(hit);
		updateRequired = true;
		hitUpdateRequired = true;
	}

	private void secondaryDamage(Hit hit) {
		secondary = decrementHP(hit);
		updateRequired = true;
		hitUpdateRequired2 = true;
	}

	private void sendDamage(Hit hit) {
		if (hitUpdateRequired) {
			secondaryDamage(hit);
			return;
		}
		primaryDamage(hit);
	}

	private void sendDamage(Hit hit, Hit hit2) {
		sendDamage(hit);
		secondaryDamage(hit2);
	}

	private void sendDamage(Hit hit, Hit hit2, Hit hit3) {
		sendDamage(hit, hit2);

		Server.getTaskScheduler().submit(new ScheduledTask(1, false) {
			@Override
			public void execute() {
				this.stop();
				if (!registered) {
					return;
				}
				sendDamage(hit3);
			}
		});
	}

	private void sendDamage(Hit hit, Hit hit2, Hit hit3, Hit hit4) {
		sendDamage(hit, hit2);

		Server.getTaskScheduler().submit(new ScheduledTask(1, false) {
			@Override
			public void execute() {
				this.stop();
				if (!registered) {
					return;
				}
				sendDamage(hit3, hit4);
			}
		});
	}

	public int getAbsX() {
		return absX;
	}

	public int getAbsY() {
		return absY;
	}

	public int getLastX() {
		return lastX;
	}

	public int getLastY() {
		return lastY;
	}

	public void setAbsX(int absX) {
		this.lastX = this.absX;
		this.absX = absX;
	}

	public void setAbsY(int absY) {
		this.lastY = this.absY;
		this.absY = absY;
	}

	public boolean isRegistered() {
		return registered;
	}

	protected void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public MobileCharacterType getType() {
		return type;
	}

	public Player asPlayer() {
		return (Player) this;
	}

	public NPC asNpc() {
		return (NPC) this;
	}
}
