package automaton.methods;

import automaton.ITransMethod;
import automaton.InfCollection;

public class KeyWordTrans implements ITransMethod {
    @Override
    public void trans(InfCollection infCollection,String tranContent){
        System.out.println("trans by "+tranContent);
        infCollection.keyWords.push(tranContent);
    }
}