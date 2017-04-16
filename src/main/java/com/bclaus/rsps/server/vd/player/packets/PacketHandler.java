package com.bclaus.rsps.server.vd.player.packets;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.clan.InputDialogueStringPacketHandler;
import com.bclaus.rsps.server.vd.clan.InterfaceAction;
import com.bclaus.rsps.server.vd.clan.ReceiveString;
import com.bclaus.rsps.server.vd.content.PrivateMessaging;
import com.bclaus.rsps.server.vd.player.Commands;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.actions.Bank10;
import com.bclaus.rsps.server.vd.player.actions.Bank5;
import com.bclaus.rsps.server.vd.player.actions.BankAll;
import com.bclaus.rsps.server.vd.player.actions.BankAllButOne;
import com.bclaus.rsps.server.vd.player.actions.BankModifiableX;
import com.bclaus.rsps.server.vd.player.actions.BankX1;
import com.bclaus.rsps.server.vd.player.actions.ClickItem;
import com.bclaus.rsps.server.vd.player.actions.ClickNPC;
import com.bclaus.rsps.server.vd.player.actions.ClickObject;
import com.bclaus.rsps.server.vd.player.actions.ClickingButtons;
import com.bclaus.rsps.server.vd.player.actions.ClickingInGame;
import com.bclaus.rsps.server.vd.player.actions.ClickingStuff;
import com.bclaus.rsps.server.vd.player.actions.InputDialogueIntegerPacketHandler;
import com.bclaus.rsps.server.vd.player.actions.ItemClick2;
import com.bclaus.rsps.server.vd.player.actions.ItemClick3;
import com.bclaus.rsps.server.vd.player.actions.ItemOnGroundItem;
import com.bclaus.rsps.server.vd.player.actions.ItemOnItem;
import com.bclaus.rsps.server.vd.player.actions.ItemOnNpc;
import com.bclaus.rsps.server.vd.player.actions.ItemOnObject;
import com.bclaus.rsps.server.vd.player.actions.ItemOnPlayer;
import com.bclaus.rsps.server.vd.player.actions.MagicOnItems;
import com.bclaus.rsps.server.vd.player.actions.SecondGroundOption;
import com.bclaus.rsps.server.vd.player.packets.impl.AttackPlayer;
import com.bclaus.rsps.server.vd.player.packets.impl.ChallengePlayer;
import com.bclaus.rsps.server.vd.player.packets.impl.ChangeAppearance;
import com.bclaus.rsps.server.vd.player.packets.impl.ChangeRegions;
import com.bclaus.rsps.server.vd.player.packets.impl.Chat;
import com.bclaus.rsps.server.vd.player.packets.impl.Dialogue;
import com.bclaus.rsps.server.vd.player.packets.impl.DropItem;
import com.bclaus.rsps.server.vd.player.packets.impl.FollowPlayer;
import com.bclaus.rsps.server.vd.player.packets.impl.MoveItems;
import com.bclaus.rsps.server.vd.player.packets.impl.PickupItem;
import com.bclaus.rsps.server.vd.player.packets.impl.RemoveItem;
import com.bclaus.rsps.server.vd.player.packets.impl.SilentPacket;
import com.bclaus.rsps.server.vd.player.packets.impl.Trade;
import com.bclaus.rsps.server.vd.player.packets.impl.Walking;
import com.bclaus.rsps.server.vd.player.packets.impl.WearItem;

public class PacketHandler {

	private static PacketType packetId[] = new PacketType[256];

	static {

		SilentPacket u = new SilentPacket();
		packetId[3] = u;
		packetId[202] = u;
		packetId[77] = u;
		packetId[86] = u;
		packetId[78] = u;
		packetId[36] = u;
		packetId[228] = u;
		packetId[226] = u;
		packetId[246] = u;
		packetId[218] = u;
		packetId[148] = u;
		packetId[183] = u;
		packetId[230] = u;
		packetId[136] = u;
		packetId[189] = u;
		packetId[152] = u;
		packetId[200] = u;
		packetId[85] = u;
		packetId[165] = u;
		packetId[238] = u;
		packetId[234] = u;
		packetId[150] = u;
		packetId[253] = new SecondGroundOption();
		packetId[14] = new ItemOnPlayer();
		packetId[40] = new Dialogue();
		ClickObject co = new ClickObject();
		packetId[132] = co;
		packetId[252] = co;
		packetId[70] = co;
		packetId[57] = new ItemOnNpc();
		ClickNPC cn = new ClickNPC();
		packetId[72] = cn;
		packetId[131] = cn;
		packetId[155] = cn;
		packetId[17] = cn;
		packetId[21] = cn;
		packetId[16] = new ItemClick2();
		packetId[75] = new ItemClick3();
		packetId[122] = new ClickItem();
		packetId[241] = new ClickingInGame();
		packetId[4] = new Chat();
		packetId[236] = new PickupItem();
		packetId[87] = new DropItem();
		packetId[185] = new ClickingButtons();
		packetId[130] = new ClickingStuff();
		packetId[103] = new Commands();
		packetId[214] = new MoveItems();
		packetId[237] = new MagicOnItems();
		AttackPlayer ap = new AttackPlayer();
		packetId[73] = ap;
		packetId[249] = ap;
		packetId[128] = new ChallengePlayer();
		packetId[39] = new Trade();
		packetId[140] = new BankAllButOne();
		packetId[141] = new BankModifiableX();
		packetId[139] = new FollowPlayer();
		packetId[41] = new WearItem();
		packetId[145] = new RemoveItem();
		packetId[117] = new Bank5();
		packetId[43] = new Bank10();
		packetId[129] = new BankAll();
		packetId[101] = new ChangeAppearance();
		final PrivateMessaging pm = new PrivateMessaging();
		packetId[188] = pm;
		packetId[126] = pm;
		packetId[215] = pm;
		packetId[59] = pm;
		packetId[95] = pm;
		packetId[133] = pm;
		packetId[74] = pm;
		packetId[135] = new BankX1();
		packetId[208] = new InputDialogueIntegerPacketHandler();
		Walking w = new Walking();
		packetId[98] = w;
		packetId[164] = w;
		packetId[248] = w;
		packetId[53] = new ItemOnItem();
		packetId[192] = new ItemOnObject();
		packetId[25] = new ItemOnGroundItem();
		ChangeRegions cr = new ChangeRegions();
		packetId[121] = cr;
		packetId[210] = cr;
		packetId[60] = new InputDialogueStringPacketHandler();
		packetId[127] = new ReceiveString();
		packetId[213] = new InterfaceAction();
		// packetId[127] = cr;
	}

	public static void processPacket(Player c, int packetType, int packetSize) {
        PacketType p = packetId[packetType];
        if(p != null && packetType > 0 && packetType < 257 && packetType == c.packetType && packetSize == c.packetSize) {
            if (Constants.sendServerPackets && c.playerRights == 3) {
                c.sendMessage("PacketType: " + packetType + ". PacketSize: " + packetSize + ".");
            }
            try {
                p.processPacket(c, packetType, packetSize);
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            c.disconnected = true;
            System.out.println(c.playerName + "is sending INVALID PacketType: " + packetType + ". PacketSize: " + packetSize);
        }
    }
}
