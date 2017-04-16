package com.bclaus.rsps.server.login;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.login.Packet.Type;
import com.bclaus.rsps.server.rsa.ISAACRandomGen;

public class RS2Decoder extends ByteToMessageDecoder {

	private final ISAACRandomGen cipher;

	private int opcode = -1;
	private int size = -1;

	public RS2Decoder(ISAACRandomGen cipher) {
		this.cipher = cipher;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {

		if (opcode == -1) {
			if (buf.readableBytes() < 1) {
				return;
			}

			opcode = buf.readUnsignedByte();
			opcode = (opcode - cipher.getNextKey()) & 0xFF;
			size = Player.PACKET_LENGTHS[opcode];
		}

		if (size == -1) {
			if (buf.readableBytes() < 1) {
				return;
			}

			size = buf.readUnsignedByte();
		}

		if (buf.readableBytes() < size) {
			return;
		}

		try {
			byte[] data = new byte[size];
			buf.readBytes(data);
			ByteBuf payload = Unpooled.buffer(size);
			payload.writeBytes(data);
			out.add(new Packet(opcode, Type.FIXED, payload));
		} finally {
			opcode = size = -1;
		}
	}

}
