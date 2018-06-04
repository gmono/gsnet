package com.gsnet.communication.test;
import com.gsnet.communication.SocketServer;
import com.gsnet.network.ClassMessageManager;


public class ServerMain {

    public static void main(String[] args) throws Exception {
        SocketServer server=new TestServer(1000,new ClassMessageManager());
        server.Start();
    }
}
