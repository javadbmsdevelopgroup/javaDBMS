package network;
/////////////////////Socket事件类////////////////////////////////
class ClientInEvent implements IOneArugumentEvent{
    @Override
    public void doEvent(Object obj1){
        System.out.println(111);
    }
}
