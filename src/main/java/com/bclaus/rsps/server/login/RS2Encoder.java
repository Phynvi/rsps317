package com.bclaus.rsps.server.login;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public final class RS2Encoder extends MessageToMessageEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> out) throws Exception {
		out.add(packet.getPayload());
	}

}
