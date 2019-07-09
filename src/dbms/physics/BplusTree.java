package dbms.physics;

import java.io.Serializable;

//更改了  2019/7/7 14：20
public class BplusTree<K extends Comparable<K>> implements Serializable {
    //
    protected BplusTreeNode<K> root;
    //
    protected BplusTreeNode<K> head;
    //
    protected int order;
    //
    protected int height;
    //
    public BplusTree(int order){
        if(order < 3){
            System.out.println("order must greater than 2");
            System.exit(0);
        }
        this.order = order;
        this.height = 0;
        this.root = new BplusTreeNode<K>(true,true);
        this.head = this.root;
    }
    //
    public BplusTree(){
        this.order = 3;
        this.height = 0;
        this.root = new BplusTreeNode<K>(true,true);
        this.head = this.root;
    }
    //
    public int getHeight() {
        return height;
    }
    //
    public int getOrder() {
        return order;
    }

    public BplusTreeNode<K> getHead() {
        return head;
    }

    public BplusTreeNode<K> getRoot() {
        return root;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setHead(BplusTreeNode<K> head) {
        this.head = head;
    }

    public void setRoot(BplusTreeNode<K> root) {
        this.root = root;
    }

    public void insert(K key, Object value){
        root.insertOrUpdate(key, value, this);
//        System.out.println(root.toString());
    }

    public Object remove(K key){
        return root.remove(key, this);
    }

    public Object get(K key){
        return root.get(key);
    }
    public static void main(String[] args){

    }

}
