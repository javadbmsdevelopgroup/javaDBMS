package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;

public class IndexCreateMethod implements INodeFunc {
    @Override
    public Object doWork(InfCollection infCollection, Object... objs){
        System.out.println("index create");
        return null;
    }
}
