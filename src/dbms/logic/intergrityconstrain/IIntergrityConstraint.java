package dbms.logic.intergrityconstrain;

import dbms.RelationSItem;
import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructureItem;

/*完整性约束检查接口*/

public interface IIntergrityConstraint {
    //参数为 一个表属性(逻辑)，表项(实例),表逻辑属性。
    /*
    TableStructureItem 是表的逻辑结构属性项  （包含比如 ["学号" 数据类型 int ,是主键...]类似这样的信息，不包含实际的数据)
    RelationSItem 是实际的某数据。  比如 学号=1111.  完整性约束函数需要对其进行检查（能不能插入表中）
    TableDBMSObj 是表的逻辑对象  (包含[关联的是哪个表，表的文件地址在哪里，表名是什么之类的信息，同时里面还包含表结构对象等。。]
                   因为完整性约束可能涉及和其他表项的关系（如受主键约束项不能和其他项数据重复).所以需要提供这个参数)
    */
    boolean check(TableStructureItem tbs, RelationSItem ri, TableDBMSObj tableDBMSObj);
}
