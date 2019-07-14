import automaton.AutomatonBuilder;
import automaton.SQLAutomaton;
import automaton.SQLSession;
import java.util.Random;
import dbms.RelationRow;
import dbms.TableReader;
import dbms.TableWriter;
import dbms.logic.*;

import java.io.File;

import java.util.ArrayList;
import java.util.List;



public class TableBuilder {
    public static String[] lastName;
    public static String[] myClass;
    public static void initLastName() {
        String ss = "李 王 张 刘 陈 杨 黄 赵 周 吴 徐 孙" +
                " 朱 马 胡 郭 林 何 高 梁 郑 罗 宋 谢 唐 韩 曹" +
                " 许 邓 萧 冯 曾 程 蔡 彭 潘 袁 于 董 余 苏 叶 吕 魏 蒋 田 杜 丁" +
                " 沈 姜 范 江 傅 钟 卢 汪 戴 崔 任 陆 廖 姚 方" +
                " 金 邱 夏 谭 韦 贾 邹 石 熊 孟 秦 阎 薛 侯 雷 白 龙 段 郝 孔 " +
                "邵 史 毛 常 万 顾 赖 武 康 贺 严 尹 钱 施 牛 洪 龚 汤 陶 黎 温 " +
                "莫 易 樊 乔 文 安 殷 颜 庄 章 鲁 倪 庞 邢 俞 翟 蓝 聂 齐 向 申 葛 岳";
        lastName = ss.split(" ");
        myClass = new String[100];
        for(int i = 0; i < 100; i++){
            myClass[i] = Integer.toString(i + 1700);
        }

    }


    public static String randomGetStudent(int i) {
        int num = 17000000;
        String xuehao = Integer.toString(num + i);
        Random random = new Random();
        String last = lastName[random.nextInt(127)];
        last += new String(new char[] { (char) (new Random().nextInt(20902) + 19968) });
        //20902
        if(random.nextInt(2) == 1)
            last += new String(new char[] { (char) (new Random().nextInt(20902) + 19968) });
        StringBuilder all = new StringBuilder();
        all.append("insert into student (学号,姓名,班级,性别) values (");
        all.append(xuehao);
        all.append(",");
        all.append(last);
        all.append(",");
        all.append(myClass[random.nextInt(100)]);
        all.append(",");
        if(random.nextInt(2) == 1)
            all.append("M");
        else
            all.append("F");
        all.append(")");
        return all.toString();
    }

    public static void generateData(){
        initLastName();
        SQLSession sqlSession=new SQLSession();   //SQL会话
        SQLAutomaton sqlAutomaton=new SQLAutomaton(AutomatonBuilder.buildAutomaton(),sqlSession);   //自动机

        //创表
        File file=new File("test");
        if(file.exists() && file.isDirectory()) file.delete();
        sqlAutomaton.matchingGrammar("create database test");
        sqlAutomaton.matchingGrammar("use test");
        sqlAutomaton.matchingGrammar("create table student (学号 int primary key ,姓名 string(12) not null ,班级 int,性别 string(1))");
        sqlAutomaton.matchingGrammar("create table stuCourse(学号 int not null ,课程编号 int not null ,选课时间 String(30) not null)");
        sqlAutomaton.matchingGrammar("create table course (课程编号 int primary key ,课程名称 string(30) not null ,课程容量 int,余剩容量 int ,已选人数 int)");
        //sqlAutomaton.matchingGrammar("insert into student values (1110000,www,333,M)");


        /*执行sql语句的例子，如
         *  sqlAutomaton.matchingGrammar("insert into student values (111,www,333)");   //插入一条数据,注意不论是字符串还是整数都没有引好
         *
         * 插入好后如果想测试读取的话
         * 按如下方式读取第i条记录:
         *  TableReader tableReader=new TableReader(new TableDBMSObj("表名",new DatabaseDBMSObj("数据库名",DatabaseDBMSObj.rootPath)));
         *   RelationRow r= tableReader.readRecord(i);
        * */


       /* for(int i=0;i<1000000;i++){
            sqlAutomaton.matchingGrammar(randomGetStudent(i));
        }*/
        /*sqlAutomaton.matchingGrammar("insert into course values (17001,JAVA从入门到放弃,100,100,0)");
        sqlAutomaton.matchingGrammar("insert into course values (17002,MySQL从删库到跑路,100,100,0)");
        sqlAutomaton.matchingGrammar("insert into course values (17003,JDK的安装与卸载,100,100,0)");*/

        ////Fill code here
        //学生数据要求百万条  选课数据没说  课程3门(要求的)
    }
    //////////////////////////////创表类。。。。用来现阶段生成测试数据的。。
    public static void main(String[] args){
        if(true){
            generateData();
            return;
        }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





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
            select_course.addItem(new TableStructureItem(DataType.STRING, 30, false, true,tbs_student,"选课时间")); //选课时间
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
