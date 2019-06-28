package dbms.logic;

import dbms.RelationSItem;

/////完整性约束基类
public  abstract class BaseIntergrityConstraint{
    public abstract boolean check(RelationSItem ri);  //对关系项进行约束检查
}