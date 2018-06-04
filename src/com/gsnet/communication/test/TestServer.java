package com.gsnet.communication.test;

import com.gsnet.communication.SocketServer;
import com.gsnet.network.AbstractMessageManager;
import com.gsnet.network.SocketChater;

public class TestServer extends SocketServer {
    /**
     * 创建chater并加入列表后调用
     *
     * @param chater 对话对象
     */
    @Override
    protected void OnChaterCreated(SocketChater chater) {
        chater.setSendedListener(new SocketChater.SendedListener() {
            @Override
            public void sended(long id) {
                System.out.printf("消息%d已发送\n",id);
            }
        });
        chater.setDefaultReceiveListener(new SocketChater.ReceiveListener() {
            @Override
            public void receive(Object msg) {
                System.out.printf("收到消息:%s\n",msg.toString());
            }
        });
    }

    public TestServer(int port, AbstractMessageManager msgmgr) {
        super(port, msgmgr);
    }
}
