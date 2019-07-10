package automaton.methods;

import automaton.InfCollection;
import automaton.SQLSession;
import com.sun.corba.se.spi.ior.ObjectKey;
import dbms.IndexCache;
import dbms.RelationRow;
import dbms.logic.DataType;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableDBMSObj;
import dbms.physics.BplusTree;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MethodTools {
    private static void printStack(Stack<String> objectStack){
        System.out.print("[");
        for(int i=0;i<objectStack.size();i++){
            System.out.print(objectStack.get(i)+",");
        }
        System.out.println("]");
    }
    private static boolean calc(String l,String r,String operator,RelationRow rr){

        if(rr==null) return false;
        if(operator.compareTo("!")==0){
            if(r.toUpperCase().compareTo("TRUE")!=0 && r.toUpperCase().compareTo("FALSE")!=0){
                return false;
            }else{
               return r.toUpperCase().compareTo("TRUE")==0?false:true;
            }
        }else{
            switch (operator){
                case "=":
                    boolean isIntComp=false;
                    try{
                        int left= Integer.parseInt(l);
                        isIntComp=true;
                    }catch (Exception e){
                    }
                    if(isIntComp){
                        try{
                            int left= Integer.parseInt(l);
                            int right= Integer.parseInt(l);
                            if(left==right) return true;
                        }catch (Exception e){
                            return false;
                        }
                    }else{
                        Object val=rr.getVal(l);
                        if(val==null) return false; //列名不存在
                        switch (rr.getConlumType(l)){
                            case INT32:
                                return val.equals(Integer.parseInt(r));
                            case STRING:
                                String recordStrVal=(String)val;
                                String realRecordStrVal="";
                                for(int i=0;i<recordStrVal.length();i++){
                                    char c=recordStrVal.charAt(i);
                                    if(c!='\u0000'){
                                        realRecordStrVal+=c;
                                    }else break;
                                }
                                return ((String)realRecordStrVal).compareTo(r)==0;
                        }
                    }
                    break;
            }
        }
        /* System.out.println(l);
        System.out.println(r);
        System.out.println(operator);
        System.out.println();*/
        return false;
    }
    public static boolean checkLogicExpression(Stack<String> expressionStack, RelationRow rr){
        Map<String,Integer> prior=new HashMap<>();
        prior.put(")",0);
        prior.put("(",1);
        prior.put("OR",2);
        prior.put("AND",3);
        prior.put("!",4);
        prior.put("=",5);
        Stack<String> inputStack=new Stack<>();
        Stack<String> charsStack=new Stack<>();
        Stack<String> operandStack=new Stack<>();
        while(!expressionStack.empty()){
            inputStack.push(expressionStack.pop());
        }
        while(!inputStack.empty()){
            String cur=inputStack.pop();
            //System.out.println(cur);
            switch (cur.toUpperCase()){
                case "(":
                charsStack.push(cur);
                //System.out.println("push "+cur);
                break;
                case ")":
                    if(charsStack.empty()) break;
                    while(!charsStack.empty() && prior.get(")")<prior.get(charsStack.peek()) ){

                        String curPeek=charsStack.peek();
                        if(curPeek.compareTo("(")==0) {
                            charsStack.pop();
                            System.out.println("pop (");
                            printStack(charsStack);
                            printStack(operandStack);

                            continue;
                        }
                        String r="";
                        String l="";
                        if(curPeek.compareTo("!")==0){
                            r=operandStack.pop();
                            System.out.println("Operand pop "+r);
                        }else{
                            r=operandStack.pop();
                            System.out.println("Operand pop "+r);
                            l=operandStack.pop();
                            System.out.println("Operand pop "+l);
                        }

                        boolean b=MethodTools.calc(l,r,curPeek,rr);
                        System.out.println("calc "+curPeek);
                        charsStack.pop();

                        operandStack.push(b?"TRUE":"FALSE");
                        printStack(charsStack);
                        printStack(operandStack);
                    }
                   // System.out.println("finish )");
                    printStack(charsStack);
                    printStack(operandStack);
                    break;
                case "OR":
                    while(prior.get("OR")<prior.get(charsStack.peek())){
                        String curPeek=charsStack.peek();
                        String r="";
                        String l="";
                        if(curPeek.compareTo("!")==0){
                            r=operandStack.pop();
                            System.out.println("Operand pop "+r);
                        }else{
                            r=operandStack.pop();
                            System.out.println("Operand pop "+r);
                            l=operandStack.pop();
                            System.out.println("Operand pop "+l);
                        }

                        boolean b=MethodTools.calc(l,r,curPeek,rr);
                        System.out.println("calc "+curPeek);
                        charsStack.pop();
                        System.out.println("push result '"+b+"' to operand");
                        operandStack.push(b?"TRUE":"FALSE");
                    }
                   // System.out.println("push "+"OR"+" to opertator");
                    charsStack.push("OR");
                    //printStack(charsStack);
                    //printStack(operandStack);
                    break;
                case "AND":

                    while(!charsStack.empty() && prior.get("AND")<prior.get(charsStack.peek())){
                        String curPeek=charsStack.peek();
                        String r="";
                        String l="";
                        if(curPeek.compareTo("!")==0){
                            r=operandStack.pop();
                           // System.out.println("Operand pop "+r);
                        }else{
                            r=operandStack.pop();
                           // System.out.println("Operand pop "+r);
                            l=operandStack.pop();
                            //System.out.println("Operand pop "+l);
                        }

                        boolean b=MethodTools.calc(l,r,curPeek,rr);
                       // System.out.println("calc "+curPeek);
                        charsStack.pop();
                       // System.out.println("push result '"+b+"' to operand");
                        operandStack.push(b?"TRUE":"FALSE");
                    }
                    //System.out.println("push "+"AND"+" to opertator");
                    charsStack.push("AND");
                    //printStack(charsStack);
                    //printStack(operandStack);
                    break;
                case "=":
                    break;

                    default:
                        String s="";
                        for(int i=0;i<cur.length();i++){
                            if(cur.charAt(i)=='=' || cur.charAt(i)=='!'){

                                if(s.compareTo("")!=0){
                                    //System.out.println("push "+s+" to operand ");
                                    operandStack.push(s);
                                    s="";
                                }
                                if(!charsStack.empty() && prior.get( String.valueOf(cur.charAt(i)))>prior.get(charsStack.peek())){
                                    //System.out.println("push "+cur.charAt(i)+" to opertator");
                                    charsStack.push(String.valueOf(cur.charAt(i)));
                                }else{
                                    charsStack.push("=");
                                    //System.out.println("push "+cur.charAt(i)+" to opertator");
                                }
                            }else{
                                s+=cur.charAt(i);
                            }
                        }
                        if(s.compareTo("")!=0){
                            //System.out.println("push "+s+" to operand ");
                            operandStack.push(s);
                            s="";
                        }
                        //System.out.println(11);
                        break;
                case "<":
                    break;
                case ">":
                    break;
            }

        }
        //printStack(charsStack);
       // printStack(operandStack);
        while (!charsStack.empty()){
            String curPeek=charsStack.peek();
            String r="";
            String l="";
            if(curPeek.compareTo("!")==0){
                r=operandStack.pop();
                //System.out.println("Operand pop "+r);
            }else{
                r=operandStack.pop();
                //System.out.println("Operand pop "+r);
                l=operandStack.pop();
               // System.out.println("Operand pop "+l);
            }

            boolean b=MethodTools.calc(l,r,curPeek,rr);
            //System.out.println("calc "+curPeek);
            charsStack.pop();
            //System.out.println("push result '"+b+"' to operand");
            operandStack.push(b?"TRUE":"FALSE");
        }
        //printStack(charsStack);
        //printStack(operandStack);
        if(operandStack.size()>1) return false;
        if(operandStack.peek().toUpperCase().compareTo("TRUE")==0) return true; else return false;

    }

    public static boolean checkTableandDatabase(SQLSession sqlSession, String tableName){
        //检查是否使用了某个数据库
        if(sqlSession.curUseDatabase.compareTo("")==0){
            System.out.println("No selected Database.");
            return false;
        }
        DatabaseDBMSObj databaseDBMSObj=new DatabaseDBMSObj(sqlSession.curUseDatabase,DatabaseDBMSObj.rootPath);
        //检查数据库中表是否存在
        if(!databaseDBMSObj.isTableExist(tableName)){
            System.out.println("Table '"+tableName+"' no exist in database "+sqlSession.curUseDatabase);
            return false;
        }
        return true;
    }


    public static int getRecordPosThroughIndex(Stack<String> expressionStack,TableDBMSObj tableDBMSObj){
        //select * from student where 学号=100;
        String conlumn="";
        String val="";
        boolean left=true;
        if(expressionStack.empty()) return -2;  //索引查询错误
        String tmpExpression=expressionStack.pop();
        for(int i=0;i<tmpExpression.length();i++){
            if(tmpExpression.charAt(i)!='='){
                if(left){
                    conlumn+=tmpExpression.charAt(i);
                }else{
                    val+=tmpExpression.charAt(i);
                }
            }else{
                left=false;
            }
        }
        if(!tableDBMSObj.tableStructure.isColumnExists(conlumn) || conlumn.compareTo(tableDBMSObj.tableStructure.indexOn)!=0) {
            System.out.println("列名不是索引");
            return -2; //查询值不是索引
        }

        DataType dataType=tableDBMSObj.tableStructure.getDataType(conlumn);


        BplusTree indexBtree=IndexCache.getInstance().getIndex(tableDBMSObj);


        if(indexBtree==null) {
            return -2;  //载入索引失败
        }

        Object pos=null;
        switch (dataType){
            case STRING:
                pos=indexBtree.get(val);
            case INT32:
                pos=indexBtree.get(Integer.parseInt(val));
        }


        if(pos==null) return -1; else return (int)pos;
    }
    public static void main(String[] args){
        Stack<String> test=new Stack<>();
       /* test.push("(");
        test.push("(");
        test.push("A=2");
        test.push("and");
        test.push("(");
        test.push("B!=3");
        test.push("or");
        test.push("C=2");
        test.push(")");
        test.push(")");
        test.push("And");
        test.push("1=1");
        test.push(")");*/
        test.push("学号=22");
        test.push("and");
        test.push("编号=24");
        test.push("AND");
        test.push("AA=24");
        //  ((A=2 and (b!=3 or c=2)) and 1=1)

        MethodTools.checkLogicExpression(test,null);

    }
}
