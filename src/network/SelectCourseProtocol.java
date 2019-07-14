package network;

import automaton.AutomatonBuilder;
import automaton.AutomatonNode;
import automaton.SQLAutomaton;
import automaton.SQLSession;
import dbms.Tools;
import dbms.view.RelationView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;


//选课服务器
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
                        int courseCode=dis.readInt();
                        System.out.println("stu:"+stuCode+" want to select "+courseCode);
                        //判断是否已选
                        Object selectedInf=SelectCourseServer.getResult("select * from stuCourse where 学号="+stuCode+" and 课程编号="+courseCode+";");
                        if(selectedInf==null || !(selectedInf instanceof RelationView)) {
                            dos.writeInt(-10);
                            dos.flush();
                            break;
                        }else if(((RelationView)selectedInf).getRowCount()>0) {
                            dos.writeInt(-11); //表示已选
                            dos.flush();
                            break;
                        }

                        //先减课容量
                        //尝试选课
                        Integer updateResult=(Integer)SelectCourseServer.getResult(
                                "update course set 余剩容量=余剩容量-1,已选人数=已选人数+1 where 课程编号="+courseCode+";");
                        if(updateResult>0){
                            System.out.println("为"+stuCode+":"+courseCode+"更新课容量成功");
                            String current = String.valueOf(System.currentTimeMillis());
                            Integer insertResult=(Integer)SelectCourseServer.getResult(
                                    "insert into stuCourse values("+stuCode+","+courseCode+","+current+")");
                            if(insertResult>0){
                                System.out.println("insert record success.");
                            }else{
                                System.out.println("insert record fail.");
                            }
                            dos.writeInt(insertResult);
                            dos.flush();
                        }else{
                            System.out.println("尝试为"+stuCode+" 选课 "+courseCode+"失败");
                            dos.writeInt(-1); //表示课容量修改失败
                            dos.flush();
                        }
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
                    case 105: //退课
                        System.out.println("收到退课请求");
                        //获取学号
                        stuCode=dis.readInt();
                        //获取课程编号
                        courseCode=dis.readInt();
                        //操作
                        //先删除选课记录
                        int deleteResult=(Integer) SelectCourseServer.getResult("delete from stuCourse where 学号="
                                +stuCode+" and 课程编号="+courseCode+";");

                        System.out.println("删除"+deleteResult);
                        if(deleteResult<=0){
                            dos.writeInt(deleteResult);
                            dos.flush();
                            break;
                        }else{
                            updateResult=(Integer)SelectCourseServer.getResult("update course set 已选人数=已选人数-1,余剩容量=余剩容量+1 where 课程编号="+courseCode+";");
                            if(updateResult>0){
                                System.out.println("为:"+stuCode+"退课成功");
                            }
                            dos.writeInt(updateResult);
                            dos.flush();
                            break;
                        }
                }
            }

        }catch (Exception e){
            if(e.getMessage()==null) e.printStackTrace();
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
