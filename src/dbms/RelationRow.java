package dbms;

import dbms.logic.ElementObj;
import dbms.logic.intergrityconstrain.IntergrityConstraint;
import dbms.logic.TableStructure;
import dbms.logic.TableStructureItem;

import java.util.ArrayList;
import java.util.List;

////关系实例
public class RelationRow implements Comparable<RelationRow>{
    //未完成
    TableStructure tbs=null;
   // TableDBMSObj tbBelongedTo=null;
    List<RelationSItem > sis=new ArrayList<>();
    public RelationRow(TableStructure structure){
        tbs=structure;
        for(TableStructureItem item:tbs.dts){
            sis.add(new RelationSItem(item.type));
        }
    }
    @Override
    public String toString(){
        String str="[";
        for(RelationSItem ris:sis){
            str+=ris.elementObj.val.toString()+" , ";
        }
        str+="]";
        return str;

    }
    public Object getVal(String conlumName){
        for(int i=0;i<tbs.dts.size();i++){
            if(tbs.dts.get(i).conlumName.compareTo(conlumName)==0){

                return sis.get(i).elementObj.val;
            }
        }
        return null;
    }
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
    public boolean checkIntegrity(){
        for(int i=0;i<sis.size();i++){
           for(IntergrityConstraint ic:tbs.dts.get(i).ics){
               if(!ic.check(sis.get(i)))return false;
           }
        }
        return true;
    }

    @Override
    public int compareTo(RelationRow r){
        //单个元素比较
        if(this.sis.size()<r.sis.size()) return -1;
        if(this.sis.size()>r.sis.size()) return 1;
        for(int i=0;i<this.sis.size();i++){
            if(this.sis.get(i).elementObj.dataType!=r.sis.get(i).elementObj.dataType) return -2;
            if(this.sis.get(i).elementObj.compareTo(r.sis.get(i).elementObj)==0){
                continue;
            }else{
                if(this.sis.get(i).elementObj.compareTo(r.sis.get(i).elementObj)<0) return -1;
                else return 1;
            }
        }
        return 0;
    }


}
