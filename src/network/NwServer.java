package network;
import java.net.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class NwServer {//building a server and create socket object for connecting
    //the constructor for this class
    private ServerSocket server = null;
    private IOStrategy sql = null;
    public static List<Socket> sockets = null;
    public boolean kkey;
    private int flag ;
    public NwServer(int port, IOStrategy sql){
        this.sql = sql;
        this.flag = 0;
        sockets = new LinkedList<Socket>();
        try {
            server = new ServerSocket(port);
        }catch (Exception e){
            System.out.println("the server building fails!");
            System.exit(0);
        }
    }
    public void startServer(){
        if(flag == 1)
            return;
        flag++;
        kkey = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("the server is ready!");
                    while (true) {
                        Socket socket = server.accept();
                        sockets.add(socket);
                        sql.service(socket);
                        if(kkey)
                            break;

                    }
                }catch (Exception e){
                    System.out.println("server blows off!");
                }
            }
        }).start();
    }
    public void endServer(){
        if(flag == 0)
            return;
            flag--;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    kkey = true;
                    Iterator<Socket> iterator = sockets.iterator();
                    while (iterator.hasNext()){
                        Socket s = iterator.next();
                        if(!s.isClosed())
                            s.close();
                    }
                    server.close();
                }catch (Exception e){
                    System.out.println("server end is failed!");
                }
            }
        }).start();
    }
}
