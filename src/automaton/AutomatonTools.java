package automaton;

import java.util.ArrayList;
import java.util.List;

//自动机工具
public class AutomatonTools {
    //单例设计模式
    private static AutomatonTools instance=null;
    private AutomatonTools(){};
    public static AutomatonTools getInstance(){
        if(instance==null) instance=new AutomatonTools();
        return instance;
    }
    //把一个句子转换成输入项集合
    public static List<InputItem> toInputList(String str){
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


        return iis;
    }


}
