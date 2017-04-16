package com.bclaus.rsps.server.vd.objects.cannon;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.mobile.Hit;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.task.ScheduledTask;

/**
 * @author lare96
 */
public class FireWorker extends ScheduledTask {

	public FireWorker(NPC npc, Player player) {
		super(2, false);
		this.hit = CannonManager.DAMAGE_INTERVAL.calculate();
		this.npc = npc;
		this.player = player;
	}

	private int hit;
	private NPC npc;
	private Player player;

	@Override
	public void execute() {
		npc.damage(new Hit(hit));
		npc.facePlayer(player.getIndex());
		player.totalDamageDealt += hit;
		player.lastNpcAttacked = npc.getIndex();
		npc.killerId = player.getIndex();
		npc.underAttackBy = player.getIndex();
		player.getPA().addSkillXP(hit * 25, 4);
		this.stop();
	}
}