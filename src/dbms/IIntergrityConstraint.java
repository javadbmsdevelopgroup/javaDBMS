package dbms;
public interface IIntergrityConstraint{
    boolean check(TableStructureItem tbs,RelationItem ri);
}
