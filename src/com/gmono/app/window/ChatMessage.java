package com.gmono.app.window;

/**
 * 通信的消息实体 一般被搭载在GroupMessage 或 PrivateMessage上互相传递
 * 此类只包括内容
 * 只与客户端相关与服务端无关（不带消息类型 用户等识别信息）
 */
public class ChatMessage {
    /**
     * 消息类型 目前0为文字消息 1为图片消息（base64编码）
     */
    public int typeid=0;
    public String text;
    @Override
    public String toString() {
        return String.format("typeid:%d text:%s",typeid,text);
    }
}
