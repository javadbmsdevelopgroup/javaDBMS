package network;
import network.event.ClientMessageIn;
import network.event.IOneArugumentEvent;
import network.event.ITwoArugumentEvent;

import java.net.*;
import java.io.*;
import java.util.Scanner;


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

        //System.out.println("Create SC by "+s.getLocalSocketAddress());
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
            PrintWriter pw=new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

            System.out.println("In client: send:"+s.getLocalSocketAddress()+"->"+s.getRemoteSocketAddress());
            System.out.println("尝试发送"+str+" to "+s.getLocalSocketAddress());
            pw.write(str+"\r\n");

            pw.flush();
            //pw.flush();
            //dos=new DataOutputStream(s.getOutputStream());
            //boolean a=true;
            //dos.writeUTF(str);

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
                    String str = dis.readUTF();
                    if(messageIn!=null){
                        System.out.println("Msg Arrive:"+s.getLocalSocketAddress()+":"+s.getRemoteSocketAddress());
                        messageIn.doEvent(str,this);     //信息到达时间处理
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
            }

        });
        tRecv.start();
    }
    public void connect(String ip,int port) throws UnknownHostException,IOException{

        s = new Socket(ip, port);

        System.out.println("Create SC by "+s.getLocalSocketAddress());
        dos = new DataOutputStream(s.getOutputStream());
        dis = new DataInputStream(s.getInputStream());
        this.ip=ip;
        this.port=port;
        System.out.println("Connect Successful.");
        beginRec();




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
            Scanner scss=new Scanner(System.in);

                //scss.nextLine();

            System.out.println("main Thread");
            sc.send("AKBsdd试试");
            System.out.println("send finish");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
