////////////////////////////////////////////////测试专用文件


import dbms.RelationRow;
import dbms.TableReader;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableDBMSObj;

import java.io.*;

/////////测试类。。。有什么想测试的代码可以在这写写;
public class Test {
    public static void main(String[] args){



        try{
        //创建表逻辑对象
            TableDBMSObj tableDBMSObj = new TableDBMSObj("student", new DatabaseDBMSObj("studentDB", "C:\\Users\\akb\\Desktop\\java\\javaDBMS\\DB"));
            System.out.println("TableDBMSObj Create Successful!");
            TableReader te=new TableReader(tableDBMSObj,20);
            RelationRow rw= te.readRecord(0);
            System.out.println(rw);
            rw= te.readRecord(1);
            System.out.println(rw);
            rw=te.readRecord(2);
            System.out.println(rw);


        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
