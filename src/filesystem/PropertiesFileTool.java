package filesystem;

public class PropertiesFileTool {
    private static PropertiesFileTool instance=null;
    private PropertiesFileTool(){
    }
    public PropertiesFileTool getInstance(){
        if(instance==null) instance=new PropertiesFileTool();
        return instance;
    }
}
