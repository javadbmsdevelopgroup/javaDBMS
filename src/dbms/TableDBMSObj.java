package dbms;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.File;
import java.io.IOException;

public class TableDBMSObj extends BaseDBMSObject{
    String tbName="";
    DatabaseDBMSObj dbBelongedTo=null;
    public TableDBMSObj(String name,DatabaseDBMSObj db) throws IOException{
        File f=new File(db.rootPath+"\\"+tbName) ;
        if(!f.exists()) throw new IOException("Database Not Found");
        //System.out.println(name);
        tbName = name.split("\\.")[0];
        dbBelongedTo=db;
        //查找表结构文件
        f=new File(db.rootPath+"\\"+db.dbName+"\\"+tbName+".tbs") ;
        //System.out.println(db.rootPath+"\\"+db.dbName+"\\"+tbName+".tbs");
        if(!f.exists()) throw new IOException("Table Structure File Not Found!");
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
