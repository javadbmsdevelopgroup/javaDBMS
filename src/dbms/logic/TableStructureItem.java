package dbms.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/////////////////////////////////////////////表结构项//////////////////////////

public class TableStructureItem implements Serializable {
    DataType type;
    int size;
    boolean isKey=false;
    List<IntergrityConstraint> ics=new ArrayList<>(); //完整性约束列表
    public TableStructureItem(DataType type,int size,boolean isKey,boolean isNotNull){
        this.type=type;
        this.size=size;
        if(isKey || isNotNull) ics.add(new IntergrityConstraint(this,new NoNULLConstain())); //创建一个非空约束
    }
    //添加完整性约束
    public void addIntergrityConstaint(IIntergrityConstraint intergrityConstraint){
        ics.add(new IntergrityConstraint(this,intergrityConstraint));
    }






}
