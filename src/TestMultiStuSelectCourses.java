import dbms.TableReadWriteLock;
import network.StudentClient;

import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class TestMultiStuSelectCourses {
    //CyclicBarrier cyclicBarrier=new CyclicBarrier(200);
    Map<Integer,TestThread> scMap=new HashMap<>();
    static Random r=new Random();
    int[] lock=new int[0];

    private Set<Integer> getRamdomStudents(int count){
        Set<Integer> integers=new HashSet<>();

        while(integers.size()<200){
            integers.add(17000000+r.nextInt(1000000));
        }
        return integers;
    }


    public boolean probabilyGet(int percentOf){
        if(percentOf>=100) return true;
        int a=r.nextInt(100);
        if(a<=percentOf) return true;
        return false;

    }
    void doTest(){
        //创建200个客户端 (还未连接)
        for(Integer x:getRamdomStudents(200)){
            scMap.put(x,new TestThread(new StudentClient(x,"127.0.0.1",9239)));
        }
        for(int code:scMap.keySet()){
            scMap.get(code).start();
        }
    }

    public static void main(String args[]){
        TestMultiStuSelectCourses testMultiStuSelectCourses=new TestMultiStuSelectCourses();
        testMultiStuSelectCourses.doTest();
    }


    class TestThread extends Thread{
        StudentClient sc=null;
        public TestThread(StudentClient sc){
            this.sc=sc;
            super.setName(String.valueOf(sc.getStuCode()));
        }
        @Override
        public void run(){

            try {
                synchronized (lock){
                    sc.shakeHand();
                }
                System.out.println(this.getName()+" Run,shake hand get name="+sc.getName()); //握手
                //模拟选课
                while(true){
                    int couserCode=17001+r.nextInt(3);
                    int result;
                    synchronized (lock){
                        result=sc.selectCourse(couserCode);
                    }

                    if(result>0){
                        System.out.println(this.getName()+":"+sc.getName()+" Select course:"+couserCode+" success");

                    }else{
                        System.out.println(this.getName()+":"+sc.getName()+" Select course:"+couserCode+" fail");
                        if(probabilyGet(30)) break;  //选课失败百分之30概率退出
                    }
                    yield();
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

}
