import java.net.*;
import java.io.*;
import java.util.*;

public class BattleshipServer {
	public static void main(String[] args) throws Exception {
        ArrayList<Socket> players = new ArrayList<>();
        
        try (
            ServerSocket serverSocket =
                new ServerSocket(12345); 
        ) {
            System.out.println("starting");
            while(true) { 
                Socket client = serverSocket.accept(); 
                players.add(client); 
                System.out.println("A player has joined! INFO: " + client);
                if(players.size() == 2) {
                    Game game = new Game(players.get(0), players.get(1));
                    game.start();
                    players.clear();
                }

            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port 12345 or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
    
}