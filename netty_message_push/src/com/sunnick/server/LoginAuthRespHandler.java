package com.sunnick.server;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sunnick.entity.Header;
import com.sunnick.entity.NettyMessage;
import com.sunnick.entity.NettyMessageType;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author sunnick
 * 
 * 服务器处理握手请求
 * 
 * 基于ip白名单，判断是否可以接入,在白名单内的ip可以接入
 *
 */
public class LoginAuthRespHandler extends ChannelHandlerAdapter{
	
	/**
	 * 白名单列表
	 */
	private String[] whiteList = {"127.0.0.1"};
	
	/**
	 * 已登录列表，防止重复登录
	 */
	private Map<String,Boolean> nodeCheck = new ConcurrentHashMap<String,Boolean>();
	
	

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		//判断是否是握手请求
		if(message.getHeader() != null && message.getHeader().getType() == NettyMessageType.LOGIN_REQ.value()){
			NettyMessage loginResp = null;
			String nodeIndex = ctx.channel().remoteAddress().toString();
			//判断是否重复登录
			if(nodeCheck.containsKey(nodeIndex)){
				loginResp = buildResponse((byte) -1);
			}
			else {
				//判断是否可以接入
				if(checkAccessable(ctx)){
					loginResp = buildResponse((byte) 0);
					nodeCheck.put(nodeIndex, true);//添加至已登录列表
				}else{
					loginResp = buildResponse((byte) -1);
				}
				System.out.println("The login response is :" + loginResp + " body [" + loginResp.getBody() + "]");
				ctx.writeAndFlush(loginResp);
			}
		}
		ctx.fireChannelRead(msg);
	}



	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.fireExceptionCaught(cause);
	}
	
	
	/**
	 * 判断是否可以接入,如果在白名单内，返回true
	 */
	private boolean checkAccessable(ChannelHandlerContext ctx){
		String ip = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
		for(String ipStr : whiteList){
			if(ipStr.equals(ip)){
				return true;
			}
		}
		return false;
		
	}
	
	
	/**
	 * 构建握手返回消息
	 */
	private NettyMessage buildResponse(byte result){
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(NettyMessageType.LOGIN_RESP.value());
		message.setHeader(header);
		message.setBody(result);
		return message;
	}

}
