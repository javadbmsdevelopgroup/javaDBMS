# javaDBMS
javaDBMS and StudentManagerSystem

A simple java Databases Management System and Student course selection system. DBMS support simple SQL sentences and index、cache...

Use:

1.SQLSession.java
It's a SQL command line,which can control DBMS directly.

2.SQLServer.java
Database Server,which can receive sql sentences and return a result.

3.SelectCourseServer.java
SelectCourseServer,which reveive commands from student client,and then convert these commands to SQL commands and send to sqlserver,then get a result and send to student.

4.StudentClient.java
A student client with command line UI.
