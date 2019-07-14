package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;
import automaton.SQLSession;
import dbms.*;
import dbms.logic.DatabaseDBMSObj;

import dbms.logic.TableDBMSObj;
import dbms.physics.BplusTree;
import filesystem.PropertiesFileTool;

import java.io.Serializable;
import java.util.Stack;
import java.util.concurrent.locks.Lock;

public class DeleteMethod implements INodeFunc, Serializable {
    @Override
    public Object doWork(InfCollection infCollection, Object... objs){
        SQLSession sqlSession=(SQLSession)objs[0];
        String tableName=infCollection.tableNames.pop();
        Lock readLock;
        Lock writeLock;
        if(!MethodTools.checkTableandDatabase(sqlSession,tableName)) return -1;

        try{
            DatabaseDBMSObj databaseDBMSObj=new DatabaseDBMSObj(sqlSession.curUseDatabase,DatabaseDBMSObj.rootPath);
            TableDBMSObj tableDBMSObj=new TableDBMSObj(tableName,databaseDBMSObj);
            TableReader reader= CacheManage.getInstance().getTableReader(sqlSession.curUseDatabase,tableName);

            int count=0;
            if(!tableDBMSObj.tableStructure.useIndex){
                int pos=0;
                RelationRow r=reader.readRecord(pos);
                //准备进行写操作。需要上锁
                writeLock= TableReadWriteLock.getInstance().getWriteLock(databaseDBMSObj.dbName+"."+tableName);
                writeLock.lock();

                while(r!=null){
                    TableWriter tableWriter=new TableWriter();

                    if(MethodTools.checkLogicExpression((Stack<String>) infCollection.logicExpressions.clone(),r)){
                        System.out.println("Try delete:"+r+" ");
                        if(tableWriter.delete(pos,tableDBMSObj)){
                            count++;
                        }
                    }
                    pos++;
                    r=reader.readRecord(pos);
                }

                //释放锁
                readLock=TableReadWriteLock.getInstance().getReadLock(databaseDBMSObj.dbName+"."+tableName);
                readLock.lock();
                writeLock.unlock();
                readLock.unlock();
                return count;
            }else{
                //运用索引的情况下进行删除
                writeLock= TableReadWriteLock.getInstance().getWriteLock(databaseDBMSObj.dbName+"."+tableName);
                writeLock.lock();


                int pos=MethodTools.getRecordPosThroughIndex((Stack<String>)infCollection.logicExpressions.clone(),tableDBMSObj);
                if(pos<0) return -1;
                TableWriter tableWriter=new TableWriter();
                tableWriter.delete(pos,tableDBMSObj);

                readLock=TableReadWriteLock.getInstance().getReadLock(databaseDBMSObj.dbName+"."+tableName);
                readLock.lock();
                writeLock.unlock();
                readLock.unlock();
                return 1;
            }



        }catch (Exception e){e.printStackTrace();return -2;}

    }
}
