package dbms;

public class IntergrityConstraint extends BaseIntergrityConstraint{
    IIntergrityConstraint constranitMethod=null;
    TableStructureItem tbs=null;
    public IntergrityConstraint(TableStructureItem tbs,IIntergrityConstraint check){
        this.tbs=tbs;
        this.constranitMethod=check;
    }
    @Override
    public boolean check(RelationItem ri){
        if(constranitMethod==null) return false;
        return constranitMethod.check(tbs,ri);
    }
}
