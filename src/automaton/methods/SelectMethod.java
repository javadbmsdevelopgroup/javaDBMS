package automaton.methods;

import automaton.INodeFunc;
import automaton.ITransMethod;
import automaton.InfCollection;
import automaton.SQLSession;
import dbms.RelationRow;
import dbms.TableReader;
import dbms.TableWriter;
import dbms.logic.DataType;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableDBMSObj;
import dbms.view.ViewLogicMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SelectMethod implements INodeFunc {
    @Override
    public Object doWork(InfCollection infCollection,Object... objs){
        //select * from course
        String tableName=infCollection.tableNames.pop();
        SQLSession sqlSession=(SQLSession)objs[0];

        if(!MethodTools.checkTableandDatabase(sqlSession,tableName)) return null;
        List<String> columnNames=new ArrayList<>();
        while(!infCollection.columNames.empty()){
            columnNames.add(infCollection.columNames.pop());
        }
        String[] colums=new String[columnNames.size()];
        columnNames.toArray(colums);
        try{
            TableDBMSObj tableDBMSObj=new TableDBMSObj(tableName,new DatabaseDBMSObj(sqlSession.curUseDatabase,DatabaseDBMSObj.rootPath));
            TableReader reader=new TableReader(tableDBMSObj,30);
            ViewLogicMapping viewLogicMapping=new ViewLogicMapping(200,colums,tableDBMSObj);
            viewLogicMapping.showHeadLine();
            if(infCollection.logicExpressions.empty()){
                infCollection.logicExpressions.push("1=1");
            }
            if(!tableDBMSObj.tableStructure.useIndex){
                int pos=0;
                RelationRow r=reader.readRecord(pos);
                while(r!=null){
                    //update course set 课程名称=java where 课程编号=1901;
                    TableWriter tableWriter=new TableWriter();

                    if(MethodTools.checkLogicExpression((Stack<String>) infCollection.logicExpressions.clone(),r)){
                        System.out.println("get : "+r);
                        viewLogicMapping.addRelation(r);
                    }else{
                        System.out.println("skip "+r);
                    }
                    pos++;
                    r=reader.readRecord(pos);

                }
            }else{
                //用索引的情况下
            }
            viewLogicMapping.flush();
            viewLogicMapping.showBottomLine();
        }catch (Exception e){
            e.printStackTrace();
        }



        System.out.println("select");
        return null;
    }
}
