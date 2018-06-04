package com.gmono.app.main;

import com.gmono.app.window.*;
import com.gsnet.communication.SocketClient;
import com.gsnet.network.SocketChater;

import javax.swing.*;
import java.io.IOException;
import java.util.HashSet;

public class WindowMain {

    static String myName=null;
    static SocketClient client=null;
    static void OnLogin(String username,String password,String ip,int port)  {
        myName=username;
        //创建连接
        try {
            client=new SocketClient(ip,port,Common.msgmgr);
            client.Start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"连接服务器失败！");
            System.exit(2);
        }
        ShowMainWindow();
    }
    static void ShowMainWindow(){
        IChatWindow window=new ChatWindow();
        //设置当聊天窗口要发送消息时的处理方法
        window.setSendListener(new ISendListener() {
            @Override
            public void send(ChatMessage msg) {
                //构建传递消息
                DeliveryMessage dmsg=new DeliveryMessage();
                dmsg.username=myName;
                dmsg.msg=msg;
                try {
                    client.Send(dmsg);
                } catch (IllegalAccessException e) {
                    JOptionPane.showMessageDialog(null,"内部错误！");
                    System.exit(1);
                }
            }
        });

        HashSet<String> others=new HashSet<>();
        //设置客户端接收到消息时的处理方法
        client.setReceiveListener(DeliveryMessage.class, new SocketChater.ReceiveListener<DeliveryMessage>() {
            @Override
            public void receive(DeliveryMessage msg) {
                //接收到消息 如果不是自己发送的 则显示
                if(!msg.username.equals(myName)){
                    window.received(msg.username,msg.msg);
                    //如果接收到了新用户的消息就添加一个用户到列表中
                    if(!others.contains(msg.username)){
                        window.addUser(msg.username);
                    }
                }
            }
        });

        window.show();
        //测试代码
//        for(;;){
//            DeliveryMessage dmsg=new DeliveryMessage();
//            dmsg.username=myName;
//            dmsg.msg=new ChatMessage();
//            dmsg.msg.text="hello";
//            try {
//                client.Send(dmsg);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
    public static void main(String[] args) {
        //显示登录窗口
        ILoginWindow login=new LoginWindow();
        login.setLoginListener(new LoginListener() {
            @Override
            public void login(String username, String password,String ip,int port) {
                OnLogin(username,password,ip,port);
            }

            @Override
            public void register(String username, String password,String ip,int port) {
                JOptionPane.showMessageDialog(null,"暂不提供注册功能！请使用自定义名字和任意密码登陆!");
            }
        });
        login.show();
        //显示
    }
}