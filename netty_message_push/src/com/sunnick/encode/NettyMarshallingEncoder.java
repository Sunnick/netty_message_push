package com.sunnick.encode;

import java.io.IOException;

import org.jboss.marshalling.Marshaller;

import io.netty.buffer.ByteBuf;

/**
 * @author sunnick
 * 
 * 使用MarshallingEncoder编码
 *
 */
public class NettyMarshallingEncoder {
	
	private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
	
	private Marshaller marshaller;

	public NettyMarshallingEncoder() throws IOException {
		this.marshaller = MarshallingCodecFactory.buildMarshalling();
	}
	
	/**
	 * @param obj 需要编码的对象
	 * @param out 编码后的结果输出至该ByteBuf
	 */
	public void encode(Object obj, ByteBuf out) {
		/*try {
			int lengthPos = out.writerIndex();
			out.writeBytes(LENGTH_PLACEHOLDER);
			ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);
			marshaller.start(output);
			marshaller.writeObject(obj);
			marshaller.finish();
			out.setIndex(lengthPos, out.writerIndex() - lengthPos -4);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				marshaller.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
		
		try {
		    int lengthPos = out.writerIndex();
		    out.writeBytes(LENGTH_PLACEHOLDER);
		    ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);
		    marshaller.start(output);
		    marshaller.writeObject(obj);
		    marshaller.finish();
		    out.setInt(lengthPos, out.writerIndex() - lengthPos - 4);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    try {
				marshaller.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
