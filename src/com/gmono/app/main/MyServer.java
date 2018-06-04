package com.gmono.app.main;

import com.gsnet.communication.SocketServer;
import com.gsnet.network.AbstractMessageManager;
import com.gsnet.network.SocketChater;

public class MyServer extends SocketServer {
    public MyServer(int port, AbstractMessageManager msgmgr) {
        super(port, msgmgr);
    }

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
             }
         });
         //收到其他消息
         chater.setDefaultReceiveListener(new SocketChater.ReceiveListener() {
             @Override
             public void receive(Object msg) {
                 System.out.printf("收到未知消息:%s\n\n",msg.toString().substring(0,100)+"...");
             }
         });
         //发送消息
        chater.setSendedListener(new SocketChater.SendedListener() {
            @Override
            public void sended(long id) {
                System.out.printf("消息 %d 已转发\n",id);
            }
        });
    }

    /**
     * 发送消息
     *
     * @param objmsg 消息对象
     */
    @Override
    public void SendMessage(Object objmsg) {
        super.SendMessage(objmsg);
        System.out.printf("分发消息:%s \n\n",objmsg.toString());
    }
}
