package dbms;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//////////////缓存锁。防止多个线程同时修改缓存产生不一致
public class CacheReadWriteLock {
    private static CacheReadWriteLock instance;
    private static Map<TableBuffer, ReentrantReadWriteLock> locks=new HashMap<>();
    public static CacheReadWriteLock getInstance(){
        if(instance==null) instance=new CacheReadWriteLock();
        return instance;
    }
    public Lock getReadLock(TableBuffer tableBuffer){
        if(!locks.containsKey(tableBuffer)){
            locks.put(tableBuffer,new ReentrantReadWriteLock());
        }
        Lock lock=locks.get(tableBuffer).readLock();
        return lock;
    }

    public Lock getWriteLock(TableBuffer tableBuffer){
        if(!locks.containsKey(tableBuffer)){
            locks.put(tableBuffer,new ReentrantReadWriteLock());
        }
        Lock lock=locks.get(tableBuffer).writeLock();
        return lock;
    }


}
