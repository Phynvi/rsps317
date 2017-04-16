package com.bclaus.rsps.server.vd.player.net;

import java.util.Iterator;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.rsa.ByteBuffer;
import com.bclaus.rsps.server.util.Misc;

/**
 * Handles updating all of the npcs for a single {@link Player}
 * 
 * @author Mobster
 *
 */
public class NpcUpdating {

	/**
	 * Updates local npcs for a player
	 * 
	 * @param plr
	 *            The {@link Player} to update npcs for
	 * @param str
	 *            The {@link ByteBuffer} to write data too
	 */
	public static void updateNPC(Player plr, ByteBuffer str) {
		PlayerUpdating.updateBlock.currentOffset = 0;

		str.createFrameVarSizeWord(65);
		str.initBitAccess();

		str.writeBits(8, plr.localNpcs.size());
		Iterator<NPC> $it = plr.localNpcs.iterator();
		while ($it.hasNext()) {
			NPC npc = $it.next();
			if (plr.withinDistance(npc)) {
				updateNPCMovement(npc, str);
				appendNPCUpdateBlock(npc, PlayerUpdating.updateBlock);
			} else {
				str.writeBits(1, 1);
				str.writeBits(2, 3);
				$it.remove();
			}
		}

		for (NPC npc : NPCHandler.NPCS) {
			if (npc != null) {
				if (!plr.withinDistance(npc)) {
					continue;
				}
				if (plr.localNpcs.add(npc)) {
					addNewNPC(plr, npc, str, PlayerUpdating.updateBlock);
				}
			}
		}

		if (PlayerUpdating.updateBlock.currentOffset > 0) {
			str.writeBits(14, 16383);
			str.finishBitAccess();
			str.writeBytes(PlayerUpdating.updateBlock.buffer, PlayerUpdating.updateBlock.currentOffset, 0);
		} else {
			str.finishBitAccess();
		}
		str.endFrameVarSizeWord();
	}

	/**
	 * Appends an npcs update block
	 * 
	 * @param npc
	 *            The {@link NPC} we are updating the block for
	 * @param str
	 *            The {@link ByteBuffer} to write the data on
	 */
	private static void appendNPCUpdateBlock(NPC npc, ByteBuffer str) {
		if (!npc.updateRequired)
			return;
		int updateMask = 0;
		if (npc.animUpdateRequired)
			updateMask |= 0x10;
		if (npc.hitUpdateRequired2)
			updateMask |= 0x8;
		if (npc.isMask80update())
			updateMask |= 0x80;
		if (npc.dirUpdateRequired)
			updateMask |= 0x20;
		if (npc.forcedChatRequired)
			updateMask |= 0x1;
		if (npc.hitUpdateRequired)
			updateMask |= 0x40;
		if (npc.transformUpdateRequired)
			updateMask |= 2;
		if (npc.FocusPointX != -1)
			updateMask |= 0x4;

		str.writeByte(updateMask);

		if (npc.animUpdateRequired)
			appendAnimUpdate(npc, str);
		if (npc.hitUpdateRequired2)
			appendHitUpdate2(npc, str);
		if (npc.isMask80update())
			appendMask80Update(npc, str);
		if (npc.dirUpdateRequired)
			appendFaceEntity(npc, str);
		if (npc.forcedChatRequired) {
			str.writeString(npc.getForcedText());
		}
		if (npc.hitUpdateRequired)
			appendHitUpdate(npc, str);
		if (npc.transformUpdateRequired)
			appendTransformUpdate(npc, str);
		if (npc.FocusPointX != -1)
			appendSetFocusDestination(npc, str);

	}

	/**
	 * Updates an npcs movement for a player
	 * 
	 * @param str
	 *            The {@link ByteBuffer} to write data on
	 */
	private static void updateNPCMovement(NPC npc, ByteBuffer str) {
		if (npc.direction == -1) {

			if (npc.updateRequired) {

				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
		} else {

			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[npc.direction]);
			if (npc.updateRequired) {
				str.writeBits(1, 1);
			} else {
				str.writeBits(1, 0);
			}
		}
	}

	private static void addNewNPC(Player player, NPC npc, ByteBuffer str, ByteBuffer updateBlock) {
		str.writeBits(14, npc.getIndex());
		int z = npc.getAbsY() - player.getAbsY();
		if (z < 0) {
			z += 32;
		}
		str.writeBits(5, z);
		z = npc.getAbsX() - player.getAbsX();
		if (z < 0) {
			z += 32;
		}
		str.writeBits(5, z);

		str.writeBits(1, 0);
		str.writeBits(14, npc.npcType);

		boolean savedUpdateRequired = npc.updateRequired;
		npc.updateRequired = true;
		appendNPCUpdateBlock(npc, updateBlock);
		npc.updateRequired = savedUpdateRequired;
		str.writeBits(1, 1);
	}

	private static void appendHitUpdate(NPC npc, ByteBuffer str) {
		if (npc.HP <= 0) {
			npc.isDead = true;
		}

		str.writeByteC(npc.primary.getDamage());
		str.writeByteS(npc.primary.getType().getId());
		str.writeByteS(Misc.getCurrentHP(npc.HP, npc.MaxHP, 100));
		str.writeByteC(100);
	}

	private static void appendFaceEntity(NPC npc, ByteBuffer str) {
		str.writeWord(npc.face);
	}

	private static void appendSetFocusDestination(NPC npc, ByteBuffer str) {
		str.writeWordBigEndian(npc.FocusPointX * 2 + 1);
		str.writeWordBigEndian(npc.FocusPointY * 2 + 1);
	}

	private static void appendAnimUpdate(NPC npc, ByteBuffer str) {
		str.writeWordBigEndian(npc.animNumber);
		str.writeByte(1);
	}

	private static void appendMask80Update(NPC npc, ByteBuffer str) {
		str.writeWord(npc.mask80var1);
		str.writeDWord(npc.mask80var2);
	}

	private static void appendHitUpdate2(NPC npc, ByteBuffer str) {
		if (npc.HP <= 0) {
			npc.isDead = true;
		}
		str.writeByteA(npc.secondary.getDamage());
		str.writeByteC(npc.secondary.getType().getId());
		str.writeByteA(Misc.getCurrentHP(npc.HP, npc.MaxHP, 100));
		str.writeByte(100);
	}

	private static void appendTransformUpdate(NPC npc, ByteBuffer str) {
		str.writeWordBigEndianA(npc.transformId);
	}

}
