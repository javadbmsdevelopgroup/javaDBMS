package dbms;

import java.util.Arrays;

public class Tools {
    private static Tools instance=null;
    public static Tools getInstance(){
        if(instance == null) instance=new Tools();
        return instance;
    }
    public int getIntFromBytes(Byte[] intBytes){
        return (intBytes[3] & 0xFF |
                (intBytes[2] & 0xFF) << 8 |
                (intBytes[1] & 0xFF) << 16 |
                (intBytes[0] & 0xFF) << 24);
    }
    public byte[] toBytes(int val){
        byte[] result = new byte[4];
        //由高位到低位
        result[0] = (byte)((val >> 24) & 0xFF);
        result[1] = (byte)((val >> 16) & 0xFF);
        result[2] = (byte)((val >> 8) & 0xFF);
        result[3] = (byte)(val & 0xFF);
        return result;
    }
    private Tools(){
    }
}
