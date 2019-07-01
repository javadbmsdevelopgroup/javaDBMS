package network;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.io.*;
import java.net.SocketException;


//////////////////////////////客户端的监听器,用来客户端监听来自服务器的消息的
public class ClientListener {
    public Socket client;
    public SocketServer server;
    private boolean connect=false;
    Thread tRecv=null;
    public ClientListener(Socket c,SocketServer serverBelonged){
        client=c;
        server=serverBelonged;

        System.out.println("Listener Create:"+c.getLocalSocketAddress()+":"+c.getRemoteSocketAddress());
    }

    public void beginRecv(){
        ///////////////这里是一个同步，防止客户端开启多个线程进行服务器的消息监听
        connect=false;
        if(tRecv!=null){
            while (tRecv.isAlive());    //等待已有线程结束
        }
        connect = true;
        ///////////////END

        //创建新的接受线程.接受来自服务器的消息
        tRecv=new Thread(
                ()->{ try {
                while (connect) {
                    /////////////////接受来自服务器的消息
                    System.out.println("listen " +client.getLocalSocketAddress()+":"+client.getRemoteSocketAddress());
                    BufferedReader br=new BufferedReader(new InputStreamReader(client.getInputStream()));
                    //String str = dis.readUTF();
                    String str = null;
                    String msg="";
                    msg=br.readLine();
                    System.out.println("Msg: "+msg);
                    if(server.getServerMessageIn()!=null){
                        server.getServerMessageIn().doEvent("aaaaa",server);      //信息到达处理
                    }
                    //taContent.setText(taContent.getText() + str + '\n');
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Listener Stop");
                server.notifyDeleteClient(client,this);
                connect=false;
            }
        });
        tRecv.start();   //启动线程
    }






}
