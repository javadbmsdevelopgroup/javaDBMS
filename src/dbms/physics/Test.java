package dbms.physics;

import dbms.RelationRow;
import dbms.TableReader;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableDBMSObj;

import java.io.*;

public class Test {

    public static void main(String[] args){
       try {
            long start=System.currentTimeMillis();
            BplusTree<Integer> bplusTree=new BplusTree<>();
            File f=new File("index.idx");
            FileInputStream is = new FileInputStream(f);
            DataInputStream dis=new DataInputStream(is);
            BufferedInputStream bufferedInputStream=new BufferedInputStream(dis);
            byte[] val=new byte[4];
            int k=0;
            int c=0;
            do{
                c=bufferedInputStream.read(val);
                if(c>0){
                    int key = (int) ((val[0] & 0xFF)
                            | ((val[1] & 0xFF)<<8)
                            | ((val[2] & 0xFF)<<16)
                            | ((val[3] & 0xFF)<<24));
                    c=bufferedInputStream.read(val);
                    int v = (int) ((val[0] & 0xFF)
                            | ((val[1] & 0xFF)<<8)
                            | ((val[2] & 0xFF)<<16)
                            | ((val[3] & 0xFF)<<24));
                    bplusTree.insert(key,v);
                    //System.out.println(k);
                }
                k++;
            }while (c>0);

            long end=System.currentTimeMillis();
            System.out.println(end-start);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


   /* public static void main(String[] args){
        boolean random = false;
        int order = 10;
        int size = 1000000;
        if(random){
            int d = 2;
            System.out.println(Math.ceil(d/2.0));
        }
        else {
            BplusTree<Integer> myTree = new BplusTree<Integer>(order);
            System.out.println(order + "叉B+树，顺序插入测试：size:"+ size +".");
            long start = System.currentTimeMillis();
            for(int i = 0; i < size; i++)
                myTree.insert(i, "数字" + Integer.toString(i));
            long end = System.currentTimeMillis() - start;
            System.out.println("花费时间：" + end);
            System.out.println(order + "叉B+树，顺序查找测试：size:"+ size +".");
            start = System.currentTimeMillis();
//            BplusTreeNode<Integer> aa = myTree.getHead();
//            while (aa!=null){
//                System.out.println(aa.toString());
//                aa = aa.getNext();
//            }
            String ss = null;
            int count = 0;
            for(int j = 0; j < size; j++){
                ss = (String)myTree.get(j);
                if(ss == null){
                    System.out.println("找不到啊弟弟！");
                    System.exit(0);
                }
                else{
                    count++;
                }
            }
            end = System.currentTimeMillis() - start;
            System.out.println("花费时间：" + end);
            System.out.println(count);
            System.out.println(order + "叉B+树，顺序删除测试：size:"+ size +".");
            start = System.currentTimeMillis();
            count = 0;
            for(int j = 0; j < size; j++){
                ss = (String)myTree.remove(j);
                if(ss == null){
                    System.out.println("找不到啊弟弟！");
                    System.exit(0);
                }
                else{
                    count++;
                }
            }
            end = System.currentTimeMillis() - start;
            System.out.println("花费时间：" + end);
            System.out.println(count);
        }
    }*/
}
