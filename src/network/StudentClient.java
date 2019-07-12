package network;

import automaton.SQLSession;
import dbms.logic.Relation;
import dbms.view.RelationView;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Scanner;
import java.util.WeakHashMap;

public class StudentClient extends NwClient{
    int stuCode=-1;
    String name="";
    ObjectOutputStream oos;
    ObjectInputStream ois;
    public StudentClient(int stuCode,String ip,int selectCourseServerPort){
        super(ip,selectCourseServerPort);
        this.stuCode=stuCode;
        startClient();
    }
    private void startClient(){
        oos=super.getOos();
        ois=super.getOis();
        //告诉服务器学号
        try{
            System.out.println("通知服务器学号");
            oos.writeInt(100); //表示接下来要通知服务器学号
            oos.flush();
            oos.writeInt(this.stuCode);
            oos.flush();
            System.out.println("通知完毕");
            name=ois.readUTF();
            System.out.println("获取 姓名="+name);
            if(name.compareTo("")==0){
                System.out.println("不存在该学生");
                System.exit(0);
            }
            Scanner sc=new Scanner(System.in);
            while(true){
                System.out.println("请输入操作指令: 0:退出 1.查看个人信息 2.查看课程信息 3.选课 4.已选课程");
                try{
                    int com=sc.nextInt();
                    switch (com){
                        case 0:
                            super.end();
                            System.exit(0);
                            break;
                        case 1:
                            RelationView stuInf=getPersonalInf();
                            stuInf.printRelationView();
                            break;
                        case 2:
                            RelationView courseInf=getCourseInf();
                            courseInf.printRelationView();
                            break;
                        case 3:
                            System.out.println("请输入课程编号");
                            RelationView scInf=getStuCourseInf();  //已选记录
                            RelationView cs=getCourseInf();      //全部课程
                            cs.addConlum("选课状态",cs.getConlumNames().size());
                            List<String> columnNames=cs.getConlumNames();

                            for(int i=0;i<cs.getRowCount();i++){
                                boolean alreadSelect=false;
                                for(int j=0;j<scInf.getRowCount();j++){
                                    if( scInf.getVal(j,"课程编号").compareTo(cs.getVal(i,"课程编号"))==0){
                                        alreadSelect=true;
                                        break;
                                    }
                                }
                                if(alreadSelect){
                                    cs.setVal(i,"选课状态","已选");
                                }else{
                                    int residue=Integer.parseInt(cs.getVal(i,"余剩容量"));
                                    if(residue==0){
                                        cs.setVal(i,"选课状态","未选不可选");
                                    }else{
                                        cs.setVal(i,"选课状态","未选可选");
                                    }
                                }
                            }
                            cs.printRelationView();
                            System.out.println("请输入课程编号:");
                            int courseCode=sc.nextInt();
                            boolean success=false;
                            //判断是否可选
                            for(int i=0;i<cs.getRowCount();i++){
                                if(cs.getVal(i,"课程编号").compareTo(String.valueOf(courseCode))==0){
                                    if(cs.getVal(i,"选课状态").indexOf("未选")>=0){
                                        System.out.println("尝试选课 "+courseCode);
                                        oos.writeInt(103);
                                        oos.flush();
                                        oos.writeInt(this.stuCode); //发送学号
                                        oos.flush();
                                        //等待结果
                                        int result=ois.readInt();
                                        if(result>0) success=true;
                                    }else {
                                        System.out.println("该课程已选");
                                    }
                                    break;
                                }
                            }
                            if(!success){
                                System.out.println("选课失败,课程编号错误或课容量为0");
                            }else{
                                System.out.println("选课成功,课程编号错误或课容量为0");
                            }
                            break;
                        case 4:
                            RelationView stuCourseInf=getStuCourseInf();
                            stuCourseInf.printRelationView();
                            break;
                    }
                }catch (Exception e2){
                    e2.printStackTrace();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private RelationView getStuCourseInf() throws Exception{
        oos.writeInt(104);  //发送已选选课信息请求
        oos.flush();
        oos.writeInt(this.stuCode);  //发送学号
        oos.flush();
        Object obj=ois.readObject();
        if(!(obj instanceof RelationView)){
            throw new Exception("获取查询对象错误");
        }
        return (RelationView) obj;
    }

    private RelationView getCourseInf() throws Exception{
        oos.writeInt(102);   //发送获取所有课程信息请求
        oos.flush();
        Object obj=ois.readObject();
        if(!(obj instanceof RelationView)){
            throw new Exception("获取查询对象错误");
        }
        return (RelationView) obj;
    }
    private RelationView getPersonalInf() throws Exception {
        oos.writeInt(101);   //发送获取学生信息请求
        oos.flush();
        oos.writeInt(this.stuCode);  //发送学号
        oos.flush();
        Object obj=ois.readObject();
        if(!(obj instanceof RelationView)){
            throw new Exception("获取查询对象错误");
        }

        return (RelationView)obj;
    }


    public static void main(String[] args){
        StudentClient cc = new StudentClient(17000000,"127.0.0.1", 9239);  //连接上选课服务器


    }
}
