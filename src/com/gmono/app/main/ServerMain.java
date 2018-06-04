package com.gmono.app.main;

import com.gsnet.communication.SocketServer;

public class ServerMain {
    public void main(String[] args){
        SocketServer server=new MyServer(Common.serverPort,Common.msgmgr);
        server.Start();
    }
}
