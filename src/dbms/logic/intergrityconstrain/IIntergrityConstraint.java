package dbms.logic.intergrityconstrain;

import dbms.RelationSItem;
import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructureItem;

public interface IIntergrityConstraint {
    //参数为 一个表属性(逻辑)，关系项(实际)
    boolean check(TableStructureItem tbs, RelationSItem ri, TableDBMSObj tableDBMSObj);
}
