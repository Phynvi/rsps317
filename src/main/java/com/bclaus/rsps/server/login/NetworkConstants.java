package com.bclaus.rsps.server.login;

import io.netty.util.AttributeKey;
import com.bclaus.rsps.server.vd.player.Player;

public final class NetworkConstants {

	public static final AttributeKey<Player> KEY = AttributeKey.valueOf("client.KEY");

	private NetworkConstants() {

	}
}
