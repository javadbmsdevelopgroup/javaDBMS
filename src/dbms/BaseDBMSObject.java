package dbms;
//数据库管理系统对象基类，包含表对象、数据库对象、视图对象等
interface IDBSObject{
    public String getType();
    public boolean create();
    public void delete();
}
public abstract class BaseDBMSObject implements IDBSObject{
    @Override
    public abstract String getType();  //获取DBMS对象类型
    @Override
    public abstract boolean create();
    @Override
    public abstract void delete();
}
