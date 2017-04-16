package com.bclaus.rsps.server.vd.content.skills.impl;

import com.bclaus.rsps.server.Constants;

public enum Adze {
    LOG(1511, 40),
    OAK_LOG(1521, 60),
    WILLOW_LOG(1519, 90),
    MAPLE_LOG(1517, 135),
    YEW_LOG(1515, 202),
    MAGIC_LOG(1513, 303);

    private final int logid;
    private final int xp;
	
    private Adze(int logid, int xp) {
        this.logid = logid;
        this.xp = xp * Constants.FIREMAKING_EXPERIENCE;
    }
	
    public int getLogID() {
        return logid;
    }
	
    public String getName() { 
        return name().toLowerCase();
    }

    public int getXP() {
        return xp;
    }

    public static Adze forLog(int id) {
        for (Adze lc : Adze.values()) {
            if (lc.getLogID() == id) {
                return lc;
            }
        }
        return null;
    }
}
