package network.event;

import network.SelectCourseServer;
import network.SocketServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

//学生客户端进入事件
public class StudentClientInEvent implements ISocketEvent {
    Socket socket=null;
    @Override
    public Object processEvent(Object eventSource,Object... arguments){
        SelectCourseServer server=(SelectCourseServer) eventSource;
        Socket studentSocket=(Socket)arguments[0];
        socket=studentSocket;
        System.out.println(socket.getInetAddress().getHostAddress()+"SUCCESS TO CONNECT...");
        new sendMessThread().start();
        return null;
    }

    class sendMessThread extends Thread{
        @Override
        public void run(){
            super.run();
            Scanner scanner=null;
            OutputStream out = null;
            try{
                if(socket != null){
                    scanner = new Scanner(System.in);
                    out = socket.getOutputStream();
                    String in = "";
                    do {
                        in = scanner.next();
                        out.write(("server saying: "+in).getBytes());
                        out.flush();//清空缓存区的内容
                    }while (!in.equals("q"));
                    scanner.close();
                    try{
                        out.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
