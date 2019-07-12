package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;
import automaton.SQLSession;
import dbms.*;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.Relation;
import dbms.logic.TableDBMSObj;
import dbms.physics.BplusTree;
import filesystem.PropertiesFileTool;

import java.io.Serializable;
import java.util.Stack;
import java.util.concurrent.locks.Lock;

public class DeleteMethod implements INodeFunc, Serializable {
    //delete from course where 课程编号=1901;
    @Override
    public Object doWork(InfCollection infCollection, Object... objs){
        System.out.println("delete method");
        SQLSession sqlSession=(SQLSession)objs[0];
        String tableName=infCollection.tableNames.pop();
        Lock readLock;
        Lock writeLock;
        if(!MethodTools.checkTableandDatabase(sqlSession,tableName)) return -1;

        try{
            DatabaseDBMSObj databaseDBMSObj=new DatabaseDBMSObj(sqlSession.curUseDatabase,DatabaseDBMSObj.rootPath);
            TableDBMSObj tableDBMSObj=new TableDBMSObj(tableName,databaseDBMSObj);
            TableReader reader= CacheManage.getInstance().getTableReader(sqlSession.curUseDatabase,tableName);


            if(!tableDBMSObj.tableStructure.useIndex){
                int pos=0;
                RelationRow r=reader.readRecord(pos);
                //准备进行写写操作。需要上锁
                writeLock= TableReadWriteLock.getInstance().getWriteLock(databaseDBMSObj.dbName+"."+tableName);
                writeLock.lock();
                readLock=TableReadWriteLock.getInstance().getReadLock(databaseDBMSObj.dbName+"."+tableName);
                readLock.lock();

                while(r!=null){
                    //System.out.println(r);
                    //delete from course where 已选人数=0;
                    TableWriter tableWriter=new TableWriter();
                    //System.out.println(pos+" "+r+" "+MethodTools.checkLogicExpression((Stack<String>) infCollection.logicExpressions.clone(),r));
                    if(MethodTools.checkLogicExpression((Stack<String>) infCollection.logicExpressions.clone(),r)){
                        System.out.println("Try delete:"+r);
                        System.out.println("删除:"+tableWriter.delete(pos,tableDBMSObj));
                    }
                    pos++;
                    r=reader.readRecord(pos);
                    // if(MethodTools.checkLogicExpression(infCollection.logicExpressions,r));
                }

                //释放锁
                writeLock.unlock();
                readLock.unlock();
                return 1;
            }else{
                //运用索引的情况下进行删除
                writeLock= TableReadWriteLock.getInstance().getWriteLock(databaseDBMSObj.dbName+"."+tableName);
                writeLock.lock();
                readLock=TableReadWriteLock.getInstance().getReadLock(databaseDBMSObj.dbName+"."+tableName);
                readLock.lock();


                BplusTree bplusTree= CacheManage.getInstance().getIndex(tableDBMSObj);
                int pos=MethodTools.getRecordPosThroughIndex((Stack<String>)infCollection.logicExpressions.clone(),tableDBMSObj);
                TableWriter tableWriter=new TableWriter();
                tableWriter.delete(pos,tableDBMSObj);

                writeLock.unlock();
                readLock.unlock();
                return 1;
            }



        }catch (Exception e){return -2;}

    }
}
