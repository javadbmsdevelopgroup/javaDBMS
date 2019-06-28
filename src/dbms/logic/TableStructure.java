package dbms.logic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


////////////////////表结构
public class TableStructure implements Serializable {
    List<TableStructureItem> dts=new ArrayList<>();  //表结构项对象
    public void addItem(TableStructureItem a){
        dts.add(a);
    }


    public void writeToStructFile(String DBName,String tableName) throws IOException {
        File f=new File(DatabaseDBMSObj.rootPath+"\\"+DBName+"\\"+tableName+".tbs");
        if(f.exists()) return;
        System.out.println("out: "+DatabaseDBMSObj.rootPath+"\\"+DBName+"\\"+tableName+".tbs");
        FileOutputStream fileOutputStream=new FileOutputStream(DatabaseDBMSObj.rootPath+"\\"+DBName+"\\"+tableName+".tbs");
        ObjectOutputStream oos=new ObjectOutputStream(fileOutputStream);
        oos.writeObject(this);
        fileOutputStream.close();
        oos.close();
        System.out.println("out: "+DatabaseDBMSObj.rootPath+"\\"+DBName+"\\"+tableName+".table");
        File f2=new File(DatabaseDBMSObj.rootPath+"\\"+DBName+"\\"+tableName+".table");

        if(!f2.exists())
        {  f2.createNewFile();}

    }

}
