package network;
import com.sun.security.ntlm.Client;

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
    private IOneArugumentEvent clientIn=null;
    private boolean opened=false;
    private List<Client> clientList=new ArrayList<>();

    public SocketServer(int port){
        this.port=port;
    }

    public void setClientInEvent(IOneArugumentEvent oe){
        clientIn=oe;
    }
    //还有离开事件，通知事件。离开事件通过发消息完成
    public boolean connect()  {
        try{
        server=new ServerSocket(port);
        opened=true;
        while (opened){
            Socket s=server.accept(); //Wait for user
            clientList.add(new Client(s));
            if(clientIn!=null) clientIn.doEvent(s);
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








    //内部类。客户端
    class Client implements Runnable{
        private Socket s;
        private DataInputStream dis = null;
        private DataOutputStream dos = null;
        private boolean bConnected = false;

        public Client(Socket s) {
            this.s = s;
            try {
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());
                bConnected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //给某个客户发消息
        public void send(String str) {
            try {
                dos.writeUTF(str);
            } catch (IOException e) {
                clientList.remove(this);
                System.out.println("对方退出了！我从List里面去掉了！");
            }
        }

        public void run() {
            try {
                //不停接收信息
                while (bConnected) {
                    String str = dis.readUTF();
                    System.out.println("------------来自本地服务器:" + str);
                    for (int i = 0; i < clientList.size(); i++) {
                        Client c = clientList.get(i);
                        c.send(str);
                    }
                }
            } catch (EOFException e) {
                System.out.println("Client closed!");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (dis != null)
                        dis.close();
                    if (dos != null)
                        dos.close();
                    if (s != null) {
                        s.close();
                    }

                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }
    }
    public static void main(String[] args){
        SocketServer ss=new SocketServer(3456);
        ss.setClientInEvent(new ClientInEvent());
        ss.close();
    }
}

