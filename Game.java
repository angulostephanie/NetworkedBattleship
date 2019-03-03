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

		boolean gameOver = false;

		 do {
			try {
				broadcastMSG(player2, Constants.OTHER_PLAYER_TURN_MSG(1));
				gameOver = player1.takeTurn(player2);
				Thread.sleep(1000);
				if(gameOver) {
					// all done
					System.out.println();
					System.out.println("--------------");
					System.out.println("GAME OVER");
					System.out.println("--------------");
					System.out.println();
					// only prints to server
					// add messaging to players
					break; 
				}
				broadcastMSG(player1, Constants.OTHER_PLAYER_TURN_MSG(2));
				gameOver = player2.takeTurn(player1);
				Thread.sleep(1000);
			} catch(InterruptedException ie){
				System.out.println(ie);
			}
		} while(!gameOver);

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

	void broadcastMSG(Player msgReceiver, String message){
		msgReceiver.out.println(message);
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
		player.out.println(Constants.DISPLAY_BOARD_MSG);
		player.printBoard();
	}
}