package dbms.logic;

import dbms.logic.intergrityconstrain.IIntergrityConstraint;
import dbms.logic.intergrityconstrain.IntergrityConstraint;
import dbms.logic.intergrityconstrain.NoNULLConstain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/////////////////////////////////////////////表结构(属性)项//////////////////////////

public class TableStructureItem implements Serializable {
    public DataType type;     //数据类型
    public int size;      //数据大小
    public boolean isKey=false;   //是否为主键
    public String conlumName="";    //列名
    TableStructure tableStructureBelongedTo=null;   //所属的表结构
    public List<IntergrityConstraint> ics=new ArrayList<>(); //完整性约束列表
    //构造
    public TableStructureItem(DataType type,int size,boolean isKey,boolean isNotNull,TableStructure ts,String name){
        this.type=type;
        this.size=size;
        this.tableStructureBelongedTo=ts;
        conlumName=name;
        //如果设置为主键，就为他添加一个主键约束
        if(isKey || isNotNull) ics.add(new IntergrityConstraint(this,new NoNULLConstain(),tableStructureBelongedTo.getTableDBMSObjBelongedTo())); //创建一个非空约束
    }
    //添加完整性约束
    public void addIntergrityConstaint(IIntergrityConstraint intergrityConstraint){
        ics.add(new IntergrityConstraint(this,intergrityConstraint,this.tableStructureBelongedTo.getTableDBMSObjBelongedTo()));
    }






}
