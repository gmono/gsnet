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
            //发送通知消息
            InformMessage msg=new InformMessage();
            msg.username=myName;
            client.Send(msg);
            client.Start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"连接服务器失败！");
            System.exit(2);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ShowMainWindow();
    }
    static void ShowMainWindow(){
        IChatWindow window=new ChatWindow();
        //设置当聊天窗口要发送消息时的处理方法
        window.setSendListener(new SendListener() {
            @Override
            public void send(ChatMessage msg) {
                //构建传递消息
                GroupMessage dmsg=new GroupMessage();
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

        HashSet<String> users=new HashSet<>();
        //设置客户端接收到消息时的处理方法
        client.setReceiveListener(GroupMessage.class, new SocketChater.ReceiveListener<GroupMessage>() {
            @Override
            public void receive(GroupMessage msg) {
                //接收到消息 如果不是自己发送的 则显示
                window.received(msg.username,msg.msg);
                //如果接收到了新用户的消息就添加一个用户到列表中
                if(!users.contains(msg.username)) {
                    window.addUser(msg.username);
                    users.add(msg.username);
                }
            }
        });
        client.setReceiveListener(InformMessage.class, new SocketChater.ReceiveListener<InformMessage>() {
            @Override
            public void receive(InformMessage msg) {
                //通知消息 处理用户进入的情况
                if(msg.type==0)
                    if(!users.contains(msg.username)){
                        users.add(msg.username);
                        window.addUser(msg.username);
                    }
                //有人退出时
                if(msg.type==1)
                {
                    users.remove(msg.username);

                }
            }
        });

        window.show();

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