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
		boolean player1Won = true;

		 do {
			try {
				broadcastMSG(player2, Constants.OTHER_PLAYER_TURN_MSG(1));
				gameOver = player1.takeTurn(player2);
				Thread.sleep(1000);
				if(gameOver) {
					player1Won = true;
					player1.out.println(Constants.WINNER_MSG);
					player1.out.println(Constants.GAME_OVER_MSG);
					player2.out.println(Constants.LOSER_MSG(player1Won));
					player2.out.println(Constants.GAME_OVER_MSG);
					break; 
				}
				broadcastMSG(player1, Constants.OTHER_PLAYER_TURN_MSG(2));
				gameOver = player2.takeTurn(player1);
				Thread.sleep(1000);
			} catch(InterruptedException ie){
				System.out.println(ie);
			}
		} while(!gameOver);

		if(!player1Won) {
			player2.out.println(Constants.WINNER_MSG);
			player2.out.println(Constants.GAME_OVER_MSG);
			player1.out.println(Constants.LOSER_MSG(player1Won));
			player1.out.println(Constants.GAME_OVER_MSG);
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