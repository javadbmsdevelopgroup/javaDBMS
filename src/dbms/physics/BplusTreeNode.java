package dbms.physics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//更改了  2019/7/7 14：20
public class BplusTreeNode <K extends Comparable<K>> implements Serializable {
    //是否为叶子节点
    protected boolean isLeaf;
    //是否为根节点
    protected boolean isRoot;
    //父节点
    protected BplusTreeNode<K> parent;
    //前一个叶子节点
    protected BplusTreeNode<K> previous;
    //后一个叶子节点
    protected BplusTreeNode<K> next;
    //关键值
    protected List<K> keys;
    //叶子节点的数据
    protected List dataOfKey;
    //孩子节点
    protected List<BplusTreeNode<K>> children;
    //构造函数1 参数：boolean isLeaf 是否为叶子节点
    public BplusTreeNode(boolean isLeaf){
        this.isLeaf = isLeaf;
        keys = new ArrayList<K>();
        if(!isLeaf){
            children = new ArrayList<BplusTreeNode<K>>();
        }
        else
            dataOfKey = new ArrayList();
    }
    //构造函数2
    public BplusTreeNode(boolean isLeaf, boolean isRoot){
        this(isLeaf);
        this.isRoot = isRoot;
    }

    public BplusTreeNode<K> getNext() {
        return next;
    }

    //查找键值为key的数据，空返回null,递归运算
    public Object get(K key){
        //
        if(isLeaf){
          int low = 0, high = keys.size() - 1;
          int mid = 0 ;
          int com = 0;
          while (low <= high){
              mid = (low + high) / 2;
              com = keys.get(mid).compareTo(key);
              if(com == 0)
                  return dataOfKey.get(mid);
              else if (com < 0)
                  low = mid + 1;
              else
                  high = mid - 1;
          }
          return null;
        }
        if(key.compareTo(keys.get(0)) < 0)
            return children.get(0).get(key);
        else {
            int low = 0;
            int high = keys.size() - 1;
            int mid = 0;
            int com = 0;
            while (low <= high){
                mid = (low + high)/2;
                com = keys.get(mid).compareTo(key);
                if(com == 0)
                {
//                    System.out.println(mid);
//                    System.out.println(children.get(mid).toString());
                    return children.get(mid).get(key);
                }
                else if (com < 0)
                    low = mid + 1;
                else
                    high = mid - 1;
            }
            return children.get(low - 1).get(key);

        }
    }
    //插入或更新一个节点    参数：key键值    value数据    tree B+树的root
    public void insertOrUpdate(K key, Object value, BplusTree<K> tree){
        if(isLeaf){
            if(keys.contains(key)|| keys.size() < tree.getOrder()){
                insertOrUpdate(key, value);
                if (tree.getHeight() == 0)
                    tree.setHeight(1);
            }else {
                BplusTreeNode<K> left = new BplusTreeNode<K>(true);
                BplusTreeNode<K> right = new BplusTreeNode<K>(true);

                if (previous != null){
                    previous.next = left;
                    left.previous = previous ;
                }
                if (next != null) {
                    next.previous = right;
                    right.next = next;
                }
                if (previous == null){
                    tree.setHead(left);
                }

                left.next = right;
                right.previous = left;
                previous = null;
                next = null;

                copy2Node(key, value, left, right, tree);

                if(parent != null){
                    int index = parent.children.indexOf(this);
                    parent.children.remove(this);
                    left.parent = parent;
                    right.parent = parent;
                    parent.children.add(index, left);
                    parent.children.add(index + 1, right);
                    K kk = right.keys.get(0);
                    if(parent.keys.contains(kk))
                        parent.keys.add(index, left.keys.get(0));
                    else
                        parent.keys.add(index+1,kk);
                    keys = null;
                    dataOfKey = null;
                    children = null;

                    parent.updateInsert(tree);
                    parent = null;
                }
                else{
                    isRoot = false;
                    BplusTreeNode<K> parent = new BplusTreeNode<K>(false, true);
                    tree.setRoot(parent);
                    left.parent = parent;
                    right.parent = parent;
                    parent.children.add(left);
                    parent.children.add(right);
                    parent.keys.add(left.keys.get(0));
                    parent.keys.add(right.keys.get(0));
                    keys = null;
                    dataOfKey = null;
                    children = null;
                }
            }
            return;
        }
        if(key.compareTo(keys.get(0)) < 0)
        {
            //更改了  2019/7/7 14：20
            keys.set(0, key);
            children.get(0).insertOrUpdate(key, value, tree);
        }
        else {
            int low = 0;
            int high = keys.size() - 1;
            int mid = 0;
            int com = 0;
            while (low <= high){
                mid = (low + high)/2;
                com = keys.get(mid).compareTo(key);
                if(com == 0){
                    children.get(mid).insertOrUpdate(key, value, tree);
                    return;
                }
                else if (com < 0)
                    low = mid + 1;
                else
                    high = mid - 1;
            }
            children.get(low - 1).insertOrUpdate(key, value, tree);

        }

    }
    //辅助完成插入操作分裂的情况，仅由 insertOrUpdate 调用
    private void copy2Node(K key, Object value, BplusTreeNode<K> left,
                           BplusTreeNode<K> right, BplusTree<K> tree){
        int leftSize = (tree.getOrder() + 1) / 2 + (tree.getOrder() + 1) % 2;
        boolean b = false;//用于记录新元素是否已经被插入
        for (int i = 0; i < keys.size(); i++) {
            if(leftSize !=0){
                leftSize --;
                if(!b&&keys.get(i).compareTo(key) > 0){
                    left.keys.add(key);
                    left.dataOfKey.add(value);
                    b = true;
                    i--;
                }else {
                    left.keys.add(keys.get(i));
                    left.dataOfKey.add(dataOfKey.get(i));
                }
            }else {
                if(!b&&keys.get(i).compareTo(key) > 0){
                    right.keys.add(key);
                    right.dataOfKey.add(key);
                    b = true;
                    i--;
                }else {
                    right.keys.add(keys.get(i));
                    right.dataOfKey.add(dataOfKey.get(i));
                }
            }
        }
        if(!b){
            right.keys.add(key);
            right.dataOfKey.add(value);
        }
    }

    //对中间节点分裂的处理
    protected void updateInsert(BplusTree<K> tree){
        //如果子节点数超出阶数，则需要分裂该节点
        if (children.size() > tree.getOrder()) {
            //分裂成左右两个节点
            BplusTreeNode<K> left = new BplusTreeNode<K>(false);
            BplusTreeNode<K> right = new BplusTreeNode<K>(false);
            //左右两个节点子节点的长度
            int leftSize = (tree.getOrder() + 1) / 2 + (tree.getOrder() + 1) % 2;
            int rightSize = (tree.getOrder() + 1) / 2;
            //复制子节点到分裂出来的新节点，并更新关键字
            for (int i = 0; i < leftSize; i++){
                left.children.add(children.get(i));
                children.get(i).parent = left;
            }
            for (int i = 0; i < rightSize; i++){
                right.children.add(children.get(leftSize + i));
                children.get(leftSize + i).parent = right;
            }
            for (int i = 0; i < leftSize; i++) {
                left.keys.add(keys.get(i));
            }
            for (int i = 0; i < rightSize; i++) {
                right.keys.add(keys.get(leftSize + i));
            }

            //如果不是根节点
            if (parent != null) {
                //调整父子节点关系
                int index = parent.children.indexOf(this);
                parent.children.remove(this);
                left.parent = parent;
                right.parent = parent;
                parent.children.add(index,left);
                parent.children.add(index + 1, right);
                K kk = right.keys.get(0);
                if(parent.keys.contains(kk))
                    parent.keys.add(index, left.keys.get(0));
                else
                    parent.keys.add(index+1,kk);
                keys = null;
                children = null;

                //父节点更新关键字
                parent.updateInsert(tree);
                parent = null;
                //如果是根节点
            }else {
                isRoot = false;
                BplusTreeNode<K> parent = new BplusTreeNode<K>(false, true);
                tree.setRoot(parent);
                tree.setHeight(tree.getHeight() + 1);
                left.parent = parent;
                right.parent = parent;
                parent.children.add(left);
                parent.children.add(right);
                parent.keys.add(left.keys.get(0));
                parent.keys.add(right.keys.get(0));
                keys = null;
                children = null;
            }
        }
    }
    //删除键值为key的数据，返回数据
    public Object remove(K key, BplusTree<K> tree){
        if(isLeaf){
            if(!keys.contains(key))
                return null;
            if(isRoot){
                if(keys.size() == 1)
                    tree.setHeight(0);
                return remove(key);
            }
            int halfOrder = (int)Math.ceil(tree.getOrder()/2.0);
            if(keys.size() > halfOrder){
                return remove(key);
            }

            if(previous != null && previous.parent == parent &&
                    previous.keys.size() > halfOrder){
                int siz = previous.keys.size() - 1;
                keys.add(0, previous.keys.remove(siz));
                dataOfKey.add(0,previous.dataOfKey.remove(siz));
                int index = parent.children.indexOf(this);
                parent.keys.set(index, keys.get(0));
                return remove(key);
            }

            if(next != null && next.parent == parent &&
                    next.keys.size() > halfOrder){
                keys.add(next.keys.remove(0));
                dataOfKey.add(next.dataOfKey.remove(0));
                int index = parent.children.indexOf(next);
                parent.keys.set(index, next.keys.get(0));
                return remove(key);
            }
            if(previous != null && previous.parent == parent &&
                    previous.keys.size() <= halfOrder){
                Object value = remove(key);
                for(int i = 0; i < keys.size(); i++){
                    previous.keys.add(keys.get(i));
                    previous.dataOfKey.add(dataOfKey.get(i));
                }
                int index = parent.children.indexOf(this);
                parent.children.remove(index);
                parent.keys.remove(index);
                keys = null;
                dataOfKey = null;
                if(next != null){
                    next.previous = previous;
                    previous.next = next;
                    previous = null;
                    next = null;
                }else {
                    previous.next = null;
                    previous = null;
                }
                if((!parent.isRoot && parent.keys.size() >= halfOrder)||
                        (parent.isRoot && parent.keys.size() >= 2)){
                    parent = null;
                    return value;
                }
                parent.updateRemove(tree);
                parent = null;
                return value;
            }

            if(next != null && next.parent == parent &&
                    next.keys.size() <= halfOrder){
                Object value = remove(key);
                for(int i = 0; i < next.keys.size(); i++){
                    keys.add(next.keys.get(i));
                    dataOfKey.add(next.dataOfKey.get(i));
                }
                next.parent = null;
                next.dataOfKey = null;
                next.keys = null;
                parent.keys.remove(parent.children.indexOf(next));
                parent.children.remove(next);
                if(next.next !=null){
                    BplusTreeNode<K> temp = next;
                    temp.next.previous = this;
                    this.next = temp.next;
                    temp.next = null;
                    temp.previous = null;
                }else {
                    next.previous = null;
                    this.next = null;
                }

                if((!parent.isRoot && parent.keys.size() >= halfOrder)||
                        (parent.isRoot && parent.keys.size() >= 2)){
                    return value;
                }
                parent.updateRemove(tree);
                return value;
            }
        }

        if(key.compareTo(keys.get(0)) < 0)
            return children.get(0).remove(key, tree);
        else{
            int low = 0;
            int high = keys.size() - 1;
            int mid = 0;
            int comm = 0;
            while (low <= high){
                mid = (low + high)/2;
                comm = keys.get(mid).compareTo(key);
                if(comm == 0)
                    return children.get(mid).remove(key, tree);
                    //更改了  2019/7/7 14：20
                else if (comm < 0)
                    low = mid + 1;
                else
                    high = mid - 1;
            }
            return children.get(low - 1).remove(key, tree);
        }
    }
    //删除一个键值为key的数据，并返回其数据
    protected Object remove(K key){
        if(keys.contains(key))
        {
            int index = keys.indexOf(key);
            keys.remove(index);
            return dataOfKey.remove(index);
        }
        return null;
    }
    //对删除一个节点后的更新
    protected void updateRemove(BplusTree<K> tree){
        int halfOrder = (int)Math.ceil(tree.getOrder()/2.0);
        if(children.size() < halfOrder){
            if(isRoot){
                if(children.size() >= 2)
                    return;
                BplusTreeNode<K> root = children.get(0);
                tree.setRoot(root);
                tree.setHeight(tree.getHeight() - 1);
                root.parent = null;
                root.isRoot = true;
                keys = null;
                children = null;
                return;
            }
            int curIdx = parent.children.indexOf(this);
            int prevIdx = curIdx - 1;
            int nextIdx = curIdx + 1;
            BplusTreeNode<K> pre = null, nex = null;
            if(prevIdx >= 0)
                pre = parent.children.get(prevIdx);
            if(nextIdx < parent.children.size())
                nex = parent.children.get(nextIdx);
            if(pre != null && pre.children.size() > halfOrder){
                int inx = pre.children.size() - 1;
                BplusTreeNode<K> borrow = pre.children.remove(inx);
                borrow.parent = this;
                children.add(0, borrow);
                keys.add(0, pre.keys.remove(inx));
                parent.keys.set(parent.children.indexOf(this), keys.get(0));
                return;
            }

            if(nex != null && nex.children.size() > halfOrder){
                BplusTreeNode<K> borrow = nex.children.remove(0);
                borrow.parent = this;
                children.add(borrow);
                keys.add(nex.keys.remove(0));
                parent.keys.set(parent.children.indexOf(nex),nex.keys.get(0));
                return;
             }

            if(pre != null && pre.children.size() <= halfOrder){
                for(int i = 0; i < children.size(); i++){
                    pre.children.add(children.get(i));
                    children.get(i).parent = pre;
                    pre.keys.add(keys.get(i));
                }
                parent.keys.remove(parent.children.indexOf(this));
                parent.children.remove(this);
                parent = null;
                children = null;
                keys = null;
                if((pre.parent.isRoot && pre.parent.children.size() >= 2) ||
                        (!pre.isRoot && pre.parent.children.size() >= halfOrder))
                    return;
                pre.parent.updateRemove(tree);
                return;
             }

            if(nex != null && nex.children.size() <= halfOrder){
                for(int i = 0; i < nex.children.size(); i++){
                    nex.children.get(i).parent = this;
                    children.add(nex.children.get(i));
                    keys.add(nex.keys.get(i));
                }
                parent.keys.remove(parent.children.indexOf(nex));
                parent.children.remove(nex);
                nex.parent = null;
                nex.children = null;
                nex.keys = null;
                if((!parent.isRoot && parent.children.size() >= halfOrder) ||
                        (parent.isRoot && parent.children.size() >= 2))
                    return;
                parent.updateRemove(tree);
                return;
             }
        }
    }
    //插入到当前的节点中
    protected void insertOrUpdate(K key, Object value){
        int low = 0;
        int high = keys.size() - 1;
        int mid = 0;
        int comm;
        while (low <= high){
            mid = (low + high) / 2;
            comm = key.compareTo(keys.get(mid));
            if(comm == 0){
                dataOfKey.remove(mid);
                dataOfKey.add(mid, value);
                return;
            }
            else if (comm < 0)
                high = mid - 1;
            else
                low = mid + 1;
        }
        keys.add(low, key);
        dataOfKey.add(low, value);
    }
    //返回节点的信息
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("isRoot: ");
        sb.append(isRoot);
        sb.append(", isLeaf: ");
        sb.append(isLeaf);
        sb.append(", keys: ");
        for(int i = 0; i < keys.size(); i++){
            sb.append(keys.get(i));
            sb.append(" ");
        }
        if(isLeaf)
        {
            sb.append("values: ");
            for(int i = 0; i < dataOfKey.size(); i++){
                sb.append( dataOfKey.get(i));
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
