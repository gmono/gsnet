package com.gmono.app.window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame implements ILoginWindow {

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

    /**
     *登陆界面
     */
    public LoginWindow(){
        super("登陆界面");
        setSize(300,200); setLayout(new GridLayout(5, 2,5,5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new Label("          姓名"));
        JTextField t1=new JTextField();
        add(t1);
        add(new Label("          密码"));
        JTextField t2=new JTextField();
        add(t2);
        add(new Label("          IP"));
        JTextField t3=new JTextField();
        add(t3);
        add(new Label("          Port"));
        JTextField t4=new JTextField();
        add(t4);
        JButton b1= new JButton("登陆");
        add(b1);
        JButton b2= new JButton("注册");
        add(b2);
        //添加事件
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                listener.login(t1.getText(),t2.getText(),t3.getText(),Integer.parseInt(t4.getText()));
            }
        });
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.register(t1.getText(),t2.getText(),t3.getText(),Integer.parseInt(t4.getText()));
            }
        });
    }
}
