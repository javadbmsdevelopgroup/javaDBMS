package network;
import network.event.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class SocketServer {

    private int port=9239;
    private ServerSocket server=null;
    private ITwoArugumentEvent serverMessageIn=null;
    private ITwoArugumentEvent clientIn=null;
    private IOneArugumentEvent clientLeave=null;
    private boolean opened=false;
    private DataInputStream dis=null;
    private List<SocketClient> clientList=new ArrayList<>();
    private Thread tRecv=null;
    private List<ClientListener> clientListener=new ArrayList<>();
    private boolean threadflagOpen=false;
    public SocketServer(int port){
        this.port=port;
    }
    public void setServerMessageInEvent(ITwoArugumentEvent event){
        serverMessageIn=event;
    }
    public void setClientInEvent(ITwoArugumentEvent oe){
        clientIn=oe;
    }


    public void beginRec(Socket client){
        ClientListener cl=new ClientListener(client,this);
        clientListener.add(cl);
        cl.beginRecv();
    }

    public void notifyDeleteClient(Socket sc,ClientListener cl){
        clientListener.remove(cl);
        for(int i=0;i<clientList.size();i++){
            if(clientList.get(i).s==sc) clientList.remove(i);
        }
        if(clientLeave!=null) clientLeave.doEvent(sc);
    }


    public boolean connect()  {
        try{
        server=new ServerSocket(port);
        opened=true;

        while (opened){
            Socket s=server.accept(); //Wait for user
            clientList.add(new SocketClient(s));    //把客户socket加入列表
            beginRec(s);
            if(clientIn!=null) clientIn.doEvent(s,this);   //客户端进入事件处理
        }
        }catch (IOException ioe){
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

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

    public void sendMessageToClient(String str,Socket client){
        try {
            System.out.println("In server: send:"+client.getLocalSocketAddress()+"->"+client.getRemoteSocketAddress());
            new DataOutputStream(client.getOutputStream()).writeUTF(str);
        } catch (IOException e) {
            clientList.remove(client);
            if(clientLeave!=null)
            { clientLeave.doEvent(client);}   //发现客户端已离开
        }
    }




    public ITwoArugumentEvent getServerMessageIn(){
        return serverMessageIn;
    }
    public static void main(String[] args){
        SocketServer ss=new SocketServer(3456);
        ss.setClientInEvent(new ClientInEvent());

        ss.connect();
        ss.close();
    }
}

