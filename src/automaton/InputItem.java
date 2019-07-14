package automaton;

import java.io.Serializable;

//输入项。能让自动机迁移的东西。。。
public class InputItem implements Serializable {
    String content;
    TransitionType inputtype;   //没用上。本来考虑到输入项也是有类型的。后来发现在本项目中不用考虑这个

    public InputItem(String c){
        content=c;
    }
    @Override
    public String toString(){
        return content;
    }
}