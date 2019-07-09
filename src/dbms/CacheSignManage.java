package dbms;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CacheSignManage {
    private static Map<String, Map<Integer,Boolean>> dirtyBitMap=new HashMap<>();
    public static void addNewTableSign(String tableName){
        dirtyBitMap.put(tableName,new TreeMap<Integer,Boolean>());
    }
    public static Boolean getDirtyBit(String tbName,int record){
        if(!dirtyBitMap.containsKey(tbName)) {
            addNewTableSign(tbName);
            dirtyBitMap.get(tbName).put(record,false);
            return false;
        }
        if(!dirtyBitMap.get(tbName).containsKey(record)) {
            dirtyBitMap.get(tbName).put(record,false);
            return false;
        }
        return dirtyBitMap.get(tbName).get(record);
    }

    public static void setDirtyBit(String tbName,int record){
        if(!dirtyBitMap.containsKey(tbName)) {
            addNewTableSign(tbName);
            dirtyBitMap.get(tbName).put(record,true);
        }
        if(!dirtyBitMap.get(tbName).containsKey(record)) {
            dirtyBitMap.get(tbName).put(record,true);
        }
    }

    public static void cleanDirtyBit(String tbName,int record){
        if(!dirtyBitMap.containsKey(tbName)) {
            addNewTableSign(tbName);
            dirtyBitMap.get(tbName).put(record,false);
        }
        if(!dirtyBitMap.get(tbName).containsKey(record)) {
            dirtyBitMap.get(tbName).put(record,false);
        }
        dirtyBitMap.get(tbName).put(record,false);
    }
}
