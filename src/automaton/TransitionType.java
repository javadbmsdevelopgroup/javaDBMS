package automaton;

import java.io.Serializable;

//迁移类型。 关键词\任意名\记号
public enum TransitionType implements Serializable {
    KEYWORD,OBJNAME,MARK;
}