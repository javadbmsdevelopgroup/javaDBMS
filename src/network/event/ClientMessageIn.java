package network.event;

public class ClientMessageIn implements ITwoArugumentEvent{
    @Override
    public void doEvent(Object message,Object client){
        System.out.println("Client Message In: "+(String)message);
    }
}
