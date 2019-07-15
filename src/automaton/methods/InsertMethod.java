package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;
import automaton.SQLSession;
import dbms.RelationRow;
import dbms.TableReadWriteLock;
import dbms.TableWriter;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructure;
import dbms.logic.TableStructureItem;
import filesystem.PropertiesFileTool;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
//插入方法
public class InsertMethod implements INodeFunc, Serializable {

    @Override
    public Object doWork(InfCollection infCollection, Object... objs){
        Lock readLock;
        Lock writeLock;
        SQLSession sqlSession = (SQLSession) objs[0];
        //判断是否已选择一个数据库
        if(sqlSession.curUseDatabase.compareTo("")==0){
            System.out.println("No selected database.");
            return -1;
        }
        //判断数据库是否存在同时里面是否存在该表
        DatabaseDBMSObj databaseDBMSObj = new DatabaseDBMSObj(sqlSession.curUseDatabase,PropertiesFileTool.getInstance().readConfig("DBRoot"));
        if(!databaseDBMSObj.isExist()){
            System.out.println("Current database not found.");
            return -2;
        }
        String tableName=infCollection.tableNames.pop();
        if(!databaseDBMSObj.isTableExist(tableName)){
            System.out.println("Table '"+tableName+"' not found.");
            return -3;
        }



        try{
            TableDBMSObj tableDBMSObj = new TableDBMSObj(tableName,new DatabaseDBMSObj(sqlSession.curUseDatabase, DatabaseDBMSObj.rootPath));

        //检验参数对应性
            if(infCollection.columNames.size()==0){  //直接用values的情况 (必须插入全部值)
                for (int t=0;t< tableDBMSObj.tableStructure.dts.size();t++){
                    TableStructureItem tableStructureItem=tableDBMSObj.tableStructure.dts.get(tableDBMSObj.tableStructure.dts.size()-1-t);
                    infCollection.columNames.push(tableStructureItem.conlumName);
                }
            }

            TableStructure tableStructure=tableDBMSObj.tableStructure;
            //检测参数个数是否匹配
            if(infCollection.columNames.size() != infCollection.others.size()){
                System.out.println("Values not match the table column's count");
                return -4;
            }else{
                //下面执行写入操作
                TableWriter tableWriter = new TableWriter();

                RelationRow relationRow = new RelationRow(tableStructure); //创建一条新行
                int size = tableStructure.dts.size();
                //填充
                for (int i=0;i<size;i++){
                    String val= infCollection.others.pop();
                    String cName=infCollection.columNames.pop();
                    switch (tableStructure.dts.get(i).type){
                        case INT32:
                            if(!relationRow.setVal(cName,Integer.parseInt(val))){
                                System.out.println("Insert Error.(Column name '"+cName+"'not exist)");
                                return -5;
                            }
                            break;
                        case STRING:
                            if(!relationRow.setVal(cName,val)){
                                System.out.println("Insert Error.(Column name '"+cName+"' not exist)");
                                return -5;
                            }
                            break;
                    }
                }
                List<RelationRow> relationRows=new ArrayList<>();
                relationRows.add(relationRow);
                writeLock= TableReadWriteLock.getInstance().getWriteLock(databaseDBMSObj.dbName+"."+tableName);
                writeLock.lock();

                tableWriter.appendRelations(relationRows,tableDBMSObj);

                readLock=TableReadWriteLock.getInstance().getReadLock(databaseDBMSObj.dbName+"."+tableName);
                readLock.lock();
                writeLock.unlock();
                readLock.unlock();
                System.out.println("Insert successful.");
                return 1;
            }
        }catch (Exception e){
            System.out.println("Insert Fails:Data type not match");
            e.printStackTrace();
            return -6;
        }


    }
}
