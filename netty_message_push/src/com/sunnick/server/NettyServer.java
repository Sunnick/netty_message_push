package com.sunnick.server;

import com.sunnick.encode.NettyMessageDecoder;
import com.sunnick.encode.NettyMessageEncoder;
import com.sunnick.entity.NettyConstant;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @author sunnick
 * 
 *         服务端
 *
 */
public class NettyServer {

	public void bind() throws InterruptedException {
			EventLoopGroup boss = new NioEventLoopGroup();
			EventLoopGroup worker = new NioEventLoopGroup();
			ServerBootstrap b = new ServerBootstrap();
			b.group(boss, worker).channel(NioServerSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel channel) throws Exception {
							channel.pipeline().addLast("NettyMessageDecoder",new NettyMessageDecoder(1024 * 1024, 0, 4));
							channel.pipeline().addLast("NettyMessageEncoder", new NettyMessageEncoder());
//							channel.pipeline().addLast("ReadTimeoutHandler", new ReadTimeoutHandler(50));
							channel.pipeline().addLast("LoginAuthRespHandler", new LoginAuthRespHandler());
							channel.pipeline().addLast("HeartBeatRespHandler", new HeartBeatRespHandler());
						}
					});

			b.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
			System.out.println("netty server start ok:" + NettyConstant.REMOTEIP + " - " + NettyConstant.PORT);
	}

}
