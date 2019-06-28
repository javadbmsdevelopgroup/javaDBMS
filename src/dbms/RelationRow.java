package dbms;

import dbms.logic.IntergrityConstraint;
import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructure;
import dbms.logic.TableStructureItem;

import java.util.ArrayList;
import java.util.List;

////关系实例
public class RelationRow {
    //未完成
    TableStructure tbs=null;
   // TableDBMSObj tbBelongedTo=null;
    List<RelationSItem> sis=new ArrayList<>();
    public RelationRow(TableStructure structure){
        tbs=structure;
        for(TableStructureItem item:tbs.dts){
            sis.add(new RelationSItem(item.type));
        }
    }

    public boolean checkIntegrity(){
        for(int i=0;i<sis.size();i++){
           for(IntergrityConstraint ic:tbs.dts.get(i).ics){
               if(!ic.check(sis.get(i)))return false;
           }
        }
        return true;
    }


}
