package dbms.logic;

import java.io.*;

/////////////////////////////表逻辑对象  包含表名 等
public class TableDBMSObj extends BaseDBMSObject{
    public String tbName="";   //表名
    public DatabaseDBMSObj dbBelongedTo=null;    //所属的数据库
    public TableStructure tableStructure=null;   //表结构
    //索引
    public boolean useIndex=false;     //是否使用索引

//构造函数，需要提供表名和一个数据库逻辑对象（表示该表所属的数据库）
    public TableDBMSObj(String name,DatabaseDBMSObj db) throws IOException,ClassNotFoundException{
        File f=new File(db.rootPath+"\\"+tbName) ;
        if(!f.exists()) throw new IOException("Database Not Found");
        //System.out.println(name);
        tbName = name.split("\\.")[0];
        dbBelongedTo=db;
        //查找表结构文件
        f=new File(db.rootPath+"\\"+db.dbName+"\\"+tbName+".tbs") ;
        //System.out.println(db.rootPath+"\\"+db.dbName+"\\"+tbName+".tbs");
        if(!f.exists()) throw new IOException("Table Structure File Not Found!");

        //读取表结构
        FileInputStream fis=new FileInputStream(db.rootPath+"\\"+db.dbName+"\\"+tbName+".tbs");
        //System.out.println(db.rootPath+"\\"+db.dbName+"\\"+tbName+".tbs");
        ObjectInputStream ois=new ObjectInputStream(fis);
        tableStructure=(TableStructure)ois.readObject();
        fis.close();
        ois.close();
    }

    @Override
    public String getType(){
        return "DBMSOBJ.Table";
    }
    @Override
    public  boolean create(){
        return false;
    }
    @Override
    public  void delete(){

    }
}
