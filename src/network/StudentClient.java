package network;

import dbms.Tools;
import dbms.view.RelationView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Scanner;


public class StudentClient extends NwClient{
    int stuCode=-1;
    String name="";
    ObjectOutputStream oos;
    ObjectInputStream ois;
    public StudentClient(int stuCode,String ip,int selectCourseServerPort){
        super(ip,selectCourseServerPort);
        this.stuCode=stuCode;

        oos=super.getOos();
        ois=super.getOis();

    }

    public void selectMain() throws Exception {
        System.out.println("--------------------------------");
        System.out.println("        欢迎来到选课系统!        ");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("--------------------------------");
//        mainInterface();
        while (true){
            System.out.println("请输入学号:(回车结束)");
            Scanner scanner = new Scanner(System.in);
            Integer f = null;
            if ((f = tryGetInt32(scanner)) != null) {
                this.stuCode = f;
                System.out.println("已输入学号:" + stuCode);
                if(shakeHand())
                    break;
            } else {
                System.out.println("学号输入格式不正确");
            }
        }

    }

    public static Integer tryGetInt32(Scanner sc){
        String s=sc.nextLine();
        try{
            while (s.compareTo("")==0)
                s=sc.nextLine();
            Integer val=Integer.parseInt(s.trim());
            return val;
        }catch (Exception e){
            return null;
        }
    }
    public boolean shakeHand() throws Exception{
        oos.writeInt(100); //表示接下来要通知服务器学号
        oos.flush();
        oos.writeInt(this.stuCode);
        oos.flush();
        name=ois.readUTF();
        if(name.compareTo("")==0){
            System.out.println("不存在该学生");
            System.exit(0);
            return false;
        }
        return true;
    }
    public String getName(){
        return name;
    }
    private void startClient(){

        //告诉服务器学号
        try{
            selectMain();
            Scanner sc=new Scanner(System.in);
            while(true){
                System.out.println("欢迎您，" + this.name);
                System.out.println("    1   个人信息");
                System.out.println("    2   课程信息");
                System.out.println("    3   进行选课");
                System.out.println("    4   已选课表");
                System.out.println("    5   退选课程");
                System.out.println("    0   退出系统");
                System.out.println();
                System.out.println("请输入操作对应的序号:");
                try{
                    int com=sc.nextInt();
                    switch (com){
                        case 0:
                            super.end();
                            System.exit(0);
                            break;
                        case 1:
                            System.out.println("个人信息如下：");
                            RelationView stuInf=getPersonalInf();
                            stuInf.printRelationView();
                            break;
                        case 2:
                            System.out.println("所有课程如下：");
                            RelationView courseInf=getCourseInf();
                            courseInf.printRelationView();
                            break;
                        case 3:
                            System.out.println("进行选课，请输入课程编号：");
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
                            int courseCode = 0;
                            while (true){
                                System.out.println("请输入课程编号:");
                                Integer f = null;
                                if ((f = tryGetInt32(sc)) != null) {
                                    courseCode = f;
                                    break;
                                } else {
                                    System.out.println("输入格式不正确");
                                }
                            }
                            boolean success=false;

                            //判断是否可选
                            for(int i=0;i<cs.getRowCount();i++){
                                if(cs.getVal(i,"课程编号").compareTo(String.valueOf(courseCode))==0){
                                    if(cs.getVal(i,"选课状态").indexOf("未选")>=0){
                                        System.out.println("尝试选课 "+courseCode);
                                        //选课
                                        int selectCourseResult=selectCourse(courseCode);
                                        success=selectCourseResult>0?true:false;
                                        if(!success){
                                            System.out.println("选课失败,课程编号错误或课容量不足");
                                        }else{
                                            System.out.println("选课成功");
                                        }


                                    }else {
                                        System.out.println("该课程已选");
                                        break;
                                    }
                                    break;
                                }
                            }


                            break;
                        case 4:
                            System.out.println("已选课程如下：");
                            RelationView stuCourseInf=getStuCourseInf();
                            stuCourseInf.printRelationView();
                            break;
                        case 5:
                            System.out.println("您已选了如下课程：");
                            RelationView rv=getStuCourseInf();
                            rv.printRelationView();
                            int c=0;
                            while (true){
                                System.out.println("请输入课程编号:");
                                Integer f = null;
                                if ((f = tryGetInt32(sc)) != null) {
                                    c = f;
                                    break;
                                } else {
                                    System.out.println("输入格式不正确");
                                }
                            }
                            if(withdrawlCourse(c)>0){
                                System.out.println("退课成功");
                            }else {
                                System.out.println("退课失败");
                            }
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

    public int getStuCode(){
        return stuCode;
    }

    public int withdrawlCourse(int course) throws Exception{

        oos.writeInt(105);
        oos.flush();
        oos.writeInt(stuCode);
        oos.flush();
        oos.writeInt(course);
        oos.flush();
        int result=ois.readInt();
        return result;
    }

    public int selectCourse(int courseCode) throws Exception{

        oos.writeInt(103);  //通知服务器要选课
        oos.flush();
        oos.writeInt(this.stuCode); //发送学号
        oos.flush();
        oos.writeInt(courseCode);  //发送课程编号
        oos.flush();
        //获取结果
        int result=ois.readInt();
        System.out.println(result);
        return result;
    }
    private RelationView getStuCourseInf() throws Exception{
        oos.writeInt(104);  //发送已选选课信息请求
        oos.flush();
        oos.writeInt(this.stuCode);  //发送学号
        oos.flush();
        Object obj=ois.readObject();
        if(obj==null || !(obj instanceof RelationView)){
            throw new Exception("获取查询对象错误");
        }
        RelationView rv=(RelationView) obj;
        for(int i=0;i<rv.getRowCount();i++){
            long t= Long.parseLong(Tools.removeAllZero(rv.getVal(i,"选课时间")));
            rv.setVal(i,"选课时间", Tools.getDateFromLong(t));
        }
        return rv;
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
        cc.startClient();
    }
}
