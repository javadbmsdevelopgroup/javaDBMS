package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;
import automaton.SQLSession;
import dbms.logic.DatabaseDBMSObj;
import filesystem.PropertiesFileTool;

import javax.swing.plaf.synth.SynthSpinnerUI;
import java.io.IOException;

public class UseMethod implements INodeFunc {
    @Override
    public Object doWork(InfCollection infCollection, Object... objs){
        String dbName = (infCollection.dbNames.pop());
        try {
            DatabaseDBMSObj.rootPath = PropertiesFileTool.getInstance().readConfig("DBRoot");
        }catch (IOException ioe){

        }

        if(!DatabaseDBMSObj.isExist(dbName)){
            System.out.println("Unknow database '"+dbName+"'");
        }else{
            if(objs.length==0){
                System.out.println("Fatal: SQLSession Error");
            }
            ((SQLSession)objs[0]).curUseDatabase=dbName;
            System.out.println("Database changed to '"+dbName+"'");
        }
        return null;
    }
}
