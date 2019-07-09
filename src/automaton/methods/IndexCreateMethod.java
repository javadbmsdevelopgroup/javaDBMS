package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;
import automaton.SQLSession;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructure;

public class IndexCreateMethod implements INodeFunc {
    @Override
    public Object doWork(InfCollection infCollection, Object... objs){
        SQLSession sqlSession=(SQLSession)objs[0];
        String tableName=infCollection.tableNames.pop();
        String indexOn=infCollection.columNames.pop();
        System.out.println("create on table_"+tableName+" "+indexOn);
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
        try {
            //要等锁
            //补充lock
            TableDBMSObj tableDBMSObj=new TableDBMSObj(tableName,databaseDBMSObj);
            if(createIndex(indexOn,tableDBMSObj)){
                //索引创建成功,重新写入文件
                TableStructure tableStructure=tableDBMSObj.tableStructure;
                tableStructure.useIndex=true;
                tableStructure.indexOn=indexOn;
                tableStructure.writeToStructFile(sqlSession.curUseDatabase,tableName);
                System.out.println("Create index on table "+tableName+" ("+indexOn+") success");
            }else{
                System.out.println("Create index fail");
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }



        return null;
    }

    private boolean createIndex(String columnName, TableDBMSObj tableDBMSObj){
        if(!tableDBMSObj.tableStructure.isColumnExists(columnName)) return false;
        return false;
    }
}
