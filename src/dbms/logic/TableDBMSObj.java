package dbms.logic;

import java.io.*;

/////////////////////////////表逻辑对象
public class TableDBMSObj extends BaseDBMSObject{
    public String tbName="";
    public DatabaseDBMSObj dbBelongedTo=null;
    public TableStructure tableStructure=null;
    //索引
    public boolean useIndex=false;


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
