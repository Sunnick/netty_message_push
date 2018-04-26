package com.sunnick.encode;

import java.io.IOException;
import java.util.Map;

import com.sunnick.entity.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author sunnick
 * 
 *         消息编码器
 *
 */
public final class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {

	/**
	 * 使用MarshallingEncoder编码
	 */
	private NettyMarshallingEncoder encoder;

	public NettyMessageEncoder() throws IOException {
		this.encoder = new NettyMarshallingEncoder();
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf sendBuf) throws Exception {
		/*
		 * if(msg == null || msg.getHeader() == null){ throw new
		 * Exception("The encode msg is null!"); } //对报文头编码 ByteBuf sendBuf =
		 * Unpooled.buffer(); sendBuf.writeInt(msg.getHeader().getCrcCode());
		 * sendBuf.writeInt(msg.getHeader().getLength());
		 * sendBuf.writeLong(msg.getHeader().getSessionId());
		 * sendBuf.writeByte(msg.getHeader().getType());
		 * sendBuf.writeByte(msg.getHeader().getPriority());
		 * sendBuf.writeInt(msg.getHeader().getAttachment().size());//
		 * 写入attachment的大小 String key = null; byte[] keyArray = null; Object
		 * value = null; for(Map.Entry<String, Object> entry :
		 * msg.getHeader().getAttachment().entrySet()){ key = entry.getKey();
		 * keyArray = key.getBytes("UTF-8"); sendBuf.writeInt(keyArray.length);
		 * sendBuf.writeBytes(keyArray); value = entry.getValue();
		 * encoder.encode(value,sendBuf); } key = null; keyArray = null; value =
		 * null; //对报文体编码 if(msg.getBody() != null){
		 * encoder.encode(msg.getBody(), sendBuf); }else{ sendBuf.writeInt(0);
		 * sendBuf.setInt(4, sendBuf.readableBytes()); }
		 */

		if (msg == null || msg.getHeader() == null)
			throw new Exception("The encode message is null");
		sendBuf.writeInt((msg.getHeader().getLength()));
		sendBuf.writeInt((msg.getHeader().getCrcCode()));
		sendBuf.writeLong((msg.getHeader().getSessionId()));
		sendBuf.writeByte((msg.getHeader().getType()));
		sendBuf.writeByte((msg.getHeader().getPriority()));
		sendBuf.writeInt((msg.getHeader().getAttachment().size()));
		String key = null;
		byte[] keyArray = null;
		Object value = null;
		for (Map.Entry<String, Object> param : msg.getHeader().getAttachment().entrySet()) {
			key = param.getKey();
			keyArray = key.getBytes("UTF-8");
			sendBuf.writeInt(keyArray.length);
			sendBuf.writeBytes(keyArray);
			value = param.getValue();
			encoder.encode(value, sendBuf);
		}
		key = null;
		keyArray = null;
		value = null;
		if (msg.getBody() != null) {
			encoder.encode(msg.getBody(), sendBuf);
		} else
			sendBuf.writeInt(0);
		sendBuf.setInt(0, sendBuf.readableBytes() - 4);
	}

}
