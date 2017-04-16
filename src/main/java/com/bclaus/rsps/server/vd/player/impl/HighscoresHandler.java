/**
 * 
 */
package com.bclaus.rsps.server.vd.player.impl;

/**
 * @author Tim http://rune-server.org/members/Someone
 *
 */
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.util.Connections;

public class HighscoresHandler extends Thread {

	public static final int REQUIRED_SESSION_EXPERIENCE = 100000;

	private Player c;
	private Connections con;// 184.172.154.184
	private long total_level, total_exp;

	public HighscoresHandler(Player c) {
		c.inProcess = true;
		this.con = new Connections("vdrspsco_hs");
		this.c = c;

	}

	private long getTotalLevel(Player player) {
		long totallevel = 0L;
		for (int i = 0; i <= 24; i++) {
			if (Player.getLevelForXP(player.playerXP[i]) > 99 && i != 24) {
				totallevel += 99;
			} else {
				totallevel += (double) Player.getLevelForXP(player.playerXP[i]);
			}
		}
		return totallevel;
	}

	private long getTotalXp(Player player) {
		long totalxp = 0L;
		for (int i = 0; i <= 24; i++) {
			totalxp += (double) player.playerXP[i];
		}
		return totalxp;
	}

	@Override
	public void run() {
		if (this.c.playerRights == 5) {
			System.out.println("here");
			return;
		}
		if (Server.disableMysql) {
			return;
		}
		try {
			//this.con.createConnection();
			this.total_level = this.getTotalLevel(this.c);
			this.total_exp = this.getTotalXp(this.c);
			final ResultSet rs = this.con.query("SELECT * FROM `hs` WHERE `username`='" + this.c.playerName + "'");
			int r = 1;
			while (rs.next() && r == 1) {
				for (int i = 0; i < 25; i++) {
					String lvl = "lvl_" + (i + 1);
					String xp = "xp_" + (i + 1);
					int level = Player.getLevelForXP(this.c.playerXP[i]);
					if (level > 99 && i != 24) {
						level = 99;
					}
					this.con.query("UPDATE hs SET " + lvl + "='" + level + "', " + xp + "='" + this.c.playerXP[i] + "' where username='" + this.c.playerName + "'");
				}
				this.con.query("UPDATE hs SET total_exp='" + this.total_exp + "', total_lvl='" + this.total_level + "' WHERE username='" + this.c.playerName + "'");
				this.con.query("UPDATE hs set prestige_rank = '" + this.c.prestigeLevel + "' WHERE username='" + this.c.playerName + "'");
				this.con.query("UPDATE hs set kills_rank = '" + this.c.playerKillCount + "' WHERE username='" + this.c.playerName + "'");
				this.con.query("UPDATE hs set deaths_rank = '" + this.c.playerDeathCount + "' WHERE username='" + this.c.playerName + "'");
				System.out.println("Highscores have been updated for " + this.c.playerName);
				r = 0;
				this.con.destroyConnection();
				this.c.inProcess = false;
				return;
			}
			final String name = this.c.playerName;
			String text = null;
			switch (this.c.gameMode) {
			case 0:
				text = "Sir";
				break;
			case 1:
				text = "Lord";
				break;
			case 2:
				text = "Legend";
				break;
			case 3:
				text = "Iron Man";
				break;
			}
			this.con.query("INSERT INTO `hs`(`username`, `titles`) VALUES('" + name + "', '" + text + "')");
			final HighscoresHandler hh = new HighscoresHandler(this.c);
			hh.start();
		} catch (SQLException e) {
			Server.disableMysql = true;
			e.printStackTrace();
		}
	}
}
