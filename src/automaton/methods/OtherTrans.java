package automaton.methods;

import automaton.ITransMethod;
import automaton.InfCollection;

import java.io.Serializable;

public class OtherTrans  implements ITransMethod, Serializable {
    @Override
    public void trans(InfCollection infCollection, String tranContent){
        infCollection.others.push(tranContent);
    }
}
