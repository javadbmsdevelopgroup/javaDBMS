package automaton;


import automaton.exception.AutomatonNodeException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//有限自动机类
public class Automaton{
    Map<Integer,AutomatonNode> automatonNodeMap=new HashMap<>();
    TranstionFunc transitionFuns=new TranstionFunc();  //转移函数
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
    //return machingNumber

    public AutomatonNode matchingGrammar(String input,Object... objects){
        System.out.println("obj count:"+objects.length);
        if(startNode==null) return null;
        List<InputItem> is=AutomatonTools.getInstance().toInputList(input);
        System.out.println("目标句子:"+input);
        for (InputItem inputItem:is){
            System.out.println(inputItem);
        }

        AutomatonNode cur=startNode;

        //System.out.println("编码后的自动机:"+getCodedAutomataStr());
        for(InputItem i:is){
            System.out.print("当前输入:"+i.content+"\t\t 状态:"+cur.nodeCode+"->");
            cur=transitionFuns.transition(automatonNodeMap.get(cur.nodeCode),i,objects);
            //System.out.println(cur.nodeCode);
            if(cur==null){
                System.out.println("\n----------------------------\n"+"无法识别的语法"+" \n"+"----------------------------");
                return null;
            }
            if(cur.owariNode) {System.out.println("----------------------------\n"+"转移到达终态，无语法错误"+" \n"+"----------------------------");}
        }

        return cur;
    }






}

