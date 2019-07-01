package dbms;

import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructure;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
/////////////////////////////////////////表的写入工具
public class TableWriter {
    //在末尾添加
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
                        //System.out.println(relationItems.get(i).sis.get(j).elementObj.val);
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
    //替换掉某条记录
    public void replace(int recordNum,RelationRow relationRow, TableDBMSObj tableDBMSObj) throws IOException{
        //不用索引的情况
        if(!tableDBMSObj.useIndex){
            //检查完整性约束
            if(!relationRow.checkIntegrity()) return;

            RandomAccessFile randomAccessFile=new RandomAccessFile(tableDBMSObj.dbBelongedTo.getRootPath()+"\\"+
                    tableDBMSObj.dbBelongedTo.dbName+"\\"+tableDBMSObj.tbName+".table","rw");
            randomAccessFile.seek(tableDBMSObj.tableStructure.getSize()*recordNum);  //移到文件指定位置
            System.out.println(tableDBMSObj.dbBelongedTo.getRootPath()+"\\"+
                    tableDBMSObj.dbBelongedTo.dbName+"\\"+tableDBMSObj.tbName+".table");


            System.out.println("Write "+relationRow);
            randomAccessFile.write(new byte[tableDBMSObj.tableStructure.getSize()]);
            randomAccessFile.seek(tableDBMSObj.tableStructure.getSize()*recordNum);  //移到文件指定位置
                for(int j=0;j<relationRow.sis.size();j++){   //分别写入每项
                    if(!relationRow.sis.get(j).elementObj.isValNull()){
                        //System.out.println(relationItems.get(i).sis.get(j).elementObj.val);
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
                    }

                }
            randomAccessFile.close();
            }

        }
        //删除掉某条记录
    public void delete(int recordNum, TableDBMSObj tableDBMSObj) throws IOException{
        //不用索引的情况
        if(!tableDBMSObj.useIndex){

            RandomAccessFile randomAccessFile=new RandomAccessFile(tableDBMSObj.dbBelongedTo.getRootPath()+"\\"+
                    tableDBMSObj.dbBelongedTo.dbName+"\\"+tableDBMSObj.tbName+".table","rw");
            randomAccessFile.seek(tableDBMSObj.tableStructure.getSize()*recordNum);  //移到文件指定位置
            System.out.println(tableDBMSObj.dbBelongedTo.getRootPath()+"\\"+
                    tableDBMSObj.dbBelongedTo.dbName+"\\"+tableDBMSObj.tbName+".table");


            randomAccessFile.write(new byte[tableDBMSObj.tableStructure.getSize()]);
            randomAccessFile.seek(tableDBMSObj.tableStructure.getSize()*recordNum);  //移到文件指定位置
            randomAccessFile.close();
        }

    }
}






