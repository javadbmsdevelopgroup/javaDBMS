package automaton;

import java.io.Serializable;

public class InputItem implements Serializable {
    String content;
    TransitionType inputtype;
    //nlp.WordType wt;

    public InputItem(String c){
        //wt=w;
        content=c;
    }
    @Override
    public String toString(){
        return content;
    }
}