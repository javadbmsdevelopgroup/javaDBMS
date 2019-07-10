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


   /* public static void main(String[] args){
        Lock readLock=TableReadWriteLock.getInstance().getReadLock("锁上的对象名字");
        readLock.lock();  //读锁
        ////读操作
        readLock.unlock();  //释放读锁

        Lock writeLock=TableReadWriteLock.getInstance().getWriteLock("锁上的对象名字");

        writeLock.lock();  //写锁
        ////写操作
        readLock.lock();   //释放写锁前要先上读锁
        writeLock.unlock();  //释放写锁
        readLock.unlock(); //释放读锁
    }*/

}
