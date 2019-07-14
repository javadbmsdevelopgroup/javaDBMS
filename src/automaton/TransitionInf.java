package automaton;

import java.io.Serializable;
import java.util.Collections;

//一条转移信息
public class TransitionInf implements Serializable {
    //描述从哪迁移到哪
    AutomatonNode from;
    AutomatonNode end;
    //迁移类型
    TransitionType type;
    //迁移内容
    String t_content="";
    //迁移成功时执行的函数
    public ITransMethod transMethod=null;

    //从某结点到某结点 转移类型 转移内容
    public TransitionInf(AutomatonNode s, AutomatonNode e, TransitionType t, String c){
        from=s;
        end=e;
        type=t;
        t_content=c;
    }

    public TransitionInf(AutomatonNode s, AutomatonNode e, TransitionType t, String c,ITransMethod iTransMethod){
        from=s;
        end=e;
        type=t;
        t_content=c;
        this.transMethod=iTransMethod;
    }



    //编码
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
