package network;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.io.*;
import java.net.SocketException;


//////////////////////////////客户端的监听器,用来客户端监听来自服务器的消息的
public class ClientListener {
    /*public SocketClient client;
    public SocketServer server;
    //ObjectOutputStream oos;
    ObjectInputStream ois;
    private boolean connect=false;
    Thread tRecv=null;
    public ClientListener(SocketClient c,SocketServer serverBelonged){
        client=c;
        server=serverBelonged;
        try {
            //oos = new ObjectOutputStream(c.s.getOutputStream());
            ois = new ObjectInputStream(c.s.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Listener Create:"+c.s.getLocalSocketAddress()+":"+c.s.getRemoteSocketAddress());
    }

    public void beginRecv(){
        ///////////////这里是一个同步，防止客户端开启多个线程进行服务器的消息监听
        connect=false;
        if(tRecv!=null){
            while (tRecv.isAlive());    //等待已有线程结束
        }
        connect = true;
        ///////////////END

        //创建新的接受线程.接受来自客户端的消息
        tRecv=new Thread(
                ()->{ try {
                while (connect) {
                    System.out.println("listen " +client.s.getLocalSocketAddress()+":"+client.s.getRemoteSocketAddress());


                    //ObjectInputStream ois=new ObjectInputStream(client.s.getInputStream());

                    Object msg="";
                    msg=ois.readObject();
                    System.out.println("收到来自客户端的OBJMsg: "+msg.toString());
                    if(server.getServerMessageIn()!=null){
                        server.getServerMessageIn().processEvent(msg,client,server);      //信息到达处理
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Listener Stop");
                server.notifyDeleteClient(client.s,this);
                connect=false;
            }
        });
        tRecv.start();   //启动线程
    }*/






}
