package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;
import automaton.SQLSession;
import dbms.CacheManage;
import dbms.RelationRow;
import dbms.TableReader;
import dbms.logic.DataType;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructure;
import dbms.physics.BplusTree;
import filesystem.PropertiesFileTool;
import javafx.scene.control.Tab;

import java.io.*;
import java.nio.ByteBuffer;

//创建索引
public class IndexCreateMethod implements INodeFunc,Serializable {
    @Override
    public Object doWork(InfCollection infCollection, Object... objs){
        SQLSession sqlSession=(SQLSession)objs[0];
        String tableName=infCollection.tableNames.pop();
        String indexOn=infCollection.columNames.pop();
        System.out.println("create on table_"+tableName+" "+indexOn);
        //检查是否使用了某个数据库
        if(sqlSession.curUseDatabase.compareTo("")==0){
            System.out.println("No selected Database.");
            return -1;
        }
        DatabaseDBMSObj databaseDBMSObj=new DatabaseDBMSObj(sqlSession.curUseDatabase,DatabaseDBMSObj.rootPath);
        //检查数据库中表是否存在
        if(!databaseDBMSObj.isTableExist(tableName)){
            System.out.println("Table '"+tableName+"' no exist in database "+sqlSession.curUseDatabase);
            return -1;
        }
        try {
            //表逻辑对象
            TableDBMSObj tableDBMSObj=new TableDBMSObj(tableName,databaseDBMSObj);
            if(createIndex(indexOn,sqlSession.curUseDatabase,tableDBMSObj)){
                //索引创建成功,重新写入表结构到文件
                TableStructure tableStructure=tableDBMSObj.tableStructure;
                tableStructure.useIndex=true;
                tableStructure.indexOn=indexOn;
                //写入文件
                tableStructure.writeToStructFile(sqlSession.curUseDatabase,tableName);
                System.out.println("Create index on table "+tableName+" ("+indexOn+") success");
                return 1;
            }else{
                System.out.println("Create index fail");
                return -2;
            }
        }catch (Exception e){
            e.printStackTrace();
            return -3;
        }



    }

    private boolean createIndex(String columnName, String dbName,TableDBMSObj tableDBMSObj){
        if(!tableDBMSObj.tableStructure.isColumnExists(columnName)) return false;
        //创建索引
        try {
            TableReader reader = CacheManage.getInstance().getTableReader(dbName,tableDBMSObj.tbName);
            int pos = 0;
            RelationRow record = reader.readRecord(pos);
            DataType dataType = tableDBMSObj.tableStructure.getDataType(columnName);


            String path=DatabaseDBMSObj.rootPath+"\\"+tableDBMSObj.dbBelongedTo.dbName+"\\"+tableDBMSObj.tbName+".lh";
            FileOutputStream fos=new FileOutputStream(path);
            DataOutputStream dos=new DataOutputStream(fos);
            BufferedOutputStream bos=new BufferedOutputStream(dos);




            //为每条记录创建索引
            while (record != null) {
                byte[] pos_bs=ByteBuffer.allocate(4).putInt(pos).array();
                switch (dataType){
                    case INT32:
                        int key_int=(Integer)record.getVal(columnName);
                        byte[] key_bs= ByteBuffer.allocate(4).putInt(key_int).array();
                        bos.write(key_bs,0,4);
                        bos.write(pos_bs,0,4);
                        break;
                    case STRING:
                        String key_str=(String)record.getVal(columnName);
                        byte[] str_bs=key_str.getBytes();
                        bos.write(str_bs,0,str_bs.length);
                        bos.write(pos_bs,0,4);
                        break;
                }

                pos++;
                record = reader.readRecord(pos);
            }

            bos.close();
            dos.close();
            fos.close();

            System.out.println("索引建立完成");
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
