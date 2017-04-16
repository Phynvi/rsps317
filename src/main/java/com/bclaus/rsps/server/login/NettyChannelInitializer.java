package com.bclaus.rsps.server.login;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public final class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		
		ch.pipeline().addLast("encoder", new RS2Encoder()).addLast("decoder", new RS2LoginProtocol()).addLast("handler", new NettyChannelHandler());
	}

}
