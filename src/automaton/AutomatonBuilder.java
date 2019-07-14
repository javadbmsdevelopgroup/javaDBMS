package automaton;

import automaton.methods.*;
import dbms.TableBuffer;
import dbms.logic.DataType;

import java.util.ArrayList;
import java.util.List;

public class AutomatonBuilder {
    //缺少limit

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
        /////Index
        AutomatonNode create_node53=new AutomatonNode(53,false);
        AutomatonNode create_node54=new AutomatonNode(54,false);
        AutomatonNode create_node55=new AutomatonNode(55,false);
        AutomatonNode create_node56=new AutomatonNode(56,false);
        AutomatonNode create_node57=new AutomatonNode(57,false);
        AutomatonNode create_node58=new AutomatonNode(58,true,new IndexCreateMethod());
        TransitionInf index_trans1=new TransitionInf(create_node6,create_node53,TransitionType.KEYWORD,"INDEX" );
        TransitionInf index_trans2=new TransitionInf(create_node53,create_node54,TransitionType.KEYWORD,"ON");
        TransitionInf index_trans3=new TransitionInf(create_node54,create_node55,TransitionType.OBJNAME,"",new TBNameTrans());
        TransitionInf index_trans4=new TransitionInf(create_node55,create_node56,TransitionType.KEYWORD,"(");
        TransitionInf index_trans5=new TransitionInf(create_node56,create_node57,TransitionType.OBJNAME,"",new ColumNameTrans());
        TransitionInf index_trans6=new TransitionInf(create_node57,create_node58,TransitionType.KEYWORD,")");
        automatonNodeList.add(create_node53);
        automatonNodeList.add(create_node54);
        automatonNodeList.add(create_node55);
        automatonNodeList.add(create_node56);
        automatonNodeList.add(create_node57);
        automatonNodeList.add(create_node58);
        transtionFunc.addAtransition(index_trans1);
        transtionFunc.addAtransition(index_trans2);
        transtionFunc.addAtransition(index_trans3);
        transtionFunc.addAtransition(index_trans4);
        transtionFunc.addAtransition(index_trans5);
        transtionFunc.addAtransition(index_trans6);
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

        //delete
        ////Node
        AutomatonNode delete_Node31=new AutomatonNode(31,false);
        AutomatonNode delete_node32=new AutomatonNode(32,false);
        AutomatonNode delete_node33=new AutomatonNode(33,false);
        AutomatonNode delete_node34=new AutomatonNode(34,false);
        AutomatonNode delete_node35=new AutomatonNode(35,false);
        AutomatonNode delete_node36=new AutomatonNode(36,true,new DeleteMethod());
        ////Edge
        TransitionInf delete_trans1=new TransitionInf(start,delete_Node31, TransitionType.KEYWORD,"DELETE");
        TransitionInf delete_trans2=new TransitionInf(delete_Node31,delete_node32, TransitionType.KEYWORD,"FROM");
        TransitionInf delete_trans3=new TransitionInf(delete_node32,delete_node33, TransitionType.OBJNAME,"",new TBNameTrans());
        TransitionInf delete_trans4=new TransitionInf(delete_node33,delete_node34, TransitionType.KEYWORD,"WHERE");
        TransitionInf delete_trans5=new TransitionInf(delete_node34,delete_node34, TransitionType.KEYWORD,"(",new KeyWordTrans());
        TransitionInf delete_trans6=new TransitionInf(delete_node34,delete_node35, TransitionType.OBJNAME,"",new ExpressionTrans());
        TransitionInf delete_trans7=new TransitionInf(delete_node35,delete_node34, TransitionType.KEYWORD,"OR",new KeyWordTrans());
        TransitionInf delete_trans8=new TransitionInf(delete_node35,delete_node34, TransitionType.KEYWORD,"AND",new KeyWordTrans());
        TransitionInf delete_trans9=new TransitionInf(delete_node35,delete_node35, TransitionType.KEYWORD,")",new KeyWordTrans());
        TransitionInf delete_trans10=new TransitionInf(delete_node35,delete_node36, TransitionType.KEYWORD,";");
        automatonNodeList.add(delete_Node31);
        automatonNodeList.add(delete_node32);
        automatonNodeList.add(delete_node33);
        automatonNodeList.add(delete_node34);
        automatonNodeList.add(delete_node35);
        automatonNodeList.add(delete_node36);
        transtionFunc.addAtransition(delete_trans1);
        transtionFunc.addAtransition(delete_trans2);
        transtionFunc.addAtransition(delete_trans3);
        transtionFunc.addAtransition(delete_trans4);
        transtionFunc.addAtransition(delete_trans5);
        transtionFunc.addAtransition(delete_trans6);
        transtionFunc.addAtransition(delete_trans7);
        transtionFunc.addAtransition(delete_trans8);
        transtionFunc.addAtransition(delete_trans9);
        transtionFunc.addAtransition(delete_trans10);



        //update
        ////nodes
        AutomatonNode update_node37=new AutomatonNode(37,false);
        AutomatonNode update_node38=new AutomatonNode(38,false);
        AutomatonNode update_node39=new AutomatonNode(39,false);
        AutomatonNode update_node40=new AutomatonNode(40,false);
        AutomatonNode update_node41=new AutomatonNode(41,false);
        AutomatonNode update_node42=new AutomatonNode(42,false);
        AutomatonNode update_node43=new AutomatonNode(43,true,new UpdateMethod());
        ////trans
        TransitionInf update_trans1=new TransitionInf(start,update_node37,TransitionType.KEYWORD,"UPDATE");
        TransitionInf update_trans2=new TransitionInf(update_node37,update_node38,TransitionType.OBJNAME,"",new TBNameTrans());
        TransitionInf update_trans3=new TransitionInf(update_node38,update_node39,TransitionType.KEYWORD,"SET");

        TransitionInf update_trans4=new TransitionInf(update_node39,update_node40,TransitionType.OBJNAME,"",new OtherTrans());
        TransitionInf update_trans45=new TransitionInf(update_node40,update_node39,TransitionType.KEYWORD,",");
        TransitionInf update_trans5=new TransitionInf(update_node40,update_node41,TransitionType.KEYWORD,"WHERE");
        TransitionInf update_trans6=new TransitionInf(update_node41,update_node41,TransitionType.KEYWORD,"(",new KeyWordTrans());
        TransitionInf update_trans7=new TransitionInf(update_node41,update_node42,TransitionType.OBJNAME,"",new ExpressionTrans());
        TransitionInf update_trans8=new TransitionInf(update_node42,update_node41,TransitionType.KEYWORD,"OR",new KeyWordTrans());
        TransitionInf update_trans9=new TransitionInf(update_node42,update_node41,TransitionType.KEYWORD,"AND",new KeyWordTrans());
        TransitionInf update_trans10=new TransitionInf(update_node42,update_node42,TransitionType.KEYWORD,")",new KeyWordTrans());
        TransitionInf update_trans11=new TransitionInf(update_node42,update_node43,TransitionType.KEYWORD,";");
        automatonNodeList.add(update_node37);
        automatonNodeList.add(update_node38);
        automatonNodeList.add(update_node39);
        automatonNodeList.add(update_node40);
        automatonNodeList.add(update_node41);
        automatonNodeList.add(update_node42);
        automatonNodeList.add(update_node43);

        transtionFunc.addAtransition(update_trans1);
        transtionFunc.addAtransition(update_trans2);
        transtionFunc.addAtransition(update_trans3);
        transtionFunc.addAtransition(update_trans4);
        transtionFunc.addAtransition(update_trans45);
        transtionFunc.addAtransition(update_trans5);
        transtionFunc.addAtransition(update_trans6);
        transtionFunc.addAtransition(update_trans7);
        transtionFunc.addAtransition(update_trans8);
        transtionFunc.addAtransition(update_trans9);
        transtionFunc.addAtransition(update_trans10);
        transtionFunc.addAtransition(update_trans11);

        //select
        AutomatonNode select_node44=new AutomatonNode(44,false);
        AutomatonNode select_node45=new AutomatonNode(45,false);
        AutomatonNode select_node46=new AutomatonNode(46,false);
        AutomatonNode select_node47=new AutomatonNode(47,false);
        AutomatonNode select_node48=new AutomatonNode(48,false);
        AutomatonNode select_node49=new AutomatonNode(49,false);
        AutomatonNode select_node50=new AutomatonNode(50,false);
        AutomatonNode select_node51=new AutomatonNode(51,false);
        AutomatonNode select_node52=new AutomatonNode(52,true,new SelectMethod());
        AutomatonNode select_node59=new AutomatonNode(59,false);
        AutomatonNode select_node60=new AutomatonNode(60,false);
        ////edges
        TransitionInf select_trans1=new TransitionInf(start,select_node44,TransitionType.KEYWORD,"SELECT");
        TransitionInf select_trans2=new TransitionInf(select_node44,select_node45,TransitionType.OBJNAME,"",new ColumNameTrans());
        TransitionInf select_trans3=new TransitionInf(select_node45,select_node44,TransitionType.KEYWORD,",");
        TransitionInf select_trans4=new TransitionInf(select_node45,select_node46,TransitionType.KEYWORD,"FROM");
        TransitionInf select_trans5=new TransitionInf(select_node46,select_node47,TransitionType.OBJNAME,"",new TBNameTrans());
        TransitionInf select_trans6=new TransitionInf(select_node47,select_node48,TransitionType.KEYWORD,"WHERE");
        TransitionInf select_trans7=new TransitionInf(select_node48,select_node48,TransitionType.KEYWORD,"(",new KeyWordTrans());
        TransitionInf select_trans8=new TransitionInf(select_node48,select_node49,TransitionType.OBJNAME,"",new ExpressionTrans());
        TransitionInf select_trans9=new TransitionInf(select_node49,select_node48,TransitionType.KEYWORD,"OR",new ExpressionTrans());
        TransitionInf select_trans10=new TransitionInf(select_node49,select_node48,TransitionType.KEYWORD,"AND",new ExpressionTrans());
        TransitionInf select_trans11=new TransitionInf(select_node49,select_node49,TransitionType.KEYWORD,")",new ExpressionTrans());

        TransitionInf select_trans12=new TransitionInf(select_node49,select_node52,TransitionType.KEYWORD,";");
        TransitionInf select_trans13=new TransitionInf(select_node49,select_node50,TransitionType.KEYWORD,"ORDER");
        TransitionInf select_trans14=new TransitionInf(select_node50,select_node50,TransitionType.KEYWORD,"BY");
        TransitionInf select_trans15=new TransitionInf(select_node50,select_node51,TransitionType.OBJNAME,"",new OtherTrans());

        TransitionInf select_trans16=new TransitionInf(select_node51,select_node52,TransitionType.KEYWORD,";");
        TransitionInf select_trans17=new TransitionInf(select_node47,select_node52,TransitionType.KEYWORD,";");
        TransitionInf select_trans18=new TransitionInf(select_node47,select_node59,TransitionType.KEYWORD,"LIMIT");
        TransitionInf select_trans19=new TransitionInf(select_node59,select_node60,TransitionType.OBJNAME,"",new OtherTrans());
        TransitionInf select_trans20=new TransitionInf(select_node60,select_node52,TransitionType.KEYWORD,";");
        TransitionInf select_trans21=new TransitionInf(select_node51,select_node59,TransitionType.KEYWORD,"LIMIT");
        TransitionInf select_trans22=new TransitionInf(select_node49,select_node59,TransitionType.KEYWORD,"LIMIT");
        TransitionInf select_trans23=new TransitionInf(select_node47,select_node50,TransitionType.KEYWORD,"ORDER");
        automatonNodeList.add(select_node44);
        automatonNodeList.add(select_node45);
        automatonNodeList.add(select_node46);
        automatonNodeList.add(select_node47);
        automatonNodeList.add(select_node48);
        automatonNodeList.add(select_node49);
        automatonNodeList.add(select_node50);
        automatonNodeList.add(select_node51);
        automatonNodeList.add(select_node52);
        automatonNodeList.add(select_node59);
        automatonNodeList.add(select_node60);
        transtionFunc.addAtransition(select_trans1);
        transtionFunc.addAtransition(select_trans2);
        transtionFunc.addAtransition(select_trans3);
        transtionFunc.addAtransition(select_trans4);
        transtionFunc.addAtransition(select_trans5);
        transtionFunc.addAtransition(select_trans6);
        transtionFunc.addAtransition(select_trans7);
        transtionFunc.addAtransition(select_trans8);
        transtionFunc.addAtransition(select_trans9);
        transtionFunc.addAtransition(select_trans10);
        transtionFunc.addAtransition(select_trans11);
        transtionFunc.addAtransition(select_trans12);
        transtionFunc.addAtransition(select_trans13);
        transtionFunc.addAtransition(select_trans14);
        transtionFunc.addAtransition(select_trans15);
        transtionFunc.addAtransition(select_trans16);
        transtionFunc.addAtransition(select_trans17);
        transtionFunc.addAtransition(select_trans18);
        transtionFunc.addAtransition(select_trans19);
        transtionFunc.addAtransition(select_trans20);
        transtionFunc.addAtransition(select_trans21);
        transtionFunc.addAtransition(select_trans22);
        transtionFunc.addAtransition(select_trans23);

        //add
        AutomatonNode add_Node61=new AutomatonNode(61,false);
        AutomatonNode add_Node615=new AutomatonNode(615,false);
        AutomatonNode add_Node62=new AutomatonNode(62,false);
        AutomatonNode add_Node63=new AutomatonNode(63,true,new AddMethod());
        AutomatonNode add_Node64=new AutomatonNode(64,false);
        AutomatonNode add_Node65=new AutomatonNode(65,false);

        TransitionInf add_trans1=new TransitionInf(start,add_Node61,TransitionType.KEYWORD,"ADD");
        TransitionInf add_trans12=new TransitionInf(add_Node61,add_Node615,TransitionType.OBJNAME,"",new TBNameTrans());
        TransitionInf add_trans2=new TransitionInf(add_Node615,add_Node62,TransitionType.KEYWORD,"CONSTRAIN");
        TransitionInf add_trans3=new TransitionInf(add_Node62,add_Node64,TransitionType.KEYWORD,"POSITIVE",new KeyWordTrans());
        TransitionInf add_trans4=new TransitionInf(add_Node64,add_Node65,TransitionType.KEYWORD,"ON");
        TransitionInf add_trans5=new TransitionInf(add_Node65,add_Node63,TransitionType.OBJNAME,"",new ColumNameTrans());
        automatonNodeList.add(add_Node61);
        automatonNodeList.add(add_Node615);
        automatonNodeList.add(add_Node62);
        automatonNodeList.add(add_Node63);
        automatonNodeList.add(add_Node64);
        automatonNodeList.add(add_Node65);
        transtionFunc.addAtransition(add_trans1);
        transtionFunc.addAtransition(add_trans12);
        transtionFunc.addAtransition(add_trans2);
        transtionFunc.addAtransition(add_trans3);
        transtionFunc.addAtransition(add_trans4);
        transtionFunc.addAtransition(add_trans5);
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
