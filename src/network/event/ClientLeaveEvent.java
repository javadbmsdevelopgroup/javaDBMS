package network.event;

import java.net.Socket;

/////客户端离开
public class ClientLeaveEvent implements ITwoArugumentEvent{
    @Override
    public void doEvent(Object client,Object server){
        System.out.println("Client Leave:"+((Socket)client).getLocalSocketAddress()+","+((Socket)client).getRemoteSocketAddress());
    }
}