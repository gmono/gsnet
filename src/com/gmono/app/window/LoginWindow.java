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
        setSize(500,300); setLayout(new GridLayout(5, 2,5,5));
        Label l1=new Label("Username");
        l1.setAlignment(Label.CENTER);
        add(l1);

        JTextField t1=new JTextField();
        add(t1);

        l1=new Label("Password");
        l1.setAlignment(Label.CENTER);
        add(l1);
        JTextField t2=new JTextField();
        add(t2);


        l1=new Label("IP");
        l1.setAlignment(Label.CENTER);
        add(l1);
        JTextField t3=new JTextField();
        add(t3);

        l1=new Label("Port");
        l1.setAlignment(Label.CENTER);
        add(l1);
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

                try {
                    listener.login(t1.getText(), t2.getText(), t3.getText(), Integer.parseInt(t4.getText()));
                    setVisible(false);
                }catch (Exception x){
                    JOptionPane.showMessageDialog(null,"输入错误！");
                }
            }
        });
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    listener.register(t1.getText(), t2.getText(), t3.getText(), Integer.parseInt(t4.getText()));
                }catch (Exception x){
                    JOptionPane.showMessageDialog(null,"输入错误！");
                }
            }
        });
    }
}
