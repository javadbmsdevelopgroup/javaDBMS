package network.event;

import network.SocketServer;

import java.net.Socket;

//服务器接收到来自某个客户端的消息的事件
public class InSelectCourseServerMsgIn implements ISocketEvent {
    Socket s;
    @Override
    public Object processEvent(Object eventSource,Object... arguments){
        s=(Socket)eventSource;
        Object msg=arguments[0];
        System.out.println("Client say:"+msg);
        return null;
    }

}
