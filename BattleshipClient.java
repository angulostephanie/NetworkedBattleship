import java.net.*;
import java.io.*;

public class BattleshipClient {
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
            System.err.println(
                "Usage: java BattleshipClient <host name> ");
            System.exit(1);
        }
		
		String hostName = args[0]; 

		try (
            Socket client = new Socket(hostName, 12345); 
            PrintWriter out =
                new PrintWriter(client.getOutputStream(), true); 
            BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in)) 
        ) {
        	String userInput;
        	System.out.println("this player can do stuff now");
        	System.out.println("gotta add methods to game class");
            while ((userInput = stdIn.readLine()) != null) { 
		 		System.out.println("~echo~ " + userInput);
		 	}

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } 
	}
}