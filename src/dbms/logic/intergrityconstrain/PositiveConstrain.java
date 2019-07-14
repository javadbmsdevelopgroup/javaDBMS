package dbms.logic.intergrityconstrain;

import dbms.RelationSItem;
import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructureItem;

import java.io.Serializable;

//非负约束（名起错了,NotNegative才对
public class PositiveConstrain implements IIntergrityConstraint, Serializable {
    @Override
    public boolean check(TableStructureItem tbs, RelationSItem ri, TableDBMSObj tableDBMSObj){
        try{
            int a=(Integer)ri.getVal();
            if(a<0) return false;
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
