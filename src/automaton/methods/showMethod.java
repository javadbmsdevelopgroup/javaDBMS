package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;
import automaton.SQLSession;
import dbms.logic.DatabaseDBMSObj;

public class ShowMethod implements INodeFunc {
    @Override
    public Object doWork(InfCollection infCollection,Object... objs){
        System.out.println("到达show终态.");
        SQLSession sqlSession=(SQLSession)objs[0];
        switch (infCollection.keyWords.pop()){
            case "TABLES":
                if(sqlSession.curUseDatabase.compareTo("")==0 || !DatabaseDBMSObj.isExist(sqlSession.curUseDatabase)){
                    System.out.println("当前未选择任何数据库,或数据库不存在");
                }
                break;
            case "DATABASES":
                break;
        }


        return null;
    }
}
