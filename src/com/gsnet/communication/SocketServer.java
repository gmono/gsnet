package com.gsnet.communication;

import com.gsnet.network.AbstractMessageManager;
import com.gsnet.network.SocketChater;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;

/**
 * 服务器类 等待客户端连接并创建 SocketChater与之交互
 */
public abstract class SocketServer {
    ServerSocket socket=null;
    AbstractMessageManager msgmgr=null;
    public SocketServer(int port,AbstractMessageManager msgmgr){
        try  {
            ServerSocket socket = new ServerSocket(port);
            this.socket=socket;
            this.msgmgr=msgmgr;
        }
        catch (Exception e){
            System.out.print("创建服务器Socket失败！");
        }
    }
    //同步操作容器
    protected List<SocketChater> chaters=new Vector<>();
    /**
     * 发送消息
     * @param objmsg 消息对象
     */
    public void SendMessage(Object objmsg){
        //给所有的客户端转发消息 不声明发送者
        chaters.forEach(new Consumer<SocketChater>() {
            @Override
            public void accept(SocketChater socketChater) {
                try {
                    socketChater.Send(objmsg);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 创建chater并加入列表后调用
     * @param chater 对话对象
     */
    protected abstract void OnChaterCreated(SocketChater chater);
    /**
     * 添加一个新chater与客户端通信
     * @param csoc 客户端通信的socket
     */
    protected SocketChater CreateNewChater(Socket csoc){
        SocketChater chater=new SocketChater(csoc,this.msgmgr);
        //加入chater列表 此时即使没有启动循环 仍然可调用chater的send函数
        chaters.add(chater);
        chater.Start();
        //调用处理函数 处理chater
        this.OnChaterCreated(chater);
        return chater;
    }

    /**
     * 启动服务器
     */
    public void Start() {
        System.out.print("服务器正在运行中......\n");
        for(;;){
            Socket clientsoc= null;
            try {
                clientsoc = this.socket.accept();
                SocketChater chater=this.CreateNewChater(clientsoc);
                System.out.printf("IP:%s %d个客户端已连接:",
                        clientsoc.getInetAddress().getHostAddress(),this.chaters.size());
            } catch (IOException e) {
                System.out.printf("服务器发生错误！\n");
                e.printStackTrace();
                break;
            }

        }
    }

}
