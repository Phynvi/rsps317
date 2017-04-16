package com.bclaus.rsps.server.vd.mobile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.task.ScheduledTask;

/**
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class PoisonCombatTask extends ScheduledTask {

	public static final Map<Integer, PoisonType> WEAPON_TYPES = new HashMap<>();
	public static final Map<Integer, PoisonType> NPC_TYPES = new HashMap<>();
	private final MobileCharacter c;
	private final Random random = new Random();

	public PoisonCombatTask(MobileCharacter c) {
		super(60, false);
		this.c = c;
	}

	@Override
	public void execute() {
		if (!c.isRegistered()) {
			stop();
			return;
		}
		if (c.poisonDamage <= 0) {
			stop();
			return;
		}
		c.damage(new Hit(c.poisonDamage, HitType.POISON));
		if (random.nextBoolean())
			c.poisonDamage--;
		if (c.poisonDamage <= 0) {
			stop();
		}
	}

	/**
	 * Gets the {@link PoisonType} for {@code item} wrapped in an optional. If a
	 * poison type doesn't exist for the item then an empty optional is
	 * returned.
	 * 
	 * @param item
	 *            the item to get the poison type for.
	 * @return the poison type for this item wrapped in an optional, or an empty
	 *         optional if no poison type exists.
	 */
	public static Optional<PoisonType> getPoisonType(Item item) {
		if (item == null || item.getId() < 1 || item.getCount() < 1)
			return Optional.empty();
		return Optional.ofNullable(WEAPON_TYPES.get(item.getId()));
	}

	/**
	 * Gets the {@link PoisonType} for {@code npc} wrapped in an optional. If a
	 * poison type doesn't exist for the NPC then an empty optional is returned.
	 * 
	 * @param npc
	 *            the NPC to get the poison type for.
	 * @return the poison type for this NPC wrapped in an optional, or an empty
	 *         optional if no poison type exists.
	 */
	public static Optional<PoisonType> getPoisonType(int npc) {
		if (npc < 0)
			return Optional.empty();
		return Optional.ofNullable(NPC_TYPES.get(npc));
	}
}
