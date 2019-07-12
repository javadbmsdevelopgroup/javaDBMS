package network;

import automaton.AutomatonBuilder;
import automaton.AutomatonNode;
import automaton.SQLAutomaton;
import automaton.SQLSession;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SQLProtocol implements IOStrategy{
    SQLAutomaton automaton;
    @Override
                         //client socket
    public void service(Socket socket) {
        try{
            ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream dis = new ObjectInputStream(socket.getInputStream());
            int command = 0;
            while (true){
                command = dis.readInt();
                switch (command){
                    case 0:
                        System.out.println("the connection is ending!");
                        try {
                            dos.close();
                            dis.close();
                            NwServer.sockets.remove(socket);
                            socket.close();
                            return;
                        }catch (Exception e){
                            System.out.println("end failed");
                        }
                        break;
                    case 1:
                        String sqlCommand = (String)(dis.readObject());
                        System.out.println("get a command:"+sqlCommand);
                        Object obj= dis.readObject();
                        SQLSession sqlSession=null;
                        if(!(obj instanceof SQLSession)){
                            break;
                        }else{
                            sqlSession=(SQLSession)obj;
                        }

                        // to find the result;
                        //String result = "I am what you want, my baby";
                        automaton=new SQLAutomaton(AutomatonBuilder.buildAutomaton(),sqlSession);
                        AutomatonNode automatonNode=(automaton.matchingGrammar(sqlCommand));
                        //System.out.println(automatonNode);
                        if(automatonNode!=null){
                            Object result=automatonNode.exeResult;
                            dos.writeObject(sqlSession);
                            System.out.println(sqlSession.curUseDatabase);
                            dos.flush();
                            dos.writeObject(result);
                            System.out.println(result);
                            dos.flush();
                            System.out.println(sqlSession.curUseDatabase);
                        }else{
                            dos.writeObject(sqlSession);
                            dos.flush();
                            dos.writeObject(null);
                            dos.flush();
                        }

                        //
                        //



                }
            }

        }catch (Exception e){
            if(!(e.getMessage().compareTo("Connection reset")==0)){
                e.printStackTrace();
            }else{
                System.out.println("client "+socket.getRemoteSocketAddress()+"is disconnected");
            }


            try {
                socket.close();
            }catch (Exception ee){
                ee.printStackTrace();
            }
        }
    }
}
