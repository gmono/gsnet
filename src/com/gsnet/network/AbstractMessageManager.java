package com.gsnet.network;

import javafx.util.Pair;
import org.json.JSONObject;

import java.lang.reflect.Field;

public abstract class AbstractMessageManager {

    //msgtype相关 如果要改变msgtype处理方法 重写以下函数
    /**
     * 工具函数 用于获取一个对象的msgtype字符串
     * @param obj 对象
     * @return msgtype
     */
    protected abstract String getMsgType(Object obj);
    protected abstract String getMsgTypeFromClass(Class cls);
    protected abstract boolean msgTypeVaildation(String msgtype);
    protected abstract Class getClassFromMsgtype(String msgtype);

    /**
     * 传入数据对象（javabean） 返回包装的msg对象
     * @param obj 数据对象
     * @return msg
     */
    public JSONObject objectToMsg(Object obj) throws IllegalAccessException {
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
        assert msgTypeVaildation(msgtype):"未注册的MsgType:"+msgtype;
        //
        JSONObject retobj=new JSONObject();
        retobj.putOnce("msgtype",msgtype);
        retobj.putOnce("data",jsonobj);
        return retobj;
    }

    /**
     * 将一个消息的消息体转化为cons类的对象
     * @param data json数据对象（msg对象实体）
     * @param cons 相关类
     * @return 合成的类对象
     * @throws Exception
     */
    private Object jsonToObject(JSONObject data,Class cons) throws Exception {
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
            assert msgTypeVaildation(msgtype):"未注册的MsgType:"+msgtype;
            //获取构造类
            Class cons=getClassFromMsgtype(msgtype);
            //得到解析对象
            Object ret=jsonToObject(data,cons);
            return new Pair<>(cons,ret);
        }
        return null;
    }
}
