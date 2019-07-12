package network;
import network.event.ISocketEvent;
import network.event.InSelectCourseServerMsgIn;
import network.event.StudentClientInEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public  class  SocketServer extends Thread{
	ServerSocket server = null;
	Socket socket = null;
	List<Socket> sockets=new ArrayList<>();
	ISocketEvent clientIn=null;
	ISocketEvent inServerMsgIn=null;
	public SocketServer (int port) {
		try {
			//设置事件
			server = new ServerSocket(port);
			//clientIn=new StudentClientInEvent();
			//inServerMsgIn=new InSelectCourseServerMsgIn();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run(){

		super.run();
		try{
			System.out.println("wait client connect...");
			socket = server.accept();
			if(clientIn!=null){
				clientIn.processEvent(this,socket);   //处理客户进入事件
			}
			sockets.add(socket);
			new ClientListener(socket).run();    //为客户端创建一个监听器

		}catch (IOException e){
			e.printStackTrace();
		}
	}

	
	class ClientListener extends Thread{
		InputStream inputStream;
		Socket s;
		public ClientListener(Socket s){
			try{
			inputStream=s.getInputStream();}
			catch (Exception e){
				e.printStackTrace();
			}
			this.s=s;
		}


		@Override
		public void run(){
			int len = 0;
			byte[] buf = new byte[1024];
			try{
			while ((len=inputStream.read(buf))!=-1){
				if(inServerMsgIn!=null){
					inServerMsgIn.processEvent(s,new String(buf,0,len));   //处理客户端消息进入事件
				}
			}
			}catch (Exception e){
				e.printStackTrace();
			}
		}

	}


	//函数入口
	public static void main(String[] args) {
		SocketServer  server = new SocketServer (1234);
		server.start();
	}
}
