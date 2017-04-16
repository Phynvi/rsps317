package com.bclaus.rsps.server.quarantine;

/**
 * 
 * @author Jason MacKeigan http://www.rune-server.org/members/jason
 * @date Jul 3, 2014, 11:38:12 PM
 */
public class QuarantineEntry {

	private String name,  identity;
	private int severity;

	/**
	 * The QuarantineEntry constructor
	 * 
	 * @param name
	 *            The players name
	 * @param ip
	 *            The players internet protocol address
	 * @param identity
	 *            The identity of the player
	 * @param macAddress
	 *            The players mac address
	 * @param severity
	 *            The severity of the quarantine
	 */
	public QuarantineEntry(String name, String identity, int severity) {
		this.setName(name);
		this.setIdentity(identity);
		this.setSeverity(severity);
	}

	/**
	 * Retrieves the identity of the quarantine
	 * 
	 * @return The identity
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * Sets the identity of the quarantine
	 * 
	 * @param identity
	 *            The identity
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	/**
	 * Retrieves the name of the quarantine
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the quarantine
	 * 
	 * @param name
	 *            The name
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Retrieves the severity of the quarantine
	 * 
	 * @return The severity
	 */
	public int getSeverity() {
		return severity;
	}

	/**
	 * Sets the severity of the quarantine
	 * 
	 * @param severity
	 *            The severity
	 */
	public void setSeverity(int severity) {
		this.severity = severity;
	}


}
