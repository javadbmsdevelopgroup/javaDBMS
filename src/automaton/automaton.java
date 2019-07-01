package automaton;


import java.util.List;
//有限自动机类
public class Automaton{
    int states;    //状态数
    int endindex;  //终态
    int sindex=0;
    TranstionFunc transitionFuns=new TranstionFunc();  //转移函数
    public Automaton(int statecount,int endstateindex,int startstateindex,TranstionFunc ts) throws Exception{
        //check validity
        //state encoded from 1(not from 0)
        if(endstateindex>statecount || endstateindex<0 || startstateindex>statecount || startstateindex<0)
            throw new Exception("arguments error");
        states=statecount;
        endindex=endstateindex;
        sindex=startstateindex;
        transitionFuns=ts;
    }


    public String getCodedAutomataStr(){
        String s="";
        return transitionFuns.getCodedStr();
    }
    //return machingNumber

    public int matchingGrammar(String input){
        List<InputItem> is=AutomatonTools.getInstance().toInputList(input);
        int cur=sindex;
        System.out.println("目标句子:"+input);
        System.out.println("编码后的自动机:"+getCodedAutomataStr());
        int c=0;
        for(InputItem i:is){
            System.out.print("当前输入:"+i.content+"\t\t 状态:"+cur+"->");
            cur=transitionFuns.transition(cur,i);
            System.out.println(cur);
            if(cur==endindex) {c++; System.out.println("----------------------------\n"+"匹配数+1 total:"+c+"   自动机状态初始化\n"+"----------------------------"); cur=1;}
        }
        //System.out.println(transitionFuns.transition(1,));
        return c;
    }






}

