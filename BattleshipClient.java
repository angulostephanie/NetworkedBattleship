import java.net.*;

import org.w3c.dom.ls.LSException;

import java.io.*;

public class BattleshipClient {
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
            System.err.println(
                "Usage: java BattleshipClient <host name> ");
            System.exit(1);
        }
		
		String hostName = args[0]; 

		try {
            Socket clientSocket = new Socket(hostName, 12345); 
            ClientThread client = new ClientThread(clientSocket);
            client.start();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } 
    }
    
    static class ClientThread extends Thread{
        Socket socket;
        PrintWriter out;
        BufferedReader in;

        public ClientThread(Socket socket){
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
        	    System.out.println("Welcome to a game of Battleship!");

                String incomingMsg;
                while ((incomingMsg = in.readLine()) != null) { 
		 		System.out.println(incomingMsg);
		 	}
            } catch(IOException e){
                System.out.println(e);
            }
            
        }
    }
}