package dbms;

import dbms.logic.DataType;
import dbms.logic.ElementObj;

public class RelationSItem {
    boolean valNull=true;
    ElementObj elementObj=null;
    public void setNull(){
        elementObj.setNull();
    }
    public Object getVal(){
        return elementObj.val;
    }
    public RelationSItem(DataType dt){
        this.elementObj=new ElementObj(dt);
    }
    public RelationSItem(DataType dt,Object obj){
        this.elementObj=new ElementObj(dt,obj);
    }
    public boolean isNULL(){
        return valNull;
    }
}
