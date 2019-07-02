package automaton;

import java.util.Stack;

//信息集，用来在自动机迁移时收集信息
public class InfCollection {
    public Stack<String> tableNames=new Stack<>();  //表名栈
    public Stack<String> dbNames=new Stack<>();    //数据库名栈
    public Stack<String> logicExpressions=new Stack<>();   //逻辑表达式栈
    public Stack<String> columNames=new Stack<>();  //列名栈
    public Stack<String> keyWords=new Stack<>(); //关键词栈
}
