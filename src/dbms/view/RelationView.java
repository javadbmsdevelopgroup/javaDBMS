package dbms.view;


import java.io.Serializable;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

///////////////////////////////////视图层的关系显示
/////目前存在的问题。当大量行存在时（比如上百万行），未解决如何显示比较好的问题。应该这里也要添加缓存。
///后面应该需要建立起 TableReader(提供了缓存技术)和RelationView的关系,通过缓存慢慢打印view

public class RelationView implements Serializable {
    List<String> conlumNames=new ArrayList<>();
    List<RelationViewItem> rows=new ArrayList<>();
    int[] maxLengths;
    //提供列名信息
    public RelationView(String... conlumNames){
        for(int i=0;i<conlumNames.length;i++){
            this.conlumNames.add(conlumNames[i]);
        }
        maxLengths=new int[this.conlumNames.size()];
        initMaxSizeArray();
    }
    private void initMaxSizeArray(){
        for(int i=0;i<conlumNames.size();i++){
            maxLengths[i]=conlumNames.get(i).length();
        }
    }
    public List<String> getConlumNames(){
        return conlumNames;
    }

    public List<RelationViewItem> getRows() {
        return rows;
    }

    //添加一行
    public void addRow(String... items){
        if(items.length!= this.conlumNames.size()) return ;
        this.rows.add(new RelationViewItem(items));
    }

    //添加一列
    public void addConlum(String name,int position){
        if(position<0 || position>conlumNames.size()) return;
        conlumNames.add(position,name);
        maxLengths=new int[conlumNames.size()];
        initMaxSizeArray();
        for(int i=0;i<rows.size();i++){
            rows.get(i).vals.add(position,"");
        }
    }

    //删除一列
    public void deleteConlum(int position){
        if(position<0 || position>=conlumNames.size()) return;
        conlumNames.remove(position);
        maxLengths=new int[conlumNames.size()];
        initMaxSizeArray();
        for(int i=0;i<rows.size();i++){
            rows.get(i).vals.remove(position);
        }
    }
    //删除一列
    public void deleteConlum(String conlumName){
        int p=-1;
        for(int i=0;i<conlumNames.size();i++){
            if(conlumNames.get(i).compareTo(conlumName)==0){
                p=i; break;
            }
        }
        deleteConlum(p);
    }
    //获取列索引
    public int getConlumIndex(String conlumName){
        int p=-1;
        for(int i=0;i<conlumNames.size();i++){
            if(conlumNames.get(i).compareTo(conlumName)==0){
                p=i; break;
            }
        }
        return p;
    }

    //输出view
    public void printRelationView(){
        printRelationView(this.rows.size());

    }
    //带限制地输出view
    public void printRelationView(int limit){
        if(limit>rows.size())
            return;


        System.out.print("+");
        //headline
        int len = 22;
        for(int i=0;i<conlumNames.size();i++){
            System.out.print(String.join("", Collections.nCopies(len, "-")));
            System.out.print("++");
        }
        System.out.println();
        //colums
        for(int i=0;i<conlumNames.size();i++){
            System.out.print("| ");
            System.out.print(getPerfetString(conlumNames.get(i)));
            System.out.print(" |");
        }

        System.out.println();
        //headline
        System.out.print("+");
        for(int i=0;i<conlumNames.size();i++){
            System.out.print(String.join("", Collections.nCopies(len, "-")));
            System.out.print("++");
        }
        System.out.println();
        //rows
        for(int i=0;i< limit;i++){
            for(int j=0;j<conlumNames.size();j++){
                System.out.print("| ");
                System.out.print(getPerfetString(rows.get(i).vals.get(j)));
                System.out.print(" |");
            }
            System.out.println();
        }
        //bottom
        System.out.print("+");
        for(int i=0;i<conlumNames.size();i++){
            System.out.print(String.join("", Collections.nCopies(len, "-")));
            System.out.print("++");
        }
        System.out.println();
    }


    public String getPerfetString(String ss){
        StringBuilder mybuild = new StringBuilder();
        int l = 0;
        int i = 0;
        while (l < ss.length()){
            if(ss.charAt(l) == '\u0000')
                break;
            if(ss.charAt(l) > 255)
                i++;
            mybuild.append("" + ss.charAt(l));
            l++;
        }
        while (mybuild.length() < 20-i)
            mybuild.append(" ");
        return mybuild.toString();
    }

    public void printHeadLine(){
        int limit=this.rows.size();
        if(limit>rows.size()) return;
        int[] maxLengths=new int[this.conlumNames.size()];
        for(int i=0;i<conlumNames.size();i++){
            maxLengths[i]=conlumNames.get(i).length();
        }
        for(int i=0;i<limit;i++){
            for(int j=0;j<conlumNames.size();j++){
                if(rows.get(i).vals.get(j).length()>maxLengths[j]) maxLengths[j]=rows.get(i).vals.get(j).length();
            }
        }
        System.out.print("+");
        //headline
        for(int i=0;i<conlumNames.size();i++){
            System.out.print(String.join("", Collections.nCopies(maxLengths[i]+2, "-")));
            System.out.print("+");
        }
        System.out.println();
        //colums
        for(int i=0;i<conlumNames.size();i++){
            System.out.print("| ");
            System.out.print(conlumNames.get(i));
            System.out.print(String.join("", Collections.nCopies(maxLengths[i]-conlumNames.get(i).length(), " ")));
            System.out.print(" |");
        }
        System.out.println();
    }
    public void printRows(){
        int limit=this.rows.size();
        if(limit>rows.size()) return;
        initMaxSizeArray();
        for(int i=0;i<limit;i++){
            for(int j=0;j<conlumNames.size();j++){
                if(rows.get(i).vals.get(j).length()>maxLengths[j]) maxLengths[j]=rows.get(i).vals.get(j).length();
            }
        }

        //rows
        for(int i=0;i< limit;i++){
            for(int j=0;j<conlumNames.size();j++){
                System.out.print("| ");
                System.out.print(rows.get(i).vals.get(j));
                System.out.print(String.join("", Collections.nCopies(maxLengths[j]-rows.get(i).vals.get(j).length(), " ")));
                System.out.print(" |");
            }
            System.out.println();
        }

    }
    public void printBottomLine(){
        int limit=this.rows.size();
        if(limit>rows.size()) return;

        //bottom
        System.out.print("+");
        for(int i=0;i<conlumNames.size();i++){
            System.out.print(String.join("", Collections.nCopies(maxLengths[i]+2, "-")));
            System.out.print("+");
        }
        System.out.println();
    }
    public String getVal(int row,String conlumName){
        return rows.get(row).vals.get(getConlumIndex(conlumName));
    }
    public void setVal(int row,String conlumName,String val){
        rows.get(row).vals.set(getConlumIndex(conlumName),val);
    }
    public int getRowCount(){
        return rows.size();
    }
    class RelationViewItem implements Serializable{
        List<String> vals = new ArrayList<>();
        public RelationViewItem(String... vals){
            for(String n:vals){
                this.vals.add(n);
            }
        }
    }


}



