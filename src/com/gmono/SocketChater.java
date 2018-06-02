package com.gmono;

import org.json.JSONObject;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * 端管理器 维护发送和接收两个线程同步队列
 * 产生收到消息事件和发送消息事件
 */
public class SocketChater {
    //线程池 每个进程中的socketchater共享此线程池
    static ExecutorService tpool= Executors.newCachedThreadPool();
    protected MessageManager msgmgr=null;
    protected Socket socket=null;
    public SocketChater(Socket socket,MessageManager msgmgr){
        this.msgmgr=msgmgr;
        this.socket=socket;
    }

    /**
     * 发送消息定义 msgText为要发送文本
     */
    public static class WillSendMessage{
        public long ID;
        public String msgText;
    }
    //发送队列
    BlockingQueue<WillSendMessage> willSendMessages=new LinkedBlockingQueue<>();
    //接收队列
    BlockingQueue<Object> receivedMessages=new LinkedBlockingQueue<>();
    /**
     * 接收消息
     */
    public  interface  ReceiveListener<T>{
        void receive(T msg);
    }
    public interface SendedListener{
        void sended(long id);
    }

    //简易发送消息ID生成器 递增
    protected long nowID=0;
    /**
     * 发送一个消息
     * @param msgobj 消息对象（必须是msgmgr中已经注册的消息）
     * @return
     */
    public long Send(Object msgobj) throws IllegalAccessException {
        WillSendMessage msg=new WillSendMessage();
        msg.ID=nowID;
        msg.msgText=msgmgr.objectToMsg(msgobj).toString();
        //加入发送队列
        boolean isachieved=willSendMessages.offer(msg);
        assert isachieved:"提交消息失败（未知原因）";
        return nowID++;
    }

    /**
     * 主动接收一个消息 如果队列中没有消息 则返回null
     * @return 消息对象或null
     */
    public Object Receive(){
        //此方法当队列为空时返回null
        Object ret=receivedMessages.poll();
        return ret;
    }
    //监听器映射表
    Dictionary<Class,ReceiveListener> listeners=new Hashtable<>();
    public <T> void setReceiveListener(Class<T> msgCls,ReceiveListener<T> listener){
        assert (listeners.get(msgCls)==null):"重复注册同一消息类型的监听器";
        listeners.put(msgCls,listener);
    }
    SendedListener sendedListener=null;
    public void setSendedListener(SendedListener listener){
        this.sendedListener=listener;
    }

    ///以下是接收循环和发送循环

    /**
     * 不断接收消息 放入接收队列
     * @throws Exception
     */
    protected void StartReceive() {
        //
        InputStream in= null;
        try {
            in = this.socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        Scanner cin=new Scanner(in);
        for(;;){
            //此处读取将阻塞
            String msg=cin.nextLine();
            JSONObject msgobj=new JSONObject(msg);
            Object obj= null;
            //
            try {
                obj = msgmgr.msgToObject(msgobj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //这里不会阻塞 由于是无限长队列 放不进去就是错误
            boolean isachieved=this.receivedMessages.offer(obj);
            assert isachieved:"接收消息错误，无法放入接受队列";
        }
    }
    protected void StartSend()  {
        //
        OutputStream out= null;
        try {
            out = this.socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        PrintStream print=new PrintStream(out);
        for(;;){
            //阻塞式读取 如果发送队列空则阻塞
            WillSendMessage msg= null;
            //
            try {

                msg = willSendMessages.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //
            //发送
            print.println(msg.msgText);
            //触发事件 未来考虑改为单独发送事件线程和已发送id队列实现
            this.sendedListener.sended(msg.ID);
        }
    }

    //接收事件循环 不断检测新消息 并产生接收事件
    protected void StartReceiveEventLoop() {
        for(;;){
            Object obj= null;
            try {
                //阻塞式读取
                obj = receivedMessages.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //得到对应类型监听器
            ReceiveListener listener=listeners.get(obj.getClass());
            //调用监听器 触发事件
            listener.receive(obj);
        }
    }

    //启动所有线程
    public void Start()
    {
        //按顺序启动
        tpool.submit(this::StartReceiveEventLoop);
        tpool.submit(this::StartReceive);
        tpool.submit(this::StartSend);
    }
}
