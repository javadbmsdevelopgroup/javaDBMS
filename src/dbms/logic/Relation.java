package dbms.logic;

import dbms.RelationRow;

import java.util.ArrayList;
import java.util.List;
/////////////////////////////////关系类。。。未完成!!!!    暂时不管该类了。改去完成view包中的RelationView类
public class Relation {
    List<RelationRow> relationRowList=new ArrayList<>();
    public int getRowsCount(){
        return relationRowList.size();
    }
}
