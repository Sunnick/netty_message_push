package com.sunnick.main;

import com.sunnick.server.NettyServer;

/**
 * @author sunnick
 * 
 * server测试类
 *
 */
public class MainServer {
	public static void main(String[] args) throws InterruptedException {
		new NettyServer().bind();
	}
}
