package dbms.view;

import dbms.RelationRow;
import dbms.TableReader;
import dbms.logic.TableDBMSObj;
import dbms.logic.TableStructureItem;

//逻辑层到视图层的映射
public class ViewLogicMapping {
    private RelationView relationView;
    private TableDBMSObj tableDBMSObj;
    public int viewBufferSize=100;
    String[] columnNames;
    //提供的参数: 视图的缓冲大小（超出后会自动输出并清空）,列名
    public ViewLogicMapping(int viewBufferSize,String[] columnNames,TableDBMSObj tableDBMSObj) throws Exception{
        this.viewBufferSize=viewBufferSize;

        this.tableDBMSObj=tableDBMSObj;
        this.columnNames=columnNames;
        if(columnNames[0].compareTo("*")==0){

            this.columnNames=new String[tableDBMSObj.tableStructure.dts.size()];
            int i=0;
            for(TableStructureItem tableStructureItem:tableDBMSObj.tableStructure.dts){

                this.columnNames[i]=tableStructureItem.conlumName;
                i++;
            }
        }
        this.relationView=new RelationView(this.columnNames);
    }

    public void showHeadLine(){
        relationView.printHeadLine();
    }
    public void addRelation(RelationRow relationRow){
        if(columnNames.length==0|| relationRow==null) return;
        String[] row=new String[columnNames.length];
        for(int i=0;i<columnNames.length;i++){
            //System.out.println(columnNames[i]);
            row[i]=relationRow.getVal(columnNames[i]).toString();
        }
        relationView.addRow(row);
        if(relationView.rows.size()==viewBufferSize) flush();
    }
    public void flush(){
        relationView.printRows();
        relationView.rows.clear();
    }
    public void showBottomLine(){
        relationView.printBottomLine();
    }
}
