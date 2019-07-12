package network.event;

import com.sun.corba.se.spi.activation.Server;
import network.SocketClient;
import network.SocketServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
///////////////////////////////////客户端进入事件
public class ClientInEvent implements ISocketEvent{
    @Override
    public Object processEvent(Object client,Object... arguments){
        System.out.println("Client In:"+((Socket)client).getLocalSocketAddress()+","+((Socket)client).getRemoteSocketAddress());
        SocketServer server=(SocketServer) arguments[0];
        server.sendMessageToClient("来自服务器的问候",(Socket)client);   //客户端进入后，向客户端发送一条消息.
        return null;
    }

    /*@Override
    public void doEvent(Object client,Object server){
        System.out.println("Client In:"+((Socket)client).getLocalSocketAddress()+","+((Socket)client).getRemoteSocketAddress());
        ((SocketServer)server).sendMessageToClient("来自服务器的问候",(Socket)client);


    }*/
}
