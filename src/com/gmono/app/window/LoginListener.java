package com.gmono.app.window;

public interface LoginListener {
    void login(String username,String password,String ip,int port);
    void register(String username,String password,String ip,int port);
}
