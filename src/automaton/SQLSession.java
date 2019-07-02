package automaton;
//数据库会话
public class SQLSession {
    public String curUseDatabase="";
    public Automaton sqlAutomaton=null;


    /////////////SQL会话测试
    public static void main(String[] args){
        SQLSession sqlSession=new SQLSession();
        SQLAutomaton sqlAutomaton=new SQLAutomaton(AutomatonBuilder.buildAutomaton(),sqlSession);
        sqlAutomaton.matchingGrammar("show tables");
    }
}
