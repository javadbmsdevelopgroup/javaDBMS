/*数据库逻辑对象*/
package dbms.logic;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/*数据库逻辑对象
* 包含数据库的具体地址   在本项目中数据库根目录设置在DB文件夹
* 数据库名之类的
*
* */
public class DatabaseDBMSObj extends BaseDBMSObject{
    public String dbName="";
    public static String rootPath="C:\\Users\\akb\\Desktop\\java\\javaDBMS\\DB";
    //构造函数，需要提供数据库根目录和数据库名
    public DatabaseDBMSObj(String dbName,String rootPath){
        this.dbName=dbName;
        this.rootPath=rootPath;
    }


    //获取数据库下所有的表（逻辑对象，没有具体数据）
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
            }catch (ClassNotFoundException c){
                c.printStackTrace();
            }
        }

        return tdos;
    }


    //获取数据库根目录
    public String getRootPath(){
        return rootPath;
    }
    //判断数据库是否存在
    public boolean isExist(){
        File f=new File(rootPath+"\\"+dbName);
        if(f.exists() && f.isDirectory()) return true; else return false;
    }




    //获取类型
    @Override
    public String getType(){
        return "DBMSOBJ.Database";
    }

    //创建一个数据库
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

    //删除一个数据库
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
