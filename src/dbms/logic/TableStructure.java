package dbms.logic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


////////////////////表结构类.这是实际的结构类。。可以写到文件(.tbs)还可以从文件中反序列化读取
public class TableStructure implements Serializable {
    //索引
    public boolean useIndex=false;     //是否使用索引
    public String indexOn="";

    private int size=0;
    private TableDBMSObj tableDBMSObjBelongedTo=null;   //所属的表逻辑对象,可以为null.为null时，表示其是一个独立的表结构，暂不属于任何表
    public List<TableStructureItem> dts=new ArrayList<>();  //表结构项对象（也就是属性项）
    public void addItem(TableStructureItem a){
        dts.add(a);
    }  //添加一个属性项

    //构造函数。提供一个表逻辑对象。让他从属一个表。也可以设置为null.表示这个表结构暂时不属于任何表(这在创建新表时非常有用)
    public TableStructure(TableDBMSObj tableDBMSObj){
        this.tableDBMSObjBelongedTo=tableDBMSObj;
    }
    public TableDBMSObj getTableDBMSObjBelongedTo(){
        return tableDBMSObjBelongedTo;
    }
    public int getConlumSize(String name){
        for (TableStructureItem tsi : dts){
            if(tsi.conlumName.compareTo(name)==0){
                return tsi.size;
            }
        }
        return -1;
    }
    //获取一条记录的大小
    public int getSize(){
        int s=0;
        for (TableStructureItem tsi : dts){
            s+=tsi.size;
        }
        //System.out.println("record size="+s);
        return s;
    }

    public DataType getDataType(String columnName){
        for(TableStructureItem tableStructureItem:dts){
            if(tableStructureItem.conlumName.compareTo(columnName)==0){
                return tableStructureItem.type;
            }
        }
        return null;
    }
    public boolean isColumnExists(String name){
        for (TableStructureItem tsi : dts){
            if(tsi.conlumName.compareTo(name)==0) return true;
        }
        return false;
    }
    //输出表结构到文件     也就是.tbs文件(存储表的结构)。同时会创建一个空的.table文件（存储实际的数据）
    public void writeToStructFile(String DBName,String tableName) throws IOException {
        File f=new File(DatabaseDBMSObj.rootPath+"\\"+DBName+"\\"+tableName+".tbs");
        if(f.exists() && f.isFile()) {
            //要检测是否有锁
             f.delete();}
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
