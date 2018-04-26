package com.sunnick.encode;

import java.io.IOException;

import org.jboss.marshalling.ByteOutput;

import io.netty.buffer.ByteBuf;

public class ChannelBufferByteOutput implements ByteOutput {
	
	private ByteBuf out;

	public ChannelBufferByteOutput(ByteBuf out) {
		this.out = out;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flush() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(int b) throws IOException {
		out.writeByte(b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		out.writeBytes(b);		
	}

	@Override
	public void write(byte[] b, int index, int length) throws IOException {
		out.writeBytes(b,index,length);
	}

	public ByteBuf getBuffer() {
        return out;
    }
}
