package dbms;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TableReadWriteLock {
    private static TableReadWriteLock instance;
    private static Map<String, ReentrantReadWriteLock> locks=new HashMap<>();
    public static TableReadWriteLock getInstance(){
        if(instance==null) instance=new TableReadWriteLock();
        return instance;
    }
    public Lock getReadLock(String tableName){
        if(!locks.containsKey(tableName)){
            locks.put(tableName,new ReentrantReadWriteLock());
        }
        Lock lock=locks.get(tableName).readLock();
        return lock;
    }

    public Lock getWriteLock(String tableName){
        if(!locks.containsKey(tableName)){
            locks.put(tableName,new ReentrantReadWriteLock());
        }
        Lock lock=locks.get(tableName).writeLock();
        return lock;
    }




}
