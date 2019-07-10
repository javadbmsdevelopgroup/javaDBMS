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
    private IndexCache(){}

    private static Map<String,BplusTree> indexCache=new HashMap<>();
    public static IndexCache getInstance(){
        if(instance==null) instance=new IndexCache();
        return instance;
    }

    //返回对应表的索引
    public BplusTree getIndex(TableDBMSObj tableDBMSObj){
        if(indexCache.containsKey(tableDBMSObj.tbName)){
            return indexCache.get(tableDBMSObj.tbName);
        }else{
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
                    return null;
                }

                System.out.println("索引缓存成功");
                indexCache.put(tableDBMSObj.tbName, bpulsTree);

                return bpulsTree;
            }catch (Exception e){
                e.printStackTrace();
            }


            return null;
        }
    }

    public static boolean loadIndex(TableDBMSObj tableDBMSObj){
        if(indexCache.containsKey(tableDBMSObj.tbName)) return true;
        //读取索引
        try{
        if(tableDBMSObj.tableStructure.useIndex){
            FileInputStream fis=new FileInputStream(tableDBMSObj.dbBelongedTo.getRootPath()+"\\"+tableDBMSObj.dbBelongedTo.dbName+"\\"
                    +tableDBMSObj.tbName+".lh");
                ObjectInputStream ois = new ObjectInputStream(fis);
                BplusTree bpTree=(BplusTree)ois.readObject();
                if(bpTree!=null){
                    indexCache.put(tableDBMSObj.tbName,bpTree);
                    return true;
                }
                return false;
        }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
