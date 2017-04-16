/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bclaus.rsps.server.vd.npc;

/**
 *
 * @author Tim/Someone
 */

public class NPCDefinitions {

	public static int NPCS = 10000;
	private static NPCDefinitions[] definitions = new NPCDefinitions[NPCS];

	/**
	 * @return the definitions
	 */
	public static NPCDefinitions[] getDefinitions() {
		return definitions;
	}

	private int npcId;
	private String npcName;
	private int npcCombat;
	private int npcHealth;

	public NPCDefinitions(int _npcId) {
		npcId = _npcId;
	}

	public NPCDefinitions(NPCDefinitions def) {
		definitions[def.getNpcId()] = def;
	}

	/**
	 * @return the npcId
	 */
	public int getNpcId() {
		return npcId;
	}

	/**
	 * @param npcId
	 *            the npcId to set
	 */
	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	/**
	 * @return the npcName
	 */
	public String getNpcName() {
		return npcName;
	}

	/**
	 * @param npcName
	 *            the npcName to set
	 */
	public void setNpcName(String npcName) {
		this.npcName = npcName;
	}

	/**
	 * @return the npcCombat
	 */
	public int getNpcCombat() {
		return npcCombat;
	}

	/**
	 * @param npcCombat
	 *            the npcCombat to set
	 */
	public void setNpcCombat(int npcCombat) {
		this.npcCombat = npcCombat;
	}

	/**
	 * @return the npcHealth
	 */
	public int getNpcHealth() {
		return npcHealth;
	}

	/**
	 * @param npcHealth
	 *            the npcHealth to set
	 */
	public void setNpcHealth(int npcHealth) {
		this.npcHealth = npcHealth;
	}
}
