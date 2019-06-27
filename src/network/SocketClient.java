package network;
import network.event.ClientMessageIn;
import network.event.IOneArugumentEvent;
import network.event.ITwoArugumentEvent;

import java.net.*;
import java.io.*;


/**
 * @author Michael Huang
 *
 */
public class SocketClient {
    Socket s = null;   //服务器的socket
    DataOutputStream dos = null;
    DataInputStream dis = null;
    String ip;
    int port;
    private boolean bConnected = false;
    private ITwoArugumentEvent messageIn=null;

    Thread tRecv;


    public SocketClient(){

    }
    public SocketClient(Socket s){
        this.s=s;
        if(s==null) throw  new NullPointerException();
        try{
        dos=new DataOutputStream(s.getOutputStream());
        dis=new DataInputStream(s.getInputStream());
        beginRec();
        }
        catch (Exception e){

        }
    }
    public void setMessageInEvent(ITwoArugumentEvent event){
        this.messageIn=event;
    }
    public boolean send(String str){
        if(s==null) return false;
        try{
            System.out.println("尝试发送"+str);
            dos.writeUTF(str);
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
                    String str = dis.readUTF();
                    if(messageIn!=null){
                        messageIn.doEvent(str,this);     //信息到达时间处理
                    }
                    //taContent.setText(taContent.getText() + str + '\n');
                }
            } catch (SocketException e) {
                System.out.println("退出");
            } catch (EOFException e) {
                System.out.println("退出");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        tRecv.start();
    }
    public void connect(String ip,int port) throws UnknownHostException,IOException{

        s = new Socket(ip, port);
        dos = new DataOutputStream(s.getOutputStream());
        dis = new DataInputStream(s.getInputStream());
        this.ip=ip;
        this.port=port;
        System.out.println("Connect Successful.");
        beginRec();
        send("aakb");



    }


    public void disconnect() {
        try {
            dos.close();
            dis.close();
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
