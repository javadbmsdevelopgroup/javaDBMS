package automaton;

import java.util.ArrayList;
import java.util.List;

public class AutomatonTools {
    private static AutomatonTools instance=null;
    private AutomatonTools(){};
    public static AutomatonTools getInstance(){
        if(instance==null) instance=new AutomatonTools();
        return instance;
    }
    public List<InputItem> toInputList(String str){
        List<InputItem> iis=new ArrayList<>();

        String[] inputTokens=str.split(" ");
        for(int i=0;i<inputTokens.length;i++){
            //System.out.println( hajime_tokens.get(i).getSurface()+'\t'+'\t'+hajime_tokens.get(i).getAllFeatures());
            String wordt=inputTokens[i];
            iis.add(new InputItem(wordt));
        }
        return iis;
    }
}
