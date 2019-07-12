package network.event;

import network.SocketClient;

import java.net.Socket;

//客户端收到来自服务器的消息的事件
public class ClientMessageIn implements ISocketEvent{
    @Override
    public Object processEvent(Object message,Object... arguments){
        Object client=arguments[0];
        System.out.println("Client Message In: "+message.toString());
        return null;
    }


}
