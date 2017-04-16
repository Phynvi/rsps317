package com.bclaus.rsps.server.vd.content.skills.impl.slayer;

import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.content.achievements.AchievementType;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.DialogueHandler;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.content.achievements.Achievements;

public class SocialSlayerData {

	public static boolean isClientAvailable(Player c, Player client) {
		if (client == null) {
			c.sendMessage("That player is currently offline, they cannot be requested.");
			return false;
		}
		if (c.getSocialSlayer() != null || c.isSocialSlaying()) {
			c.sendMessage("You currently have a co-op partner, if this isn't true, logout.");
			return false;
		}
		if (client.getSocialSlayer() != null || client.isSocialSlaying()) {
			c.sendMessage("The player you're inviting is already in a co-op session.");
			return false;
		}
		if (c.taskAmount > 0 || c.slayerTask > 0) {
			c.sendMessage("You already have a task you must finish.");
			return false;
		}
		if (client.taskAmount > 0 || client.slayerTask > 0) {
			c.sendMessage(client.playerName + " already has a task they must finish first.");
			return false;
		}
		return true;
	}

	public static void obtainSlayerInformation(Player c) {
		if (c.taskAmount > 0) {
			if (c.isSocialSlaying()) {
				String partner;
				if (c.getSocialSlayer() != null)
					partner = c.getSocialSlayer().getPartner().playerName;
				else
					partner = "none";
				c.dialogueAction = 502;
				c.getDH().sendOption5("@blu@Co-op Partner: " + partner, "@blu@Co-op Kills: " + c.getSocialSlayerKills(), "@blu@Task Name: " + NPCHandler.getNpcListName(c.slayerTask), "@blu@Amount: " + c.taskAmount, "Close");
			} else {
				c.getDH();
				DialogueHandler.sendStatement(c, "You currently have " + c.taskAmount + "x " + NPCHandler.getNpcListName(c.slayerTask) + " remaining as a task.");
				c.nextChat = 0;
			}
		} else {
			c.getDH();
			DialogueHandler.sendStatement(c, "You don't have a task to check, talk to a slayer master to get one.");
			c.nextChat = 0;
		}
	}

	public static void finishTask(Player c) {
		try {
			if (c.getSocialSlayer() != null) {
				if (c.getSocialSlayer().getPartner() != null) {
					c.getSocialSlayer().getPartner().sendMessage("You have finished your social slayer task and have been rewarded.");
					c.getSocialSlayer().getPartner().slaypoints += 10 + Misc.random(15);
					c.sendMessage("You have finished your social slayer task, and have been awarded Points each!.");
					c.slaypoints += 10 + Misc.random(15);
				}
				finalizeSlayer(c.getSocialSlayer().getPartner());
			}
			finalizeSlayer(c);
			Achievements.increase(c, AchievementType.SOCIAL_SLAYER, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void finalizeSlayer(Player c) {
		c.setSocialSlaying(false);
		c.setSocialSlayerKills(0);
		c.setSocialSlayerDisconnections(0);
		c.setLastSocialPlayer(null);
		c.slayerBossTask = false;
		c.slayerTask = -1;
		c.taskAmount = 0;
		if (c.getSocialSlayer() != null)
			c.setSocialSlayerNull();
	}

	public static void sendInviteInterface(Player send, Player receive) {
		try {
			send.sendMessage("<col=20>You have invited " + receive.playerName + " to start social slayer with you...</col>");
			receive.getDH().sendOption5("@blu@" + send.playerName + " @blu@is inviting you to start Social Slayer with them", "@red@Slayer Level: " + send.playerLevel[18] + "", "@red@Combat Level: " + send.combatLevel + "", "Would you like to join them?", "Would you like to do boss tasks?");
			receive.socialSlayerInviteClient = send;
			receive.teleAction = -1;
			receive.dialogueAction = 500;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
