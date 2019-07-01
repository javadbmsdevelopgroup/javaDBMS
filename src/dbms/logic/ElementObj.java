package dbms.logic;



//////////////////表数据元素对象
public class ElementObj implements Comparable<ElementObj>{
    public  DataType dataType;   //数据类型
    public  Object  val;        //具体的数据
    boolean valNull=true;      //是否为空数据
    //设置为Null值
    public void setNull(){
        valNull=true;
    }
    //判断是否为NUll值
    public boolean isValNull(){return valNull;}
    //构造函数,只提供数据类型。设置为null数据
    public ElementObj(DataType dt){
        this.dataType=dt;
    }
    //构造函数，提供具体数据和数据类型
    public  ElementObj(DataType dt,Object obj){
        this.dataType=dt;
        this.val=obj;
        this.valNull=false;
    }
    //重载hashCode,因为重载了equals,如果不重载hashcode会存在相同的对象hash值不同的问题
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
    //重载判断相等的函数
    @Override
    public boolean equals(Object elementObj){
        if(elementObj instanceof ElementObj){
            return val.equals(((ElementObj) elementObj).val);
        }else {return elementObj.equals(this);}
    }

    //重载比较函数
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

    //设置值
    public void setVal(Object val){
        this.val=val;
        valNull=false;
    }
}
