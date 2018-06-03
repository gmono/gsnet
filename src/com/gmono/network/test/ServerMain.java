package com.gmono.network.test;
import com.gmono.network.SocketServer;
import org.json.JSONObject;


public class ServerMain {

    public static void main(String[] args) throws Exception {
        SocketServer server=new SocketServer(1000);
        server.Start();
    }
}
