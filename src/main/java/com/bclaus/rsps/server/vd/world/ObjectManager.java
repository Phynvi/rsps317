package com.bclaus.rsps.server.vd.world;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.player.Player;

/**
 * @author Sanity
 */

public class ObjectManager {

	public static List<Object> objects = new LinkedList<Object>();

	public static void process() {

		for (Iterator<Object> itr = objects.iterator(); itr.hasNext();) {

			Object o = itr.next();
			if (o != null) {
				if (o.tick > 0)
					o.tick--;
				if (o.tick == 0 && !o.remove) {
					o.tick = -1;
					if (o.getNewId() > 0) {
						updateObject(o);
						if (o.objectId == 734) {
							itr.remove();
						}
					}
				} else if (o.tick == 0)
					itr.remove();
			}
		}
	}

	public static Object objectExists(int x, int y, int height) {
		for (Object o : objects) {
			if (o.objectX == x && o.objectY == y && o.height == height) {
				return o;
			}
		}
		return null;
	}

	public static void removeObject(int x, int y) {
		for (int j = 0; j < World.PLAYERS.capacity(); j++) {
			if (World.PLAYERS.get(j) != null) {
				Player c = World.PLAYERS.get(j);
				c.getPA().object(-1, x, y, 0, 10);
			}
		}
	}

	public static void removeObject(Position p) {
		removeObject(p.getX(), p.getY());
	}

	public static void removeObjectDatabase(Position p) {
		for (Iterator<Object> itr = objects.iterator(); itr.hasNext();) {
			Object o = itr.next();

			if (o == null)
				continue;

			if (o.getPosition().equals(p)) {
				itr.remove();
			}
		}
		removeObject(p.getX(), p.getY());
	}

	/*
	 * public FarmingPatches getFarmingPatches() { return farmingPatches; }
	 */

	public static void updateObject(Object o) {
		for (int j = 0; j < World.PLAYERS.capacity(); j++) {
			if (World.PLAYERS.get(j) != null) {
				Player c = World.PLAYERS.get(j);
				c.getPA().object(o.newId, o.objectX, o.objectY, o.face, o.type);
			}
		}
	}

	public static void placeObject(Object o) {
		for (int j = 0; j < World.PLAYERS.capacity(); j++) {
			if (World.PLAYERS.get(j) != null) {
				Player c = World.PLAYERS.get(j);
				if (c.distanceToPoint(o.objectX, o.objectY) <= 60)
					c.getPA().object(o.objectId, o.objectX, o.objectY, o.face, o.type);
			}
		}
	}

	public static void placeObjectDatabase(Object o) {
		objects.add(o);
		placeObject(o);
	}

	public static Object getObject(int x, int y, int height) {
		for (Object o : objects) {
			if (o.objectX == x && o.objectY == y && o.height == height)
				return o;
		}
		return null;
	}

	public static void loadObjects(Player c) {
		if (c == null)
			return;
		for (Object o : objects) {
			if (loadForPlayer(o, c))
				c.getPA().object(o.objectId, o.objectX, o.objectY, o.face, o.type);
		}
		loadCustomSpawns(c);
		c.getFarming().updateObjects();
	}

	public static void loadCustomSpawns(Player c) {/*
		 * boss booth
		 */
		//c.getPA().object(24599, 3084, 3494, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2737, 3477, 0, 0);
		c.getPA().checkObjectSpawn(-1, 2737, 3476, 0, 0);
		c.getPA().checkObjectSpawn(-1, 2557, 3299, 0, 0);
		c.getPA().checkObjectSpawn(3273, 3660, 3525, 0, 10); // Skill Shop
		c.getPA().checkObjectSpawn(409, 3091, 3506, 0, 10);
		c.getPA().checkObjectSpawn(2858, 2389, 9675, 0, 10);
		c.getPA().checkObjectSpawn(356, 2386, 9675, 0, 10);
		c.getPA().checkObjectSpawn(356, 2386, 9676, 0, 10);
		c.getPA().checkObjectSpawn(356, 2388, 9676, 0, 10);
		c.getPA().checkObjectSpawn(356, 2387, 9674, 0, 10);
		c.getPA().checkObjectSpawn(356, 2390, 9673, 0, 10);
		c.getPA().checkObjectSpawn(356, 2391, 9673, 0, 10);
		c.getPA().checkObjectSpawn(356, 2392, 9674, 0, 10);
		c.getPA().checkObjectSpawn(356, 2391, 9680, 0, 10);
		c.getPA().checkObjectSpawn(356, 2389, 9679, 0, 10);
		c.getPA().checkObjectSpawn(356, 2390, 9679, 0, 10);
		c.getPA().checkObjectSpawn(356, 2388, 9678, 0, 10);
		c.getPA().checkObjectSpawn(356, 2390, 9675, 0, 10);
		c.getPA().checkObjectSpawn(5574, 3209, 3207, 0, 10);//Sack for cook's Assistant
		c.getPA().checkObjectSpawn(2213, 3051, 9760, 0, 10); // Depost mine.
																// (Bank)
		c.getPA().checkObjectSpawn(2213, 2332, 9816, 0, 10); // Depost mine.
																// (Donor)
		c.getPA().checkObjectSpawn(2213, 2333, 9816, 0, 10); // Depost mine.
																// (Donor
		c.getPA().checkObjectSpawn(409, 2335, 9816, 0, 10); // Depost mine.
															// (Bank)
		c.getPA().checkObjectSpawn(2213, 2889, 4845, 0, 10);
		c.getPA().checkObjectSpawn(2213, 3095, 3491, 0, 5);
		c.getPA().checkObjectSpawn(2213, 3095, 3489, 0, 5);
		c.getPA().checkObjectSpawn(2213, 3096, 3493, 0, 10);
		c.getPA().checkObjectSpawn(2213, 3098, 3493, 0, 10);
		//Zulrah
		c.getPA().checkObjectSpawn(306, 2275, 3126, 0, 10);
		c.getPA().checkObjectSpawn(2732, 2275, 3122, 0, 10);
		c.getPA().checkObjectSpawn(1988, 2274, 3120, 0, 10);
		c.getPA().checkObjectSpawn(718, 2279, 3119, 0, 10);
		c.getPA().checkObjectSpawn(363, 2278, 3120, 0, 10);
		c.getPA().checkObjectSpawn(366, 2279, 3120, 0, 10);
		c.getPA().checkObjectSpawn(366, 2280, 3120, 0, 10);
		c.getPA().checkObjectSpawn(360, 2280, 3121, 0, 10);
		
		
		c.getPA().checkObjectSpawn(2213, 2338, 9816, 0, 10); // Depost mine.
																// (Donor
		c.getPA().checkObjectSpawn(2213, 2339, 9816, 0, 10); // Depost mine.
																// (Donor
		
		c.getPA().checkObjectSpawn(-1, 2935, 3451, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2935, 3450, 0, 10);
		c.getPA().checkObjectSpawn(6552, 3096, 3506, 0, 10);
		c.getPA().checkObjectSpawn(1755, 3055, 9774, 0, 0);
		c.getPA().checkObjectSpawn(1596, 3008, 3850, 1, 0);
		c.getPA().checkObjectSpawn(1596, 3008, 3849, -1, 0);
		c.getPA().checkObjectSpawn(1596, 3040, 10307, -1, 0);
		c.getPA().checkObjectSpawn(1596, 3040, 10308, 1, 0);
		c.getPA().checkObjectSpawn(1596, 3022, 10311, -1, 0);
		c.getPA().checkObjectSpawn(1596, 3022, 10312, 1, 0);
		c.getPA().checkObjectSpawn(1596, 3044, 10341, -1, 0);
		c.getPA().checkObjectSpawn(1596, 3044, 10342, 1, 0);
		c.getPA().checkObjectSpawn(2213, 3286, 3372, 0, 10);
		c.getPA().checkObjectSpawn(2213, 3080, 9502, 1, 10);
		c.getPA().checkObjectSpawn(2475, 3233, 9312, 1, 10);
		c.getPA().checkObjectSpawn(4551, 2522, 3595, 1, 10);
		/** Stalls **/
		c.getPA().checkObjectSpawn(6163, 3049, 4967, 1, 10); // Stall 1 Edge
		c.getPA().checkObjectSpawn(6165, 3049, 4969, 1, 10);
		c.getPA().checkObjectSpawn(6166, 3049, 4971, 1, 10);
		c.getPA().checkObjectSpawn(6164, 3049, 4973, 1, 10);
		c.getPA().checkObjectSpawn(6162, 3049, 4975, 1, 10);
		c.getPA().checkObjectSpawn(4874, 3046, 4975, 1, 10);
		c.getPA().checkObjectSpawn(5162, 3043, 4977, 1, 10);
		/** End Stalls **/
		c.getPA().checkObjectSpawn(2213, 2926, 3173, 0, 10);// karmaja bank
		c.getPA().checkObjectSpawn(2213, 2855, 3441, 0, 10);// bank
		c.getPA().checkObjectSpawn(14859, 2327, 9819, 0, 10);
		c.getPA().checkObjectSpawn(14859, 2327, 9820, 0, 10);
		c.getPA().checkObjectSpawn(14859, 2327, 9821, 0, 10);
		c.getPA().checkObjectSpawn(14859, 2327, 9822, 0, 10);
		c.getPA().checkObjectSpawn(14859, 2327, 9823, 0, 10);
		c.getPA().checkObjectSpawn(14859, 2327, 9824, 0, 10);
		c.getPA().checkObjectSpawn(2105, 2327, 9816, 0, 10);
		c.getPA().checkObjectSpawn(2105, 2327, 9814, 0, 10);
		c.getPA().checkObjectSpawn(2213, 2924, 9917, 0, 10);
		c.getPA().checkObjectSpawn(2644, 2811, 3440, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2445, 3089, -1, 0);
		c.getPA().checkObjectSpawn(-1, 2445, 3090, -1, 0);
		// trees
		c.getPA().checkObjectSpawn(2213, 2821, 3460, 1, 10);// endTrees
		//IronZone
		c.getPA().checkObjectSpawn(1306, 2849, 2960, -1, 10);
		c.getPA().checkObjectSpawn(1306, 2855, 2959, -1, 10);
		// Catherby
		c.getPA().checkObjectSpawn(1276, 2840, 3439, -1, 10);
		c.getPA().checkObjectSpawn(1281, 2843, 3436, -1, 10);
		c.getPA().checkObjectSpawn(1307, 2844, 3440, -1, 10);
		c.getPA().checkObjectSpawn(1308, 2846, 3437, -1, 10);
		c.getPA().checkObjectSpawn(1309, 2841, 3443, -1, 10);
		c.getPA().checkObjectSpawn(1306, 2851, 3438, -1, 10);
		c.getPA().checkObjectSpawn(14859, 3044, 9735, 0, 10);
		c.getPA().checkObjectSpawn(3044, 2853, 3424, 3, 10);
		c.getPA().checkObjectSpawn(2783, 2851, 3425, 1, 10);
		c.getPA().checkObjectSpawn(-1, 2885, 4850, -1, 10);
		c.getPA().checkObjectSpawn(2467, 2885, 4849, -1, 10);
		c.getPA().checkObjectSpawn(172, 3083, 3506, 1, 10);
		c.getPA().checkObjectSpawn(2213, 3226, 3222, 1, 10);
		c.getPA().checkObjectSpawn(2213, 3084, 3514, 1, 10);
		c.getPA().checkObjectSpawn(24599, 2332, 9811, 1, 10);
		//construction
		c.getPA().checkObjectSpawn(4272, 3294, 3505, 0, 10);
		c.getPA().checkObjectSpawn(15931, 3312, 3514, 0, 10);
		if (c.heightLevel == 2)
			c.getPA().checkObjectSpawn(2466, 2873, 5323, 2, 10);
		else
			c.getPA().checkObjectSpawn(2466, 2873, 5323, 6, 10);
	}

	public static boolean loadForPlayer(Object o, Player c) {
		return !(o == null || c == null) && c.distanceToPoint(o.objectX, o.objectY) <= 60 && c.heightLevel == o.height;
	}

	public static void addObject(Object o) {
		if (getObject(o.objectX, o.objectY, o.height) == null) {
			objects.add(o);
			placeObject(o);
		}
	}

}