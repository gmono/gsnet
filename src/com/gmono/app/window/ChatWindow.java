package com.gmono.app.window;

import javax.swing.*;
import java.awt.*;

public class ChatWindow extends JFrame implements IChatWindow {
    /**
     * 添加一个聊天室用户到列表中
     *
     * @param user 用户名
     */
    @Override
    public void addUser(String user) {

    }

    /**
     * 当接收到消息时被调用
     *
     * @param username 发消息的用户
     * @param msg      接收的消息
     */
    @Override
    public void received(String username, ChatMessage msg) {

    }

    //要发送消息时调用此监听器
    ISendListener sendListener;
    /**
     * 设置发送监听器
     *
     * @param sendListener 当要发送消息时调用此监听器
     */
    @Override
    public void setSendListener(ISendListener sendListener) {
        this.sendListener=sendListener;
    }
}
