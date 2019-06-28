/*数据库逻辑对象*/
package dbms;
import javafx.scene.control.Tab;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class DatabaseDBMSObj extends BaseDBMSObject{
    public String dbName="";
    public String rootPath="";
    public DatabaseDBMSObj(String dbName,String rootPath){
        this.dbName=dbName;
        this.rootPath=rootPath;
    }

    public List<TableDBMSObj> listTables(){
        File dbf=new File(rootPath+"\\"+dbName);
        if(!dbf.exists() || !dbf.isDirectory()) return null;
        List<TableDBMSObj> tdos=new ArrayList<>();
        File[] ts=dbf.listFiles();
        for(File f:ts){
            try{
            tdos.add(new TableDBMSObj(f.getName(),this));
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }

        return tdos;
    }


    public String getRootPath(){
        return rootPath;
    }
    public boolean isExist(){
        File f=new File(rootPath+"\\"+dbName);
        if(f.exists() && f.isDirectory()) return true; else return false;
    }




    @Override
    public String getType(){
        return "DBMSOBJ.Database";
    }
    @Override
    public boolean create(){

        File f=new File(rootPath+"\\"+dbName);
        if(f.exists() && f.isDirectory()) return false;
        else{
            //System.out.println("Create Database");
            f.mkdir();
            if(f.exists()) return true; else return false;

        }
    }
    @Override
    public void delete(){
        if(isExist()) return;
        else{
            File f=new File(rootPath+"\\"+dbName);
            f.delete();
        }
    }


    public static void main(String[] args){
        DatabaseDBMSObj db=new DatabaseDBMSObj("studentDB","C:\\Users\\akb\\Desktop\\java\\javaDBMS\\DB");
        if(db.create()){
            System.out.println("Create Successful");
        };
        db.listTables();
    }


}
