package dbms.logic.intergrityconstrain;

import dbms.RelationSItem;
import dbms.logic.BaseIntergrityConstraint;
import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructureItem;

import java.io.Serializable;

//对于某表某属性的完整性约束类
public class IntergrityConstraint extends BaseIntergrityConstraint implements Serializable {
    IIntergrityConstraint constranitMethod=null;    //实现了完整性约束接口的对象
    TableStructureItem tbs=null;
    TableDBMSObj tableDBMSObj=null;
    public IntergrityConstraint(TableStructureItem tbsI,IIntergrityConstraint check,TableDBMSObj tableDBMSObj){
        this.tbs=tbsI;
        this.constranitMethod=check;
        this.tableDBMSObj=tableDBMSObj;
    }

    @Override
    public boolean check(RelationSItem ri){
        if(constranitMethod==null) return false;
        return constranitMethod.check(tbs,ri,tableDBMSObj);
    }

}
