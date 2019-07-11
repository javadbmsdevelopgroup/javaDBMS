package dbms;

import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableDBMSObj;
import dbms.physics.BplusTree;
import filesystem.PropertiesFileTool;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CacheManage {
    private static  CacheManage instance=null;
    private static Map<String, TableReader> readerMap=new HashMap<>();
    private static IndexCache indexCache=IndexCache.getInstance();
    public static CacheManage getInstance(){
        if(instance==null) instance=new CacheManage();
        return instance;
    }



    public static void loadAllindex(){
        String root=PropertiesFileTool.getInstance().readConfig("DBRoot");
        File dbF=new File(root);
        if(!dbF.exists() || !dbF.isDirectory()) return;
        File[] dbs=dbF.listFiles();
        for(File f:dbs){
            if(!f.isDirectory()) continue;
            File[] tbs=f.listFiles();
            String dbname=f.getName();

            for(File tbf:tbs){
                if(tbf.getName().indexOf(".lh")>=0){
                    System.out.println("Load Index:"+dbname+"\\"+tbf.getName());
                    try {
                        indexCache.loadIndex(new TableDBMSObj(tbf.getName().substring(0, (int) (tbf.getName().length() - 3))
                                        ,new DatabaseDBMSObj(dbname, DatabaseDBMSObj.rootPath)  )
                                );
                    }catch (Exception e){
                        System.out.println("索引缓存失败");
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public TableReader getTableReader(String dbName,String tbName){
        String keyStr=dbName+"."+tbName;
        if(readerMap.containsKey(keyStr)){
            return readerMap.get(keyStr);
        }else{
            try{
                TableDBMSObj tableDBMSObj=new TableDBMSObj(tbName,new DatabaseDBMSObj(dbName,DatabaseDBMSObj.rootPath));
                TableReader tableReader=new TableReader(tableDBMSObj,
                        Integer.parseInt(PropertiesFileTool.getInstance().readConfig("PageSize")));
                readerMap.put(dbName+"."+tbName,tableReader);
                //如果用了索引，顺便加载索引
                if(tableDBMSObj.tableStructure.useIndex){
                    indexCache.loadIndex(tableDBMSObj);
                }
                return tableReader;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }


        }
    }

    public boolean resetRecordInCache(String dbName,String tbName,RelationRow r,int recordPos){
        String keyStr=dbName+"."+tbName;
        if(!readerMap.containsKey(keyStr)) return false;
        TableReader reader=readerMap.get(keyStr);
        try{
            indexCache.resetRecord(r,new TableDBMSObj(tbName,new DatabaseDBMSObj(dbName,DatabaseDBMSObj.rootPath)),recordPos);
        }catch (Exception e){
            e.printStackTrace();
        }

        return  reader.resetBufferRecord(recordPos,r);

    }

    public BplusTree getIndex(TableDBMSObj tableDBMSObj){
        return indexCache.getIndex(tableDBMSObj);
    }
}
