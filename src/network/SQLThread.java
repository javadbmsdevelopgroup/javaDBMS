package network;

import java.net.ServerSocket;
import java.net.Socket;

public class SQLThread extends Thread{
    private Socket socket;
    private SQLStrategy sql;
    public SQLThread(Socket socket, SQLStrategy sql){
        this.socket = socket;
        this.sql = sql;
    }

    @Override
    public void run() {
        sql.service(socket);
    }
}
