import java.net.*;
import java.util.Arrays; 

public class Game {
	Socket player1;
	Socket player2;

	Game(Socket player1, Socket player2) {
		this.player1 = player1;
		this.player2 = player2;
	}

	void start() {
		System.out.println("Both players are here, let's start!");
	}

}