package network;

import automaton.SQLSession;
import network.event.InSelectCourseServerMsgIn;
import network.event.StudentClientInEvent;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


//选课服务器
public class SelectCourseServer{
    static Map<Integer, Socket> stuClientMap=new HashMap<>();
    static SQLClient sqlClient=new SQLClient("127.0.0.1",2999);

    static SQLSession sqlSession=new SQLSession("test");


    public static Object getResult(String com){
        Object[] objs=sqlClient.getResult(com,sqlSession);
        if(objs==null ||objs.length<2) return null;
        return objs[1];
    }

    public static void main(String[] args){
        NwServer selectCourseServer=new NwServer(9239,new ThreadSupport(new SelectCourseProtocol()));

        // SelectCourseServer selectCourseServer=new SelectCourseServer(9239);
        System.out.println("input 1 start, input 2 end, input 3 all-end");
        Scanner scanner = new Scanner(System.in);
        int x = 0;
        while (true){
            System.out.println("input:");
            try{
                x = scanner.nextInt();
            }
            catch (Exception e){
                scanner.nextLine();
                continue;
            }
            if(x == 1)
                selectCourseServer.startServer();
            else if (x == 2)
                selectCourseServer.endServer();
            else
                break;
        }
    }

}
