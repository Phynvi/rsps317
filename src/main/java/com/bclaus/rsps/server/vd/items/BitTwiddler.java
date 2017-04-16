/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bclaus.rsps.server.vd.items;

/**
 *
 * @author Tim/Someone
 */
public class BitTwiddler {
	private int value;

	public BitTwiddler(int value) {
		this.value = value;
	}

	public BitTwiddler() {
	}

	public void setBit(int id, boolean flag) {
		// will cause the bit to flip
		if (flag == bitSet(id))
			return;
		if (flag)
			value |= (int) Math.pow(2, (id - 1));
		else
			value ^= (int) Math.pow(2, (id - 1));
	}

	public boolean bitSet(int id) {
		return (value & (int) Math.pow(2, (id - 1))) != 0;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
