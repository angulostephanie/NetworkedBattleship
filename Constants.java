public class Constants {

	static int BOARD_SIZE = 10;
	static int MAX_NUM_SHIPS = 3;
	static int SMALL_SHIP = 1;
	static int AVG_SHIP = 2;
	static int BIG_SHIP = 3;

	static int EMPTY_POS = 0;
	static int SHIP_POS = 1;
	static int MISSED_POS = -1;
	static int HIT_POS = 2;


	// Descriptive messages 
	static String WELCOME_MSG = "Both players are here, let's battle!";
	static String FORMAT_MSG_1 = "Format to place a ship on the board is: ";
	static String FORMAT_MSG_2 = "x0, y0, x1, y1";
	static String FORMAT_MSG_3 = "x, y";
	static String DISPLAY_BOARD_MSG = "This is what your board looks like: ";
	static String FINISHED_ADD_MSG = "You have finished adding your ships! Waiting for the other player to finish..";
	static String YOUR_TURN_MSG = "Your turn! Enter in x and y coordinates to shoot down your opponent's ship! Format: ";
	static String MISSED_SHOT_MSG = "Oops! You missed.";
	static String MISSED_SHOT_UPDATE_MSG = "Phew! Your opponent shot and missed.";
	static String HIT_SHOT_MSG = "You hit a ship!";
	static String HIT_SHOT_UPDATE_MSG = "Agh! You've been hit by your opponent.";
	static String WINNER_MSG = "Congrats, you won!! :)";
	static String GAME_OVER_MSG = " --------------- \n|   GAME OVER   | \n --------------- ";

	static String ADD_SHIP_MSG(int size) {
		return "Just added a ship of unit size [" + (size) + "].";
	}

	static String SHIP_DEAD_MSG_1(int size) {
		return "Oh no! Your ship of unit size [" + (size) + "] has sunken. :(";
	}

	static String SHIP_DEAD_MSG_2(int size) {
		return "Congrats! You have sunken your opponent's ship of unit size [" + (size) + "].";
	}

	static String OTHER_PLAYER_TURN_MSG(int playerNum){
		return "It is Player " + (playerNum) + "'s turn. Please wait...";
	}

	static String LOSER_MSG(boolean player1) {
		String player = " Player 2 ";
		if(player1) {
			player = " Player 1 "; 
		}
		return "Sorry YOU lost," + player + " won! Better luck next time. :/";
	}

	// Error Messages
	static String ERR_MSG_BOUNDS = "The entered coordinates are out of bounds, try again.";
	static String ERR_MSG_ORIENTATION = "Ships must be either horizontal or vertical, try again.";
	static String ERR_MSG_LENGTH = "Ship must be within 2 - 4 units long, try again";
	static String ERR_MSG_UNAVAILABLE = "There is already a ship in this range, try again.";
	static String ERR_MSG_ALREADY_SHOT = "You've already shot here, try again.";
	static String ERR_MSG_INVALID = "Please enter in something of valid format.";

	static String ERR_MSG_SIZE(int size) {
		return "Please enter exactly " + size + " numbers.";
	}
	static String ERR_MSG_ALREADY_USED(int distance) {
		return "The " + distance + " unit sized ship has already been used, try again.";
	}
}

