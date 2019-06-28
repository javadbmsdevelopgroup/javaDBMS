package dbms;

/////完整性约束基类
public  abstract class BaseIntergrityConstraint{
    public abstract boolean check(RelationItem ri);  //对关系项进行约束检查
}