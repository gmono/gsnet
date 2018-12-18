package com.gmono.app.main;

import com.gsnet.communication.SocketServer;

public class ServerMain {
    public static void main(String[] args){
        SocketServer server=new MyServer(Common.serverPort,Common.msgmgr);
        System.out.printf("当前监听端口：%d",Common.serverPort);
        server.Start();
    }
}
