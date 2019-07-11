package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;
import automaton.SQLSession;
import dbms.logic.DataType;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableStructure;
import dbms.logic.TableStructureItem;
import filesystem.PropertiesFileTool;

import java.io.IOException;

public class CreateMethod implements INodeFunc {
    //create table student (stuCode int primary key ,name string(50) not null ,classNum int)
    @Override
    public Object doWork(InfCollection infCollection, Object... objs){
        if(infCollection.keyWords.size()==0){
            //Database
            System.out.println("Create "+ infCollection.dbNames);
            DatabaseDBMSObj databaseDBMSObj=new DatabaseDBMSObj(infCollection.dbNames.pop(), PropertiesFileTool.getInstance().readConfig("DBRoot"));
            if(databaseDBMSObj.create()){
                System.out.println("Database '"+databaseDBMSObj.dbName+"' create successful");
                return 1;
            }else{
                System.out.println("Database '"+databaseDBMSObj.dbName+"' create fail");
                return -1;
            }

        }else{
            //Table
            SQLSession sqlSession=(SQLSession)objs[0];
            if(sqlSession.curUseDatabase.compareTo("")==0){
                System.out.println("No selected database");
                return -2;
            }
            String tbName=infCollection.tableNames.pop();


            TableStructure newTableStructure = new TableStructure(null);
            while (!infCollection.keyWords.empty()){
                String cname=infCollection.columNames.pop();
                String type=infCollection.keyWords.pop();
                System.out.println(type+" "+cname);
                if(type.toUpperCase().compareTo("INT")==0 || type.toUpperCase().compareTo("STRING")==0){

                    newTableStructure.addItem(new TableStructureItem(
                            (type.toUpperCase().compareTo("INT")==0? DataType.INT32:DataType.STRING),
                            (type.toUpperCase().compareTo("INT")==0? 4:Integer.parseInt(infCollection.others.pop())),
                            false,false,newTableStructure,cname
                            ));
                    //System.out.println("false false "+cname+" "+type);
                }else{
                    boolean isNotNULL=false;
                    boolean isKey=false;
                    while(!(type.toUpperCase().compareTo("INT")==0 || type.toUpperCase().compareTo("STRING")==0)){
                        if(type.toUpperCase().compareTo("NULL")==0) isNotNULL=true;
                        if(type.toUpperCase().compareTo("KEY")==0) isKey=true;
                        type=infCollection.keyWords.pop();
                    }
                    //infCollection.keyWords.push(type);
                    newTableStructure.addItem(new TableStructureItem(
                            (type.toUpperCase().compareTo("INT")==0? DataType.INT32:DataType.STRING),
                            (type.toUpperCase().compareTo("INT")==0? 4:Integer.parseInt(infCollection.others.pop())),
                            isKey,isNotNULL,newTableStructure,cname
                    ));
                    //System.out.println(isKey+" "+isNotNULL+" "+cname+" "+type);

                }


            }

            try {
                newTableStructure.writeToStructFile(((SQLSession) objs[0]).curUseDatabase, tbName);
                System.out.println("Create table '"+tbName+"' in database '"+((SQLSession) objs[0]).curUseDatabase+"' successful");
                return 2;
            }catch (IOException e){
                e.printStackTrace();
                return -2;
            }
        }

    }
}
