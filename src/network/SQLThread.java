package network;

import java.net.ServerSocket;
import java.net.Socket;

public class SQLThread extends Thread{
    private Socket socket;
    private IOStrategy sql;
    public SQLThread(Socket socket, IOStrategy sql){
        this.socket = socket;
        this.sql = sql;
    }

    @Override
    public void run() {
        sql.service(socket);
    }
}
