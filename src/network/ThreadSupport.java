package network;

import java.net.Socket;

public class ThreadSupport implements IOStrategy{
    private IOStrategy sql;
    public ThreadSupport( IOStrategy sql){
        this.sql = sql;
    }

    @Override
    public void service(Socket socket) {//create a thread for transmitting information to client
        new SQLThread(socket, sql).start();
    }
}
