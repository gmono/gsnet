package com.gmono.network;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * 通用通信类 提供投递函数
 */
public class CustomMessageManager extends AbstractMessageManager{
    public CustomMessageManager() {

    }
    //同步容器 用于接收到的消息转换为对象
    protected Dictionary<String,Class> msgTypeToClass=new Hashtable<>();

    //msgtype相关 如果要改变msgtype处理方法 重写以下函数
    /**
     * 工具函数 用于获取一个对象的msgtype字符串
     * @param obj 对象
     * @return msgtype
     */
    protected String getMsgType(Object obj){
        Class cls=obj.getClass();
        return getMsgTypeFromClass(cls);
    }
    protected String getMsgTypeFromClass(Class cls){
        CustomMessageType msgtype=(CustomMessageType) cls.getAnnotation(CustomMessageType.class);
        assert (msgtype!=null):"错误的消息对象，MsgType为空";
        return msgtype.msgType();
    }
    protected boolean msgTypeVaildation(String msgtype){
        return msgTypeToClass.get(msgtype)!=null;
    }
    protected Class getClassFromMsgtype(String msgtype){
        assert msgTypeVaildation(msgtype):"无法获取Class,错误的MSGTYPE:"+msgtype;
        return msgTypeToClass.get(msgtype);
    }



    /**
     * 注册一个msgtype 注意：msgtype在不同的Manager实现中不一定总是需要注册
     * @param cls 要注册的类型
     * @return
     */
    public boolean registerMsgType(Class cls){
        CustomMessageType msgtype=(CustomMessageType)cls.getAnnotation(CustomMessageType.class);
        String tname=msgtype.msgType();
        if(msgTypeToClass.get(tname)!=null) return false;
        msgTypeToClass.put(tname,cls);
        return true;
    }
}
