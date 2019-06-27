package network;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.io.*;
import java.net.SocketException;

public class ClientListener {
    public Socket client;
    public SocketServer server;
    private boolean connect=false;
    Thread tRecv=null;
    public ClientListener(Socket c,SocketServer serverBelonged){
        client=c;
        server=serverBelonged;
        try {
            //dis=new DataInputStream(c.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("Listener Create:"+c.getLocalSocketAddress()+":"+c.getRemoteSocketAddress());
    }

    public void beginRecv(){
        connect=false;
        if(tRecv!=null){
            while (tRecv.isAlive());
        }
        connect = true;
        // dis=new DataInputStream(this.server.);

        //创建新的接受线程
        tRecv=new Thread(()->{
            try {
                while (connect) {
                    System.out.println("listen " +client.getLocalSocketAddress()+":"+client.getRemoteSocketAddress());
                    BufferedReader br=new BufferedReader(new InputStreamReader(client.getInputStream()));
                    //String str = dis.readUTF();
                    String str = null;
                    String msg="";
                    msg=br.readLine();
                    System.out.println("Msg: "+msg);
                    if(server.getServerMessageIn()!=null){
                        server.getServerMessageIn().doEvent("aaaaa",server);     //信息到达处理
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
        tRecv.start();
    }
}
