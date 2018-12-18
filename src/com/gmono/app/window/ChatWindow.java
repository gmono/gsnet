package com.gmono.app.window;

import javafx.scene.input.KeyCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatWindow extends Frame implements IChatWindow {
    List userlist=null;
    JTextArea view=null;
    JTextField input=null;
    JButton send=null;

    /**
     * 删除一个用户
     *
     * @param user 用户名
     */
    @Override
    public void removeUsser(String user) {
        this.userlist.remove(user);
    }
    protected void onsend(){
        ChatMessage msg=new ChatMessage();
        msg.text=input.getText();
        if(msg.text=="") return;
        input.setText("");
        sendListener.send(msg);
    }
    public ChatWindow(){
//        UIManager.put("TextField.inactiveForeground", new Color(0, 0, 0));
        this.setTitle("聊天窗口");
        this.setSize(1000,500);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        List list=new List();
        //高度无效
        list.setSize(100,0);
        this.add(list,BorderLayout.WEST);
        //右边区域
        Container cont=new Container();
        cont.setLayout(new BorderLayout());
        this.add(cont,BorderLayout.CENTER);
        //内容显示
        JTextArea area=new JTextArea();
        area.setBorder(BorderFactory.createLineBorder(new Color(0xf0c0cb)));
        area.setLineWrap(true);
//        area.setEnabled(false); //颜色问题

        Container inputs=new Container();
        //
        inputs.setLayout(new BorderLayout());
        inputs.setSize(0,100);
        cont.add(area,BorderLayout.CENTER);
        cont.add(inputs,BorderLayout.SOUTH);
        //给inputs添加按钮
        JButton sendbtn=new JButton("Send");
        sendbtn.setSize(150,0);
        JTextField input=new JTextField();
        inputs.add(input,BorderLayout.CENTER);
        inputs.add(sendbtn,BorderLayout.EAST);
        //添加发送事件
        sendbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onsend();
            }
        });
        input.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()== 10){
                    onsend();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        ////
        this.send=sendbtn;
        this.view=area;
        this.userlist=list;
        this.input=input;
        ///
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }
    /**
     * 添加一个聊天室用户到列表中
     *
     * @param user 用户名
     */
    @Override
    public void addUser(String user) {
        this.userlist.add(user);
    }

    /**
     * 当接收到消息时被调用
     *
     * @param username 发消息的用户
     * @param msg      接收的消息
     */
    @Override
    public void received(String username, ChatMessage msg) {
        StringBuilder builder=new StringBuilder();
        builder.append(username);
        builder.append(":\n");
        builder.append(msg.text);
        builder.append("\n\n\n");
        this.view.setText(this.view.getText()+builder.toString());
    }

    //要发送消息时调用此监听器
    SendListener sendListener;
    /**
     * 设置发送监听器
     *
     * @param sendListener 当要发送消息时调用此监听器
     */
    @Override
    public void setSendListener(SendListener sendListener) {
        this.sendListener=sendListener;
    }
}
