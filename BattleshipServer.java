import java.net.*;
import java.io.*;
import java.util.*;

public class BattleshipServer {
	public static void main(String[] args) throws Exception {
        Map<String, ArrayList<Socket>> rooms = new HashMap<>();

        if (args.length != 1) {
            System.err.println("Usage: java BattleshipServer <port number>");
            System.exit(1);
        }
        
        int portNumber = Integer.parseInt(args[0]);
        
        try (
            ServerSocket serverSocket =
                new ServerSocket(portNumber); 
        ) {
            System.out.println("Starting");
            while(true) { 
                Socket client = serverSocket.accept(); 
                System.out.println("A player has joined! INFO: " + client);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                String room = in.readLine();
                System.out.println("Client [" + client + "] asked to be in channel [" + room + "]");
                
                ArrayList<Socket> players;
                if(rooms.containsKey(room)) {
                    if(rooms.get(room).size() == 2) {
                        System.out.println("This room is already full, try again.");
                        out.println("This room is already full, try again.");
                        boolean validRoom = false;
                        while(!validRoom) {
                            room = in.readLine();
                            if(!rooms.containsKey(room)) {
                                players = new ArrayList<>();
                                rooms.put(room, players);
                                break;
                            } else if(rooms.get(room).size() < 2) {
                                break;
                            } else {
                                System.out.println("This room is also already full, try again.");
                                out.println("This room is also already full, try again.");
                            }
                        }
                    }
                    players = rooms.get(room);
                } else {
                    players = new ArrayList<>();
                    rooms.put(room, players);
                }

                players.add(client);
                rooms.put(room, players);
                if(players.size() == 2) {
                    Game game = new Game(players.get(0), players.get(1));
                    game.start();
                } else {
                     out.println("Entering chatroom #" + room + ", now we wait for the opponent!");
                }

            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
    
}