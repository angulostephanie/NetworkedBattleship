import java.io.IOException;
import java.net.*;
import java.util.Arrays; 

public class Game {
	Player player1;
	Player player2;

	Game(Socket socket1, Socket socket2) {
		this.player1 = new Player(socket1);
		this.player2 = new Player(socket2);
	}

	void start() {
		player1.out.println(Constants.WELCOME_MSG);
		player2.out.println(Constants.WELCOME_MSG);

		setUpShips(player1, player2);

		boolean gameOver = player1.lost() || player1.lost();

		while(!gameOver) {
			player1.takeTurn(player2);
			if(gameOver) break; // add logic who won after while loop
			player2.takeTurn(player1);
		}

	}
	
	static void setUpShips(Player player1, Player player2){
		try {
			SetupBoard t1 = new SetupBoard(player1);
			SetupBoard t2 = new SetupBoard(player2);
			t1.start();
			t2.start();
			t1.join();
			t2.join();
		}  catch(InterruptedException ie){
			System.out.println(ie);
		}
	}
}

class SetupBoard extends Thread {
	Player player;

	public SetupBoard(Player player){
		this.player = player;
	}

	public void run(){
		int count = 0;
		while(count < Constants.MAX_NUM_SHIPS){
			if(player.addShip())
				count++;
		}
		player.out.println(Constants.FINISHED_ADD_MSG);
		player.out.println(Constants.DIPLAY_BOARD_MSG);
		player.printBoard();
	}
}