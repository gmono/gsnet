package com.gmono.app.window;

/**
 * 聊天窗口视图接口
 */
public interface IChatWindow {
    void show();
    /**
     * 添加一个聊天室用户到列表中
     * @param user 用户名
     */
    void addUser(String user);

    /**
     * 当接收到消息时被调用
     * @param username 发消息的用户
     * @param msg 接收的消息
     */
    void received(String username,ChatMessage msg);

    /**
     * 设置发送监听器
     * @param sendListener 当要发送消息时调用此监听器
     */
    void setSendListener(ISendListener sendListener);
}
