package com.gmono.app.window;

/**
 * 登陆窗口视图接口
 */
public interface ILoginWindow {
    /**
     * 显示窗口
     */
    void show();

    /**
     * 设置Login事件监听器
     * @param listener
     */
    void setLoginListener(LoginListener listener);

}
