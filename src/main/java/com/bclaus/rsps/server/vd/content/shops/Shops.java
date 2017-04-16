package com.bclaus.rsps.server.vd.content.shops;
/*package server.legacy.content.shops;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import server.legacy.World;
import server.legacy.player.Player;
import server.legacy.player.PlayerHandler;

import com.sun.security.ntlm.Client;


*//**
 * ShopScript shop system.
 * @author Rob/Bubletan
 *//*
public class Shops {

	private static final String PATH = "./Data/cfg/shops.txt";
	protected static final int MAX_STOCKS = 36;
	private static final int MILLIS_BETWEEN_RESTORATIONS = 7500;
			
	private static long lastRestoration = 0;
	private static Map<Integer, Shop> shops;

	public static void loadShops() {
		//System.out.println("Loading shops... ");
		String data = null;
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(PATH));
			data = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(encoded)).toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (data == null) {
			System.out.println("Error!");
			return;
		}
		shops = new HashMap<Integer, Shop>();
		String[] components = data.replaceAll("\\r\\n|\\r|\\n|\\t", "").split("\\}");
		for (int i = 0; i < components.length; i++) {
			String[] s = components[i].split("\\{");
			String[] main = s[0].split("\"|\'");
			shops.put(Integer.parseInt(main[0].replaceAll(" ", "")), new Shop(main[1],
					s.length > 1 ? s[1] : null));
		}
		//System.out.println("Successfully Loaded All Shops");
		//return;
	}
	
	public static void process() {
		long millis = System.currentTimeMillis();
		if (millis - lastRestoration > MILLIS_BETWEEN_RESTORATIONS) {
			for (Map.Entry<Integer, Shop> entry : shops.entrySet()) {
				if (entry.getValue().restore()) {
					int shop = entry.getKey();
					for (Player p : World.PLAYERS) {
						if (p != null && p.shopIndex == shop) {
							ShopExecutor.update((Player)p, Shops.get(shop));
						}
					}
				}
			}
			lastRestoration = millis;
		}
	}
	
	public static Shop get(int index) {
		return shops.get(index);
	}
}*/