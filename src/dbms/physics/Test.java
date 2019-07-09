package dbms.physics;

public class Test {

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
