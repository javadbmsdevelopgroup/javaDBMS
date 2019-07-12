package network.event;

import network.SocketServer;

import java.net.Socket;

/////客户端离开事件
public class ClientLeaveEvent implements ISocketEvent{

    @Override
    public Object processEvent(Object client,Object... arguments){

        SocketServer server=(SocketServer) arguments[0];
        System.out.println("Client Leave:client local:"+((Socket)client).getLocalSocketAddress()+",client remote"+((Socket)client).getRemoteSocketAddress());
        return null;
    }


    /*@Override
    public void doEvent(Object client,Object server){
        System.out.println("Client Leave:"+((Socket)client).getLocalSocketAddress()+","+((Socket)client).getRemoteSocketAddress());
    }*/
}