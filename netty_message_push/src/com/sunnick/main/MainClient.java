package com.sunnick.main;

import com.sunnick.client.NettyClient;
import com.sunnick.entity.NettyConstant;

/**
 * @author sunnick
 * 
 * client测试类
 *
 */
public class MainClient {
	public static void main(String[] args) throws InterruptedException {
		new NettyClient(NettyConstant.REMOTEIP,NettyConstant.PORT).connect();
	}
}
