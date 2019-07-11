package dbms;

import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableDBMSObj;
import dbms.physics.BplusTree;
import filesystem.PropertiesFileTool;

import java.util.HashMap;
import java.util.Map;

public class CacheManage {
    private static  CacheManage instance=null;
    private Map<String, TableReader> readerMap=new HashMap<>();
    private IndexCache indexCache=IndexCache.getInstance();
    public static CacheManage getInstance(){
        if(instance==null) instance=new CacheManage();
        return instance;
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
