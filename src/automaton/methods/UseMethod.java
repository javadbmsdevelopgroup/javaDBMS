package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;
import automaton.SQLSession;
import dbms.logic.DatabaseDBMSObj;
import filesystem.PropertiesFileTool;

import javax.swing.plaf.synth.SynthSpinnerUI;
import java.io.IOException;
import java.io.Serializable;

public class UseMethod implements INodeFunc, Serializable {
    @Override
    public Object doWork(InfCollection infCollection, Object... objs){
        String dbName = (infCollection.dbNames.pop());

        DatabaseDBMSObj.rootPath = PropertiesFileTool.getInstance().readConfig("DBRoot");

        if(!DatabaseDBMSObj.isExist(dbName)){
            System.out.println("Unknow database '"+dbName+"'");
            return -1;
        }else {
            if (objs.length == 0) {
                System.out.println("Fatal: SQLSession Error");
                return -1;
            }
            ((SQLSession) objs[0]).curUseDatabase = dbName;
            System.out.println("Database changed to '" + dbName + "'");
            return 1;
        }
    }
}
