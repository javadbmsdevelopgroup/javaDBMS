package dbms;

import dbms.exceptions.BufferSizeException;
import javafx.scene.control.Tab;

import java.util.HashMap;
import java.util.Map;

public class TableBuffer {
    //表数据缓冲区，可以缓存size个页
    //页表地图，通过页号找对应的页
    Map<Integer,TablePage> pageMap=new HashMap<>();
    int curSize=0;
    int totleSize;   //页大小
    int earliest=0;
    public TableBuffer(int size) throws BufferSizeException{
        if(size<=0) throw new BufferSizeException("BufferSize small than 0");
        totleSize=size;
    }
    //向缓冲区添加一页
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
