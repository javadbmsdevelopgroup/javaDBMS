package dbms;

import dbms.logic.DatabaseDBMSObj;

import dbms.logic.TableDBMSObj;
import dbms.physics.BplusTree;
import dbms.view.RelationView;
import filesystem.PropertiesFileTool;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
//////////////////////////////////////缓存管理
public class CacheManage {
    private static  CacheManage instance=null;             //使用单例模式
    private static Map<String, TableReader> readerMap=new HashMap<>();   //管理着TableReader缓存（每个TableReader有一个缓冲区)
    private static IndexCache indexCache=IndexCache.getInstance();    //管理着索引缓存

    private CacheManage(){}
    public static CacheManage getInstance(){
        if(instance==null) instance=new CacheManage();
        return instance;
    }


    public static void initCache(){

        for(String tbdbName:readerMap.keySet()){
            readerMap.get(tbdbName).cleanBuffer();
        }
        readerMap.clear();
        indexCache.init();
    }


    //缓存整个表
    public static boolean cacheWholeTable(String dbName,String tbName){
        if(!DatabaseDBMSObj.isExist(dbName)) {
            return false;
        }
        try {
            TableDBMSObj tableDBMSObj = new TableDBMSObj(tbName, new DatabaseDBMSObj(dbName, DatabaseDBMSObj.rootPath));
            int count=tableDBMSObj.getRecordCount(dbName);

            TableReader reader=new TableReader(tableDBMSObj,count,2);

            int pos=0;
            RelationRow r=reader.readRecord(pos);
            while(r!=null){
                pos++;
                r=reader.readRecord(pos);
            }
            readerMap.put(dbName+"."+tbName,reader);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //缓存所有索引
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

    //获取tabeReader
    public TableReader getTableReader(String dbName,String tbName){
        String keyStr=dbName+"."+tbName;
        if(readerMap.containsKey(keyStr)){
            return readerMap.get(keyStr);
        }else{
            try{
                System.out.println(tbName+" "+dbName);;
                TableDBMSObj tableDBMSObj=new TableDBMSObj(tbName,new DatabaseDBMSObj(dbName,DatabaseDBMSObj.rootPath));
                TableReader tableReader=new TableReader(tableDBMSObj,
                        Integer.parseInt(PropertiesFileTool.getInstance().readConfig("PageSize")),
                        Integer.parseInt(PropertiesFileTool.getInstance().readConfig("BlockSize"))
                        );
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

    //同步缓存用的。 当真实表的数据有变动时，会调用这个函数。防止表数据和索引数据和缓存数据的不一致.
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

    //获取索引
    public BplusTree getIndex(TableDBMSObj tableDBMSObj){
        return indexCache.getIndex(tableDBMSObj);
    }
}
