package com.gmono.network;
import org.json.JSONObject;


public class Main {

    public static void main(String[] args) throws Exception {
	// write your code here
//        CustomMessageManager mgr=new CustomMessageManager();
        ClassMessageManager mgr=new ClassMessageManager();
        AbstractMessageManager manager=mgr;
//        mgr.registerMsgType(TestMsg.class);
//        mgr.registerMsgType(TestMsg2.class);
        JSONObject obj=manager.objectToMsg(new TestMsg());
        System.out.print(obj);
        TestMsg msg=(TestMsg)(manager.msgToObject(obj).getValue());
        System.out.print(msg.sub.myname);
        System.out.print(new JSONObject("{\"hello\":100}").get("hello"));
    }
}
