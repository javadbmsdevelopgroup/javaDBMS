package dbms;

import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructureItem;

public class TablePage {
    private int pageSize=20;  //页大小（单位:记录）
    RelationRow[] records=null;  //页内记录
    public TableDBMSObj tableDBMSObj=null;
    private int recordSize=-1;
    public int getPageSize(){
        return  pageSize;
    }
    public TablePage(TableDBMSObj tableDBMSObj, int pageSize){
        this.pageSize=pageSize;
        this.tableDBMSObj=tableDBMSObj;
        calcSize();
        records=new RelationRow[pageSize];
    }
    private void calcSize(){
        this.recordSize=tableDBMSObj.tableStructure.getSize();
    }
}
