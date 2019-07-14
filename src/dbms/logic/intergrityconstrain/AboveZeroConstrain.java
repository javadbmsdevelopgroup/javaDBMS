package dbms.logic.intergrityconstrain;

import dbms.RelationSItem;
import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructureItem;

import java.io.Serializable;

public class AboveZeroConstrain implements IIntergrityConstraint, Serializable {
    @Override
    public boolean check(TableStructureItem tbs, RelationSItem ri, TableDBMSObj tableDBMSObj){
        try{
            int a=Integer.parseInt((String)ri.getVal());
            if(a<=0) return false;
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
