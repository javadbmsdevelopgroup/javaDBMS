package network;

import automaton.AutomatonBuilder;
import automaton.AutomatonNode;
import automaton.SQLAutomaton;
import automaton.SQLSession;
import dbms.view.RelationView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SelectCourseProtocol implements IOStrategy{



    @Override
    public void service(Socket socket) {
        try{
            ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream dis = new ObjectInputStream(socket.getInputStream());
            int command = 0;
            int stuCode;
            while (true){
                command = dis.readInt();
                switch (command){
                    case 100:
                        System.out.println("客户端将通知学号");
                        stuCode=dis.readInt();
                        System.out.println("收到学号通知"+stuCode);
                        String stuName="xxx";          //返回学生的姓名
                        //查询
                        Object obj=SelectCourseServer.getResult("select * from student where 学号="+stuCode+";");
                        if(obj==null) {
                            dos.writeUTF("");
                            dos.flush();
                            break;
                        }
                        RelationView rv=(RelationView)obj;
                        if(rv.getRowCount()==0){
                            dos.writeUTF("");
                            dos.flush();
                            break;
                        }
                        stuName=rv.getVal(0,"姓名");
                        dos.writeUTF(stuName);
                        dos.flush();
                        break;
                    case 101:
                        System.out.println("收到学生信息查询请求");
                        stuCode=dis.readInt();
                        //查询
                        Object stuInf=SelectCourseServer.getResult("select * from student where 学号="+stuCode+";");
                        dos.writeObject(stuInf);
                        dos.flush();
                        break;
                    case 102:
                        System.out.println("收到课程信息查询请求");
                        //查询
                        Object courseInf=SelectCourseServer.getResult("select * from course;");
                        dos.writeObject(courseInf);
                        dos.flush();
                        break;

                        default:
                            System.out.println("Unknow command"+command);
                            break;
                    case 103:
                        System.out.println("收到选课请求");
                        //获取学号
                        stuCode=dis.readInt();

                        break;
                    case 104:
                        System.out.println("收到选课信息查询");
                        //获取学号
                        stuCode=dis.readInt();
                        //查询
                        Object stuCourseInf=SelectCourseServer.getResult("select * from stuCourse where 学号="+stuCode+";");
                        dos.writeObject(stuCourseInf);
                        dos.flush();

                        break;
                }
            }

        }catch (Exception e){
            if(!(e.getMessage().compareTo("Connection reset")==0)){
                e.printStackTrace();
            }else{
                System.out.println("client "+socket.getRemoteSocketAddress()+"is disconnected");
            }

            try {
                socket.close();
            }catch (Exception ee){
                ee.printStackTrace();
            }
        }
    }
}
