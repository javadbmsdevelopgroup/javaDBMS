package network;
/////////////////////////////////////Socket事件接口类////////////////////////////////
//无参事件接口
interface INonArugumentEvent{
    void doEvent();
}
//一个参数的事件接口
interface IOneArugumentEvent{
    void doEvent(Object obj1);
}