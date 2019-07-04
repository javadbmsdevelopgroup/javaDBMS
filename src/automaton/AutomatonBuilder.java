package automaton;

import automaton.methods.*;
import dbms.TableBuffer;

import java.util.ArrayList;
import java.util.List;

public class AutomatonBuilder {
    public static Automaton buildAutomaton(){
        AutomatonNode start=new AutomatonNode(0,false);  //起点
        List<AutomatonNode> automatonNodeList=new ArrayList<>();
        TranstionFunc transtionFunc=new TranstionFunc();
        //show
        ////node
        AutomatonNode show_node1=new AutomatonNode(1,false);
        AutomatonNode show_node2=new AutomatonNode(2,true,new ShowMethod());
        AutomatonNode show_node3=new AutomatonNode(3,true,new ShowMethod());
        ////trans Edge
        TransitionInf show_tans1=new TransitionInf(start,show_node1,TransitionType.KEYWORD,"SHOW");
        TransitionInf show_tans2=new TransitionInf(show_node1,show_node2,TransitionType.KEYWORD,"DATABASES",new KeyWordTrans());
        TransitionInf show_tran3=new TransitionInf(show_node1,show_node3,TransitionType.KEYWORD,"TABLES",new KeyWordTrans());
        automatonNodeList.add(start);
        automatonNodeList.add(show_node1);
        automatonNodeList.add(show_node2);
        automatonNodeList.add(show_node3);
        transtionFunc.addAtransition(show_tans1);
        transtionFunc.addAtransition(show_tans2);
        transtionFunc.addAtransition(show_tran3);


        //use
        ////node
        AutomatonNode use_node4=new AutomatonNode(4,false);
        AutomatonNode use_node5=new AutomatonNode(5,true,new UseMethod());
        ////trans Edge
        TransitionInf use_tans4=new TransitionInf(start,use_node4,TransitionType.KEYWORD,"USE");
        TransitionInf use_tans5=new TransitionInf(use_node4,use_node5,TransitionType.OBJNAME,"",new DBNameTrans());

        automatonNodeList.add(use_node4);
        automatonNodeList.add(use_node5);
        transtionFunc.addAtransition(use_tans4);
        transtionFunc.addAtransition(use_tans5);

        //create
        ////node
        /////Database
        AutomatonNode create_node6 = new AutomatonNode(6,false);
        AutomatonNode create_node7 = new AutomatonNode(7,false);
        AutomatonNode create_node8 = new AutomatonNode(8,true,new CreateMethod());
        /////Table
        AutomatonNode create_node9= new AutomatonNode(9,false);
        AutomatonNode create_node10= new AutomatonNode(10,false);
        AutomatonNode create_node11= new AutomatonNode(11,false);
        AutomatonNode create_node12= new AutomatonNode(12,false);
        AutomatonNode create_node13= new AutomatonNode(13,false);
        AutomatonNode create_node14= new AutomatonNode(14,false);
        AutomatonNode create_node15= new AutomatonNode(15,false);
        AutomatonNode create_node16= new AutomatonNode(16,true,new CreateMethod());
        AutomatonNode create_node17= new AutomatonNode(17,false);
        AutomatonNode create_node18= new AutomatonNode(18,false);
        ////trans Edge
        /////Database
        TransitionInf create_trans6=new TransitionInf(start,create_node6,TransitionType.KEYWORD,"CREATE");
        TransitionInf create_trans7=new TransitionInf(create_node6,create_node7,TransitionType.KEYWORD,"DATABASE");
        TransitionInf create_trans8= new TransitionInf(create_node7,create_node8,TransitionType.OBJNAME,"",new DBNameTrans());
        /////Table
        TransitionInf create_trans9 = new TransitionInf(create_node6,create_node9,TransitionType.KEYWORD,"TABLE");
        TransitionInf create_trans10 = new TransitionInf(create_node9,create_node10,TransitionType.OBJNAME,"",new TBNameTrans());
        TransitionInf create_trans11 = new TransitionInf(create_node10,create_node11,TransitionType.KEYWORD,"(");
        TransitionInf create_trans12 = new TransitionInf(create_node11,create_node12,TransitionType.OBJNAME,"",new ColumNameTrans());
        TransitionInf create_trans13 = new TransitionInf(create_node12,create_node11,TransitionType.KEYWORD,",");
        TransitionInf create_trans14 = new TransitionInf(create_node12,create_node12,TransitionType.KEYWORD,"INT",new KeyWordTrans());
        TransitionInf create_trans15 = new TransitionInf(create_node12,create_node13,TransitionType.KEYWORD,"STRING",new KeyWordTrans());
        TransitionInf create_trans16= new TransitionInf(create_node13,create_node14,TransitionType.KEYWORD,"(");
        TransitionInf create_trans17= new TransitionInf(create_node14,create_node15,TransitionType.OBJNAME,"",new OtherTrans());
        TransitionInf create_trans18= new TransitionInf(create_node15,create_node12,TransitionType.KEYWORD,")");
        TransitionInf create_trans19= new TransitionInf(create_node12,create_node16,TransitionType.KEYWORD,")");
        TransitionInf create_trans20 = new TransitionInf(create_node12,create_node17,TransitionType.KEYWORD,"PRIMARY");
        TransitionInf create_trans21 = new TransitionInf(create_node17,create_node12,TransitionType.KEYWORD,"KEY",new KeyWordTrans());
        TransitionInf create_trans22 = new TransitionInf(create_node12,create_node18,TransitionType.KEYWORD,"NOT");
        TransitionInf create_trans23 = new TransitionInf(create_node18,create_node12,TransitionType.KEYWORD,"NULL",new KeyWordTrans());


        automatonNodeList.add(create_node6);
        automatonNodeList.add(create_node7);
        automatonNodeList.add(create_node8);
        automatonNodeList.add(create_node9);
        automatonNodeList.add(create_node10);
        automatonNodeList.add(create_node11);
        automatonNodeList.add(create_node12);
        automatonNodeList.add(create_node13);
        automatonNodeList.add(create_node14);
        automatonNodeList.add(create_node15);
        automatonNodeList.add(create_node16);
        automatonNodeList.add(create_node17);
        automatonNodeList.add(create_node18);
        transtionFunc.addAtransition(create_trans6);
        transtionFunc.addAtransition(create_trans7);
        transtionFunc.addAtransition(create_trans8);
        transtionFunc.addAtransition(create_trans9);
        transtionFunc.addAtransition(create_trans10);
        transtionFunc.addAtransition(create_trans11);
        transtionFunc.addAtransition(create_trans12);
        transtionFunc.addAtransition(create_trans13);
        transtionFunc.addAtransition(create_trans14);
        transtionFunc.addAtransition(create_trans15);
        transtionFunc.addAtransition(create_trans16);
        transtionFunc.addAtransition(create_trans17);
        transtionFunc.addAtransition(create_trans18);
        transtionFunc.addAtransition(create_trans19);
        transtionFunc.addAtransition(create_trans20);
        transtionFunc.addAtransition(create_trans21);
        transtionFunc.addAtransition(create_trans22);
        transtionFunc.addAtransition(create_trans23);


        //drop
        AutomatonNode drop_Node19=new AutomatonNode(19,false);
        AutomatonNode drop_Node20=new AutomatonNode(20,false);
        AutomatonNode drop_Node21=new AutomatonNode(21,true,new DropMethod());
        ////trans Edge

        TransitionInf drop_trans24=new TransitionInf(start,drop_Node19,TransitionType.KEYWORD,"DROP",new KeyWordTrans());
        TransitionInf drop_trans25=new TransitionInf(drop_Node19,drop_Node20,TransitionType.KEYWORD,"DATABASE",new KeyWordTrans());
        TransitionInf drop_trans26=new TransitionInf(drop_Node19,drop_Node20,TransitionType.KEYWORD,"TABLE",new KeyWordTrans());
        TransitionInf drop_trans27=new TransitionInf(drop_Node20,drop_Node21,TransitionType.OBJNAME,"",new OtherTrans());
        automatonNodeList.add(drop_Node19);
        automatonNodeList.add(drop_Node20);
        automatonNodeList.add(drop_Node21);
        transtionFunc.addAtransition(drop_trans24);
        transtionFunc.addAtransition(drop_trans25);
        transtionFunc.addAtransition(drop_trans26);
        transtionFunc.addAtransition(drop_trans27);




        //insert
        ////node
        AutomatonNode insert_node22 = new AutomatonNode(22,false);
        AutomatonNode insert_node23 = new AutomatonNode(23,false);
        AutomatonNode insert_node24 = new AutomatonNode(24,false);
        AutomatonNode insert_node25 = new AutomatonNode(25,false);
        AutomatonNode insert_node_26 = new AutomatonNode(26,false);
        AutomatonNode insert_node_27 = new AutomatonNode(27,false);
        AutomatonNode insert_node_28 = new AutomatonNode(28,false);
        AutomatonNode insert_node_29 = new AutomatonNode(29,false);
        AutomatonNode insert_node_30 = new AutomatonNode(30,true,new InsertMethod());
        ////edge
        TransitionInf insert_trans28 = new TransitionInf(start,insert_node22,TransitionType.KEYWORD,"INSERT");
        TransitionInf insert_trans29 = new TransitionInf(insert_node22,insert_node23,TransitionType.KEYWORD,"INTO");
        TransitionInf insert_trans30 = new TransitionInf(insert_node23,insert_node24,TransitionType.OBJNAME,"",new TBNameTrans());
        TransitionInf insert_trans31 = new TransitionInf(insert_node24,insert_node25,TransitionType.KEYWORD,"(");
        TransitionInf insert_trans32 = new TransitionInf(insert_node25,insert_node25,TransitionType.KEYWORD,",");
        TransitionInf insert_trans33 = new TransitionInf(insert_node25,insert_node25,TransitionType.OBJNAME,"",new ColumNameTrans());
        TransitionInf insert_trans34 = new TransitionInf(insert_node25,insert_node_26,TransitionType.KEYWORD,")");
        TransitionInf insert_trans35 = new TransitionInf(insert_node_26,insert_node_27,TransitionType.KEYWORD,"VALUES");
        TransitionInf insert_trans36 = new TransitionInf(insert_node_27,insert_node_28,TransitionType.KEYWORD,"(");
        TransitionInf insert_trans37 = new TransitionInf(insert_node_28,insert_node_29,TransitionType.OBJNAME,"",new OtherTrans());
        TransitionInf insert_trans38 = new TransitionInf(insert_node_29,insert_node_28,TransitionType.KEYWORD,",");
        TransitionInf insert_trans39 = new TransitionInf(insert_node_29,insert_node_30,TransitionType.KEYWORD,")");
        TransitionInf insert_trans40 = new TransitionInf(insert_node24,insert_node_27,TransitionType.KEYWORD,"VALUES");
        automatonNodeList.add(insert_node22);
        automatonNodeList.add(insert_node23);
        automatonNodeList.add(insert_node24);
        automatonNodeList.add(insert_node25);
        automatonNodeList.add(insert_node_26);
        automatonNodeList.add(insert_node_27);
        automatonNodeList.add(insert_node_28);
        automatonNodeList.add(insert_node_29);
        automatonNodeList.add(insert_node_30);
        transtionFunc.addAtransition(insert_trans28);
        transtionFunc.addAtransition(insert_trans29);
        transtionFunc.addAtransition(insert_trans30);
        transtionFunc.addAtransition(insert_trans31);
        transtionFunc.addAtransition(insert_trans32);
        transtionFunc.addAtransition(insert_trans33);
        transtionFunc.addAtransition(insert_trans34);
        transtionFunc.addAtransition(insert_trans35);
        transtionFunc.addAtransition(insert_trans36);
        transtionFunc.addAtransition(insert_trans37);
        transtionFunc.addAtransition(insert_trans38);
        transtionFunc.addAtransition(insert_trans39);
        transtionFunc.addAtransition(insert_trans40);

        try{
            Automaton automaton=new Automaton(automatonNodeList,transtionFunc);
            System.out.println("自动机创建成功");
            return automaton;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


}
