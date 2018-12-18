package com.gmono.app.main;

import com.gmono.app.window.ChatMessage;

/**
 *
 */
public class GroupMessage {
    public String username;
    public ChatMessage msg;

    @Override
    public String toString() {
        return "username:"+username+"\n"+msg.toString();
    }
}
