package dbms;

import dbms.logic.DataType;
import dbms.logic.ElementObj;
import dbms.logic.intergrityconstrain.IntergrityConstraint;
import dbms.logic.TableStructure;
import dbms.logic.TableStructureItem;

import java.util.ArrayList;
import java.util.List;

////表中的一行。。也就是一个元组
public class RelationRow implements Comparable<RelationRow>{

    TableStructure tbs=null;   //对应的表结构
    boolean deleted=false;
   // TableDBMSObj tbBelongedTo=null;

    List<RelationSItem > sis=new ArrayList<>();   //元组中的每项

    public boolean isDeleted() {
        return deleted;
    }

    public RelationRow(TableStructure structure){
        tbs=structure;
        for(TableStructureItem item:tbs.dts){
            sis.add(new RelationSItem(item.type));
        }
    }

    //重载toString
    @Override
    public String toString(){
        String str="[";
        for(RelationSItem ris:sis){
            str+=ris.elementObj.val.toString()+" , ";
        }
        str+="] "+(deleted?1:0);
        return str;

    }
    public DataType getConlumType(String conlumName){
        for(int i=0;i<tbs.dts.size();i++){
            if(tbs.dts.get(i).conlumName.compareTo(conlumName)==0){
                return sis.get(i).elementObj.dataType;
            }
        }
        return null;
    }
    //获取某列的值
    public Object getVal(String conlumName){
        for(int i=0;i<tbs.dts.size();i++){
            if(tbs.dts.get(i).conlumName.compareTo(conlumName)==0){
                return sis.get(i).elementObj.val;
            }
        }
        return null;
    }
    //设置值，提供列名和值
    public boolean setVal(String conlumName,Object val) {
        for(int i=0;i<tbs.dts.size();i++){
            if(tbs.dts.get(i).conlumName.compareTo(conlumName)==0){
                sis.get(i).elementObj.setVal(val);
                return true;
            }
        }
        //throw new ConlumNameNotFound("Conlum Name '"+conlumName+"' Not Found!");
        return false;
    }

    //检查完整性
    public boolean checkIntegrity(){
        for(int i=0;i<sis.size();i++){
           for(IntergrityConstraint ic:tbs.dts.get(i).ics){
               if(!ic.check(sis.get(i)))return false;
           }
        }
        return true;
    }


    //重载比较函数，用来比较两个元组
    @Override
    public int compareTo(RelationRow r){
        if(tbs==null || !tbs.useIndex) return 0;
        else{
            String index=tbs.indexOn;
            for(int i=0;i<tbs.dts.size();i++){
                if(tbs.dts.get(i).conlumName.compareTo(index)==0){
                    return this.sis.get(i).elementObj.compareTo(r.sis.get(i).elementObj);
                }
            }
        }
        return 0;
    }


}
