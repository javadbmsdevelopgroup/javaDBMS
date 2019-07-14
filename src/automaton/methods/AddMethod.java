package automaton.methods;

import automaton.INodeFunc;
import automaton.InfCollection;
import automaton.SQLSession;
import dbms.logic.DatabaseDBMSObj;
import dbms.logic.TableDBMSObj;
import dbms.logic.intergrityconstrain.NoNULLConstain;
import dbms.logic.intergrityconstrain.PositiveConstrain;

import java.io.Serializable;


///////////////////这是处理Add命令的 (add命令目前只有用来添加完整性约束)
public class AddMethod implements INodeFunc, Serializable {
    @Override
    public Object doWork(InfCollection infCollection, Object... objs){
        String constrainName=infCollection.keyWords.pop();    //完整性约束名称
        /*对于SQLAutomaton.doWork提供的第一个object参数都是SQLSession对象
        * 里面包含了某个SQL会话中当前正在使用哪个数据库等信息
        * 因此下面这句代码进行强制类型转换，获取SQLSession对象
        * */
        SQLSession sqlSession=(SQLSession)objs[0];
        String tbName=infCollection.tableNames.pop();    //表名
        String cName=infCollection.columNames.pop();     //列名

        switch (constrainName.toUpperCase()){
            case "POSITIVE":  //非负完整性约束(名字不太合适。。凑合用吧)
                if(MethodTools.checkTableandDatabase(sqlSession,tbName)){    //checkTableandDatabase()方法是检查数据库名和表名的合法性
                    try {
                        //建立表逻辑对象(具体见TableDBMSObj类)
                        TableDBMSObj tableDBMSObj = new TableDBMSObj(tbName, new DatabaseDBMSObj(sqlSession.curUseDatabase, DatabaseDBMSObj.rootPath));
                        //添加一个完整性约束
                        if(tableDBMSObj.tableStructure.addConstain(cName,new PositiveConstrain(),tbName,sqlSession.curUseDatabase))
                        {
                            System.out.println("添加完整性约束成功");
                            return 1;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        return -1;
                    }
                }
                break;
        }
        return -2;
    }
}
