/**
 *
 * @author Antony
 */
package com.bclaus.rsps.server.vd.content.skills.impl.slayer;

import com.bclaus.rsps.server.vd.player.Player;

/**
 * [17:21:50] Dj: 1860 5245 - first floor 2040 5232 - second floor 2128 5288 -
 * third floor 2304 5200 - fourth floor
 */
public class StrongholdSecurity {

	static int objectData[][] = { { 1865, 5227 }, { 1865, 5226 }, { 1868, 5226 }, { 1868, 5227 }, { 1867, 5217 }, { 1867, 5218 }, { 1870, 5217 }, { 1870, 5218 }, { 1894, 5213 }, { 1894, 5212 }, { 1897, 5213 }, { 1897, 5212 }, { 1904, 5203 }, { 1904, 5204 }, { 1907, 5203 }, { 1907, 5204 }, { 1882, 5188 }, { 1882, 5189 }, { 1879, 5189 }, { 1879, 5188 }, { 1879, 5240 }, { 1879, 5239 }, { 1876, 5240 }, { 1876, 5239 }, { 1884, 5244 }, { 1884, 5243 }, { 1887, 5244 }, { 1887, 5243 }, { 1889, 5235 }, { 1889, 5236 }, { 1886, 5235 }, { 1886, 5236 }, { 1904, 5242 }, { 1904, 5243 }, { 1908, 5242 }, { 1908, 5243 
		}, 

	};

	static int[] objectDoors = {

	16123, 16124, 

	};

	public static boolean handlingSecurityDoors(Player c, int object) {
		for (int i = 0; i < objectDoors.length; i++) {
			if (object == objectDoors[i]) {
				return true;
			}
		}
		return false;
	}

	public static void handleObjects(Player player, int objectId, int obX, int obY, int face) {
		switch (objectId) {
		case 16123:
		case 16124:
		case 16066:
		case 16065:
			for (int i = 0; i < objectData.length; i++) {
				if (player.absX == objectData[i][0] && player.absY == objectData[i][1]) {
					player.getPA().walkTo(-1, 0);
					return;
				}
			}
			if (player.absX == 1890 && player.absY == 5208 || player.absX == 1889 && player.absY == 5208 || player.absX == 1876 && player.absY == 5195 || player.absX == 1877 && player.absY == 5195 || player.absX == 1876 && player.absY == 5192 || player.absX == 1877 && player.absY == 5192) {
				player.getPA().walkTo(0, -1);
				return;
			}
			if (player.absX == obX && player.absY == obY)
				player.getPA().walkTo(0, +1);
			if (player.absY == obY && player.absX < obX)
				player.getPA().walkTo(+1, 0);
			if (player.absY > obY && player.absX == obX)
				player.getPA().walkTo(0, -1);
			if (player.absY < obY && player.absX == obX)
				player.getPA().walkTo(0, 1);
			break;
		}
	}
}
