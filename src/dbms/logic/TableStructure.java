package dbms.logic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


////////////////////表结构类
public class TableStructure implements Serializable {

    private int size=0;
    private TableDBMSObj tableDBMSObjBelongedTo=null;   //所属的表逻辑对象
    public List<TableStructureItem> dts=new ArrayList<>();  //表结构项对象（也就是属性项）
    public void addItem(TableStructureItem a){
        dts.add(a);
    }  //添加一个属性项

    public TableStructure(TableDBMSObj tableDBMSObj){
        this.tableDBMSObjBelongedTo=tableDBMSObj;
    }
    public TableDBMSObj getTableDBMSObjBelongedTo(){
        return tableDBMSObjBelongedTo;
    }
    //获取一条记录的大小
    public int getSize(){
        int s=0;
        for (TableStructureItem tsi : dts){
            s+=tsi.size;
        }
        System.out.println("record size="+s);
        return s;
    }
    //输出表结构到文件     也就是.tbs文件(存储表的结构)。同时会创建一个空的.table文件（存储实际的数据）
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
