package automaton.methods;

import automaton.INodeFunc;
import automaton.ITransMethod;
import automaton.InfCollection;

public class SelectMethod implements INodeFunc {
    @Override
    public Object doWork(InfCollection infCollection,Object... objs){
        System.out.println("select");
        return null;
    }
}
