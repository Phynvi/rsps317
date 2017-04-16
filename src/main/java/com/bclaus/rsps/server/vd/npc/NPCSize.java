/**
 *
 * @author Antony
 */
package com.bclaus.rsps.server.vd.npc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NPCSize {

	public static void init() throws IOException {
		final BufferedReader reader = new BufferedReader(new FileReader("./Data/npc_sizes.txt"));
		String line = null;
		long startup = System.currentTimeMillis();

		while ((line = reader.readLine()) != null) {
			String[] args = line.split(": ");
			int npcId = Integer.valueOf(args[0]);
			int size = Integer.valueOf(args[1]);

			if (npcId >= NPC_SIZES.length) {
				System.out.println("Error while parsing npc size: [npcId, maxNpcId] - [" + npcId + ", " + (NPC_SIZES.length - 1) + "]");
				break;
			}
			NPC_SIZES[npcId] = size;
		}
		reader.close();
		System.out.println("Successfully loaded " + (NPC_SIZES.length - 1) + " npc sizes in " + (System.currentTimeMillis() - startup) + "ms");
	}

	public static int getSize(int npcId) {
		return npcId < NPC_SIZES.length ? NPC_SIZES[npcId] : 1;
	}

	private static final int[] NPC_SIZES = new int[6391];
}
