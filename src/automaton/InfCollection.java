package automaton;

import java.io.Serializable;
import java.util.Stack;

//信息手机去，用来在自动机迁移时收集信息.里面有一堆栈
public class InfCollection implements Serializable {
    public Stack<String> tableNames=new Stack<>();  //表名栈
    public Stack<String> dbNames=new Stack<>();    //数据库名栈
    public Stack<String> logicExpressions=new Stack<>();   //逻辑表达式栈
    public Stack<String> columNames=new Stack<>();  //列名栈
    public Stack<String> keyWords=new Stack<>(); //关键词栈
    public Stack<String> others=new Stack<>();
    public Object resultObj=null;
    public void cleanStacks(){
        tableNames.clear();
        dbNames.clear();
        logicExpressions.clear();
        columNames.clear();
        keyWords.clear();
        others.clear();
        resultObj=null;
    }
}
