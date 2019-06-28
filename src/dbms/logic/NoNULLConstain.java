package dbms.logic;

import dbms.RelationItem;

import java.io.Serializable;

public class NoNULLConstain implements IIntergrityConstraint, Serializable {
    @Override
    public boolean check(TableStructureItem tbs, RelationItem ri){
        if(tbs.isKey && ri.isNULL()) return false;
        return true;
    }
}
