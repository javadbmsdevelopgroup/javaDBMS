package dbms.logic;

import dbms.RelationItem;

import java.io.Serializable;

//对于某表某属性的完整性约束类
public class IntergrityConstraint extends BaseIntergrityConstraint implements Serializable {
    IIntergrityConstraint constranitMethod=null;    //实现了完整性约束接口的对象
    TableStructureItem tbs=null;
    public IntergrityConstraint(TableStructureItem tbsI,IIntergrityConstraint check){
        this.tbs=tbsI;
        this.constranitMethod=check;
    }

    @Override
    public boolean check(RelationItem ri){
        if(constranitMethod==null) return false;
        return constranitMethod.check(tbs,ri);
    }

}
