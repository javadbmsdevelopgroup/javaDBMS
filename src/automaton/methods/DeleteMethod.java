package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;
import automaton.SQLSession;
import dbms.RelationRow;
import dbms.TableReader;
import dbms.TableWriter;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.Relation;
import dbms.logic.TableDBMSObj;

import java.util.Stack;

public class DeleteMethod implements INodeFunc {
    //delete from course where 课程编号=1901;
    @Override
    public Object doWork(InfCollection infCollection, Object... objs){
        System.out.println("delete method");
        SQLSession sqlSession=(SQLSession)objs[0];
        String tableName=infCollection.tableNames.pop();
        //检查是否使用了某个数据库
        if(sqlSession.curUseDatabase.compareTo("")==0){
            System.out.println("No selected Database.");
            return null;
        }
        DatabaseDBMSObj databaseDBMSObj=new DatabaseDBMSObj(sqlSession.curUseDatabase,DatabaseDBMSObj.rootPath);
        //检查数据库中表是否存在
        if(!databaseDBMSObj.isTableExist(tableName)){
            System.out.println("Table '"+tableName+"' no exist in database "+sqlSession.curUseDatabase);
            return null;
        }
        try{
            TableDBMSObj tableDBMSObj=new TableDBMSObj(tableName,databaseDBMSObj);
            TableReader reader=new TableReader(tableDBMSObj,30);
            if(!tableDBMSObj.tableStructure.useIndex){
                int pos=0;
                RelationRow r=reader.readRecord(pos);
                while(r!=null){
                    //System.out.println(r);
                    //delete from course where 已选人数=0;
                    TableWriter tableWriter=new TableWriter();
                    //System.out.println(pos+" "+r+" "+MethodTools.checkLogicExpression((Stack<String>) infCollection.logicExpressions.clone(),r));
                    if(MethodTools.checkLogicExpression((Stack<String>) infCollection.logicExpressions.clone(),r)){
                        System.out.println("Try delete:"+r);
                        System.out.println(tableWriter.delete(pos,tableDBMSObj));
                        reader.deletePage(pos/reader.getPageSize());
                        pos--;
                    }
                    pos++;
                    r=reader.readRecord(pos);
                    // if(MethodTools.checkLogicExpression(infCollection.logicExpressions,r));
                }
            }else{
                //运用索引的情况下进行删除
            }



        }catch (Exception e){return null;}

        return null;
    }
}
