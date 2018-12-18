package com.gsnet.communication.test;

import com.gmono.app.main.DeliveryMessage;
import com.gmono.app.window.ChatMessage;
import com.gsnet.network.ClassMessageManager;
import com.gsnet.network.SocketChater;
import com.gsnet.communication.SocketClient;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) throws IOException, IllegalAccessException, InterruptedException {
        SocketClient client=new SocketClient("127.0.0.1",1000,new ClassMessageManager());
        client.setDefaultReceiveListener(new SocketChater.ReceiveListener() {
            @Override
            public void receive(Object msg) {
                System.out.printf("接收到消息:%s\n",msg);
            }
        });
        client.Start();
        for(;;){
            DeliveryMessage dmsg=new DeliveryMessage();
            dmsg.username="gaozijian";
            dmsg.msg=new ChatMessage();
            dmsg.msg.text="hello";
            client.Send(dmsg);
            Thread.sleep(1000);
        }
    }
}
