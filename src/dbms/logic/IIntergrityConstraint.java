package dbms.logic;

import dbms.RelationSItem;

public interface IIntergrityConstraint {
    //参数为 一个表属性，关系项
    boolean check(TableStructureItem tbs, RelationSItem ri);
}
