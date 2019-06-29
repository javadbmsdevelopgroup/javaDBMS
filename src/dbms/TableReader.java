package dbms;

import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructureItem;

import javax.management.relation.Relation;
import java.util.List;

public class TableReader {

    public TableDBMSObj tableDBMSObj=null;
    //创建一个10页的缓冲区
    TableBuffer tableBuffer=new TableBuffer(10);


    int kh=1;
    //构造需要 表逻辑对象 页大小
    public TableReader(TableDBMSObj tableDBMSObj,int pageSize){

    }

    private boolean readRecord(){

    }


}
