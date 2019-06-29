package dbms.logic;

public class ElementObj {
    public DataType dataType;
    public Object val;
    boolean valNull=true;
    public void setNull(){
        valNull=true;
    }
    public boolean isValNull(){return valNull;}
    public ElementObj(DataType dt){
        this.dataType=dt;
    }
    public  ElementObj(DataType dt,Object obj){
        this.dataType=dt;
        this.val=obj;
        this.valNull=false;
    }

    public void setVal(Object val){
        this.val=val;
        valNull=false;
    }
}
