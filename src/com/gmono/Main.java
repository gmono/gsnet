package com.gmono;
import javafx.util.Pair;
import org.json.JSONObject;


public class Main {

    public static void main(String[] args) throws Exception {
	// write your code here
        MessageManager manager=new MessageManager();
        manager.registerMsgType(TestMsg.class);
        manager.registerMsgType(TestMsg2.class);
        JSONObject obj=manager.objectToMsg(new TestMsg());
        System.out.print(obj);
        TestMsg msg=(TestMsg)(manager.msgToObject(obj).getValue());
        System.out.print(msg.sub.myname);
        System.out.print(new JSONObject("{\"hello\":100}").get("hello"));
    }
}
