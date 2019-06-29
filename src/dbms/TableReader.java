package dbms;

import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructureItem;

import javax.management.relation.Relation;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.List;

public class TableReader {

    public TableDBMSObj tableDBMSObj=null;
    //创建一个10页的缓冲区
    TableBuffer tableBuffer=new TableBuffer(10);

    int pageSize=0;  //页大小

    int kh=1;
    //构造需要 表逻辑对象 页大小
    public TableReader(TableDBMSObj tableDBMSObj,int pageSize) throws FileNotFoundException {
        this.pageSize=pageSize;
        this.tableDBMSObj=tableDBMSObj;

    }

    //读取第i行记录所在的块 (从0开始算)
    private RelationRow readRecord(int recordPosition){
        if(tableBuffer.isPageExist(recordPosition/pageSize)==false){
            //先执行页的置换算法或加载算法
            //首先读取一页
            int p=recordPosition/pageSize;
            int s=tableDBMSObj.tableStructure.getSize();
            RandomAccessFile randomAccessFile=null;
            try{
            randomAccessFile=new RandomAccessFile(tableDBMSObj.dbBelongedTo.getRootPath()+"\\"+tableDBMSObj.dbBelongedTo.dbName+"\\"
                    +tableDBMSObj.tbName+".table","rw");
            randomAccessFile.seek(s*p);
            TablePage tp=new TablePage(this.tableDBMSObj,pageSize);
            for(int i=0;i<pageSize;i++){
                RelationRow record=new RelationRow();
            }

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }else{
            return tableBuffer.getPage(recordPosition/pageSize).records[recordPosition % pageSize];
        }
    }


}
