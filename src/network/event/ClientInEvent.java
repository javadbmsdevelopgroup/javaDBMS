package network.event;

import network.SocketClient;
import network.SocketServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
///////////////////////////////////客户端进入
public class ClientInEvent implements ITwoArugumentEvent{
    @Override
    public void doEvent(Object client,Object server){
        System.out.println("Client In:"+((Socket)client).getLocalSocketAddress()+","+((Socket)client).getRemoteSocketAddress());
        ((SocketServer)server).sendMessageToClient("来自服务器的问候",(Socket)client);


    }
}
