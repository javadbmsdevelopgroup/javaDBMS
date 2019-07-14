////////////////////////////////////////////////测试专用文件


import dbms.RelationRow;
import dbms.TableReader;
import dbms.TableWriter;
import dbms.Tools;
import dbms.logic.*;
import dbms.physics.*;

import filesystem.PropertiesFileTool;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/////////测试类。。。有什么想测试的代码可以在这写写;
public class Test {



    public static void main(String[] args){


      /*  if(true){
            try {
                PropertiesFileTool.getInstance().writeConfig("PageSize", "30");
                PropertiesFileTool.getInstance().writeConfig("IndexSize","10000");
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }
        try {
            TableDBMSObj tableDBMSObj=new TableDBMSObj("student",new DatabaseDBMSObj("test",DatabaseDBMSObj.rootPath));
            TableReader tableReader = new TableReader(tableDBMSObj,30);
            int curPos=0;
            RelationRow record=tableReader.readRecord(0);
            tableDBMSObj.tableStructure.useIndex=true;
            tableDBMSObj.tableStructure.indexOn="学号";
            DataType dataType=tableDBMSObj.tableStructure.getDataType(tableDBMSObj.tableStructure.indexOn);

            BplusTree bplusTree=new BplusTree();
            switch (dataType){
                case STRING:
                    bplusTree=new BplusTree<String>();
                    break;
                case INT32:
                    bplusTree=new BplusTree<Integer>();
                    break;
            }


            String indexOn=tableDBMSObj.tableStructure.indexOn;
            //建索引
            while (record!=null){
                //System.out.println(record);
                switch (dataType){
                    case INT32:
                        bplusTree.insert((Integer) record.getVal(indexOn),curPos);
                        break;
                    case STRING:
                        bplusTree.insert((String) record.getVal(indexOn),curPos);
                        break;
                }

                curPos++;
                record=tableReader.readRecord(curPos);

            }
            FileOutputStream fos=new FileOutputStream(DatabaseDBMSObj.rootPath+"\\"+tableDBMSObj.dbBelongedTo.dbName+"\\"+tableDBMSObj.tbName+".lh");
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(bplusTree);
            oos.close();
            fos.close();
            System.out.println("索引建立完成");
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }
}
