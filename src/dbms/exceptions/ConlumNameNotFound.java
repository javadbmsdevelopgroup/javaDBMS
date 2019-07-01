package dbms.exceptions;
////////////////////////////////////////////////////////尝试访问不存在的列///////////////////////////////
public class ConlumNameNotFound extends Exception {
    public ConlumNameNotFound(String str){
        super(str);
    }
}
