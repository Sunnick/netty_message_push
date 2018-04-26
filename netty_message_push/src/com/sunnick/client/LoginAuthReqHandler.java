package com.sunnick.client;

import com.sunnick.entity.Header;
import com.sunnick.entity.NettyMessage;
import com.sunnick.entity.NettyMessageType;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author sunnick
 * 
 * 客户端channelHandler，用于在通道激活时发起握手请求
 *
 */
public class LoginAuthReqHandler extends ChannelHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		//如果是握手应答消息，则需要判断是否认证成功
		if(message.getHeader() != null && message.getHeader().getType() == NettyMessageType.LOGIN_RESP.value()){
			byte loginResult = (byte) message.getBody();
			if(loginResult != (byte)0){
				//握手失败，关闭连接
				ctx.close();
			}else{
				System.out.println("Login is ok:" + message);
				ctx.fireChannelRead(message);
			}
		}else{
			ctx.fireChannelRead(message);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(buildLoginReq());
	}
	
	/**
	 * 构建握手消息
	 */
	private NettyMessage buildLoginReq(){
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(NettyMessageType.LOGIN_REQ.value());
		message.setHeader(header);
		return message;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.fireExceptionCaught(cause);
	}
}
