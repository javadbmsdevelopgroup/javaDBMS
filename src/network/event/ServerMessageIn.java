package network.event;

import java.net.Socket;

public class ServerMessageIn implements IThreeArugumentEvent{
    /////服务器消息进入
    @Override
    public void doEvent(Object message,Object client,Object server){
        System.out.println("Serveer Message In:"+((Socket)client).getLocalSocketAddress()+" M="+(String)message);
    }

}
