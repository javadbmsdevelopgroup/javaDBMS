////////////////////////////////////////////////测试专用文件


import dbms.RelationRow;
import dbms.TableReader;
import dbms.TableWriter;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.Relation;
import dbms.logic.TableDBMSObj;
import filesystem.PropertiesFileTool;

import java.io.*;

/////////测试类。。。有什么想测试的代码可以在这写写;
public class Test {
    public static void multiThreadTest(){

    }
    public static void main(String[] args){
        TableWriter tableWriter=new TableWriter();
        try{
            TableDBMSObj tableDBMSObj=new TableDBMSObj("student",new DatabaseDBMSObj("test",DatabaseDBMSObj.rootPath));
            tableWriter.delete(0,tableDBMSObj);
            tableWriter.delete(0,tableDBMSObj);
        }catch (Exception e){


        }

    }
}
