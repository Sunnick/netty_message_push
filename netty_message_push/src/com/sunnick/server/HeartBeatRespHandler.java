package com.sunnick.server;

import com.sunnick.entity.Header;
import com.sunnick.entity.NettyMessage;
import com.sunnick.entity.NettyMessageType;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class HeartBeatRespHandler extends ChannelHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		//心跳请求包
		if(message.getHeader() != null && message.getHeader().getType() == NettyMessageType.HEARTBEAT_REQ.value()){
			System.out.println("receive a heart beat:" + message);
			NettyMessage resp = buildHeartBeat();
			System.out.println("send heart beat response to client:" + resp);
			ctx.writeAndFlush(resp);
		}else{
			ctx.fireChannelRead(msg);
		}
	}


	
	/**
	 * 构建心跳应答包
	 */
	private NettyMessage buildHeartBeat(){
		NettyMessage msg = new NettyMessage();
		Header header = new Header();
		header.setType(NettyMessageType.HEARTBEAT_RESP.value());
		msg.setHeader(header);
		return msg;
	}



	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		super.exceptionCaught(ctx, cause);
	}
	
	
	

}
