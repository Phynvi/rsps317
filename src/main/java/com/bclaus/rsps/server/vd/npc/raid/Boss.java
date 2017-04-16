package com.bclaus.rsps.server.vd.npc.raid;

import com.bclaus.rsps.server.util.Misc;
public enum Boss {

	/**
	 * to-do Randomize locations
	 */
	kING_BLACK_DRAGON(50, "King Black Dragon", 4000, 4000, 100, 10, 50, 50, "Varrock Square!"),
	ZURAL(2, "Zural", 4000, 4000, 100, 10, 50, 50, "Edgeville Bridge!"),
	KREE(6222, "Kree'arra", 2965, 3392, 900, 81, 630, 545, "Falador Gatess!"),
	JAD(2745, "Jad", 2738, 3469, 900, 81, 630, 545, "Camelot Party Room!!"), 
	BANDOS(2, "General Graardor", 4000, 1400, 1200, 35, 650, 650, "Cabbage Field");

	public static Boss random() {
		Boss[] bosses = values();
		return bosses[Misc.random(Boss.values().length - 1)];
	}

	private int _ID;
	private String name;
	private int coordX, coordY;
	private int _HP;
	private int maxHit;
	private int attack;
	private int defence;
	private String msg;

	private Boss(int _ID, String _NPCName, int coordX, int coordY, int _HP, int maxHit, int attack, int defence, String msg) {
		this._ID = _ID;
		this.name = _NPCName;
		this.coordX = coordX;
		this.coordY = coordY;
		this._HP = _HP;
		this.maxHit = maxHit;
		this.attack = attack;
		this.defence = defence;
		this.msg = msg;
	}

	public int getAttack() {
		return attack;
	}

	public int getDefence() {
		return defence;
	}

	public String getName() {
		return name;
	}

	public int getHP() {
		return _HP;
	}

	public int getCoordinateX() {
		return coordX;
	}

	public int getCoordinateY() {
		return coordY;
	}

	public int getID() {
		return _ID;
	}

	public int getMaxHit() {
		return maxHit;
	}

	public String getMessage() {
		return msg;
	}

}
