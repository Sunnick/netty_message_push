package com.sunnick.client;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sunnick.encode.NettyMessageDecoder;
import com.sunnick.encode.NettyMessageEncoder;
import com.sunnick.entity.NettyConstant;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @author sunnick
 *
 *	客户端
 */
public class NettyClient {

	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	
	private EventLoopGroup group = new NioEventLoopGroup();
	
	private String host;
	private int port;
	
	/**
	 * @param host IP地址
	 * @param port 端口号
	 */
	public NettyClient(String host,int port) {
		this.host = host;
		this.port = port;
	}
	
	
	/**
	 * 建立连接
	 * @param port
	 * @param host
	 * @throws InterruptedException 
	 */
	public void connect() throws InterruptedException{
		try{
			//初始化客户端
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>(){
					@Override
					protected void initChannel(SocketChannel channel) throws Exception {
						channel.pipeline().addLast("MessageDecoder",new NettyMessageDecoder(
								NettyConstant.MAX_FRAME_LENGTH,NettyConstant.LENGTH_FIELD_OFFSET,NettyConstant.LENGTH_FIELD_LENGTH));
						channel.pipeline().addLast("MessageEncoder",new NettyMessageEncoder());
						channel.pipeline().addLast("ReadTimeoutHandler",new ReadTimeoutHandler(NettyConstant.TIMEOUT_SECONDS));
						channel.pipeline().addLast("LoginAuthReqHandler",new LoginAuthReqHandler());
						channel.pipeline().addLast("HeartBeatReqHandler",new HeartBeatReqHandler());
					}
				});
			
			//发起异步连接操作
			ChannelFuture future = b.connect(new InetSocketAddress(host,port)).sync();
			future.channel().closeFuture().sync();//等待连接断开，断开后才会执行后边的代码
		}finally{
			//定时发起重连
			executor.execute(new Runnable(){
				@Override
				public void run() {
					try {
						TimeUnit.SECONDS.sleep(5);
						connect();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

}
