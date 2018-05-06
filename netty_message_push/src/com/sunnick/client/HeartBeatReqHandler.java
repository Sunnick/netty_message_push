package com.sunnick.client;

import java.util.concurrent.TimeUnit;

import com.sunnick.entity.Header;
import com.sunnick.entity.NettyConstant;
import com.sunnick.entity.NettyMessage;
import com.sunnick.entity.NettyMessageType;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * @author sunnick
 * 
 * 心跳检测包，不需要携带消息提，5s一个心跳
 *
 */
public class HeartBeatReqHandler extends ChannelHandlerAdapter{

	private volatile ScheduledFuture<?> heartBeat;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		//握手成功，主动发起心跳
		if(message.getHeader() != null && message.getHeader().getType() == NettyMessageType.LOGIN_RESP.value()){
			heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, NettyConstant.HEART_BEAT_MILLISECONDS, TimeUnit.MILLISECONDS);
		}
		//收到心跳包
		else if(message.getHeader() != null && message.getHeader().getType() == NettyMessageType.HEARTBEAT_RESP.value()){
			System.out.println("client receice a heart beat message:" + message);
		}else{
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if(heartBeat != null){
			heartBeat.cancel(true);
			heartBeat = null;
		}
		ctx.fireExceptionCaught(cause);
	}
	
	
	
	/**
	 * 构建心跳包
	 */
	private NettyMessage buildHeartBeat(){
		NettyMessage msg = new NettyMessage();
		Header header = new Header();
		header.setType(NettyMessageType.HEARTBEAT_REQ.value());
		msg.setHeader(header);
		return msg;
	}
	
	/**
	 * @author sunnick
	 *	心跳任务
	 */
	class HeartBeatTask implements Runnable{
		
		private final ChannelHandlerContext context;
		
		public HeartBeatTask( final ChannelHandlerContext ctx) {
			this.context = ctx;
		}

		@Override
		public void run() {
			NettyMessage beat = buildHeartBeat();
			System.out.println("client send a heartBeat:" + beat);
			context.writeAndFlush(beat);
		}
		
	}
	
	
}
