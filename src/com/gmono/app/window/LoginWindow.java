package com.gmono.app.window;

import java.awt.*;

public class LoginWindow extends Frame implements ILoginWindow {

    LoginListener listener=null;
    /**
     * 设置Login事件监听器
     *
     * @param listener
     */
    @Override
    public void setLoginListener(LoginListener listener) {
        this.listener=listener;
    }
}
