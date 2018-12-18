package com.gmono.app.main;

import com.gsnet.communication.SocketServer;
import com.gsnet.network.AbstractMessageManager;
import com.gsnet.network.SocketChater;

import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MyServer extends SocketServer {
    public MyServer(int port, AbstractMessageManager msgmgr) {
        super(port, msgmgr);
    }

    HashSet<String> users=new HashSet<>();
    /**
     * 创建chater并加入列表后调用
     *
     * @param chater 对话对象
     */
    @Override
    protected void OnChaterCreated(SocketChater chater) {
         chater.setReceiveListener(DeliveryMessage.class, new SocketChater.ReceiveListener<DeliveryMessage>() {
             @Override
             public void receive(DeliveryMessage msg) {
                 //分发消息
                 SendMessage(msg);
                 //输出
                 System.out.printf("转发消息:%s\n",msg.toString());
             }
         });
         //收到其他消息
         chater.setDefaultReceiveListener(new SocketChater.ReceiveListener() {
             @Override
             public void receive(Object msg) {
                 System.out.printf("收到未知消息:%s\n\n",msg.toString());
             }
         });
         //发送消息
        chater.setSendedListener(new SocketChater.SendedListener() {
            @Override
            public void sended(long id) {
                System.out.printf("消息 %d 已转发\n",id);
            }
        });
        //发送通知消息 通知有人加入

        chater.setReceiveListener(InformMessage.class, new SocketChater.ReceiveListener<InformMessage>() {
            @Override
            public void receive(InformMessage msg) {
                //通知所有人 （包括发送者)
                if(msg.type==0)
                {
                    users.add(msg.username);
                    for(String m:users){
                        InformMessage smsg=new InformMessage();
                        smsg.username=m;
                        smsg.type=0;
                        SendMessage(smsg);
                    }
                }

            }
        });

    }

//    /**
//     * 发送消息
//     *
//     * @param objmsg 消息对象
//     */
//    @Override
//    public void SendMessage(Object objmsg) {
//        super.SendMessage(objmsg);
//    }
}
