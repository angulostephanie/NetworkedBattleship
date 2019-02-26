import java.net.*;
import java.util.Arrays; 

public class Game{
	Player player1;
	Player player2;

	Game(Socket socket1, Socket socket2) {
		this.player1 = new Player(socket1);
		this.player2 = new Player(socket2);
	}

	void start() {
		String WELCOME_MSG = "Both players are here, let's start! Player 1's turn to add ships.";
		System.out.println(WELCOME_MSG);
		player1.out.println(WELCOME_MSG);
		player2.out.println(WELCOME_MSG);
		player1.addShip();
		if(player1.addShip()){
			player2.addShip();
		}
		
	}

}