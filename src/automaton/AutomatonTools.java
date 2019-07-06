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
    public static List<InputItem> toInputList(String str){
        //create table student (stuCode int ,name string(50) ,classNum int)
        List<InputItem> iis=new ArrayList<>();
        String cur="";
        for(int i=0;i<str.length();i++){
            char c=str.charAt(i);
            if(c==' '){
                if(cur.compareTo("")!=0){
                    iis.add(new InputItem(cur));
                    cur="";
                }
            }else if(c==',' || c=='(' || c==')' || c==';' || c=='*'){
                if(cur.compareTo("")!=0){
                    iis.add(new InputItem(cur));
                    cur="";
                }
                iis.add(new InputItem(String.valueOf(c)));
            }
            else{
                cur+=c;
            }
        }
        if(cur.compareTo("")!=0){
            iis.add(new InputItem(cur));
        }

/*
        String[] inputTokens=str.split(" ");
        for(int i=0;i<inputTokens.length;i++){
            //System.out.println( hajime_tokens.get(i).getSurface()+'\t'+'\t'+hajime_tokens.get(i).getAllFeatures());
            String wordt=inputTokens[i];
            iis.add(new InputItem(wordt));
        }
        */
        return iis;
    }

    public static void main(String[] args){
        List<InputItem> inputItems=toInputList("create table student (stuCode int ,name string(50) ,classNum int)");
        for(InputItem inputItem:inputItems){
            System.out.println(inputItem.content);
        }
    }
}
