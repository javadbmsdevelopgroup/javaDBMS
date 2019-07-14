package automaton;


import automaton.exception.AutomatonNodeException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//有限自动机类
public class Automaton implements Serializable {
    Map<Integer,AutomatonNode> automatonNodeMap=new HashMap<>();
    TranstionFunc transitionFuns;  //转移函数
    AutomatonNode startNode=null;
    //提供节点集，转移函数
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

    public int getStateCount(){
        return automatonNodeMap.size();
    }
    public String getCodedAutomataStr(){
        String s="";
        return transitionFuns.getCodedStr();
    }


    public AutomatonNode matchingGrammar(String input,Object... objects){
        this.transitionFuns.infCollection.cleanStacks();

        if(startNode==null) return null;
        List<InputItem> is=AutomatonTools.getInstance().toInputList(input);

        AutomatonNode cur=startNode;

        for(InputItem i:is){
            //System.out.print("当前输入:"+i.content+"\t\t 状态:"+cur.nodeCode+"->");
            cur=transitionFuns.transition(automatonNodeMap.get(cur.nodeCode),i,objects);
            if(cur==null){
                System.out.println("\n----------------------------\n"+"无法识别的语法"+" \n"+"----------------------------");
                return null;
            }
        }
        if(!cur.owariNode){
            System.out.println("\n----------------------------\n"+"未正常到达自动机终态"+" \n"+"----------------------------");
            return null;
        }
        return cur;
    }






}

