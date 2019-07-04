package filesystem;

import java.io.*;
import java.util.Properties;
/////////////////////配置文件读取工具。采用单例模式
public class PropertiesFileTool {
    private static PropertiesFileTool instance=null;
    private PropertiesFileTool(){
    }
    public static PropertiesFileTool getInstance(){
        if(instance==null) instance=new PropertiesFileTool();
        return instance;
    }
    public void writeConfig(String key,String value) throws IOException {
        FileOutputStream fileOutputStream=new FileOutputStream("config.properties",true);
        Properties prop=new Properties();
        prop.setProperty(key,value);
        prop.store(fileOutputStream,"");
        fileOutputStream.close();
    }
    public String readConfig(String key)  {
        try{
        InputStream in=new BufferedInputStream(new FileInputStream("config.properties"));
        Properties prop=new Properties();
        prop.load(in);
        return prop.getProperty(key);
        }catch (IOException ioe){
            return "";
        }

    }

    public static void main(String[] args){
        try{
            PropertiesFileTool.getInstance().writeConfig("DBRoot","C:\\Users\\akb\\Desktop\\java\\javaDBMS\\DB");
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
