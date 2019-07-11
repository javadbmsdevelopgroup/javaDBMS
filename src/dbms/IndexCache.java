package dbms;

import dbms.logic.DataType;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableDBMSObj;
import dbms.physics.BplusTree;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

public class IndexCache {
    private static IndexCache instance=null;
    public boolean changed=false;
    private IndexCache(){}

    private static Map<String,BplusTree> indexCache=new HashMap<>();
    public static IndexCache getInstance(){
        if(instance==null) instance=new IndexCache();
        return instance;
    }

    //返回对应表的索引
    public BplusTree getIndex(TableDBMSObj tableDBMSObj){
        String strKey=tableDBMSObj.dbBelongedTo.dbName+"."+tableDBMSObj.tbName;
        if(indexCache.containsKey(strKey)){
            return indexCache.get(strKey);
        }else{
            if(!loadIndex(tableDBMSObj)) return null;
            return indexCache.get(strKey);
        }
    }

    public boolean loadIndex(TableDBMSObj tableDBMSObj){
        String strKey=tableDBMSObj.dbBelongedTo.dbName+"."+tableDBMSObj.tbName;
        if(indexCache.containsKey(strKey)) return true;
        String path= DatabaseDBMSObj.rootPath+"\\"+tableDBMSObj.dbBelongedTo.dbName+"\\"+tableDBMSObj.tbName+".lh";
        System.out.println(path);
        try{
            FileInputStream fis=new FileInputStream(path);
            DataInputStream dis=new DataInputStream(fis);
            BufferedInputStream bis=new BufferedInputStream(dis);
            DataType dataType=tableDBMSObj.tableStructure.getDataType(tableDBMSObj.tableStructure.indexOn);

            BplusTree bpulsTree=null;
            int keySize=tableDBMSObj.tableStructure.getConlumSize(tableDBMSObj.tableStructure.indexOn);

            switch (dataType){
                case INT32:
                    bpulsTree=new BplusTree<Integer>();
                    break;
                case STRING:
                    bpulsTree=new BplusTree<String>();
                    break;
            }

            byte[] key=new byte[keySize];
            byte[] pos=new byte[4];
            int sign=0;
            int k=0;

            do{
                sign=bis.read(key);
                sign=bis.read(pos);
                int realPos=  (pos[3] & 0xFF |
                        (pos[2] & 0xFF) << 8 |
                        (pos[1] & 0xFF) << 16 |
                        (pos[0] & 0xFF) << 24);
                switch (dataType){
                    case STRING:
                        bpulsTree.insert(new String(key),realPos);
                        break;
                    case INT32:
                        int realKey=  (key[3] & 0xFF |
                                (key[2] & 0xFF) << 8 |
                                (key[1] & 0xFF) << 16 |
                                (key[0] & 0xFF) << 24);

                        //System.out.println(key[0]+" "+key[1]+" "+key[2]+" "+key[3]);
                        bpulsTree.insert(realKey,realPos);
                        //System.out.println(keySize+"["+realKey+","+realPos+"]");
                        break;
                }
                k++;
            }while (sign>0);
            if(bpulsTree==null) {
                return true;
            }
            System.out.println("索引缓存成功");
            indexCache.put(strKey, bpulsTree);

            return true;
        }catch (Exception e){

            return false;
        }



    }

    public void resetRecord(RelationRow r,TableDBMSObj tableDBMSObj,int recordPos){
        BplusTree bplusTree=getIndex(tableDBMSObj);
        if(bplusTree==null) return;
        DataType dataType=tableDBMSObj.tableStructure.getDataType(tableDBMSObj.tableStructure.indexOn);
        switch (dataType){
            case STRING:
                String val=(String)bplusTree.get((String)r.getVal(tableDBMSObj.tableStructure.indexOn));
                if(val==null){
                    bplusTree.insert((String)r.getVal(tableDBMSObj.tableStructure.indexOn),recordPos);
                    changed=true;
                }
                break;
            case INT32:
                Integer v=(Integer) bplusTree.get((int)r.getVal(tableDBMSObj.tableStructure.indexOn));
                if(v==null){
                    bplusTree.insert((int)r.getVal(tableDBMSObj.tableStructure.indexOn),recordPos);
                }
                break;
        }

    }
}
