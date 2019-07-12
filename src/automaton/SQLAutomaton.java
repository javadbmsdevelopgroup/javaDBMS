package automaton;

import automaton.exception.AutomatonNodeException;

import java.io.Serializable;
import java.util.List;

public class SQLAutomaton extends Automaton implements Serializable {
    SQLSession sqlSession=null;
    public SQLAutomaton(List<AutomatonNode> automatonNodes, TranstionFunc ts,SQLSession sqlSession) throws AutomatonNodeException {
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
