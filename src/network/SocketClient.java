package network;
import java.net.*;
import java.io.*;


/**
 * @author Michael Huang
 *
 */
public class ChatClient {
    Socket s = null;
    DataOutputStream dos = null;
    DataInputStream dis = null;
    private boolean bConnected = false;


    Thread tRecv = new Thread(new RecvThread());

    public void connect(String ip,int port) {
        try {
            s = new Socket(ip, port);
            dos = new DataOutputStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());
            System.out.println("~~~~~~~~连接成功~~~~~~~~!");
            bConnected = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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



    private class RecvThread implements Runnable {

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

    }
}
