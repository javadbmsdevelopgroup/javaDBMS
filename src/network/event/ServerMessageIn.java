package network.event;

import java.net.Socket;

public class ServerMessageIn implements IThreeArugumentEvent{
    /////服务器收到消息的事件
    @Override
    public void doEvent(Object message,Object client,Object server){
        System.out.println("Serveer Message In:"+((Socket)client).getLocalSocketAddress()+" M="+(String)message);;
    }

}
