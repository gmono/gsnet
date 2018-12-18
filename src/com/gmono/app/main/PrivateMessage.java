package com.gmono.app.main;

import com.gmono.app.window.ChatMessage;

public class PrivateMessage {
    public String from_username;
    public String to_username;
    public ChatMessage msg;

    @Override
    public String toString() {
        return String.format("%s->%s:%s",from_username,to_username,msg.toString());
    }
}
