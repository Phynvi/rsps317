package com.bclaus.rsps.server.vd.content.skills.impl.slayer;

import com.bclaus.rsps.server.vd.player.Player;

public class SocialSlayer {

	private Player client, partner;

	public SocialSlayer(Player client, Player partner) {
		this.client = client;
		this.partner = partner;
	}

	public Player getClient() {
		return client;
	}

	public Player getPartner() {
		return partner;
	}

	public void createSocialTask() {
		Slayer.appendSocialSlayerTask(client, partner);
		client.setSocialSlaying(true);
		client.setSocialSlayerKills(0);
		partner.setSocialSlaying(true);
		partner.setSocialSlayerKills(0);
	}

}
