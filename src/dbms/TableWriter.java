package dbms;

import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructure;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

public class TableWriter {
    public void appendRelations(List<RelationRow> relationItems, TableDBMSObj tableDBMSObj) throws IOException {
        //不用索引的情况
        if(!tableDBMSObj.useIndex){
            //检查完整性约束
            for(int i=0;i<relationItems.size();i++){
                if(!relationItems.get(i).checkIntegrity()) return;
            }
            RandomAccessFile randomAccessFile=new RandomAccessFile(tableDBMSObj.dbBelongedTo.getRootPath()+"\\"+
                    tableDBMSObj.dbBelongedTo.dbName+"\\"+tableDBMSObj.tbName+".table","rw");
            randomAccessFile.seek(randomAccessFile.length());  //移到文件末尾
            System.out.println(tableDBMSObj.dbBelongedTo.getRootPath()+"\\"+
                    tableDBMSObj.dbBelongedTo.dbName+"\\"+tableDBMSObj.tbName+".table");

            for(int i=0;i<relationItems.size();i++){
                System.out.println("Write "+relationItems.get(i));
                for(int j=0;j<relationItems.get(i).sis.size();j++){   //分别写入每项
                    if(!relationItems.get(i).sis.get(j).elementObj.isValNull()){
                        System.out.println(relationItems.get(i).sis.get(j).elementObj.val);
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
            }
            randomAccessFile.close();
        }
    }






}
