package automaton;

import java.util.Collections;

//一条转移信息
public class TransitionInf {
    AutomatonNode from; //from which state
    AutomatonNode end; //end in which state


    TransitionType type;
    String t_content="";
    public TransitionInf(AutomatonNode s, AutomatonNode e, TransitionType t, String c){
        from=s;
        end=e;
        type=t;
        t_content=c;
    }
    public String getEncodedStr() {
        if(from.nodeCode<=0 || end.nodeCode<=0) return "";
        StringBuilder coded=new StringBuilder("");
        //编码起始状态
        coded.append(String.join("", Collections.nCopies(from.nodeCode, "0")).toString());
        //编码字符/类型
        switch (type){
            case KEYWORD:
                coded.append('1');
                coded.append(t_content);
                break;
            case OBJNAME:
                coded.append("11");
                coded.append("["+t_content+"]");
                break;
        }

        //编码新状态
        coded.append('1');
        coded.append(String.join("", Collections.nCopies(end.nodeCode, "0")).toString());
        return coded.toString();
    }
}
