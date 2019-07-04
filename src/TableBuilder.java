import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import com.sun.org.apache.xml.internal.serialize.LineSeparator;
import dbms.RelationRow;
import dbms.TableWriter;
import dbms.logic.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;



public class TableBuilder {
    //////////////////////////////创表类。。。。用来现阶段生成测试数据的。。。会创建3个表。。。。最后不会用上这个类。。这个类是用来手动生成测试数据而已
    public static void main(String[] args){
        try {
            //表逻辑对象
           // TableDBMSObj tableDBMSObj_student=new TableDBMSObj("student",new DatabaseDBMSObj("studentDB",DatabaseDBMSObj.rootPath));
            //TableDBMSObj tableDBMSObj_course=new TableDBMSObj("course",new DatabaseDBMSObj("studentDB",DatabaseDBMSObj.rootPath));
            //ableDBMSObj tableDBMSObj_elect=new TableDBMSObj("stuCourse",new DatabaseDBMSObj("studentDB",DatabaseDBMSObj.rootPath));
            //学生表
            TableStructure tbs_student = new TableStructure(null);
            tbs_student.addItem(new TableStructureItem(DataType.INT32, 4, true, true,tbs_student,"学号"));  //学号
            tbs_student.addItem(new TableStructureItem(DataType.INT32, 4, false, true,tbs_student,"班级"));  //班级
            tbs_student.addItem(new TableStructureItem(DataType.STRING, 10, false, true,tbs_student,"姓名")); //姓名
            tbs_student.addItem(new TableStructureItem(DataType.STRING, 1, false, false,tbs_student,"性别")); //性别 M/F
            tbs_student.writeToStructFile("studentDB", "student");

            //课程表
            TableStructure tbs_course = new TableStructure(null);
            tbs_course.addItem(new TableStructureItem(DataType.INT32, 4, true, true,tbs_student,"课程编号"));  //课程编号
            tbs_course.addItem(new TableStructureItem(DataType.STRING, 20, false, true,tbs_student,"课程名称"));  //课程名称
            tbs_course.addItem(new TableStructureItem(DataType.INT32, 4, false, true,tbs_student,"课程容量")); //课程容量
            tbs_course.addItem(new TableStructureItem(DataType.INT32, 4, false, false,tbs_student,"剩余容量")); //剩余容量
            tbs_course.addItem(new TableStructureItem(DataType.INT32, 4, false, false,tbs_student,"已选人数")); //已选人数
            tbs_course.writeToStructFile("studentDB", "course");

            //选课信息
            TableStructure select_course = new TableStructure(null);
            select_course.addItem(new TableStructureItem(DataType.INT32, 4, true, true,tbs_student,"学号"));  //学号
            select_course.addItem(new TableStructureItem(DataType.INT32, 4, false, true,tbs_student,"课程编号"));  //课程编号
            select_course.addItem(new TableStructureItem(DataType.STRING, 30, false, true,tbs_student,"课程容量")); //课程容量
            tbs_course.writeToStructFile("studentDB", "stuCourse");



            //添加数据
            TableWriter tw=new TableWriter();
            try {
                //创建表逻辑对象
                TableDBMSObj tableDBMSObj = new TableDBMSObj("student", new DatabaseDBMSObj("studentDB", "C:\\Users\\akb\\Desktop\\java\\javaDBMS\\DB"));
                System.out.println("TableDBMSObj Create Successful!");
                //读取表结构对象

                RelationRow rw1=new RelationRow(tableDBMSObj.tableStructure);
                if (!rw1.setVal("学号",1234)) System.out.println("set 学号 fail");
                if (!rw1.setVal("班级",1708)) System.out.println("set 班级 fail");
                if (!rw1.setVal("姓名","aaadd")) System.out.println("set 姓名 fail");
                if (!rw1.setVal("性别","M")) System.out.println("set 性别 fail");
                RelationRow rw2=new RelationRow(tableDBMSObj.tableStructure);
                if (!rw2.setVal("学号",1344)) System.out.println("set 学号 fail");
                if (!rw2.setVal("班级",1707)) System.out.println("set 班级 fail");
                if (!rw2.setVal("姓名","aa1dd")) System.out.println("set 姓名 fail");
                if (!rw2.setVal("性别","F")) System.out.println("set 性别 fail");

                System.out.println(rw1);
                System.out.println(rw2);
                List<RelationRow> rws=new ArrayList<>();
                rws.add(rw1);
                rws.add(rw2);
                tw.appendRelations(rws,tableDBMSObj);
            }catch (Exception e){
                e.printStackTrace();
            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
