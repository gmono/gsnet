package com.gmono.app.main;

import com.gsnet.network.AbstractMessageManager;
import com.gsnet.network.ClassMessageManager;

public class Common {
    //客户端和服务端共用的msgmgr
    static AbstractMessageManager msgmgr=new ClassMessageManager();
    static int serverPort=1000;
    static{

    }
}
