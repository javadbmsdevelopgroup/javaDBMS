package network;


import dbms.CacheManage;

import java.util.Scanner;

public class SQLServer {
   public static void main(String[] args) {
      NwServer ss = new NwServer(2999, new ThreadSupport(new SQLProtocol()));
      System.out.println("正在缓存索引...");
      CacheManage.loadAllindex();

      System.out.println("input 1 start, input 2 end, input 3 all-end");
      Scanner scanner = new Scanner(System.in);
      int x = 0;
      while (true){
          System.out.println("input:");
          try{
              x = scanner.nextInt();
          }
          catch (Exception e){
              scanner.nextLine();
              continue;
          }
          if(x == 1)
              ss.startServer();
          else if (x == 2)
              ss.endServer();
          else
              break;
      }


   }
}
