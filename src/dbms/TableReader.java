package dbms;

import com.sun.org.apache.regexp.internal.RE;
import dbms.exceptions.BufferSizeException;
import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructureItem;

import javax.management.relation.Relation;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.List;

///////////////////////////////表读取器
public class TableReader {

    public TableDBMSObj tableDBMSObj=null;   //表逻辑对象
    TableBuffer tableBuffer;        //缓冲
    int pageSize;  //页大小

    int kh=1;

    public int getPageSize(){
        return pageSize;
    }
    public void replaceRecord(int recordIndex,RelationRow newR){
        int pageIndex=recordIndex/pageSize; //计算页号
        int offset=recordIndex%pageSize;
        if(tableBuffer.pageMap.containsKey(pageIndex)){
            tableBuffer.pageMap.get(pageIndex).records[offset]=newR;
        }
    }
    public void deletePage(int pageIndex){
        if(!tableBuffer.isPageExist(pageIndex)){
            return;
        }else{
            tableBuffer.deletePage(pageIndex);
        }
    }
    //构造需要 表逻辑对象 页大小
    public TableReader(TableDBMSObj tableDBMSObj,int pageSize) throws FileNotFoundException, BufferSizeException {
        this.pageSize=pageSize;
        this.tableDBMSObj=tableDBMSObj;
        this.tableBuffer = new TableBuffer(pageSize);
    }



    //读取第i行记录所在的块 (从0开始算)
    public RelationRow readRecord(int recordPosition){
        System.out.println("尝试读取第"+recordPosition+"条记录");
        if(tableBuffer.isPageExist(recordPosition/pageSize)==false){
            System.out.println("相应页不在缓冲区,尝试加入");
            //首先读取一页
            int p=recordPosition/pageSize;
            int s=tableDBMSObj.tableStructure.getSize();
            RandomAccessFile randomAccessFile=null;
            try{
                //打开表文件

            randomAccessFile=new RandomAccessFile(tableDBMSObj.dbBelongedTo.getRootPath()+"\\"+tableDBMSObj.dbBelongedTo.dbName+"\\"
                    +tableDBMSObj.tbName+".table","rw");
            randomAccessFile.seek(s*p);
            if(recordPosition*tableDBMSObj.tableStructure.getSize()>randomAccessFile.length()){
                //尝试访问文件中不存在的记录
                randomAccessFile.close();
                return null;

            }
            //页对象
            TablePage tp=new TablePage(this.tableDBMSObj,pageSize,recordPosition/pageSize);
            //读取一页
            for(int i=0;i<pageSize;i++){
                if(randomAccessFile.getFilePointer()<randomAccessFile.length()){
                    RelationRow record=new RelationRow(this.tableDBMSObj.tableStructure);
                    //读取每个属性值
                    for(TableStructureItem relationSItem:this.tableDBMSObj.tableStructure.dts){
                        //System.out.println(relationSItem.conlumName+" "+relationSItem.type);
                        switch (relationSItem.type){
                            case STRING:
                                byte[] bytes=new byte[relationSItem.size];
                                randomAccessFile.read(bytes);
                                record.setVal(relationSItem.conlumName,new String(bytes));
                                break;
                            case INT32:
                                record.setVal(relationSItem.conlumName,randomAccessFile.readInt());
                                break;
                        }
                    }
                    //加入页内
                    tp.records[i]=record;
                    //System.out.println(record);
                }
                else{
                    tp.records[i]=null;
                }
            }
                //先执行页的置换算法或加载算法
                //加新页到缓冲区，置换算法、加载算法由缓冲区类完成
                System.out.println("加入缓冲区");
                tableBuffer.addPage(tp);
                randomAccessFile.close();
            return tp.records[recordPosition%pageSize]; //返回指定行

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }else{
            //页已存在缓冲，直接返回
            System.out.println("相应页在缓冲区,直接返回");

            return tableBuffer.getPage(recordPosition/pageSize).records[recordPosition % pageSize];
        }

    }


}
