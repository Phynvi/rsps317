package com.bclaus.rsps.server.vd.npc.boss;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.bclaus.rsps.server.vd.npc.boss.impl.Glod;
import com.bclaus.rsps.server.vd.npc.boss.impl.Rex;
import com.bclaus.rsps.server.vd.npc.boss.impl.Saradomin;
import com.bclaus.rsps.server.vd.npc.boss.impl.Zulrah;
import com.bclaus.rsps.server.vd.npc.boss.impl.Armadyl;
import com.bclaus.rsps.server.vd.npc.boss.impl.Bandos;
import com.bclaus.rsps.server.vd.npc.boss.impl.Barrelchest;
import com.bclaus.rsps.server.vd.npc.boss.impl.Callisto;
import com.bclaus.rsps.server.vd.npc.boss.impl.Cave_Kraken;
import com.bclaus.rsps.server.vd.npc.boss.impl.Jungle_Demon;
import com.bclaus.rsps.server.vd.npc.boss.impl.KBD;
import com.bclaus.rsps.server.vd.npc.boss.impl.Prime;
import com.bclaus.rsps.server.vd.npc.boss.impl.Scorpia;
import com.bclaus.rsps.server.vd.npc.boss.impl.Spinyolp;
import com.bclaus.rsps.server.vd.npc.boss.impl.Supreme;
import com.bclaus.rsps.server.vd.npc.boss.impl.Veneatis;
import com.bclaus.rsps.server.vd.npc.boss.impl.Vetion;
import com.bclaus.rsps.server.vd.npc.boss.impl.Zamorak;

public class Bosses {
	
	private static Map<Integer, Boss> bosses = new HashMap<>();
	
	public static final Barrelchest BARRELCHEST = new Barrelchest(5666);
	public static final Bandos BANDOS = new Bandos(6260);
	public static final Zamorak ZAMORAK = new Zamorak(6203);
	public static final Saradomin SARADOMIN = new Saradomin(6247);
	public static final Armadyl ARMA = new Armadyl(6222);
	public static final KBD KINGBLACKDRAGON = new KBD(50);
	public static final Vetion VETION = new Vetion(4175);
	public static final Scorpia SCORPIA = new Scorpia(4172);
	public static final Callisto CALLISTO = new Callisto(4174);
	public static final Veneatis VENEATIS = new Veneatis(4173);
	public static final Glod GLOD = new Glod(5996);
	public static final Prime PRIME = new Prime(2882);
	public static final Supreme SUPREME = new Supreme(2881);
	public static final Rex REX = new Rex(2883);
	public static final Spinyolp SPINYOLP = new Spinyolp(2892);
	public static final Zulrah ZULRAH = new Zulrah(2042);
	public static final Cave_Kraken CAVE_KRAKEN = new Cave_Kraken(3848);
	public static final Jungle_Demon JUNGLE_DEMON = new Jungle_Demon(1472);

	static {
		bosses.put(BARRELCHEST.npcId, BARRELCHEST);
		bosses.put(BANDOS.npcId, BANDOS);
		bosses.put(ZAMORAK.npcId, ZAMORAK);
		bosses.put(SARADOMIN.npcId, SARADOMIN);
		bosses.put(KINGBLACKDRAGON.npcId, KINGBLACKDRAGON);
		bosses.put(ARMA.npcId, ARMA);
		bosses.put(CALLISTO.npcId, CALLISTO);
		bosses.put(SCORPIA.npcId, SCORPIA);
		bosses.put(VETION.npcId, VETION);
		bosses.put(VENEATIS.npcId, VENEATIS);
		bosses.put(GLOD.npcId, GLOD);
		bosses.put(PRIME.npcId, PRIME);
		bosses.put(SUPREME.npcId, SUPREME);
		bosses.put(REX.npcId, REX);
		bosses.put(SPINYOLP.npcId, SPINYOLP);
		bosses.put(ZULRAH.npcId, ZULRAH);
		bosses.put(CAVE_KRAKEN.npcId, CAVE_KRAKEN);
		bosses.put(JUNGLE_DEMON.npcId, JUNGLE_DEMON);
		
	}
	
	public static Boss get(int npcId) {
		if (!bosses.containsKey(npcId))
			return null;
		return bosses.get(npcId);
	}
	
	public static boolean isBoss(int npcId) {
		return Objects.nonNull(get(npcId));
	}
}
