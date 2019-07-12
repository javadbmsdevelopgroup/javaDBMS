package network;
import network.event.ClientMessageIn;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import network.event.ISocketEvent;

/**
 * @author Michael Huang
 *
 */



//////////////一个封装的类，将客户端socket封装起来了
public class SocketClient {

    Socket s = null;   //服务器的socket
    ObjectInputStream ois=null;
    ObjectOutputStream oos=null;
    String ip;
    int port;
    private boolean bConnected = false;
    private ISocketEvent messageIn=null;
    Thread tRecv;


    public SocketClient(){


    }



    public SocketClient(Socket s){
        this.s=s;

        //System.out.println("Create SC by "+s.getLocalSocketAddress());
        try{
        oos=new ObjectOutputStream(s.getOutputStream());}
        catch (Exception e){
            e.printStackTrace();
        }
        if(s==null) throw  new NullPointerException();

    }
    public void setMessageInEvent(ISocketEvent event){
        this.messageIn=event;
    }


    //...从客户端发消息给服务器的函数。。
    public boolean send(Object obj){

        if(s==null) return false;
        try{
            if(oos==null) oos=new ObjectOutputStream(s.getOutputStream());
            System.out.println("In client: send obj:"+s.getLocalSocketAddress()+"->"+s.getRemoteSocketAddress());
            System.out.println("尝试发送"+obj.toString()+" to "+s.getLocalSocketAddress());
            oos.writeObject(obj);
            oos.flush();


        }catch (IOException ioe){
            ioe.printStackTrace();
            return false;
        }
        return true;
    }


    //从服务器上获取信息
    public void beginRec(){
        bConnected=false;
        if(tRecv!=null){
            while (tRecv.isAlive());
        }
        bConnected = true;
        tRecv=new Thread(()->{
            try {
                while (bConnected) {
                    //连接成功
                    System.out.println("Listen:"+s.getLocalSocketAddress()+":"+s.getRemoteSocketAddress());
                    //监听对象消息



                    if(ois==null){
                        ois=new ObjectInputStream(s.getInputStream());
                    }

                    Object obj=ois.readObject();
                    if(messageIn!=null){
                        System.out.println("Msg Arrive:"+s.getLocalSocketAddress()+":"+s.getRemoteSocketAddress());
                        messageIn.processEvent(obj,this);     //客户端的信息到达事件处理
                    }
                    //taContent.setText(taContent.getText() + str + '\n');
                }
            } catch (SocketException e) {
                //recving=false;
                System.out.println("退出");
            } catch (EOFException e) {
                //recving=false;
                System.out.println("退出");
            } catch (IOException e) {
                //recving=false;
                e.printStackTrace();
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }

        });
        tRecv.start();
    }
    public void connect(String ip,int port) throws UnknownHostException,IOException{

        s = new Socket(ip, port);

        System.out.println("Create SC by "+s.getLocalSocketAddress());

        this.ip=ip;
        this.port=port;
        System.out.println("Connect Successful.");
        beginRec();




    }


    public void disconnect() {
        try {

            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /*private class RecvThread implements Runnable {

        public void run() {
            try {
                while (bConnected) {
                    //连接成功
                    String str = dis.readUTF();
                    //taContent.setText(taContent.getText() + str + '\n');
                }
            } catch (SocketException e) {
                System.out.println("退出了，bye!");
            } catch (EOFException e) {
                System.out.println("退出了，bye!");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }*/


    public static void main(String[] args){
        SocketClient sc=new SocketClient();
        try{
            sc.setMessageInEvent(new ClientMessageIn());
            sc.connect("127.0.0.1",3456);
            Scanner scss=new Scanner(System.in);

                //scss.nextLine();

            System.out.println("main Thread");
            sc.send("adaase4311");
            System.out.println("send finish");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
