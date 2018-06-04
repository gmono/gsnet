package com.gsnet.network;

import java.lang.annotation.*;

/**
 * 消息标识 声明消息类型(字符串)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface CustomMessageType {
    public String msgType();
}
