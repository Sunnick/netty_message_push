package com.sunnick.encode;

import java.io.IOException;

import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import io.netty.buffer.ByteBuf;

/**
 * @author sunnick
 * 
 * 使用MarshallingDecoder进行解码
 *
 */
public class NettyMarshallingDecoder {
	
	private final Unmarshaller unmarshaller;
	
	public NettyMarshallingDecoder() throws IOException{
		unmarshaller = MarshallingCodecFactory.buildUnMarshalling();
	}
	
	/**
	 * 对二进制进行解码
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	protected Object decode(ByteBuf in) throws IOException, ClassNotFoundException{
		int objectSize = in.readInt();
		ByteBuf buf = in.slice(in.readerIndex(),objectSize);
		ByteInput input = new ChannelBufferByteInput(buf);
		try{
			unmarshaller.start(input);
			Object obj = unmarshaller.readObject();
			unmarshaller.finish();
			in.readerIndex(in.readerIndex() + objectSize);
			return obj;
		}finally{
			unmarshaller.close();
		}
	}
}
