package dbms.logic;

import dbms.RelationRow;

import java.util.ArrayList;
import java.util.List;
/////////////////////////////////关系类。。。未完成
public class Relation {
    List<RelationRow> relationRowList=new ArrayList<>();
    public int getRowsCount(){
        return relationRowList.size();
    }
}
