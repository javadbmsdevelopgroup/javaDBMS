package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;

public class DeleteMethod implements INodeFunc {
    @Override
    public Object doWork(InfCollection infCollection, Object... objs){
        System.out.println("delete method");
        return null;
    }
}
