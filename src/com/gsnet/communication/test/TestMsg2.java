package com.gsnet.communication.test;

import com.gsnet.network.CustomMessageType;

@CustomMessageType(msgType = "test2")
public class TestMsg2 {
    public TestMsg2(){}
    public String myname="hello";
}
