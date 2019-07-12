package automaton.methods;

import automaton.ITransMethod;
import automaton.InfCollection;

import java.io.Serializable;

public class ExpressionTrans implements ITransMethod, Serializable {
    @Override
    public void trans(InfCollection infCollection, String tranContent){
        //System.out.println("trans by "+tranContent);
        infCollection.logicExpressions.push(tranContent);
    }
}
