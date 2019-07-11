package automaton.methods;

import automaton.INodeFunc;
import automaton.ITransMethod;
import automaton.InfCollection;
import automaton.SQLSession;
import dbms.*;
import dbms.logic.DataType;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableDBMSObj;
import dbms.view.RelationView;
import dbms.view.ViewLogicMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.locks.Lock;

public class SelectMethod implements INodeFunc {
    ViewLogicMapping viewLogicMapping;
    TableReader reader;
    Lock readLock=null;
    Lock writeLock=null;
    @Override
    public Object doWork(InfCollection infCollection,Object... objs){
        //select * from course
        String tableName=infCollection.tableNames.pop();
        SQLSession sqlSession=(SQLSession)objs[0];
        int limit=-1;
        String orderBy="";
        if(!MethodTools.checkTableandDatabase(sqlSession,tableName)) return -1;
        List<String> columnNames=new ArrayList<>();
        while(!infCollection.columNames.empty()){
            columnNames.add(infCollection.columNames.pop());
        }
        String[] colums=new String[columnNames.size()];
        columnNames.toArray(colums);
        try{
            TableDBMSObj tableDBMSObj=new TableDBMSObj(tableName,new DatabaseDBMSObj(sqlSession.curUseDatabase,DatabaseDBMSObj.rootPath));
            reader=new TableReader(tableDBMSObj,30);
            viewLogicMapping=new ViewLogicMapping(200,colums,tableDBMSObj);
            viewLogicMapping.showHeadLine();
            if(infCollection.logicExpressions.empty()){
                infCollection.logicExpressions.push("1=1");
            }



            if(infCollection.others.size()==1){
                String val=infCollection.others.pop();
                try{
                    limit=Integer.parseInt(val);
                }catch (Exception e){
                    orderBy=val;
                }
            }else if(infCollection.others.size()>1){
                try{
                    limit=Integer.parseInt(infCollection.others.pop());
                    orderBy=infCollection.others.pop();
                }catch (Exception e){ }
            }
            //上锁
            readLock= TableReadWriteLock.getInstance().getReadLock(tableDBMSObj.dbBelongedTo.dbName+"."+tableName);
            readLock.lock();
            if(!tableDBMSObj.tableStructure.useIndex){
                return sequentialQuery(limit,(Stack<String>) infCollection.logicExpressions.clone()); //无索引下顺序查询
            }else{
                //用索引的情况下

                int pos= MethodTools.getRecordPosThroughIndex((Stack<String>)infCollection.logicExpressions.clone(),tableDBMSObj);
                if(pos>=0){
                    RelationRow r=reader.readRecord(pos);
                    viewLogicMapping.addRelation(r);
                }else if(pos==-2){
                    System.out.println("索引查询出现错误");
                    return sequentialQuery(limit,(Stack<String>) infCollection.logicExpressions.clone());
                }else{
                    System.out.println("未查询到记录");
                    return viewLogicMapping;
                }
            }
           /* readLock.unlock();
            viewLogicMapping.flush();
            viewLogicMapping.showBottomLine();*/
        }catch (Exception e){
            if(readLock!=null ) readLock.unlock();
            e.printStackTrace();
            return viewLogicMapping;
        }



        return null;
    }

    public RelationView sequentialQuery(int limit, Stack<String> logicExpressionStack){

        if(reader==null || viewLogicMapping==null) return null;
        int pos=0;
        RelationRow r=reader.readRecord(pos);
        while(r!=null && ( (limit<0) || (limit>0 && pos<limit))){
            //update course set 课程名称=java where 课程编号=1901;

            if(MethodTools.checkLogicExpression((Stack<String>) logicExpressionStack.clone(),r)){
                //System.out.println("get : "+r);
                viewLogicMapping.addRelation(r);
            }else{
                //System.out.println("skip "+r);
            }
            pos++;
            //if(pos%10000==0) System.out.println(pos);
            System.out.println(pos+":"+r);
            r=reader.readRecord(pos);

        }
        return viewLogicMapping.getRelationView();
    }
}
