package automaton.methods;

import automaton.ITransMethod;
import automaton.InfCollection;

import java.io.Serializable;

public class KeyWordTrans implements ITransMethod, Serializable {
    @Override
    public void trans(InfCollection infCollection,String tranContent){
        //System.out.println("trans by "+tranContent);
        infCollection.keyWords.push(tranContent);
    }
}
