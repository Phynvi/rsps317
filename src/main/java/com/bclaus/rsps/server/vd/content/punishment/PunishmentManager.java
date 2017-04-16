package com.bclaus.rsps.server.vd.content.punishment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.net.PlayerUpdating;
import com.bclaus.rsps.server.util.NameUtils;

/*
 * @Author Tim http://rune-server.org/members/Someone
 */

public class PunishmentManager {

	private static final Logger logger = Logger.getLogger(PunishmentManager.class.getName());

	public enum PunishmentType {
		MUTE, BAN, IPBAN
	}

	public static void punish(PunishmentType type, String name, String host, String by, String reason, Date duration, boolean perm, Player player) {
		Calendar c = Calendar.getInstance();
		String folder = new StringBuilder().append(type.toString().toLowerCase()).append("s").toString();
		File file = new File("./data/punishment/" + folder + "/" + name + ".report");
		String punishment = "";
		switch (type) {
		case MUTE:
			punishment = "muted";
			break;
		case BAN:
			punishment = "banned";
			break;
		case IPBAN:
			punishment = "ip-banned";
			break;
		}
		if (file.exists()) {
			if (player != null)
				player.sendMessage("[ Server ] " + name + " is already " + punishment + ".");
			return;
		}
		PunishmentReport report = null;
		if (type != PunishmentType.IPBAN)
			report = new PunishmentReport(by, reason, c.getTime(), duration, perm);
		else
			report = new PunishmentReport(host, by, reason, c.getTime(), duration, perm);
		if (report.write(type == PunishmentType.IPBAN ? host : name, folder)) {
			if (player != null) {
				player.sendMessage("You have successfully " + punishment + " " + name + ", " + (report.isPermPunishment() ? "Duration: Forever," : "For " + report.getTimeToString() + ","));
				player.sendMessage("Reason: " + reason);
			}
		} else {
			player.sendMessage("Unable to " + type.toString().toLowerCase() + " " + name + ".");
		}
		Player target = PlayerUpdating.getPlayerByName(name);
		if (target != null) {
			target.sendMessage("You have been " + punishment + " by " + NameUtils.formatName(player.playerName) + ",");
			target.sendMessage("Reason: " + reason + ".");
			if (type == PunishmentType.MUTE)
				target.setMuteReport(report);
			else
				target.logout();
		}
	}

	public static void liftPunishment(String name, String folder, Player player) {
		File file = new File("./data/punishment/" + folder + "/" + name + ".report");
		if (!file.exists()) {
			if (player != null) {
				player.sendMessage("Unable to lift punishment for " + name + ".");
			}
		} else {
			boolean success = file.delete();
			if (success) {
				player.sendMessage("You have successfully lifted " + name + "'s punishment.");
			}
			Player target = PlayerUpdating.getPlayerByName(name);
			if (target != null) {
				if (folder.equals("mutes")) {
					target.setMuteReport(null);
					target.sendMessage("You have been unmuted.");
				}
			}
		}
	}

	public static class PunishmentReport {

		private boolean reportExists;
		private String host = "";
		private String by = "";
		private String reason = "";
		private Date datePunished;
		private Date punishmentDuration;
		private boolean permPunishment;

		public PunishmentReport() {
		}

		public PunishmentReport(String by, String reason, Date datePunished, Date punishmentDuration, boolean permPunishment) {
			this.by = by;
			this.reason = reason;
			this.datePunished = datePunished;
			this.punishmentDuration = punishmentDuration;
			this.permPunishment = permPunishment;
			this.reportExists = true;
		}

		public PunishmentReport(String host, String by, String reason, Date datePunished, Date punishmentDuration, boolean permPunishment) {
			this.host = host;
			this.by = by;
			this.reason = reason;
			this.datePunished = datePunished;
			this.punishmentDuration = punishmentDuration;
			this.permPunishment = permPunishment;
			this.reportExists = true;
		}

		public void check(String name, String path) {
				try {
					
					File file = new File("./data/punishment/" + path + "/" + name + ".report");
					if (!file.exists()) {
						return;
					}
					FileInputStream fis = new FileInputStream(new File("./data/punishment/" + path + "/" + name + ".report"));
					DataInputStream dis = new DataInputStream(fis);
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
					try {
						try {
							reportExists = true;
							boolean hasHost = dis.readBoolean();
							if (hasHost) {
								host = dis.readUTF();
							}
							by = dis.readUTF();
							reason = dis.readUTF();
							try {
								datePunished = sdf.parse(dis.readUTF());
								punishmentDuration = sdf.parse(dis.readUTF());
							} catch (ParseException e) {
								e.printStackTrace();
							}
							permPunishment = dis.readBoolean();
						} catch (IOException e) {
							logger.log(Level.WARNING, "Error reading punishment report for " + name + ".", e);
						}
					} finally {
						try {
							fis.close();
							dis.close();
						} catch (IOException e) {
							logger.log(Level.WARNING, "Error reading punishment report for " + name + ".", e);
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
		}

		public boolean write(String string, String path) {
				try {
					FileOutputStream fos = new FileOutputStream(new File("./data/punishment/" + path + "/" + string + ".report"));
					DataOutputStream dos = new DataOutputStream(fos);
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
					try {
						try {
							dos.writeBoolean(host == "" ? false : true);
							if (host != "") {
								dos.writeUTF(host);
							}
							dos.writeUTF(by);
							dos.writeUTF(reason);
							dos.writeUTF(sdf.format(datePunished));
							dos.writeUTF(sdf.format(punishmentDuration));
							dos.writeBoolean(permPunishment);
							return true;
						} catch (IOException e) {
							logger.log(Level.WARNING, "Error writing punishment report for " + string + ".", e);
							return false;
						}
					} finally {
						try {
							dos.close();
							fos.close();
						} catch (IOException e) {
							logger.log(Level.WARNING, "Error writing punishment report for " + string + ".", e);
							return false;
						}
					}
				} catch (FileNotFoundException e) {
					logger.log(Level.WARNING, "Error writing punishment report for " + string + ".", e);
					return false;
				}
		}

		public String getHost() {
			return host;
		}

		public String getBy() {
			return by;
		}

		public String getReason() {
			return reason;
		}

		public int getSecondsTillLift() {
			Calendar punishedDuration = Calendar.getInstance();
			Calendar today = Calendar.getInstance();
			punishedDuration.setTime(this.punishmentDuration);
			long delta = punishedDuration.getTimeInMillis() - today.getTimeInMillis();
			long seconds = delta / 1000;
			return (int) seconds;
		}

		public long getMilsTillLift() {
			Calendar punishedDuration = Calendar.getInstance();
			Calendar today = Calendar.getInstance();
			punishedDuration.setTime(this.punishmentDuration);
			return punishedDuration.getTimeInMillis() - today.getTimeInMillis();
		}

		public String getTimeToString() {
			String date = "";
			Calendar punishedDuration = Calendar.getInstance();
			Calendar today = Calendar.getInstance();
			punishedDuration.setTime(this.punishmentDuration);
			long delta = punishedDuration.getTimeInMillis() - today.getTimeInMillis();
			long seconds = delta / 1000;
			long minutes = seconds / 60;
			long hours = minutes / 60;
			long days = hours / 24;
			String hoursLeft = (((days * 24) - hours) + "").replace("-", "");
			String minutesLeft = (((hours * 60) - minutes) + "").replace("-", "");
			String secondsLeft = (((minutes * 60) - seconds) + "").replace("-", "");
			date = "" + (days >= 1 ? days + " day" + (days > 1 ? "s" : "") + ", " : "") + "" + (Integer.parseInt(hoursLeft) >= 1 ? hoursLeft + " hours, " : "") + "" + (Integer.parseInt(minutesLeft) >= 1 ? minutesLeft + " minutes" : "") + "" + (Integer.parseInt(secondsLeft) >= 1 ? ", " + secondsLeft + " seconds." : ".");
			return date;
		}

		public String getExpireMessage() {

			if (permPunishment)
				return "Your mute will never expire, apply on forums.";
			return "Your mute will expire in " + getTimeToString();
		}

		public boolean isActive() {
			return permPunishment ? true : getSecondsTillLift() > 0;
		}

		public boolean exists() {
			return reportExists;
		}

		public Date getDatePunished() {
			return datePunished;
		}

		public boolean isPermPunishment() {
			return permPunishment;
		}

	}

}