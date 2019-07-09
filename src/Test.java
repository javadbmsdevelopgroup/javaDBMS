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
    public static void main(String[] args){

        try{
            TableDBMSObj tableDBMSObj= new TableDBMSObj("course",
                    new DatabaseDBMSObj("test", PropertiesFileTool.getInstance().readConfig("DBRoot")));
            TableReader tableReader = new TableReader(tableDBMSObj,30);
            TableWriter tw=new TableWriter();

            RelationRow rp=new RelationRow(tableDBMSObj.tableStructure);
            rp.setVal("课程编号",1901);
            rp.setVal("课程名称","JDK的安装与卸载");
            rp.setVal("课程容量",100);
            rp.setVal("余剩容量",100);
            rp.setVal("已选人数",0);
            //System.out.println(tw.replace(1,rp,tableDBMSObj));
            int cur=0;
            RelationRow rr=tableReader.readRecord(0);
            while(rr!=null){
                cur++;
                System.out.println(rr);
                rr=tableReader.readRecord(cur);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
