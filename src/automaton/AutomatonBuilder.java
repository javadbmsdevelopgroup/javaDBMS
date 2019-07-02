package automaton;

import automaton.methods.KeyWordTrans;
import automaton.methods.ShowMethod;

import java.util.ArrayList;
import java.util.List;

public class AutomatonBuilder {
    public static Automaton buildAutomaton(){
        AutomatonNode start=new AutomatonNode(0,false);  //起点
        List<AutomatonNode> automatonNodeList=new ArrayList<>();
        TranstionFunc transtionFunc=new TranstionFunc();
        //show
        ////node
        AutomatonNode show_node1=new AutomatonNode(1,false);
        AutomatonNode show_node2=new AutomatonNode(2,true,new ShowMethod());
        AutomatonNode show_node3=new AutomatonNode(3,true,new ShowMethod());
        ////trans Edge
        TransitionInf show_tans1=new TransitionInf(start,show_node1,TransitionType.KEYWORD,"SHOW");
        TransitionInf show_tans2=new TransitionInf(show_node1,show_node2,TransitionType.KEYWORD,"DATABASES",new KeyWordTrans());
        TransitionInf show_tran3=new TransitionInf(show_node1,show_node3,TransitionType.KEYWORD,"TABLES",new KeyWordTrans());
        automatonNodeList.add(start);
        automatonNodeList.add(show_node1);
        automatonNodeList.add(show_node2);
        automatonNodeList.add(show_node3);
        transtionFunc.addAtransition(show_tans1);
        transtionFunc.addAtransition(show_tans2);
        transtionFunc.addAtransition(show_tran3);


        try{
            Automaton automaton=new Automaton(automatonNodeList,transtionFunc);
            System.out.println("自动机创建成功");
            return automaton;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


}
