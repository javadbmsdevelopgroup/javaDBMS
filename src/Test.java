////////////////////////////////////////////////测试专用文件


import java.io.*;

interface I1{
    void doSomething();
}
class A implements Serializable {
    I1 i;
    A(I1 x){
        i=x;
    }
}
class B{
    A a1=null;
    B(A a1){
        this.a1=a1;
    }
}
class C implements I1{
    @Override
    public void doSomething(){
        System.out.println(111);
    }
}

public class Test {
    public static void main(String[] args){
        B b1=new B(new A(new C()));
        b1.a1.i.doSomething();
        try{
        //FileOutputStream fos=new FileOutputStream("obj",true);
        //ObjectOutputStream oos=new ObjectOutputStream(fos);
        //oos.writeObject(b1);
        //fos.close();
        //oos.close();
            FileInputStream fis=new FileInputStream("obj");
            ObjectInputStream ois=new ObjectInputStream(fis);
            B b2=(B)ois.readObject();
            b2.a1.i.doSomething();
            fis.close();
            ois.close();
        }catch (Exception e){

        }

    }
}
