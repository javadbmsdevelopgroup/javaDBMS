package automaton;

import java.util.List;

public class AutomatonNode {
    public int nodeCode=-1;
    public boolean owariNode=false;
    public INodeFunc nodeMethod=null;
    public Object exeResult=null;

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
