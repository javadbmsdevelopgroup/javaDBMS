import dbms.logic.DataType;
import dbms.logic.TableStructure;
import dbms.logic.TableStructureItem;

import java.io.IOException;

public class TableBuilder {
    //////////////////////////////创表
    public static void main(String[] args){
        try {
            //学生表
            TableStructure tbs_student = new TableStructure();
            tbs_student.addItem(new TableStructureItem(DataType.INT32, 4, true, true));  //学号
            tbs_student.addItem(new TableStructureItem(DataType.INT32, 4, false, true));  //班级
            tbs_student.addItem(new TableStructureItem(DataType.STRING, 10, false, true)); //姓名
            tbs_student.addItem(new TableStructureItem(DataType.STRING, 1, false, false)); //性别 M/F
            tbs_student.writeToStructFile("studentDB", "student");

            //课程表
            TableStructure tbs_course = new TableStructure();
            tbs_course.addItem(new TableStructureItem(DataType.INT32, 4, true, true));  //课程编号
            tbs_course.addItem(new TableStructureItem(DataType.STRING, 20, false, true));  //课程名称
            tbs_course.addItem(new TableStructureItem(DataType.INT32, 4, false, true)); //课程容量
            tbs_course.addItem(new TableStructureItem(DataType.INT32, 4, false, false)); //剩余容量
            tbs_course.addItem(new TableStructureItem(DataType.INT32, 4, false, false)); //已选人数
            tbs_course.writeToStructFile("studentDB", "course");

            //选课信息
            TableStructure select_course = new TableStructure();
            select_course.addItem(new TableStructureItem(DataType.INT32, 4, true, true));  //学号
            select_course.addItem(new TableStructureItem(DataType.INT32, 4, false, true));  //课程编号
            select_course.addItem(new TableStructureItem(DataType.STRING, 30, false, true)); //课程容量
            tbs_course.writeToStructFile("studentDB", "stuCourse");
            
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
