package network.event;

import network.SocketClient;

import java.net.Socket;

public class ServerMessageIn implements ISocketEvent{
    /////服务器收到消息的事件
    @Override
    public Object processEvent(Object message,Object... arguments){
        Object client=arguments[0];
        Object server=arguments[1];
        System.out.println("Server Message In:"+" M="+message.toString());;
        return null;
    }
    /*@Override
    public void doEvent(Object message,Object client,Object server){
        System.out.println("Serveer Message In:"+((Socket)client).getLocalSocketAddress()+" M="+(String)message);;
    }*/

}
