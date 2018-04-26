package com.sunnick.encode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sunnick.entity.Header;
import com.sunnick.entity.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author sunnick
 * 
 *         消息解码器,使用LengthFieldBasedFrameDecoder
 *
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

	NettyMarshallingDecoder decoder;

	/**
	 * @param maxFrameLength
	 * @param lengthFieldOffset
	 * @param lengthFieldLength
	 * @throws IOException
	 */
	public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		decoder = new NettyMarshallingDecoder();
	}

	/*
	 * 解码具体实现
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		/*
		 * ByteBuf frame = (ByteBuf) super.decode(ctx, in); if (frame == null)
		 * return null; NettyMessage message = new NettyMessage(); Header header
		 * = new Header();
		 * 
		 * //设置header header.setCrcCode(in.readInt());
		 * header.setLength(in.readInt()); header.setSessionId(in.readLong());
		 * header.setType(in.readByte()); header.setPriority(in.readByte());
		 * //设置header里的attch int size = in.readInt(); if(size > 0){
		 * Map<String,Object> attch = new HashMap<String,Object>(); int keySize
		 * = 0; byte[] keyArray = null; String key = null; for (int i = 0;i <
		 * size;i ++){ keySize = in.readInt(); keyArray = new byte[keySize];
		 * in.readBytes(keyArray);//从in中读取响应的字节数据至keyArray key = new
		 * String(keyArray,"UTF-8"); attch.put(key,decoder.decode(in)); }
		 * keyArray = null; key = null; header.setAttachment(attch); }
		 * 
		 * //设置message if(in.readableBytes() > 4){
		 * message.setBody(decoder.decode(in)); } message.setHeader(header);
		 * return message;
		 */

		ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		if (frame == null) {
			return null;
		}

		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setLength(frame.readInt());
		header.setCrcCode(frame.readInt());
		header.setSessionId(frame.readLong());
		header.setType(frame.readByte());
		header.setPriority(frame.readByte());

		int size = frame.readInt();
		if (size > 0) {
			Map<String, Object> attch = new HashMap<String, Object>(size);
			int keySize = 0;
			byte[] keyArray = null;
			String key = null;
			for (int i = 0; i < size; i++) {
				keySize = frame.readInt();
				keyArray = new byte[keySize];
				frame.readBytes(keyArray);
				key = new String(keyArray, "UTF-8");
				attch.put(key, decoder.decode(frame));
			}
			keyArray = null;
			key = null;
			header.setAttachment(attch);
		}
		if (frame.readableBytes() > 4) {
			message.setBody(decoder.decode(frame));
		}
		message.setHeader(header);
		return message;
	}

}
