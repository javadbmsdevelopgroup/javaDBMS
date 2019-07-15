package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;
import automaton.SQLSession;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import dbms.*;
import dbms.logic.DataType;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableDBMSObj;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.Serializable;
import java.util.Stack;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;

public class UpdateMethod implements INodeFunc, Serializable {
    @Override
    public Object doWork(InfCollection infCollection, Object... objs){
        Lock readLock=null;
        Lock writeLock=null;
        SQLSession sqlSession=(SQLSession)objs[0];
        String tableName=infCollection.tableNames.pop();

        if(!MethodTools.checkTableandDatabase(sqlSession,tableName)) return null;
        try{
            DatabaseDBMSObj databaseDBMSObj=new DatabaseDBMSObj(sqlSession.curUseDatabase,DatabaseDBMSObj.rootPath);
            TableDBMSObj tableDBMSObj=new TableDBMSObj(tableName,databaseDBMSObj);
            TableReader reader=CacheManage.getInstance().getTableReader(sqlSession.curUseDatabase,tableName);
            //加入判断列名是否存在

            int count=0;


            writeLock= TableReadWriteLock.getInstance().getWriteLock(databaseDBMSObj.dbName+"."+tableName);
            writeLock.lock();

            if(!tableDBMSObj.tableStructure.useIndex){
                int pos=0;
                RelationRow r=reader.readRecord(pos);
                if(r!=null){
                    r=r.clone();
                }
                while(r!=null){
                    TableWriter tableWriter=new TableWriter();

                    if(MethodTools.checkLogicExpression((Stack<String>) infCollection.logicExpressions.clone(),r)){
                        System.out.println("Try update: "+r);

                        Stack<String> setExpressStack = (Stack<String>)infCollection.others.clone();
                        //开干每个表达式
                        while (!setExpressStack.empty()){
                            String setExpression=setExpressStack.pop();
                            //System.out.println("pop"+setExpression);
                            String conlumName="";
                            String val="";
                            boolean l=true;
                            for(int i=0;i<setExpression.length();i++){
                                if(setExpression.charAt(i)!='=')
                                {
                                    if(l)
                                    {conlumName+=setExpression.charAt(i);}
                                    else{val+=setExpression.charAt(i); }
                                }
                                else{
                                    l=false;
                                    continue;
                                }

                            }

                            if(!tableDBMSObj.tableStructure.isColumnExists(conlumName)){
                                System.out.println("Column name '"+conlumName+"' not exists.");
                                return -1;
                            }
                            switch (r.getConlumType(conlumName)){
                                case INT32:
                                    try{
                                        r.setVal(conlumName,Integer.parseInt(val));
                                    }catch (Exception e){
                                        try{
                                            //update course set 课程编号=课程编号+1 where 课程编号=17003;
                                            r.setVal(conlumName,MethodTools.calcVal(val,r));
                                        }catch (Exception e2){
                                            e2.printStackTrace();
                                            if(writeLock!=null) writeLock.unlock();
                                            if(readLock!=null) readLock.unlock();
                                            return null;
                                        }
                                    }
                                    break;
                                case STRING:
                                    r.setVal(conlumName,val);
                                    break;
                            }



                        }

                        //检查完整性
                        if(!r.checkIntegrity(tableDBMSObj.tableStructure))
                        {
                            System.out.println("完整性约束检查失败");
                        }else{
                            if(tableWriter.replace(pos,r,tableDBMSObj)){
                                count++;
                                CacheManage.getInstance().resetRecordInCache(tableDBMSObj.dbBelongedTo.dbName,tableName,r,pos);
                            }
                        }



                    }
                    pos++;
                    r=reader.readRecord(pos);
                    if(r!=null){
                        r=r.clone();
                    }

                }
            }else{
                //运用索引的情况下进行更新
                int pos=MethodTools.getRecordPosThroughIndex((Stack<String>) infCollection.logicExpressions.clone(),tableDBMSObj);
                if(pos>0){
                    TableWriter tableWriter=new TableWriter();
                    RelationRow r=reader.readRecord(pos);
                    while (!infCollection.others.empty()){
                        String setExpression=infCollection.others.pop();
                        String conlumName="";
                        String val="";
                        boolean l=true;
                        for(int i=0;i<setExpression.length();i++){
                            if(setExpression.charAt(i)!='=')
                            {
                                if(l)
                                {conlumName+=setExpression.charAt(i);}
                                else{val+=setExpression.charAt(i); }
                            }
                            else{
                                l=false;
                                continue;
                            }

                        }

                        if(!tableDBMSObj.tableStructure.isColumnExists(conlumName)){
                            System.out.println("Column name '"+conlumName+"' not exists.");
                            return -1;
                        }
                        switch (r.getConlumType(conlumName)){
                            case INT32:
                                try{
                                    r.setVal(conlumName,Integer.parseInt(val));
                                }catch (Exception e){
                                    try{
                                        //update course set 课程编号=课程编号+1 where 课程编号=17003;
                                        r.setVal(conlumName,MethodTools.calcVal(val,r));
                                    }catch (Exception e2){
                                        e2.printStackTrace();
                                        if(writeLock!=null) writeLock.unlock();
                                        if(readLock!=null) readLock.unlock();
                                        return null;
                                    }
                                }
                                break;
                            case STRING:
                                r.setVal(conlumName,val);
                                break;
                        }

                        r.setVal(conlumName,r.getConlumType(conlumName)== DataType.STRING?val:Integer.parseInt(val));


                    }
                    //检查完整性
                    if(!r.checkIntegrity(tableDBMSObj.tableStructure))
                    {
                        System.out.println("完整性约束检查失败");
                    }else{
                        if(tableWriter.replace(pos,r,tableDBMSObj)){
                            count++;
                            CacheManage.getInstance().resetRecordInCache(tableDBMSObj.dbBelongedTo.dbName,tableName,r,pos);
                        }

                    }

                }

            }

            readLock=TableReadWriteLock.getInstance().getReadLock(databaseDBMSObj.dbName+"."+tableName);
            readLock.lock();

            writeLock.unlock();
            readLock.unlock();
            return count;
        }catch (Exception e){
            if(writeLock!=null) writeLock.unlock();
            if(readLock!=null) readLock.unlock();
            e.printStackTrace();
            return -1;
        }
    }
}
