package dbms;

import javafx.scene.control.Tab;

import java.util.HashMap;
import java.util.Map;

public class TableBuffer {
    //表数据缓冲区，可以缓存size个页
    //页表地图，通过页号找对应的页
    Map<Integer,TablePage> pageMap=new HashMap<>();
    int curSize=0;
    int totleSize=0;
    public TableBuffer(int size){
        totleSize=size;
    }
    //向缓冲区添加一页
    public boolean addPage(TablePage tablePage){
        if(totleSize-curSize<=0) return false; //空间已满
        if(pageMap.containsKey(tablePage.getPageNum())) return true;
        pageMap.put(tablePage.getPageNum(),tablePage);
        return true;
    }

    public TablePage getPage(int pageNum){
        if(pageMap.containsKey(pageNum)) return pageMap.get(pageNum);
        else return null;
    }

    public boolean isPageExist(int pageNum){
        if(pageMap.containsKey(pageNum)) return true; else return false;

    }
    public boolean replacePage(int pageNum,TablePage newPage){
        if(!pageMap.containsKey(pageNum)) return false;
        pageMap.remove(pageNum);
        pageMap.put(newPage.getPageNum(),newPage);
        return true;
    }
}
