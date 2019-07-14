import automaton.Automaton;
import automaton.AutomatonBuilder;
import automaton.SQLAutomaton;
import automaton.SQLSession;

public class ResetDataSet {
    public static void main(String[] args){
        SQLSession sqlSession=new SQLSession();
        SQLAutomaton sqlAutomaton=new SQLAutomaton(AutomatonBuilder.buildAutomaton(),sqlSession);
        sqlAutomaton.matchingGrammar("use test");
        sqlAutomaton.matchingGrammar("update course set 余剩容量=课程容量,已选人数=0 where 1=1;");
        sqlAutomaton.matchingGrammar("delete from stuCourse where 1=1;");
        sqlAutomaton.matchingGrammar("use studentDB");
        sqlAutomaton.matchingGrammar("update course set 余剩容量=课程容量,已选人数=0 where 1=1;");
        sqlAutomaton.matchingGrammar("delete from stuCourse where 1=1;");
    }
}
