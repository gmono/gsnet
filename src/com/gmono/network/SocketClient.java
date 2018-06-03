package com.gmono.network;


import java.io.IOException;
import java.net.Socket;

/**
 * socket客户端 连接服务器 得到socket并应用其初始化chater
 */
public class SocketClient extends SocketChater {
    public SocketClient(String ip,int port, AbstractMessageManager msgmgr) throws IOException {
        super(new Socket(ip,port),msgmgr);
    }
}
