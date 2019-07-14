package automaton;

import automaton.exception.AutomatonNodeException;

import java.io.Serializable;
import java.util.List;

//SQL语句的自动机，继承了普通的自动机.
public class SQLAutomaton extends Automaton implements Serializable {
    SQLSession sqlSession=null;  //sql会话对象

    public SQLAutomaton(List<AutomatonNode> automatonNodes, TranstionFunc ts,SQLSession sqlSession) throws AutomatonNodeException {
        //初始化父类
        super(automatonNodes,ts);
        this.sqlSession=sqlSession;
    }

    public SQLAutomaton(Automaton automaton,SQLSession sqlSession){
        super(automaton);
        this.sqlSession=sqlSession;
    }
    public AutomatonNode matchingGrammar(String input){
        return super.matchingGrammar(input,this.sqlSession);
    }
}
