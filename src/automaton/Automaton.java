package automaton;


import automaton.exception.AutomatonNodeException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//基本有限自动机类
public class Automaton implements Serializable {
    Map<Integer,AutomatonNode> automatonNodeMap=new HashMap<>();  //自动机结点Map
    TranstionFunc transitionFuns;  //迁移函数
    AutomatonNode startNode=null;   //起始结点

    //提供节点集，转移函数进行初始化
    public Automaton(List<AutomatonNode> automatonNodes,TranstionFunc ts) throws AutomatonNodeException {
        //check validity
        //state encoded from 1(not from 0)
        for(AutomatonNode node : automatonNodes){
            if(automatonNodeMap.containsKey(node.nodeCode)) throw new AutomatonNodeException("Repeat Automaton Code Number");
            else{
                automatonNodeMap.put(node.nodeCode,node);
            }
        }
        transitionFuns=ts;
        if(automatonNodeMap.containsKey(0)){
            startNode=automatonNodeMap.get(0);
        }
    }

    public Automaton(Automaton automaton){
        transitionFuns=automaton.transitionFuns;
        startNode=automaton.startNode;
        automatonNodeMap=automaton.automatonNodeMap;
    }

    //节点数
    public int getStateCount(){
        return automatonNodeMap.size();
    }
    //自动机编码。（没用上)
    public String getCodedAutomataStr(){
        String s="";
        return transitionFuns.getCodedStr();
    }


    //开始匹配句子
    public AutomatonNode matchingGrammar(String input,Object... objects){
        this.transitionFuns.infCollection.cleanStacks();  //清空信息收集器的堆栈
        if(startNode==null) return null;
        //将句子转化为输入项列表
        List<InputItem> is=AutomatonTools.getInstance().toInputList(input);

        //当前所在节点
        AutomatonNode cur=startNode;

        for(InputItem i:is){

            //尝试转移  参数1:开始节点  参数二:输入项 参数三：提供的转移参数
            cur=transitionFuns.transition(automatonNodeMap.get(cur.nodeCode),i,objects);

            //=null就是无法转移.语法无效
            if(cur==null){
                System.out.println("\n----------------------------\n"+"无法识别的语法"+" \n"+"----------------------------");
                return null;
            }
        }
        //判断是否是终止结点。不是终止结点说明语义不完整
        if(!cur.owariNode){
            System.out.println("\n----------------------------\n"+"未正常到达自动机终态"+" \n"+"----------------------------");
            return null;
        }
        return cur;
    }






}

