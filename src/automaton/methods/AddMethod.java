package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;
import automaton.SQLSession;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableDBMSObj;
import dbms.logic.intergrityconstrain.NonNegativeConstaint;
import dbms.logic.intergrityconstrain.PositiveConstrain;

import java.io.Serializable;

public class AddMethod implements INodeFunc, Serializable {
    @Override
    public Object doWork(InfCollection infCollection, Object... objs){
        String constrainName=infCollection.keyWords.pop();
        SQLSession sqlSession=(SQLSession)objs[0];
        String tbName=infCollection.tableNames.pop();
        String cName=infCollection.columNames.pop();
        switch (constrainName.toUpperCase()){
            case "POSITIVE":
                if(MethodTools.checkTableandDatabase(sqlSession,tbName)){
                    try {
                        TableDBMSObj tableDBMSObj = new TableDBMSObj(tbName, new DatabaseDBMSObj(sqlSession.curUseDatabase, DatabaseDBMSObj.rootPath));
                        tableDBMSObj.tableStructure.addConstain(cName,new PositiveConstrain());
                    }catch (Exception e){
                        e.printStackTrace();
                        return -1;
                    }
                }
                break;
        }
        return null;
    }
}
