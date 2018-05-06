package com.sunnick.entity;

/**
 * @author sunnick
 * 
 * 静态信息
 *
 */
public final class NettyConstant {
    public static final String REMOTEIP = "127.0.0.1";
    public static final int PORT = 8080;
    public static final int LOCAL_PORT = 12088;
    public static final String LOCALIP = "127.0.0.1";
    
    /**
     * 解码流的最大长度
     */
    public static final int  MAX_FRAME_LENGTH = 1024 * 1024;
    
    /**
     * 解码字符的偏移量
     */
    public static final int  LENGTH_FIELD_OFFSET = 0;
    
    /**
     * length字段类型为int，4个字节
     */
    public static final int  LENGTH_FIELD_LENGTH = 4;
    
    /**
     * 超时时间
     */
    public static final int  TIMEOUT_SECONDS = 50;
    
    /**
     * 心跳间隔
     */
    public static final int  HEART_BEAT_MILLISECONDS = 30 * 1000;
    
    
}
