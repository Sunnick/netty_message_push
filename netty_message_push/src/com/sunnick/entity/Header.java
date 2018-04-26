package com.sunnick.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sunnick
 * 
 * 报文头定义
 *
 */
public final class Header {
	
	/**
	 * 消息长度
	 */
	private int length;
	/**
	 * 0xabcd 固定值，表示是自定义协议，2字节
	 * 主版本号：1～255,1字节
	 * 次版本号：1～255,1字节
	 */
	private int crcCode = 0xabcd0101;

	/**
	 * 会话ID 
	 */
	private long sessionId;
	/**
	 * 消息类型：
	 * 0 业务请求消息
	 * 1 业务响应消息
	 * 2 ONE WAY消息（既是请求，也是应答）
	 * 3 握手请求消息
	 * 4 握手应答消息
	 * 5 心跳请求消息
	 * 6 心跳应答消息
	 */
	private byte type;
	/**
	 * 消息优先级
	 */
	private byte priority;
	/**
	 * 消息附件，用于业务扩展
	 */
	private Map<String,Object> attachment = new HashMap<String,Object>();
	public final int getCrcCode() {
		return crcCode;
	}
	public final void setCrcCode(int crcCode) {
		this.crcCode = crcCode;
	}
	public final int getLength() {
		return length;
	}
	public final void setLength(int length) {
		this.length = length;
	}
	public final long getSessionId() {
		return sessionId;
	}
	public final void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
	public final byte getType() {
		return type;
	}
	public final void setType(byte type) {
		this.type = type;
	}
	public final byte getPriority() {
		return priority;
	}
	public final void setPriority(byte priority) {
		this.priority = priority;
	}
	public final Map<String, Object> getAttachment() {
		return attachment;
	}
	public final void setAttachment(Map<String, Object> attachment) {
		this.attachment = attachment;
	}
	
	public String toString(){
		return "header[" + crcCode + "," + length + "," + sessionId + "," + type + "," + priority + "," + attachment  + "]";
	}
	
}
