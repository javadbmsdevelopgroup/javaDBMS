package dbms.logic;

public class ElementObj implements Comparable<ElementObj>{
    public  DataType dataType;
    public  Object  val;
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
    @Override
    public int hashCode () {
        switch (dataType){
            case STRING:
                char[] charArr = ((String)val).toCharArray();
                int hash = 0;
                for(char c : charArr) {
                    hash = hash  + c;
                }
                break;
            case INT32:
                return (Integer)val;
        }
        return 0;
    }
    @Override
    public boolean equals(Object elementObj){
        if(elementObj instanceof ElementObj){
            return val.equals(((ElementObj) elementObj).val);
        }else {return elementObj.equals(this);}
    }
    @Override
    public int compareTo(ElementObj e2){
        if(e2.dataType!=this.dataType) return -1;
        switch (dataType){
            case INT32:
                return (Integer)val-(Integer)e2.val;
            case STRING:
                return ((String)val).compareTo((String) e2.val);
        }
        return -1;
    }
    public void setVal(Object val){
        this.val=val;
        valNull=false;
    }
}
