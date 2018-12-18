package com.gmono.app.main;

import com.gsnet.communication.SocketServer;
import com.gsnet.network.AbstractMessageManager;
import com.gsnet.network.SocketChater;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MyServer extends SocketServer {
    public MyServer(int port, AbstractMessageManager msgmgr) {
        super(port, msgmgr);
    }

    HashSet<String> users=new HashSet<>();
    Map<String,SocketChater> chaters=new HashMap<>();
    /**
     * 创建chater并加入列表后调用
     * 在此对“与用户通信”的对象做设置，相当于对用户的行为做设置
     * @param chater 对话对象
     */
    @Override
    protected void OnChaterCreated(final SocketChater chater) {
         chater.setReceiveListener(GroupMessage.class, new SocketChater.ReceiveListener<GroupMessage>() {
             @Override
             public void receive(GroupMessage msg) {
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
                if(msg.type==0&&users.contains(msg.username))
                {
                    //加入用户集合
                    users.add(msg.username);
                    //加入用户通信对象映射表
                    chaters.put(msg.username,chater);
                    for(String m:users){
                        InformMessage smsg=new InformMessage();
                        smsg.username=m;
                        smsg.type=0;
                        SendMessage(smsg);
                    }
                }

            }
        });
        //这里设置私聊消息的处理策略 转发给指定的人
        chater.setReceiveListener(PrivateMessage.class, new SocketChater.ReceiveListener<PrivateMessage>() {
            @Override
            public void receive(PrivateMessage msg) {
                //转发给对应的用户
                if(!chaters.containsKey(msg.from_username)||!chaters.containsKey(msg.to_username))
                {
                    System.out.printf("错误消息,用户不存在:%s\n",msg.toString());
                    return;
                }
                //转发给指定用户
                SocketChater tochater=chaters.get(msg.from_username);
                try {
                    tochater.Send(msg);
                    System.out.printf("转发一条Private消息:%s",msg.toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
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
