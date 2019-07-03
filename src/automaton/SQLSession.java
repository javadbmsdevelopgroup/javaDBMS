package automaton;

import java.util.Scanner;

//sql会话
public class SQLSession {
    public String curUseDatabase="";
    public Automaton sqlAutomaton=null;


    /////////////SQL会话
    public static void main(String[] args){
        SQLSession sqlSession=new SQLSession();
        SQLAutomaton sqlAutomaton=new SQLAutomaton(AutomatonBuilder.buildAutomaton(),sqlSession);
        Scanner sc=new Scanner(System.in);
        while (true){
            System.out.print("YuiSQL> ");
            String sql=sc.nextLine();
            if(sql.compareTo("\\q")==0) return;
            sqlAutomaton.matchingGrammar(sql);
        }

    }
}
