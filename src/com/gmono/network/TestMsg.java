package com.gmono.network;

import java.io.Serializable;

@CustomMessageType(msgType = "test")
public class TestMsg implements Serializable {
    public TestMsg(){}
    public String name="gaozijian";
    public int test=3;
    public TestMsg2 sub=new TestMsg2();
    public TestMsg2 sub2=new TestMsg2();
    public TestMsg2 sub3=new TestMsg2();
    public TestMsg2 sub4=new TestMsg2();

    @Override
    public String toString(){
        StringBuilder builder=new StringBuilder();
        builder.append("name:"+name+"\n");
        builder.append("test"+new Integer(test).toString()+"\n");
        return builder.toString();

    }

}
