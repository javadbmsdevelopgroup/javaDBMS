package dbms.logic;

import dbms.RelationItem;

public interface IIntergrityConstraint {
    boolean check(TableStructureItem tbs, RelationItem ri);
}
