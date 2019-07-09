package dbms;

import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructure;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
/////////////////////////////////////////表的写入工具
public class TableWriter {
    //在末尾添加
    public void appendRelations(List<RelationRow> relationItems, TableDBMSObj tableDBMSObj) throws IOException {
        //不用索引的情况
        if(!tableDBMSObj.tableStructure.useIndex){
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
    public boolean replace(int recordNum,RelationRow relationRow, TableDBMSObj tableDBMSObj) throws IOException{
        String path=tableDBMSObj.dbBelongedTo.getRootPath()+"\\"+
                tableDBMSObj.dbBelongedTo.dbName+"\\"+tableDBMSObj.tbName;
        int size=tableDBMSObj.tableStructure.getSize();
        //不用索引的情况
        if(!tableDBMSObj.tableStructure.useIndex){
            //检查完整性约束
            if(!relationRow.checkIntegrity()) return false;

            RandomAccessFile randomAccessFile=new RandomAccessFile(path+".table","rw");
            randomAccessFile.seek(size*recordNum);  //移到文件指定位置


            System.out.println("Write "+relationRow);
            randomAccessFile.write(new byte[size]);
            randomAccessFile.seek(size*recordNum);  //移到文件指定位置
                for(int j=0;j<relationRow.sis.size();j++){   //分别写入每项
                    //if(!relationRow.sis.get(j).elementObj.isValNull()){
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
                    //}

                }
            randomAccessFile.close();
                return true;
            }
        return false;
        }
        //删除掉某条记录
    private void setLocker(TableDBMSObj tableDBMSObj){
        String path=tableDBMSObj.dbBelongedTo.getRootPath()+"\\"+
                tableDBMSObj.dbBelongedTo.dbName+"\\"+tableDBMSObj.tbName;
        File f=new File(path+".lock");
        try{
           f.createNewFile();
        }catch (Exception e){

        }
    }
    private void releaseLocker(){

    }
    //确认一个删除操作

    public boolean delete(int recordNum, TableDBMSObj tableDBMSObj) throws IOException{
        String path=tableDBMSObj.dbBelongedTo.getRootPath()+"\\"+
                tableDBMSObj.dbBelongedTo.dbName+"\\"+tableDBMSObj.tbName;
        //不用索引的情况

        if(!tableDBMSObj.tableStructure.useIndex){

            RandomAccessFile randomAccessFile=new RandomAccessFile(path+".table","rw");
            int size=tableDBMSObj.tableStructure.getSize();
            if(recordNum*size>randomAccessFile.length()) return false;

            byte bs[]=new byte[size];
            RandomAccessFile tmpFile=new RandomAccessFile(path+".tmp","rw");
            for(int i=0;i<recordNum;i++){
                randomAccessFile.readFully(bs,0,size);
                tmpFile.write(bs);
            }
            randomAccessFile.skipBytes(size);
            while (randomAccessFile.getFilePointer()<randomAccessFile.length()){
                randomAccessFile.readFully(bs,0,size);
                tmpFile.write(bs);
            }

           /* randomAccessFile.seek(size*recordNum);  //移到文件指定位置
            System.out.println(tableDBMSObj.dbBelongedTo.getRootPath()+"\\"+
                    tableDBMSObj.dbBelongedTo.dbName+"\\"+tableDBMSObj.tbName+".table");

            String s=new String(new byte[size]);
            randomAccessFile.writeBytes(s);
            randomAccessFile.seek(tableDBMSObj.tableStructure.getSize()*recordNum);  //移到文件指定位置*/
            tmpFile.close();
            randomAccessFile.close();
            File f=new File(path+".table");
            System.out.println("删除:"+f.delete());
            f=new File(path+".tmp");
            f.renameTo(new File(path+".table"));
            return true;
        }
        return false;
    }
}






