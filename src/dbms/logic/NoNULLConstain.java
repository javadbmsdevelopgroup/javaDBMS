package dbms.logic;

import dbms.RelationSItem;

import java.io.Serializable;

public class NoNULLConstain implements IIntergrityConstraint, Serializable {
    @Override
    public boolean check(TableStructureItem tbs, RelationSItem ri){
        if(tbs.isKey && ri.isNULL()) return false;
        return true;
    }
}
