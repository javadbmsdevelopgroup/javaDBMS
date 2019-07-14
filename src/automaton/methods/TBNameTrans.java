package automaton.methods;

import automaton.ITransMethod;
import automaton.InfCollection;

import java.io.Serializable;

public class TBNameTrans implements ITransMethod, Serializable {
    @Override
    public void trans(InfCollection infCollection, String tranContent){
        infCollection.tableNames.push(tranContent);
    }
}
