package network;

import java.net.Socket;

public class ThreadSupport implements SQLStrategy{
    private SQLStrategy sql;
    public ThreadSupport( SQLStrategy sql){
        this.sql = sql;
    }

    @Override
    public void service(Socket socket) {//create a thread for transmitting information to client
        new SQLThread(socket, sql).start();
    }
}
