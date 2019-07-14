package network;

import automaton.SQLSession;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.SynchronousQueue;

//服务器基类
public class NwClient {
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;

    Socket socket = null;

    public ObjectInputStream getOis() {
        return ois;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public NwClient(String host, int port){
        try {
            socket = new Socket(host, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());


        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Client socket build failed!");
            System.exit(0);
        }
    }
    //transmit a sql command and a object to Server and get result
    public Object[] getResult(String sqlCommand, Object sql)  {
        try {

            oos.writeInt(1);
            oos.flush();
            oos.writeObject(sqlCommand);
            oos.flush();
            oos.writeObject(sql);
            oos.flush();
            sql = ois.readObject();
            //System.out.println("+"+((SQLSession)sql).curUseDatabase);
            Object result = ois.readObject();
            Object[] obj = new Object[2];
            obj[0] = sql;
            obj[1] = result;
            return obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
    public void end(){
        try {
            oos.writeInt(0);
            oos.flush();
            System.out.println("Client ending!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
