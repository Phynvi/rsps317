package com.bclaus.rsps.server.vd.player;

import com.bclaus.rsps.server.vd.npc.NPC;

public class Boundary {

	public static final Boundary KRACKEN = new Boundary(2314, 3692, 2339, 3715);
	public static final Boundary QUARANTINE = new Boundary(2441, 4760, 2481, 4795);
	public static final Boundary ZULRAH = new Boundary(3475, 3246, 3512, 3281);

	private int x, y, xOffset, yOffset;

	public Boundary(int x, int y, int xOffset, int yOffset) {
		this.x = x;
		this.y = y;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public static boolean isInBounds(NPC npc, Boundary boundary) {
		return npc.getX() >= boundary.x && npc.getX() <= boundary.xOffset && npc.getY() >= boundary.y && npc.getY() <= boundary.yOffset;
	}

	public static boolean isInBounds(Player player, Boundary boundary) {
		return player.getX() >= boundary.x && player.getX() <= boundary.xOffset && player.getY() >= boundary.y && player.getY() <= boundary.yOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getxOffset() {
		return xOffset;
	}

	public void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}

}
