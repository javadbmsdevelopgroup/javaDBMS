package network.event;

public interface ISocketEvent {
    Object source=null;
    Object processEvent(Object eventSource,Object... arguments);
}
