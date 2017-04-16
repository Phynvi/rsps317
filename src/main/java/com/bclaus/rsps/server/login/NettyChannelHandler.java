package com.bclaus.rsps.server.login;

import com.bclaus.rsps.server.vd.player.Player;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

@Sharable
public class NettyChannelHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
		if (!isIgnored(e)) {
			e.getCause().printStackTrace();
		}
		safelyClose(ctx.channel());
	}

	private void safelyClose(Channel channel) {
		if (channel.isOpen()) {
			channel.close();
		}
	}

	private boolean isIgnored(Throwable e) {
		String msg = e.getMessage();

		if (msg == null) {
			return false;
		}

		if (msg.equals("An existing connection was forcibly closed by the remote host")) {
			return true;
		} else if (msg.equals("An established connection was aborted by the software in your host machine")) {
			return true;
		}

		return false;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			if (msg instanceof Player) {
				ctx.attr(NetworkConstants.KEY).set((Player) msg);
			} else if (msg instanceof Packet) {
				Player client = ctx.attr(NetworkConstants.KEY).get();
				if (client != null) {
					client.queueMessage((Packet) msg);
				}
			}
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Player client = ctx.attr(NetworkConstants.KEY).getAndRemove();
		if (client == null) {
			return;
		}
		if (client != null) {
			client.disconnected = true;
		}
	}

}
