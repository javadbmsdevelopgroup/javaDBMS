package dbms.logic.intergrityconstrain;

import dbms.RelationSItem;
import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructureItem;

import java.io.Serializable;

public class NoNULLConstain implements IIntergrityConstraint, Serializable {
    @Override
    public boolean check(TableStructureItem tbs, RelationSItem ri, TableDBMSObj tableDBMSObj){
        if(tbs.isKey && ri.isNULL()) return false;
        return true;
    }
}
