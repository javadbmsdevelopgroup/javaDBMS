package network;

import automaton.AutomatonBuilder;
import automaton.SQLAutomaton;
import automaton.SQLSession;
import dbms.view.RelationView;

import java.net.SocketException;
import java.util.Scanner;

public class SQLClient {

    public static void main(String[] args){
        ManagerClient cc = new ManagerClient("127.0.0.1", 2999);
        SQLSession sqlSession=new SQLSession();

        //sqlSession.sqlAutomaton=sqlAutomaton;
        String com;
        while(true){
            Scanner sc=new Scanner(System.in);
            com=sc.nextLine();
            if(com.compareTo("\\q")==0) {
                cc.end();
                System.exit(0);
            }



            Object[] re =  cc.getResult(com,sqlSession);
            if(re!=null){
                SQLSession sqlSession1=(SQLSession) re[0];
                Object result=re[1];
                if(result==null) {
                    System.out.println("执行错误");
                    continue;
                }

                if(result instanceof RelationView){
                    ((RelationView)result).printRelationView();
                }else{
                    System.out.println(result.toString());
                }
            }
//update course set 课程编号=课程编号+10 where 课程名称=JDK的安装与卸载;
//delete from course where 课程编号=17013;


        }



        //cc.end();
//        cc.serverEnd();
//        NwClient c = new NwClient("127.0.0.1", 2999);
//        c.end();
    }
}
