package dbms;

import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructure;
import dbms.physics.BplusTree;
import sun.misc.Cache;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.Map;
/////////////////////////////////////////表的写入工具
public class TableWriter {
    //在末尾添加
    public void appendRelations(List<RelationRow> relationItems, TableDBMSObj tableDBMSObj) throws IOException {

            //检查完整性约束
            for(int i=0;i<relationItems.size();i++){
                if(!relationItems.get(i).checkIntegrity(tableDBMSObj.tableStructure)) {

                    return;
                }
            }

            int recordSize=tableDBMSObj.tableStructure.getSize();
            RandomAccessFile randomAccessFile=new RandomAccessFile(tableDBMSObj.dbBelongedTo.getRootPath()+"\\"+
                    tableDBMSObj.dbBelongedTo.dbName+"\\"+tableDBMSObj.tbName+".table","rw");
            randomAccessFile.seek(randomAccessFile.length());  //移到文件末尾*/


        //逐行写入
            for(int i=0;i<relationItems.size();i++){
                RelationRow record=relationItems.get(i);
                //写入标志位
                randomAccessFile.writeByte(0);
                for(int j=0;j<record.sis.size();j++){   //分别写入每项

                    if(!record.sis.get(j).elementObj.isValNull()){

                        //写入数据
                        switch (relationItems.get(i).sis.get(j).elementObj.dataType){
                            case INT32:
                                int val=((int)relationItems.get(i).sis.get(j).elementObj.val);
                                randomAccessFile.writeInt(val);

                                break;
                            case STRING:
                                byte[] strBytes = ((String)relationItems.get(i).sis.get(j).elementObj.val).getBytes();
                                int limit = tableDBMSObj.tableStructure.dts.get(j).size;
                                for(int k=0;k<limit;k++){
                                    if(k<strBytes.length){
                                       randomAccessFile.writeByte(strBytes[k]);

                                    }else{
                                        randomAccessFile.writeByte(0);

                                    }
                                }
                                break;
                        }


                    }
                }
                int pos=(int)randomAccessFile.length()/recordSize-1;
                CacheManage.getInstance().resetRecordInCache(tableDBMSObj.dbBelongedTo.dbName,tableDBMSObj.tbName,record,pos);

            }
            int recordNum=((int)randomAccessFile.length()/tableDBMSObj.tableStructure.getSize())-1;
            randomAccessFile.close();



    }

    //替换掉某条记录
    public boolean replace(int recordNum,RelationRow relationRow, TableDBMSObj tableDBMSObj) throws IOException{
        String path=tableDBMSObj.dbBelongedTo.getRootPath()+"\\"+
                tableDBMSObj.dbBelongedTo.dbName+"\\"+tableDBMSObj.tbName;
        int size=tableDBMSObj.tableStructure.getSize();


        //不用索引的情况
            //检查完整性约束
            if(!relationRow.checkIntegrity(tableDBMSObj.tableStructure)) {

                return false;
            }

            RandomAccessFile randomAccessFile=new RandomAccessFile(path+".table","rw");
            randomAccessFile.seek((size+1)*recordNum);  //移到文件指定位置


            System.out.println("Write "+relationRow);
            randomAccessFile.write(new byte[size+1]);
            randomAccessFile.seek((size+1)*recordNum);  //移到文件指定位置
            randomAccessFile.writeByte(relationRow.deleted?1:0);
            if(!relationRow.deleted){
                for(int j=0;j<relationRow.sis.size();j++){   //分别写入每项

                    switch (relationRow.sis.get(j).elementObj.dataType){
                        case INT32:
                            int val=((int)relationRow.sis.get(j).elementObj.val);
                            randomAccessFile.writeInt(val);
                            break;
                        case STRING:
                            byte[] strBytes = ((String)relationRow.sis.get(j).elementObj.val).getBytes();
                            int limit = tableDBMSObj.tableStructure.dts.get(j).size;
                            for(int k=0;k<limit;k++){
                                if(k<strBytes.length){
                                    randomAccessFile.writeByte(strBytes[k]);
                                }else{
                                    randomAccessFile.writeByte(0);
                                }
                            }
                            break;
                    }
                    //}

                }
            }

            CacheManage.getInstance().resetRecordInCache(tableDBMSObj.dbBelongedTo.dbName,tableDBMSObj.tbName,relationRow,recordNum);
            randomAccessFile.close();




        return true;
    }
        //删除掉某条记录



    public boolean delete(int recordNum, TableDBMSObj tableDBMSObj) throws IOException{
        TableReader reader=CacheManage.getInstance().getTableReader(tableDBMSObj.dbBelongedTo.dbName,tableDBMSObj.tbName);
        RelationRow r=reader.readRecord(recordNum);
        if(r.deleted) return true;
        System.out.println("要删掉"+r);
        if(r==null) return false;
        r.deleted=true;
        return  replace(recordNum,r,tableDBMSObj);
    }
}






