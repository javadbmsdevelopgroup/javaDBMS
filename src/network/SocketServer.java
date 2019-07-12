package network;
import network.event.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

///////////////////////////////////////////////Socket服务器类//////////////////////////////////////////////////
public class SocketServer{

    private int port=9239;    //端口
    private ServerSocket server=null;
    //事件接口
    private ISocketEvent serverMessageIn=null;   //服务器信息抵达事件
    private ISocketEvent clientIn=null;         //客户端进入事件
    private ISocketEvent clientLeave=null;       //客户端离开事件
    private boolean opened=false;                   //连接是否已打开
    private DataInputStream dis=null;
    private List<SocketClient> clientList=new ArrayList<>();    //客户端列表
    private Thread tRecv=null;
    private List<ClientListener> clientListener=new ArrayList<>();         //客户端监听器
    private boolean threadflagOpen=false;
    public SocketServer(int port){
        this.port=port;
        setServerMessageInEvent(new ServerMessageIn());
    }                            //构造函数
    public void setServerMessageInEvent(ISocketEvent event){
        serverMessageIn=event;
    }       //设置服务器收到信息的事件
    public void setClientInEvent(ISocketEvent oe){
        clientIn=oe;
    }                  //设置客户端进入事件




    public void beginRec(SocketClient client){
        ClientListener cl=new ClientListener(client,this);     //创建一个客户端监听器
        clientListener.add(cl);
        cl.beginRecv();
    }

    public void notifyDeleteClient(Socket sc,ClientListener cl){
        clientListener.remove(cl);
        for(int i=0;i<clientList.size();i++){
            if(clientList.get(i).s==sc) clientList.remove(i);
        }
        if(clientLeave!=null) clientLeave.processEvent(sc);
    }


    //打开服务器
    public boolean connect()  {
        try{
        server=new ServerSocket(port);
        opened=true;

        while (opened){
            Socket s=server.accept(); //开始等待客户端进入    (此函数会阻塞当前线程，直到有客户端进入)
            SocketClient sc=new SocketClient(s);
            clientList.add(sc);    //把客户socket加入列表
            beginRec(sc);    //让客户端进入接受消息的状态
            if(clientIn!=null) clientIn.processEvent(s,this);   //客户端进入事件处理
        }
        }catch (IOException ioe){
            ioe.printStackTrace();
            return false;
        }
        return true;
    }


    //关闭服务器
    public void close(){
        try{
            for(int i=0;i<clientList.size();i++){
                clientList.get(i).s.close();
            }
            clientList.clear();
            server.close();
            opened=false;
        }catch (Exception e){
            clientList.clear();
            opened=false;
        }
    }


    //发送消息到某个客户端  参数一:文本  参数二：客户端socket
    public void sendMessageToClient(Object obj,Socket client){
        try {
            System.out.println("In server: send:"+client.getLocalSocketAddress()+"->"+client.getRemoteSocketAddress());

            ObjectOutputStream oos=new ObjectOutputStream(client.getOutputStream());
            oos.writeObject(obj);
            oos.flush();

        } catch (IOException e) {
            clientList.remove(client);
            if(clientLeave!=null)
            { clientLeave.processEvent(client);}   //发现客户端已离开
        }
    }




    public ISocketEvent getServerMessageIn(){
        return serverMessageIn;
    }


    public static void main(String[] args){
        SocketServer ss=new SocketServer(3456);
        ss.setClientInEvent(new ClientInEvent());

        ss.connect();
        ss.close();
    }
}

