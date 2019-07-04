package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;
import automaton.SQLSession;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableStructure;
import filesystem.PropertiesFileTool;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

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


        //检验参数对应性
        if(infCollection.columNames.size()==0){  //直接用values的情况 (必须插入全部值)

        }
        return null;
    }
}
