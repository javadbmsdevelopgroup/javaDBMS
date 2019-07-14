package automaton;

import java.io.Serializable;

public class InputItem implements Serializable {
    String content;
    TransitionType inputtype;

    public InputItem(String c){
        content=c;
    }
    @Override
    public String toString(){
        return content;
    }
}