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
import java.io.Serializable;

//create指令实现。。。功能很强大。可以创表，可以创索引（见IndexCreateMethod类），可以创数据库
public class CreateMethod implements INodeFunc, Serializable {

    @Override
    public Object doWork(InfCollection infCollection, Object... objs){
        if(infCollection.keyWords.size()==0){
            //创建数据库
            System.out.println("Create "+ infCollection.dbNames);
            //创建数据库逻辑对象
            DatabaseDBMSObj databaseDBMSObj=new DatabaseDBMSObj(infCollection.dbNames.pop(), PropertiesFileTool.getInstance().readConfig("DBRoot"));
            if(databaseDBMSObj.create()){
                System.out.println("Database '"+databaseDBMSObj.dbName+"' create successful");
                return 1;
            }else{
                System.out.println("Database '"+databaseDBMSObj.dbName+"' create fail");
                return -1;
            }
        }else{
            //创建表
            SQLSession sqlSession=(SQLSession)objs[0];
            if(sqlSession.curUseDatabase.compareTo("")==0){
                System.out.println("No selected database");
                return -2;
            }
            String tbName=infCollection.tableNames.pop();   //表名


            //创建一个独立的表结构对象
            TableStructure newTableStructure = new TableStructure(null);
            //这个while从栈中获取收集的信息完整表结构的填写操作
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
                }else{
                    boolean isNotNULL=false;
                    boolean isKey=false;
                    while(!(type.toUpperCase().compareTo("INT")==0 || type.toUpperCase().compareTo("STRING")==0)){
                        if(type.toUpperCase().compareTo("NULL")==0) isNotNULL=true;
                        if(type.toUpperCase().compareTo("KEY")==0) isKey=true;
                        type=infCollection.keyWords.pop();
                    }

                    newTableStructure.addItem(new TableStructureItem(
                            (type.toUpperCase().compareTo("INT")==0? DataType.INT32:DataType.STRING),
                            (type.toUpperCase().compareTo("INT")==0? 4:Integer.parseInt(infCollection.others.pop())),
                            isKey,isNotNULL,newTableStructure,cname
                    ));


                }


            }

            try {
                //写入表结构文件到硬盘(.tbs文件)
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
