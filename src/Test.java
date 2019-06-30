////////////////////////////////////////////////测试专用文件


import dbms.RelationRow;
import dbms.TableReader;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableDBMSObj;

import java.io.*;

interface I1{
    void doSomething();
}
class A implements Serializable {
    I1 i;
    A(I1 x){
        i=x;
    }
}
class B{
    A a1=null;
    B(A a1){
        this.a1=a1;
    }
}
class C implements I1{
    @Override
    public void doSomething(){
        System.out.println(111);
    }
}

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
