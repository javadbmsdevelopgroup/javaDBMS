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
        String tableName=infCollection.tableNames.pop();  //表名
        Lock readLock;
        Lock writeLock;
        //检测表名和数据库名的合法性
        if(!MethodTools.checkTableandDatabase(sqlSession,tableName)) return -1;

        try{
            //创建数据库逻辑对象
            DatabaseDBMSObj databaseDBMSObj=new DatabaseDBMSObj(sqlSession.curUseDatabase,DatabaseDBMSObj.rootPath);
            //创建表逻辑对象
            TableDBMSObj tableDBMSObj=new TableDBMSObj(tableName,databaseDBMSObj);
            //获取TableReader
            TableReader reader= CacheManage.getInstance().getTableReader(sqlSession.curUseDatabase,tableName);

            //记录影响的行数
            int count=0;


            if(!tableDBMSObj.tableStructure.useIndex){  //不用索引的情况.顺序检测符合条件的记录，删除
                int pos=0;
                RelationRow r=reader.readRecord(pos);
                //准备进行写操作。需要上锁
                writeLock= TableReadWriteLock.getInstance().getWriteLock(databaseDBMSObj.dbName+"."+tableName);
                writeLock.lock();

                while(r!=null){
                    TableWriter tableWriter=new TableWriter();
                    if(MethodTools.checkLogicExpression((Stack<String>) infCollection.logicExpressions.clone(),r)){
                        if(!r.isDeleted()){
                            System.out.println("Try delete:"+r+" ");
                        }

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


                /*getRecordPosThroughIndex是一个很强大的函数，只需要提供逻辑表达式栈和一个表逻辑对象就能返回符合逻辑表达式的行所在的位置*/
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
