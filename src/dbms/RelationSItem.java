package dbms;

import dbms.logic.DataType;
import dbms.logic.ElementObj;

import java.io.Serializable;

//元组中的某项
public class RelationSItem implements Serializable {
    boolean valNull=true;   //是否为null值
    ElementObj elementObj=null;   //元素对象
    public void setNull(){   //设置为null值
        elementObj.setNull();
    }
    //获取值
    public Object getVal(){
        return elementObj.val;
    }
    //构造函数
    public RelationSItem(DataType dt){
        this.elementObj=new ElementObj(dt);
    }
    public RelationSItem(DataType dt,Object obj){
         this.elementObj=new ElementObj(dt,obj); valNull=false;
    }
    public boolean isNULL(){
        return valNull;
    }
}
