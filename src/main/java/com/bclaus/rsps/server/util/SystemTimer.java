package com.bclaus.rsps.server.util;

public class SystemTimer {

	private long time = System.currentTimeMillis();

	public SystemTimer headStart(long startAt) {
		time = System.currentTimeMillis() - startAt;
		return this;
	}

	public SystemTimer reset(long i) {
		time = i;
		return this;
	}

	public SystemTimer reset() {
		time = System.currentTimeMillis();
		return this;
	}

	public long elapsed() {
		return System.currentTimeMillis() - time;
	}

	public boolean elapsed(long time) {
		return elapsed() >= time;
	}
	
	public long getTime() {
		return time;
	}
	
	public SystemTimer() {
		time = 0;
	}
}