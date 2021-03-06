import java.net.*;


import org.w3c.dom.ls.LSException;

import java.io.*;

public class BattleshipClient {
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
            System.err.println(
                "Usage: java BattleshipClient <host name> <port number> ");
            System.exit(1);
        }
		
		String hostName = args[0]; 
        int portNumber = Integer.parseInt(args[1]);

		try {
            String userInput;
            Socket socket = new Socket(hostName, portNumber); 
            PrintWriter out =
                    new PrintWriter(socket.getOutputStream(), true); 
                BufferedReader stdIn =
                    new BufferedReader(
                        new InputStreamReader(System.in)); 
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ServerListener listener = new ServerListener(socket);
            listener.start();
            System.out.println("Welcome to a game of Battleship!");
            System.out.println("Please enter a chatroom to be a part of!");

            while((userInput = stdIn.readLine()) != null){
                out.println(userInput);
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } 
    }
    
    static class ServerListener extends Thread{
        Socket socket;

        public ServerListener(Socket socket){
            this.socket = socket;
        }
        
        @Override
        public void run(){
            try{
                PrintWriter out =
                    new PrintWriter(socket.getOutputStream(), true); 
                BufferedReader stdIn =
                    new BufferedReader(
                        new InputStreamReader(System.in)); 
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                String serverInput;
                while((serverInput = in.readLine()) != null){
                    System.out.println(serverInput);
                }
    
                
            } catch(IOException e){
                System.out.println(e);
                System.exit(1);
            }
            
        }
    }
}