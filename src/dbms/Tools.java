package dbms;

import automaton.AutomatonBuilder;
import automaton.SQLAutomaton;
import automaton.SQLSession;
import automaton.methods.IndexCreateMethod;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableDBMSObj;
import filesystem.PropertiesFileTool;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.locks.Lock;

public class Tools {
    private static Tools instance=null;
    public static Tools getInstance(){
        if(instance == null) instance=new Tools();
        return instance;
    }
    public static String refFormatNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar=Calendar.getInstance();
        Date date = calendar.getTime();
        String dateStringParse = sdf.format(date);
        return dateStringParse;
    }
    public static String getDateFromLong(long TimeMillis){
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateformat.format(TimeMillis);
        return dateStr;
    }
    public static String removeAllZero(String source){
        String re="";
        for(int i=0;i<source.length();i++){
            if(source.charAt(i)!='\u0000')
            {
                re+=source.charAt(i);
            }else{
                break;
            }
        }
        return re;
    }
    public int getIntFromBytes(Byte[] intBytes){
        return (intBytes[3] & 0xFF |
                (intBytes[2] & 0xFF) << 8 |
                (intBytes[1] & 0xFF) << 16 |
                (intBytes[0] & 0xFF) << 24);
    }
    public byte[] toBytes(int val){
        byte[] result = new byte[4];
        //由高位到低位
        result[0] = (byte)((val >> 24) & 0xFF);
        result[1] = (byte)((val >> 16) & 0xFF);
        result[2] = (byte)((val >> 8) & 0xFF);
        result[3] = (byte)(val & 0xFF);
        return result;
    }


    //碎片整理
    public static void debrisCleanUp(){
        String root= DatabaseDBMSObj.rootPath;
        File dbF=new File(root);
        Lock readLock=null;
        Lock writeLock=null;
        if(!dbF.exists() || !dbF.isDirectory()) return;
        File[] dbs=dbF.listFiles();

        for(File f:dbs){
            if(!f.isDirectory()) continue;
            File[] tbs=f.listFiles();
            String dbname=f.getName();
            for(File tbf:tbs){
                if(tbf.getName().indexOf(".table")>=0){
                    System.out.println("clean up:"+dbname+"\\"+tbf.getName());

                    try {
                        String tbName=tbf.getName().substring(0, (int) (tbf.getName().length() - 6));
                        TableReader reader=CacheManage.getInstance().getTableReader(dbname,tbName);
                        int pos=0;
                        int size=0;
                        TableDBMSObj tableDBMSObj=new TableDBMSObj(tbName,new DatabaseDBMSObj(dbname,DatabaseDBMSObj.rootPath));
                        FileOutputStream fos=new FileOutputStream(root+"\\"+dbname+"\\"+tbName+".tmp");
                        BufferedOutputStream bos=new BufferedOutputStream(fos);
                        FileInputStream fis=new FileInputStream(root+"\\"+dbname+"\\"+tbName+".table");
                        BufferedInputStream bis=new BufferedInputStream(fis);
                        byte[] b=new byte[tableDBMSObj.tableStructure.getSize()+1];

                        while (bis.read(b,0,b.length)>0){
                            if(b[0]==1) continue;
                            bos.write(b,0,b.length);
                        }
                        bis.close();
                        fis.close();
                        bos.close();
                        fos.close();

                        if(tableDBMSObj.tableStructure.useIndex){
                            //重建索引
                            if(IndexCache.getInstance().isChanged(dbname,tbName)){
                                SQLSession sqlSession=new SQLSession();
                                sqlSession.curUseDatabase=dbname;
                                (new SQLAutomaton(AutomatonBuilder.buildAutomaton(),sqlSession)).matchingGrammar(
                                        "create index on "+tbName+" ("+tableDBMSObj.tableStructure.indexOn+")",sqlSession
                                );
                            }


                        }


                    }catch (Exception e){

                       e.printStackTrace();
                    }
                }
            }
        }


    }
    private Tools(){
    }
}
