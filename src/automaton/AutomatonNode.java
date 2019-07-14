package automaton;

import java.io.Serializable;
import java.util.List;

//自动机结点
public class AutomatonNode implements Serializable {
    public int nodeCode=-1;   //结点编号
    public boolean owariNode=false;  //是否是一个终止结点
    public INodeFunc nodeMethod=null;  //结点处理函数.如果是终止结点，到达该节点后将会执行实现了这个接口的函数。返回值会放在底下的exeResult
    public Object exeResult=null;     //处理函数执行结果

    public AutomatonNode(int nodeCode,boolean owariNode){
        this.nodeCode=nodeCode;
        this.owariNode=owariNode;
    }

    public AutomatonNode(int nodeCode,boolean owariNode,INodeFunc method){
        this.nodeCode=nodeCode;
        this.owariNode=owariNode;
        nodeMethod=method;
    }

}
