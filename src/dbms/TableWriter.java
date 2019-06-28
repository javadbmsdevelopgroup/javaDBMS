package dbms;

import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructure;

import java.util.List;

public class TableWriter {
    public void appendRelations(List<RelationRow> relationItems, TableDBMSObj tableDBMSObj){
        //不用索引的情况
        if(!tableDBMSObj.useIndex){
            //检查完整性约束
            for(int i=0;i<relationItems.size();i++){
                if(!relationItems.get(i).checkIntegrity()) return;
            }
        }
    }
}
