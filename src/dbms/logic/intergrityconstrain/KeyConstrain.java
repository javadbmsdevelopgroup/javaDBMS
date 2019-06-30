package dbms.logic.intergrityconstrain;

import dbms.RelationRow;
import dbms.RelationSItem;
import dbms.TableReader;
import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructureItem;

//主键约束
public class KeyConstrain implements IIntergrityConstraint{
    @Override
    //在已知ri是主键对应元素的情况下，知道表结构对应该项，以及相应表逻辑对象后，对主键约束进行检查
    public boolean check(TableStructureItem tbs, RelationSItem ri, TableDBMSObj tableDBMSObj){
        if(ri.isNULL()) return false;   //主键不能为空
        //主键不能有重复值
        if(!tableDBMSObj.useIndex){
            //不用索引的情况
            try{
                TableReader tr=new TableReader(tableDBMSObj,10);
                int recordNum=0;
                RelationRow rw;
                do {
                    rw= tr.readRecord(recordNum);
                    if(rw==null) break;
                    Object val=rw.getVal(tbs.conlumName);
                    if(val==null) return false;
                    if(val.equals(ri.getVal())==true) return false; //发现重复值
                    recordNum++;

                }while(true);
            }catch (Exception e){
                e.printStackTrace();
            }
            return true;

        }else{
            //用索引的情况
            return true;
        }
    }
}
