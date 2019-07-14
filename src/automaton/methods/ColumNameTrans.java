package automaton.methods;

import automaton.ITransMethod;
import automaton.InfCollection;

import java.io.Serializable;

public class ColumNameTrans implements ITransMethod, Serializable {
    @Override
    public void trans(InfCollection infCollection, String tranContent){
        infCollection.columNames.push(tranContent);
    }
}
