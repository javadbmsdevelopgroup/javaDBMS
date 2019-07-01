package network.event;

//客户端收到来自服务器的消息的事件
public class ClientMessageIn implements ITwoArugumentEvent{
    @Override
    public void doEvent(Object message,Object client){
        System.out.println("Client Message In: "+(String)message);
    }
}
