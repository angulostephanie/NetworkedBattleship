import java.io.IOException;
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
		String WELCOME_MSG = "Both players are here, let's start!";
		System.out.println(WELCOME_MSG);
		player1.out.println(WELCOME_MSG);
		player2.out.println(WELCOME_MSG);
		setUpShips(player1, player2);
	}
	
	static void setUpShips(Player player1, Player player2){
		try{
			AddShipThread t1 = new AddShipThread(player1);
			AddShipThread t2 = new AddShipThread(player2);
			t1.start();
			t2.start();
			t1.join();
			t2.join();
		}  catch(InterruptedException ie){
			System.out.println(ie);
		}
	}
}

class AddShipThread extends Thread{
	Player player;
	int MAX_NUM_SHIPS = 4;

	public AddShipThread(Player player){
		this.player = player;
	}

	public void run(){
		int count = 0;
		while(count < MAX_NUM_SHIPS){
			if(player.addShip())
				count++;
		}
		player.out.println("You have finished adding your ships");
	}
}