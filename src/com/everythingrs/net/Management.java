package com.everythingrs.net;

public class Management {
	
}

//package com.everythingrs.net;
//
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//
//import io.netty.bootstrap.ServerBootstrap;
//
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//
///**
// * This class will be used for our future console api (the ability to do
// * commands directly from a web panel)
// * 
// * @author Nelson
// *
// */
//
//public class Management {
//
//	public static ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
//
//	private int port;
//
//	/**
//	 * Constructor
//	 * 
//	 * @param port
//	 */
//
//	public Management(int port) {
//		this.port = port;
//	}
//
//	/**
//	 * Runs our netty server
//	 * 
//	 * @throws Exception
//	 */
//
//	public void run() throws Exception {
//		EventLoopGroup bossGroup = new NioEventLoopGroup();
//		EventLoopGroup workerGroup = new NioEventLoopGroup();
//		try {
//			ServerBootstrap b = new ServerBootstrap();
//			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
//					.childHandler(new ChannelInitializer<SocketChannel>() {
//						@Override
//						public void initChannel(SocketChannel ch) throws Exception {
//							ch.pipeline().addLast(new ServerHandler());
//						}
//					}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
//
//			// Bind and start to accept incoming connections.
//			ChannelFuture f = b.bind(port).sync();
//
//			System.out.println("Successfully binded to port: " + port);
//			// Wait until the server socket is closed.
//			// In this example, this does not happen, but you can do that to gracefully
//			// shut down your server.
//			f.channel().closeFuture().sync();
//		} finally {
//			workerGroup.shutdownGracefully();
//			bossGroup.shutdownGracefully();
//		}
//	}
//
//	/**
//	 * Our main method. Initializes our netty server which will bind to port 43599
//	 * 
//	 * @param args
//	 * @throws Exception
//	 */
//
//	public static void main(String[] args) throws Exception {
//		final int port = 43599;
//		new Management(port).run();
//	}
//
//	/**
//	 * Initializes our netty server which will bind to port 43599
//	 */
//
//	public void init() {
//		try {
//			new Management(43599).run();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}