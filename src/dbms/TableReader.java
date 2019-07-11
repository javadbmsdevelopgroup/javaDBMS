package dbms;

import com.sun.org.apache.regexp.internal.RE;
import dbms.exceptions.BufferSizeException;
import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructureItem;
import dbms.physics.BplusTree;

import javax.management.relation.Relation;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

///////////////////////////////表读取器
public class TableReader {

    public TableDBMSObj tableDBMSObj=null;   //表逻辑对象
    TableBuffer tableBuffer;        //缓冲
    int pageSize;  //页大小
    BplusTree indexCache=null;
    int kh=1;



    public boolean resetBufferRecord(int recordPos,RelationRow r){
        int pageNum=recordPos/this.pageSize;
        if(!tableBuffer.pageMap.containsKey(pageNum)) return false;
        tableBuffer.pageMap.get(pageNum).records[recordPos%pageSize]=r;

        return true;
    }
    public int getPageSize(){
        return pageSize;
    }
    public void replaceRecord(int recordIndex,RelationRow newR){

        int pageIndex=recordIndex/pageSize; //计算页号
        int offset=recordIndex%pageSize;
        if(tableBuffer.pageMap.containsKey(pageIndex)){
            //获取缓冲区写锁
            Lock writeLock=CacheReadWriteLock.getInstance().getWriteLock(tableBuffer);
            writeLock.lock();
            tableBuffer.pageMap.get(pageIndex).records[offset]=newR;
            writeLock.unlock();
        }
    }
    public void deletePage(int pageIndex){
        Lock readLock=CacheReadWriteLock.getInstance().getReadLock(tableBuffer);
        readLock.lock();
        boolean exists=tableBuffer.isPageExist(pageIndex);
        readLock.unlock();
        if(!exists){
            return;
        }else{
            //获取缓冲区写锁
            Lock writeLock=CacheReadWriteLock.getInstance().getWriteLock(tableBuffer);
            writeLock.lock();
            tableBuffer.deletePage(pageIndex);
            writeLock.unlock();
        }
    }



    //构造需要 表逻辑对象 页大小
    public TableReader(TableDBMSObj tableDBMSObj,int pageSize) throws FileNotFoundException, BufferSizeException {
        this.pageSize=pageSize;
        this.tableDBMSObj=tableDBMSObj;
        this.tableBuffer = new TableBuffer(pageSize);
        //IndexCache.loadIndex(tableDBMSObj);
    }




    //读取第i行记录所在的块 (从0开始算)
    public RelationRow readRecord(int recordPosition){
        //锁
        /*Lock tableReadLock=null;*/

        //System.out.println("尝试读取第"+recordPosition+"条记录");

        if(tableBuffer.isPageExist(recordPosition/pageSize)==false){
            //System.out.println("相应页不在缓冲区,尝试加入");
            //首先读取一页
            int p=recordPosition/pageSize;
            int s=tableDBMSObj.tableStructure.getSize();
            RandomAccessFile randomAccessFile=null;

            try{
                //打开表文件,加读锁
               /* tableReadLock=TableReadWriteLock.getInstance().getReadLock(tableDBMSObj.tbName);
                tableReadLock.lock();*/
            randomAccessFile=new RandomAccessFile(tableDBMSObj.dbBelongedTo.getRootPath()+"\\"+tableDBMSObj.dbBelongedTo.dbName+"\\"
                    +tableDBMSObj.tbName+".table","rw");
            //System.out.println("s="+s+",p="+p+"page size="+pageSize);
            randomAccessFile.seek((s+1)*p*pageSize); //调到指定位置
            if(recordPosition*tableDBMSObj.tableStructure.getSize()>randomAccessFile.length()){
                /*tableReadLock.unlock();   //释放读锁*/
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
                    byte deleted=randomAccessFile.readByte();
                    if(deleted==0){
                        //读取每个属性值
                        for(TableStructureItem relationSItem:this.tableDBMSObj.tableStructure.dts){
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
                    }else{
                        record.deleted=true;
                        tp.records[i]=record;
                        randomAccessFile.skipBytes(s); //跳过一条记录
                    }

                }
                else{
                    tp.records[i]=null;
                }
            }
                //先执行页的置换算法或加载算法
                //加新页到缓冲区，置换算法、加载算法由缓冲区类完成
                //System.out.println("加入缓冲区");
                tableBuffer.addPage(tp);
                randomAccessFile.close();
                /*tableReadLock.unlock();  //释放读锁*/

                //CacheSignManage.cleanDirtyBit(tableDBMSObj.tbName,recordPosition);


            return tp.records[recordPosition%pageSize]; //返回指定行

            }catch (Exception e){
               /* tableReadLock.unlock();*/
                e.printStackTrace();
                return null;
            }
        }else{
            //页已存在缓冲，直接返回
            //System.out.println("相应页在缓冲区,直接返回");
            RelationRow r=tableBuffer.getPage(recordPosition/pageSize).records[recordPosition % pageSize];
            return r;
        }

    }


}
