package dbms.logic.intergrityconstrain;

import dbms.RelationSItem;
import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructureItem;

import java.io.Serializable;
///////////////////////////////////////////完整性约束类////////////////////////////////////////////////////////

//对于某表某属性的完整性约束类
public class IntergrityConstraint extends BaseIntergrityConstraint implements Serializable {
    IIntergrityConstraint constranitMethod=null;    //完整性约束函数
    TableStructureItem tbs=null;                   //表结构项  (对应的是表某列的属性)
    TableDBMSObj tableDBMSObj=null;               //表逻辑对象
    public IntergrityConstraint(TableStructureItem tbsI,IIntergrityConstraint check,TableDBMSObj tableDBMSObj){
        this.tbs=tbsI;
        this.constranitMethod=check;
        this.tableDBMSObj=tableDBMSObj;
    }

    //完整性约束检查
    @Override
    public boolean check(RelationSItem ri){
        if(constranitMethod==null) return false;
        return constranitMethod.check(tbs,ri,tableDBMSObj);
    }

}
