package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;

public class UpdateMethod implements INodeFunc {
    @Override
    public Object doWork(InfCollection infCollection, Object... objs){
        System.out.println("update");
        return null;
    }
}
