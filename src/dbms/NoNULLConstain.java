package dbms;

public class NoNULLConstain implements IIntergrityConstraint {
    @Override
    public boolean check(TableStructureItem tbs,RelationItem ri){
        if(tbs.isKey && ri.isNULL()) return false;
        return true;
    }
}
