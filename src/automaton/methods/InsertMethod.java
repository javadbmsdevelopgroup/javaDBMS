package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;
import automaton.SQLSession;
import dbms.RelationRow;
import dbms.TableWriter;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructure;
import dbms.logic.TableStructureItem;
import filesystem.PropertiesFileTool;


import java.util.ArrayList;
import java.util.List;

public class InsertMethod implements INodeFunc {
    @Override
    public Object doWork(InfCollection infCollection, Object... objs){
        SQLSession sqlSession = (SQLSession) objs[0];
        //判断是否已选择一个数据库
        if(sqlSession.curUseDatabase.compareTo("")==0){
            System.out.println("No selected database.");
            return null;
        }
        //判断数据库是否存在同时里面是否存在该表
        DatabaseDBMSObj databaseDBMSObj = new DatabaseDBMSObj(sqlSession.curUseDatabase,PropertiesFileTool.getInstance().readConfig("DBRoot"));
        if(!databaseDBMSObj.isExist()){
            System.out.println("Current database not found.");
            return null;
        }
        String tableName=infCollection.tableNames.pop();
        if(!databaseDBMSObj.isTableExist(tableName)){
            System.out.println("Table '"+tableName+"' not found.");
            return null;
        }



        try{
            TableDBMSObj tableDBMSObj = new TableDBMSObj(tableName,new DatabaseDBMSObj(sqlSession.curUseDatabase, DatabaseDBMSObj.rootPath));
//insert into student values(111,222)
        //检验参数对应性
            for (TableStructureItem tableStructureItem:tableDBMSObj.tableStructure.dts){
                System.out.println(tableStructureItem.conlumName+" "+tableStructureItem.type);
            }
            if(infCollection.columNames.size()==0){  //直接用values的情况 (必须插入全部值)
                for (int t=0;t< tableDBMSObj.tableStructure.dts.size();t++){
                    TableStructureItem tableStructureItem=tableDBMSObj.tableStructure.dts.get(tableDBMSObj.tableStructure.dts.size()-1-t);
                    infCollection.columNames.push(tableStructureItem.conlumName);
                    System.out.println("push "+tableStructureItem.conlumName);
                }
            }

            TableStructure tableStructure=tableDBMSObj.tableStructure;
            //检测参数个数是否匹配
            if(infCollection.columNames.size() != infCollection.others.size()){
                System.out.println("Values not match the table column's count");
                return null;
            }else{
                //write
                TableWriter tableWriter = new TableWriter();

                RelationRow relationRow = new RelationRow(tableStructure);
                int size = tableStructure.dts.size();
                for (int i=0;i<size;i++){
                    String val= infCollection.others.pop();
                    String cName=infCollection.columNames.pop();
                    switch (tableStructure.dts.get(i).type){
                        case INT32:
                            System.out.println(cName);
                            if(!relationRow.setVal(cName,Integer.parseInt(val))){
                                System.out.println("Insert Error.(Column name '"+cName+"'not exist)");
                                return null;
                            }
                            break;
                        case STRING:
                            if(!relationRow.setVal(cName,val)){
                                System.out.println("Insert Error.(Column name '"+cName+"' not exist)");
                                return null;
                            }
                            break;

                    }
                }
                List<RelationRow> relationRows=new ArrayList<>();
                relationRows.add(relationRow);
                tableWriter.appendRelations(relationRows,tableDBMSObj);
                System.out.println("Insert successful.");
                return null;
            }
        }catch (Exception e){
            System.out.println("Insert Fails:Data type not match");
            e.printStackTrace();
        }

        return null;
    }
}
