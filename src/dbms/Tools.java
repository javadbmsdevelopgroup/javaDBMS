package dbms;

import automaton.SQLSession;
import automaton.methods.IndexCreateMethod;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.Relation;
import dbms.logic.TableDBMSObj;
import filesystem.PropertiesFileTool;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;

public class Tools {
    private static Tools instance=null;
    public static Tools getInstance(){
        if(instance == null) instance=new Tools();
        return instance;
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
    public void debrisCleanUp(){
        String root= DatabaseDBMSObj.rootPath;
        File dbF=new File(root);
        Lock readLock=null;
        Lock writeLock=null;
        if(!dbF.exists() || !dbF.isDirectory()) return;
        File[] dbs=dbF.listFiles();
        writeLock.lock();
        readLock.lock();
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
                            SQLSession sqlSession=new SQLSession();
                            sqlSession.curUseDatabase=dbname;
                            sqlSession.sqlAutomaton.matchingGrammar("create index on "+tbName+" ("+tableDBMSObj.tableStructure.indexOn+")",sqlSession);
                        }


                    }catch (Exception e){
                        writeLock.unlock();
                        readLock.unlock();
                       e.printStackTrace();
                    }
                }
            }
        }
        writeLock.unlock();
        readLock.unlock();
    }
    private Tools(){
    }
}
