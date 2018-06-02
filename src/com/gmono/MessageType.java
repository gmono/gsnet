package com.gmono;

import java.lang.annotation.*;

/**
 * 消息标识 声明消息类型(字符串)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface MessageType {
    public String msgType();
}
