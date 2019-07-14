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

//这是一个工具类
public class MethodTools {
    //打印一个栈
    private static void printStack(Stack<String> objectStack){
        System.out.print("[");
        for(int i=0;i<objectStack.size();i++){
            System.out.print(objectStack.get(i)+",");
        }
        System.out.println("]");
    }
    //计算一个表达式。如  学号=学号+1  l:左操作数，必须是一个列名  r:右表达式 operator:比如=/And/or  rr:一行记录
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
                case "AND":
                    if(l.toUpperCase().compareTo("TRUE")==0 && r.toUpperCase().compareTo("TRUE")==0) return true;
                    return false;
                case "OR":
                    if(l.toUpperCase().compareTo("True")==0 || r.toUpperCase().compareTo("TRUE")==0) return true;
                    return false;
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

        return false;
    }
    //检测逻辑表达式的值
    public static boolean checkLogicExpression(Stack<String> expressionStack, RelationRow rr){
        Map<String,Integer> prior=new HashMap<>();
        //先存优先级
        prior.put(")",0);
        prior.put("(",1);
        prior.put("OR",2);
        prior.put("AND",3);
        prior.put("!",4);
        prior.put("=",5);
        Stack<String> inputStack=new Stack<>();
        Stack<String> charsStack=new Stack<>();  //符号栈
        Stack<String> operandStack=new Stack<>();  //操作数栈
        //把表达式栈的值取出来放到输入栈中
        while(!expressionStack.empty()){
            inputStack.push(expressionStack.pop());
        }
        //处理输入栈
        while(!inputStack.empty()){
            String cur=inputStack.pop();

            switch (cur.toUpperCase()){
                case "(":
                charsStack.push(cur);

                break;
                case ")":
                    if(charsStack.empty()) break;
                    while(!charsStack.empty() && prior.get(")")<prior.get(charsStack.peek()) ){
                        String curPeek=charsStack.peek();
                        if(curPeek.compareTo("(")==0) {
                            charsStack.pop();

                            printStack(charsStack);
                            printStack(operandStack);

                            continue;
                        }
                        String r="";
                        String l="";
                        if(curPeek.compareTo("!")==0){
                            r=operandStack.pop();

                        }else{
                            r=operandStack.pop();

                            l=operandStack.pop();

                        }

                        boolean b=MethodTools.calc(l,r,curPeek,rr);

                        charsStack.pop();

                        operandStack.push(b?"TRUE":"FALSE");
                        printStack(charsStack);
                        printStack(operandStack);
                    }

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

                        }else{
                            r=operandStack.pop();

                            l=operandStack.pop();

                        }

                        boolean b=MethodTools.calc(l,r,curPeek,rr);

                        charsStack.pop();

                        operandStack.push(b?"TRUE":"FALSE");
                    }

                    charsStack.push("OR");

                    break;
                case "AND":

                    while(!charsStack.empty() && prior.get("AND")<prior.get(charsStack.peek())){
                        String curPeek=charsStack.peek();
                        String r="";
                        String l="";
                        if(curPeek.compareTo("!")==0){
                            r=operandStack.pop();

                        }else{
                            r=operandStack.pop();

                            l=operandStack.pop();

                        }

                        boolean b=MethodTools.calc(l,r,curPeek,rr);

                        charsStack.pop();

                        operandStack.push(b?"TRUE":"FALSE");
                    }

                    charsStack.push("AND");

                    break;
                case "=":
                    break;

                    default:
                        String s="";
                        for(int i=0;i<cur.length();i++){
                            if(cur.charAt(i)=='=' || cur.charAt(i)=='!'){

                                if(s.compareTo("")!=0){

                                    operandStack.push(s);
                                    s="";
                                }
                                if(!charsStack.empty() && prior.get( String.valueOf(cur.charAt(i)))>prior.get(charsStack.peek())){
                                    charsStack.push(String.valueOf(cur.charAt(i)));
                                }else{
                                    charsStack.push("=");

                                }
                            }else{
                                s+=cur.charAt(i);
                            }
                        }
                        if(s.compareTo("")!=0){

                            operandStack.push(s);
                            s="";
                        }

                        break;
                case "<":
                    break;
                case ">":
                    break;
            }

        }

        //开始计算
        while (!charsStack.empty()){
            String curPeek=charsStack.peek();
            String r="";
            String l="";
            if(curPeek.compareTo("!")==0){
                r=operandStack.pop();
            }else{
                r=operandStack.pop();
                l=operandStack.pop();
            }

            boolean b=MethodTools.calc(l,r,curPeek,rr);
            charsStack.pop();
            operandStack.push(b?"TRUE":"FALSE");
        }

        if(operandStack.size()>1) return false;
        //返回栈顶逻辑值
        if(operandStack.peek().toUpperCase().compareTo("TRUE")==0) return true; else return false;

    }

    //检测是否存在某个数据库和某个表
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


    //计算表达式值
    public static int calcVal(String expression,RelationRow r) throws Exception{
        Stack<Integer> operands=new Stack<>();
        Stack<String> opereator=new Stack<>();
        String cur="";
        for(int i=0;i<expression.length();i++) {
            char c = expression.charAt(i);
            switch (c) {
                case '+':
                case '-':
                    if (cur.compareTo("") == 0) {
                        throw new Exception("表达式错误");
                    }
                    Integer val = null;
                    try {
                        val = Integer.parseInt(cur);
                    } catch (Exception e) {
                    }

                    if (val == null) {
                        if (r.getConlumType(cur) == null || r.getConlumType(cur) != DataType.INT32)
                            throw new Exception("类型错误");
                        val = (Integer) r.getVal(cur);
                    }
                    cur = "";
                    operands.push(val);
                    opereator.push("" + c);

                    break;

                default:
                    cur = cur + c;
            }
        }

            if (cur.compareTo("") != 0) {
                Integer val = null;
                try {
                    val = Integer.parseInt(cur);
                } catch (Exception e) {
                }

                if (val == null) {
                    if (r.getConlumType(cur) == null || r.getConlumType(cur) != DataType.INT32)
                        throw new Exception("类型错误");
                    val = (Integer) r.getVal(cur);
                }
                cur = "";
                operands.push(val);
            }

            while (!opereator.empty()){
                int result;
                int left;
                int right;
                String op=opereator.pop();
                right=operands.pop();
                left=operands.pop();
                if(op.compareTo("+")==0){
                    result=left+right;
                    operands.push(result);
                }else if(op.compareTo("-")==0){
                    result=left-right;
                    operands.push(result);
                }
            }
            return operands.pop();
    }

    //通过索引寻找符合逻辑表达式的记录所在位置
    public static int getRecordPosThroughIndex(Stack<String> expressionStack,TableDBMSObj tableDBMSObj){
        //select 学号,姓名 from student where 学号=17000000;
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

            System.out.println("列名"+conlumn+"不是索引 "+tableDBMSObj.tableStructure.indexOn);
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
                break;
            case INT32:
                pos=indexBtree.get(Integer.parseInt(val));
                break;
        }


        if(pos==null) return -1; else return (int)pos;
    }


}
