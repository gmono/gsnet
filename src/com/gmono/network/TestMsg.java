package com.gmono.network;

import java.io.Serializable;

@CustomMessageType(msgType = "test")
public class TestMsg implements Serializable {
    public TestMsg(){}
    public String name="gaozijian";
    public int test=3;
    public TestMsg2 sub=new TestMsg2();
}