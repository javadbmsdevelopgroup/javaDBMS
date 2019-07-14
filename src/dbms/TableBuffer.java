package dbms;

import dbms.exceptions.BufferSizeException;
import javafx.scene.control.Tab;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

///////////////////////////////////////////表缓冲类
public class TableBuffer {
    //表数据缓冲区，可以缓存size个页
    //页表地图，通过页号找对应的页


    Map<Integer,TablePage> pageMap=new HashMap<>();          //相当于一个页表。是一个散列表  key=页号  TablePage=具体的某页对象
    int curSize=0;             //当前的缓冲区大小（已存了多少页）
    int totleSize;   //缓冲区的总大小，也就是最多能存多少页
    int earliest=0;    //因为页置换算法采用FIFO，所以需要记录当前最早进来的页的位置（如果需要置换的话就换掉那页）
    //缓冲区大小（多少页）
    public TableBuffer(int size) throws BufferSizeException{
        if(size<=0) throw new BufferSizeException("BufferSize small than 0");
        totleSize=size;
    }

    public void clean(){
        pageMap.clear();
    }
    public void deletePage(int pageindex){
        if(pageMap.containsKey(pageindex)){
            pageMap.remove(pageindex);
        }
    }
    //向缓冲区添加一页，如果缓冲区已满会执行页置换算法
    public boolean addPage(TablePage tablePage){
        if(totleSize-curSize<=0) {
            //执行页置换算法   暂时先用FIFO  (后续要完善可以加个接口，实现各种页置换算法

            //空间已满
            this.replacePage(earliest,tablePage);
            earliest=(earliest+1)%totleSize;
            return true;
        }

        if(pageMap.containsKey(tablePage.getPageNum())) return true;
        System.out.println(tablePage.getPageNum());
        pageMap.put(tablePage.getPageNum(),tablePage);
        return true;
    }

    //获取某一页。提供页号。不存在的话返回null
    public TablePage getPage(int pageNum){
        if(pageMap.containsKey(pageNum)) return pageMap.get(pageNum);
        else return null;
    }

    //判断某页是否在当前缓冲区里
    public boolean isPageExist(int pageNum){
        if(pageMap.containsKey(pageNum)) return true; else return false;

    }

    //替换掉缓冲区中的某页
    public boolean replacePage(int pageNum,TablePage newPage){
        if(!pageMap.containsKey(pageNum)) return false;
        pageMap.remove(pageNum);
        pageMap.put(newPage.getPageNum(),newPage);
        return true;
    }
}
