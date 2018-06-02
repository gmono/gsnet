package com.gmono;

import javafx.util.Pair;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 通用通信类 提供投递函数
 */
public class MessageManager {
    public MessageManager() {

    }
    //同步容器 用于接收到的消息转换为对象
    protected Dictionary<String,Class> msgTypeToClass=new Hashtable<>();

    /**
     * 工具函数 用于获取一个对象的msgtype字符串
     * @param obj 对象
     * @return msgtype
     */
    static String getMsgType(Object obj){
        Class cls=obj.getClass();
        MessageType msgtype=(MessageType) cls.getAnnotation(MessageType.class);
        assert (msgtype!=null):"错误的消息对象，MsgType为空";
        return msgtype.msgType();
    }

    /**
     * 传入数据对象（javabean） 返回包装的msg对象
     * @param obj 数据对象
     * @return msg
     */
     JSONObject objectToMsg(Object obj) throws IllegalAccessException {
         //构建消息体
        JSONObject jsonobj=new JSONObject();
        Field[] fields=obj.getClass().getFields();
        for(Field f:fields){
            String name=f.getName();
            Object val=f.get(obj);
            assert (val!=null):"消息属性值不能为NULL！";
            //如果是基本类型则直接置入
            if(val instanceof Number || val instanceof String){
                jsonobj.put(name,val);
            }
            else{
                //如果是对象类型 则此对象必须是已在注册的消息
                JSONObject submsg=objectToMsg(val);
                jsonobj.put(name,submsg);
            }
        }
        //获得消息头（类型)
        String msgtype=getMsgType(obj);
        //检测消息类型存在性
        assert (msgTypeToClass.get(msgtype)!=null):"未注册的MsgType:"+msgtype;
        //
        JSONObject retobj=new JSONObject();
        retobj.putOnce("msgtype",msgtype);
        retobj.putOnce("data",jsonobj);
        return retobj;
    }

    /**
     * 将一个消息的消息体转化为cons类的对象
     * @param data
     * @param cons
     * @return
     * @throws Exception
     */
    protected Object jsonToObject(JSONObject data,Class cons) throws Exception {
        //创建对应类对象并填充
        Object obj=cons.newInstance();
        //填充对象
        for(String key:data.keySet()){
            //得到值对象
            Object val=data.get(key);
            //得到字段
            Field f=null;
            try {
                f=cons.getField(key);
            } catch (NoSuchFieldException e) {
                throw new NoSuchFieldException("JSON中数据对象属性过多！");
            }
            //填充 如果是基本类型则直接赋值 否则递归调用本函数
            if(val instanceof Number||val instanceof String){
                f.set(obj,val);
            }
            else if(val instanceof JSONObject){
                //递归解析 这里的submsg为一个完整的消息 可以直接用msgToObject处理
                JSONObject submsg=(JSONObject)val;
                Object subobj=msgToObject(submsg).getValue();
                //赋值给指定字段
                f.set(obj,subobj);
            }
            else {
                throw new Exception("错误，出现不可解析数据字段");
            }
        }
        return obj;
    }

    /**
     * 传入msg 返回数据对象和类对象
     * @param jsonobj msg
     * @return <类对象，数据对象>
     */
    Pair<Class,Object> msgToObject(JSONObject jsonobj) throws Exception {
        if(jsonobj.has("msgtype")&&jsonobj.has("data")){
            String msgtype=jsonobj.getString("msgtype");
            JSONObject data=jsonobj.getJSONObject("data");
            //检测消息类型合法性
            assert (msgTypeToClass.get(msgtype)!=null):"未注册的MsgType:"+msgtype;
            //获取构造类
            Class cons=msgTypeToClass.get(msgtype);
            //得到解析对象
            Object ret=jsonToObject(data,cons);
            return new Pair<>(cons,ret);
        }
        return null;
    }
    public boolean registerMsgType(Class cls){
        MessageType msgtype=(MessageType)cls.getAnnotation(MessageType.class);
        String tname=msgtype.msgType();
        if(msgTypeToClass.get(tname)!=null) return false;
        msgTypeToClass.put(tname,cls);
        return true;
    }
}
