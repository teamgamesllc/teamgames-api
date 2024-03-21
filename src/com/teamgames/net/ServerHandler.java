package com.teamgames.net;

//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelFutureListener;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//import io.netty.util.ReferenceCountUtil;

/**
 * Handles a server-side channel.
 */

public class ServerHandler {
	
}

//public class ServerHandler extends ChannelInboundHandlerAdapter {
//
//	@Override
//	public void channelRead(ChannelHandlerContext ctx, Object msg) {
//		ByteBuf in = (ByteBuf) msg;
//		String message = "";
//		String remoteAddress = ctx.channel().remoteAddress().toString();
//		try {
//			if (!remoteAddress.startsWith("/127.0.0.1")) {
//				ctx.writeAndFlush("Not correct remote address! Connection closed")
//						.addListener(ChannelFutureListener.CLOSE);
//				ctx.close();
//				System.out.println("Closing connection");
//				return;
//			}
//			while (in.isReadable()) {
//				final char read = (char) in.readByte();
//				message = (message + read);
//				System.out.flush();
//			}
//		} finally {
//			ReferenceCountUtil.release(msg); 
//		}
//		System.out.println("message: " + message);
//		//PacketHandler.handle(id, message);
//	}
//
//	@Override
//	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { 
//		// Close the connection when an exception is raised.
//		cause.printStackTrace();
//		ctx.close();
//	}
//}