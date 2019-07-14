package dbms;

import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructureItem;
///////////////////////////////////////页类
public class TablePage {
    private int pageSize=20;  //页大小（单位:记录） ，一页可以存多少条记录
    RelationRow[] records=null;  //页内记录数组
    public TableDBMSObj tableDBMSObj=null;      //表逻辑对象
    private int recordSize=-1;  //一条记录的大小
    private int pageNum=-1;    //页号
    public int getPageSize(){
        return  pageSize;
    }   //获取页大小
    public int getPageNum(){
        return pageNum;
    }
    public TablePage(TableDBMSObj tableDBMSObj, int pageSize,int pageNum){
        this.pageSize=pageSize;
        this.tableDBMSObj=tableDBMSObj;
        calcSize();
        records=new RelationRow[pageSize];
        this.pageNum=pageNum;
    }
    private void calcSize(){
        this.recordSize=tableDBMSObj.tableStructure.getSize();
    }
}
