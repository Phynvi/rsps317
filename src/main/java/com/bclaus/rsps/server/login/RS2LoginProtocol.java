package com.bclaus.rsps.server.login;

import com.bclaus.rsps.server.quarantine.QuarantineIO;
import com.bclaus.rsps.server.util.DisplayName;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.net.PlayerUpdating;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.List;

import com.bclaus.rsps.server.Connection;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.player.PlayerSave;
import com.bclaus.rsps.server.rsa.ISAACRandomGen;
import com.bclaus.rsps.server.util.Misc;

public class RS2LoginProtocol extends ByteToMessageDecoder {
	private static final BigInteger RSA_MODULUS = new BigInteger("90604448754591493352380844722864319002387763465541328346156778662925128412567020598050767153375399371383409750307095827175881402714812633294315418411607363292733387711977888645139125292315430845469919970953538281576547997217549417893904539695819220747782660749822485448723804075635206512225733120777765849431");

	private static final BigInteger RSA_EXPONENT = new BigInteger("72800702915422333162076733787956607326345991368722108265258858470954946626750793256828285510926578200065290966040883152467380850291580610291062386409765951070943517850487614006695102310220467450402122818740567164877623769263855837358775757746601758775440617883055878202947451326881349134312885231094398073921");

	private enum State {
		CONNECTED, LOGGING_IN
	}

	private State state = State.CONNECTED;

	private String readRS2String(ByteBuf in) {
		StringBuilder bldr = new StringBuilder();
		byte b;
		while (in.isReadable() && ((b = in.readByte()) != 10)) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}

	
	private static Player login(ChannelHandlerContext ctx, ISAACRandomGen inCipher, ISAACRandomGen outCipher, int version, int uid, String name, String pass, String identity, String macAddress, String clientRequest) {
		int returnCode = 2;
		name = name.trim();
		name = name.toLowerCase();
		pass = pass.toLowerCase();

		if (name.length() > 12) {
			returnCode = 8;
		}

		Player cl = new Player(ctx.channel());
		cl.playerName = name;
		cl.playerName2 = cl.playerName;
		cl.connectedFrom = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
		cl.setIdentity(identity);
		cl.playerPass = pass;
		cl.setInStreamDecryption(inCipher);
		cl.setOutStreamDecryption(outCipher);
		cl.outStream.packetEncryption = outCipher;
		cl.saveCharacter = false;
		cl.isActive = true;
		char first = name.charAt(0);
		cl.properName = Character.toUpperCase(first) + name.substring(1, name.length());
		cl.setMacAddress(macAddress);
		int quarantineSeverityLevel = QuarantineIO.getHighestSeverityLevel(cl.playerName, cl.getIdentity());
		if (QuarantineIO.contains(name, cl.getIdentity()) && quarantineSeverityLevel == 0) {
			cl.getQuarantine().setQuarantined(true);
			QuarantineIO.add(name,  cl.getIdentity(), quarantineSeverityLevel);
		}
		if (cl.getQuarantine().isQuarantined() && quarantineSeverityLevel == 1) {
			returnCode = 4;
		}
		if (Connection.isNamedBanned(cl.playerName)) {
			returnCode = 4;
		}
		if (PlayerUpdating.isPlayerOn(name)) {
			returnCode = 5;
		}
		if (PlayerUpdating.getPlayerCount() >= World.PLAYERS.capacity()) {
			returnCode = 7;
		}
		if (World.updateRunning) {
			returnCode = 14;
		}
		if (uid == 0 || uid == 99735086 || uid == 69) {
			returnCode = 18;
		}
		if (uid != 889057271) {
			returnCode = 18;
		}
		if (Connection.isIdBanned(cl.getIdentity())) {
			PlayerUpdating.sendMessageStaff(cl.playerName + " just tried to log in. This user is ID banned.");
			returnCode = 4;
		}
		if (version != Constants.CLIENT_VERSION) {
			returnCode = 18;
		}
		int count = 0;
		for (Player plr : World.PLAYERS) {
			if (plr == null)
				continue;
			if (plr.connectedFrom.equals(cl.connectedFrom)) {
				count++;
			}
		}
		
		if (returnCode == 2) {
			int load = PlayerSave.loadGame(cl, cl.playerName, cl.playerPass);
			if (clientRequest.equals("register")) {
				if (PlayerSave.playerExists(name)) {
					try {
						if (PlayerSave.passwordMatches(name, pass)) {
							returnCode = 22;
						} else {
							returnCode = 23;
						}
					} catch (IOException e) {
						returnCode = 23;
					}
				} else {
					returnCode = 22;
					cl.saveFile = true;
					PlayerSave.saveGame(cl);
				}
			}
			if (returnCode == 2) {
				if (load == 3 || DisplayName.displayExists(cl.playerName)) {
					returnCode = 3;
					cl.saveFile = false;
				} else {
					for (int i = 0; i < cl.playerEquipment.length; i++) {
						if (cl.playerEquipment[i] == 0) {
							cl.playerEquipment[i] = -1;
							cl.playerEquipmentN[i] = 0;
						}
					}
					if (!World.register(cl)) {
						returnCode = 7;
						cl.saveFile = false;
					} else {
						cl.saveFile = true;
					}
				}
			}
		}
		if (returnCode != 2) {
			sendReturnCode(ctx.channel(), returnCode);
			return null;
		}
		cl.saveCharacter = true;
		cl.packetType = -1;
		cl.packetSize = 0;
		PacketBuilder bldr = new PacketBuilder();
		bldr.put((byte) returnCode);
		bldr.put((byte) cl.playerRights);
		bldr.put((byte) 0);
		ctx.writeAndFlush(bldr.toPacket());
		
		/**
		 * This is modifying the player from this thread, and not the logic thread, which 'might' cause an issue.
		 */
		synchronized (World.LOCK) {
			cl.initialize();
			cl.initialized = true;
		}
		return cl;
	}

	public static void sendReturnCode(Channel channel, int code) {
		PacketBuilder bldr = new PacketBuilder();
		bldr.put((byte) code);
		ChannelFuture future = channel.writeAndFlush(bldr.toPacket());
		future.addListener(ChannelFutureListener.CLOSE);
	}

	private static void finish(ByteBuf buf) {
		if (buf.isReadable()) {
			buf.readBytes(buf.readableBytes());
		}
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
		switch (state) {
		case CONNECTED:
			if (buf.readableBytes() < 2) {
				finish(buf);
				ctx.close();
				return;
			}
			int request = buf.readUnsignedByte();
			if (request != 14) {
				System.out.println("Invalid login request: " + request);
				finish(buf);
				ctx.close();
				return;
			}
			buf.readUnsignedByte();
			ctx.channel().writeAndFlush(new PacketBuilder().putLong(0).put((byte) 0).putLong(new SecureRandom().nextLong()).toPacket());
			state = State.LOGGING_IN;
			break;
		case LOGGING_IN:
			if (buf.readableBytes() < 2) {
				finish(buf);
				ctx.close();
				return;
			}

			int loginType = buf.readUnsignedByte();
			if ((loginType != 16) && (loginType != 18)) {
				System.out.println("Invalid login type: " + loginType);
				finish(buf);
				ctx.close();
				return;
			}
			int blockLength = buf.readUnsignedByte();
			int encryptedBlockLength = blockLength - 40;
			if (buf.readableBytes() < blockLength) {
				finish(buf);
				ctx.close();
				return;
			}

			buf.readByte();

			buf.readShort();

			buf.readByte();

			for (int i = 0; i < 9; i++) {
				buf.readInt();
			}

			encryptedBlockLength--;
			if (encryptedBlockLength != (buf.readUnsignedByte())) {
				System.out.println("Encrypted size mismatch.");
				finish(buf);
				ctx.close();
				return;
			}
			byte[] encryptedBytes = new byte[encryptedBlockLength];
			buf.readBytes(encryptedBytes);
			ByteBuf encryptedBuffer = Unpooled.wrappedBuffer(new BigInteger(encryptedBytes).modPow(RSA_EXPONENT, RSA_MODULUS).toByteArray());

			int rsaOpcode = encryptedBuffer.readUnsignedByte();
			if (rsaOpcode != 10) {
				sendReturnCode(ctx.channel(), 24);
				ctx.close();
				return;
			}

			long clientHalf = encryptedBuffer.readLong();
			long serverHalf = encryptedBuffer.readLong();
			int[] isaacSeed = { (int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32), (int) serverHalf };
			ISAACRandomGen inCipher = new ISAACRandomGen(isaacSeed);
			for (int i = 0; i < isaacSeed.length; i++) {
				isaacSeed[i] += 50;
			}
			ISAACRandomGen outCipher = new ISAACRandomGen(isaacSeed);
			int uid = encryptedBuffer.readInt();
			int clientVersion = encryptedBuffer.readInt();
			String name = Misc.formatPlayerName(readRS2String(encryptedBuffer));
			String pass = readRS2String(encryptedBuffer);
			String identity = readRS2String(encryptedBuffer);
			String macAddress = readRS2String(encryptedBuffer);
			String clientRequest = readRS2String(encryptedBuffer);
			ctx.channel().pipeline().replace("decoder", "decoder", new RS2Decoder(inCipher));
			Player client = login(ctx, inCipher, outCipher, clientVersion, uid, name, pass, identity, macAddress, clientRequest);
			out.add(client == null ? encryptedBuffer : client);
		}
	}
}
