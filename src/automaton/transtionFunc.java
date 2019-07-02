package automaton;


import java.util.ArrayList;
import java.util.List;

//转移函数
public class TranstionFunc {
    List<TransitionInf> tifs=new ArrayList<>();   //转移函数信息集合,对应状态机上的一条边
    InfCollection infCollection;
    Automaton belongedAutomaton=null;
    int stateCount=0;    //

    public TranstionFunc(){
        infCollection=new InfCollection();
    }
    public TranstionFunc(Automaton automaton){
        infCollection=new InfCollection();
        belongedAutomaton=automaton;
    }
    public void cleanInfCollection(){
        infCollection=new InfCollection();
    }
    void addAtransition(AutomatonNode from, AutomatonNode end, TransitionType t, String input){  //添加一条状态转移信息   从那哪个状态到那个状态，转移类型，以及转移条件
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
    AutomatonNode transition(AutomatonNode from,List<InputItem> input){
        AutomatonNode cur=from;

        for(InputItem s:input) {
            boolean t_exe=false;
            TransitionInf tf_strOany=null;
            TransitionInf tf_WordTypeOany=null;
            cur=transition(cur,s);

        }

        return cur;
    }

    AutomatonNode transition(AutomatonNode from,InputItem input,Object... objects){
        AutomatonNode cur=from;
        boolean t_exe=false;
        TransitionInf tf_strOany=null;
        TransitionInf tf_WordTypeOany=null;
        for(int i=0;i<tifs.size();i++){
            TransitionInf tmp_tif=tifs.get(i);
            if(tmp_tif.from.nodeCode!=cur.nodeCode) continue;
            //System.out.println("找到起始为"+tmp_tif.from.nodeCode+" begin");
            switch (tmp_tif.type){
                //转移类型是关键字或记号x`x`
                case KEYWORD:case MARK:

                    if(tmp_tif.t_content.compareTo(input.content.toUpperCase())==0){
                        System.out.println("转移类型 "+"keyword/mark");
                        //System.out.println(tmp_tif.t_content.toUpperCase());
                        System.out.println(cur.nodeCode+"->"+tifs.get(i).end.nodeCode);
                        cur=tmp_tif.end;
                        t_exe=true;
                        if(tmp_tif.transMethod!=null) tmp_tif.transMethod.trans(this.infCollection,tmp_tif.t_content);
                        if(cur.owariNode) cur.nodeMethod.doWork(this.infCollection,objects);
                        break;
                    }
                    break;
                case OBJNAME:
                    System.out.println(String.valueOf(cur)+"->"+tifs.get(i).end);
                    cur=tifs.get(i).end;
                    t_exe=true;
                    if(tifs.get(i).transMethod!=null) tifs.get(i).transMethod.trans(this.infCollection,tmp_tif.t_content);
                    if(cur.owariNode) cur.nodeMethod.doWork(this.infCollection,objects);
                    break;
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
                return null; //对于某个输入无法转移
        }

        return cur;
    }
}
