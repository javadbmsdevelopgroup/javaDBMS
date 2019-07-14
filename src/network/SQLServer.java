package network;


import dbms.CacheManage;
import dbms.view.RelationView;

import java.util.Scanner;

public class SQLServer {
   public static void main(String[] args) {
       NwServer ss = new NwServer(2999, new ThreadSupport(new SQLProtocol()));
       System.out.println("正在缓存索引...");
       CacheManage.loadAllindex();


      System.out.println("input 1 start, input 2 end, input 3 end,input 4 put a table to cache");
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
          switch (x){
              case 1:
                  ss.startServer();
                  break;
              case 2:
                  ss.endServer();
                  break;
              case 4:
                  System.out.println("input \"databaseName.tableName\":");
                  String name="";
                  while (name.compareTo("")==0){
                      name= scanner.nextLine();
                  }

                  String[] str=name.split("\\.");
                  if(str.length!=2) break;
                  if(CacheManage.cacheWholeTable(str[0],str[1])){
                      System.out.println("缓存成功");
                  }else {
                      System.out.println("缓存失败");
                  }

              }

          if(x==3) break;

          }


      }


   }

