package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;
import automaton.SQLSession;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableDBMSObj;
import dbms.view.RelationView;
import filesystem.PropertiesFileTool;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShowMethod implements INodeFunc, Serializable {
    @Override
    public Object doWork(InfCollection infCollection,Object... objs){
        System.out.println("到达show终态.");
        SQLSession sqlSession=(SQLSession)objs[0];
        switch (infCollection.keyWords.pop()){
            case "TABLES":
                if(sqlSession.curUseDatabase.compareTo("")==0 || !DatabaseDBMSObj.isExist(sqlSession.curUseDatabase)){
                    System.out.println("No database selected");
                }else{
                    DatabaseDBMSObj databaseDBMSObj;
                    try {
                        databaseDBMSObj= new DatabaseDBMSObj(sqlSession.curUseDatabase, PropertiesFileTool.getInstance().readConfig("DBRoot"));
                    }catch (Exception e){
                        System.out.println("Fatal: Error:Database Root Not Found!");
                        return null;
                    }
                    RelationView tablesView=new RelationView("Tables_in_"+sqlSession.curUseDatabase);
                    for(TableDBMSObj t: databaseDBMSObj.listTables()){
                        tablesView.addRow(t.tbName);
                    }
                    tablesView.printRelationView();
                }
                break;
            case "DATABASES":
                    String dbRootPath= PropertiesFileTool.getInstance().readConfig("DBRoot");
                    //System.out.println(dbRoot);
                    File dbRoot = new File(dbRootPath);
                    File[] dbFiles = dbRoot.listFiles();
                    RelationView databasesView = new RelationView("Databases");
                    for(File dbf : dbFiles){
                        if(dbf.isDirectory()){
                            databasesView.addRow(dbf.getName());
                        }
                    }
                    databasesView.printRelationView();

                break;
        }

        return null;
    }
}