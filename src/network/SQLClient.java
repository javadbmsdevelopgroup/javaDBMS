package network;

import automaton.AutomatonBuilder;
import automaton.SQLAutomaton;
import automaton.SQLSession;
import dbms.view.RelationView;

import java.net.SocketException;
import java.util.Scanner;


//SQL客户端
public class SQLClient extends NwClient{

    public SQLClient(String ip,int port){
        super(ip,port);
    }

    public static void main(String[] args){
        SQLClient cc = new SQLClient("127.0.0.1", 2999);
        SQLSession sqlSession=new SQLSession();


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



        }




    }
}
