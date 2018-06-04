package com.gsnet.network;

/**
 * 直接以类名为消息msgtype
 */
public class ClassMessageManager extends AbstractMessageManager {
    protected String getMsgType(Object obj){
        Class cls=obj.getClass();
        return getMsgTypeFromClass(cls);
    }
    protected String getMsgTypeFromClass(Class cls){
        return cls.getName();
    }
    protected boolean msgTypeVaildation(String msgtype){
        try {
            Class.forName(msgtype);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    protected Class getClassFromMsgtype(String msgtype){
        assert msgTypeVaildation(msgtype):"无法获取Class,错误的MSGTYPE:"+msgtype;
        try {
            return Class.forName(msgtype);
        } catch (ClassNotFoundException e) {
            System.out.print("搜索类时发生未知错误！");
            e.printStackTrace();
        }
        return null;
    }
}
