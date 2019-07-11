package automaton;

import dbms.CacheManage;
import dbms.TableReadWriteLock;
import dbms.view.RelationView;

import java.util.Scanner;
import java.util.concurrent.locks.Lock;

//sql会话              这是主要进行数据库操作的地方
public class SQLSession {
    public String curUseDatabase="";
    public Automaton sqlAutomaton=null;

    /////////////SQL会话
    public static void main(String[] args){
        SQLSession sqlSession=new SQLSession();
        SQLAutomaton sqlAutomaton=new SQLAutomaton(AutomatonBuilder.buildAutomaton(),sqlSession);
        Scanner sc=new Scanner(System.in);
        CacheManage.loadAllindex();


        System.out.println("SQL会话创建成功");
        while (true){
            System.out.print("YuiSQL> ");
            String sql=sc.nextLine();
            if(sql.compareTo("\\q")==0) return;
            Object obj=sqlAutomaton.matchingGrammar(sql).exeResult;
            if(obj instanceof RelationView){
                ((RelationView)((RelationView) obj)).printRelationView();
            }
        }



    }

}
