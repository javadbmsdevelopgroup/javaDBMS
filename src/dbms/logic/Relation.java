package dbms.logic;

import dbms.RelationRow;

import java.util.ArrayList;
import java.util.List;

public class Relation {
    List<RelationRow> relationRowList=new ArrayList<>();
    public int getRowsCount(){
        return relationRowList.size();
    }
}
