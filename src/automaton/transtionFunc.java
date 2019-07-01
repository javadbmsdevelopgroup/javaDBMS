package automaton;


import java.util.ArrayList;
import java.util.List;

//转移函数
public class TranstionFunc {
    List<TransitionInf> tifs=new ArrayList<>();   //转移函数信息集合,对应状态机上的一条边
    int stateCount=0;    //
    void addAtransition(int from, int end, TransitionType t, String input){  //添加一条状态转移信息   从那哪个状态到那个状态，转移类型，以及转移条件
        tifs.add(new TransitionInf(from,end,t,input));
    }
    void addAtransition(TransitionInf ti){  //添加一条状态转移信息
        tifs.add(ti);
    }   //同样是添加一条转移信息

    //转移函数编码
    public String getCodedStr(){
        String r="";
        for (TransitionInf ti:tifs){
            r+=ti.getEncodedStr()+"|";
        }
        return r;
    }


    //尝试对输入集合转移
    //多个输入用空格隔开,返回最终状态 -1表示失败
    int transition(int from,List<InputItem> input){
        int cur=from;

        for(InputItem s:input) {
            boolean t_exe=false;
            TransitionInf tf_strOany=null;
            TransitionInf tf_WordTypeOany=null;
            for(int i=0;i<tifs.size();i++){
                if(tifs.get(i).from!=cur) continue;
                //转移类型是字符串
                if(tifs.get(i).type== TransitionType.STR){

                    if(tifs.get(i).t_content=="o*"){
                        tf_strOany=tifs.get(i);
                        continue;
                    }
                    //System.out.println("转移类型为字符:"+s.content+"  "+tifs.get(i).t_content);
                    if(tifs.get(i).t_content.compareTo(s.content)==0){
                        System.out.println(String.valueOf(cur)+"->"+tifs.get(i).end);
                        cur=tifs.get(i).end;
                        t_exe=true;
                        break;
                    }
                }else{//转移类型是词性
                    /*
                    if(tifs.get(i).t_content=="o*"){
                        tf_WordTypeOany=tifs.get(i);
                        continue;
                    }

                    if(tifs.get(i).t_content==s.wt.toString()){
                        System.out.println(String.valueOf(cur)+"->"+tifs.get(i).end);
                        cur=tifs.get(i).end;
                        t_exe=true;
                        break;
                    }
*/

                }
            }


            if(!t_exe){
                if(tf_strOany!=null){
                    System.out.println(String.valueOf(cur)+"->"+ tf_strOany.end);
                    cur=tf_strOany.end;
                }else if(tf_WordTypeOany!=null){
                    System.out.println(String.valueOf(cur)+"->"+tf_WordTypeOany.end);
                    cur=tf_WordTypeOany.end;
                }else
                    return -1; //对于某个输入无法转移
            }
        }


        return cur;
    }
    int transition(int from,InputItem input){
        int cur=from;
        //for(inputItem s:input) {
        boolean t_exe=false;
        TransitionInf tf_strOany=null;
        TransitionInf tf_WordTypeOany=null;
        for(int i=0;i<tifs.size();i++){
            if(tifs.get(i).from!=cur) continue;
            //转移类型是字符串
            if(tifs.get(i).type== TransitionType.STR){

                if(tifs.get(i).t_content=="o*"){
                    tf_strOany=tifs.get(i);
                    continue;
                }
                //System.out.println("转移类型为字符:"+s.content+"  "+tifs.get(i).t_content);
                if(tifs.get(i).t_content.compareTo(input.content)==0){
                    //System.out.println(String.valueOf(cur)+"->"+tifs.get(i).end);
                    cur=tifs.get(i).end;
                    t_exe=true;
                    break;
                }
            }else{//转移类型是词性
                /*
                if(tifs.get(i).t_content=="o*"){
                    tf_WordTypeOany=tifs.get(i);
                    continue;
                }

                if(tifs.get(i).t_content==input.wt.toString()){
                    //System.out.println(String.valueOf(cur)+"->"+tifs.get(i).end);
                    cur=tifs.get(i).end;
                    t_exe=true;
                    break;
                }
*/

            }
        }


        if(!t_exe){
            if(tf_strOany!=null){
                //System.out.println(String.valueOf(cur)+"->"+ tf_strOany.end);
                cur=tf_strOany.end;
            }else if(tf_WordTypeOany!=null){
                //System.out.println(String.valueOf(cur)+"->"+tf_WordTypeOany.end);
                cur=tf_WordTypeOany.end;
            }else
                return -1; //对于某个输入无法转移
        }
        //}


        return cur;
    }
}
