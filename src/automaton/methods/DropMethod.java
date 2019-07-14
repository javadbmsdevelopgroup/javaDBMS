package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;
import automaton.SQLSession;
import filesystem.PropertiesFileTool;

import java.io.File;
import java.io.Serializable;

//删除数据库/表的方法
public class DropMethod implements INodeFunc, Serializable {
    @Override
    public Object doWork(InfCollection infCollection, Object... objs){
        String objType = infCollection.keyWords.pop().toUpperCase();
        String objName = infCollection.others.pop();
        SQLSession sqlSession=(SQLSession)objs[0];
        String root = PropertiesFileTool.getInstance().readConfig("DBRoot");
        switch (objType){
            case "DATABASE":

                File dbFile = new File(root+"\\"+objName);
                if(dbFile.delete()){
                    if(sqlSession.curUseDatabase.compareTo(objName)==0) sqlSession.curUseDatabase="";
                    System.out.println("Drop database '"+objName+"' successful.");
                    return 1;
                }else{
                    System.out.println("Drop database '"+objName+"' fail.");
                    return -1;
                }
            case "TABLE":
                if(sqlSession.curUseDatabase.compareTo("")==0){
                    System.out.println("No selected database");
                    return -1;
                }else{
                    File tableFile1 = new File(root+"\\"+sqlSession.curUseDatabase+"\\"+objName+".tbs");
                    File tableFile2 = new File(root+"\\"+sqlSession.curUseDatabase+"\\"+objName+".table");
                    if(tableFile1.delete() && tableFile2.delete()){
                        System.out.println("Drop table '"+objName+"' successful.");
                        return 1;
                    }else{
                        System.out.println("Drop table '"+objName+"' fail.");
                        return -1;
                    }

                }
        }
        System.out.println(objType +" "+objName);
        return null;
    }
}
