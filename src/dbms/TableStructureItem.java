package dbms;

import java.util.ArrayList;
import java.util.List;

////////////////////////表结构项
public class TableStructureItem {
    String type="";
    int size;
    boolean isKey=false;
    List<IntergrityConstraint> ics=new ArrayList<>(); //完整性约束列表
    public TableStructureItem(String type,int size){
        this.type=type;
        this.size=size;
    }
}
