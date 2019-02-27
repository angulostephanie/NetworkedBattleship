public class Constants {

	static int BOARD_SIZE = 10;
	static int SMALL_SHIP = 1;
	static int AVG_SHIP = 2;
	static int BIG_SHIP = 3;

	// Descriptive messages 
	static String WELCOME_MSG = "Both players are here, let's battle!";
	static String FORMAT_MSG_1 = "Format to place a ship on the board is: ";
	static String FORMAT_MSG_2 = "x0, y0, x1, y1";
	
	static String FINISHED_ADD_MSG = "You have finished adding your ships!";

	// Error Messages
	static String ERR_MSG_SIZE = "Please enter 4 numbers.";
	static String ERR_MSG_BOUNDS = "The entered coordinates are out of bounds, try again.";
	static String ERR_MSG_ORIENTATION = "Ships must be either horizontal or vertical, try again.";
	static String ERR_MSG_LENGTH = "Ship must be within 2 - 4 units long, try again";
	static String ERR_MSG_UNAVAILABLE = "There is already a ship in this range, try again.";


	static String ERR_MSG_ALREADY_USED(int distance) {
		return "The " + distance + " unit sized ship has already been used, try again.";
	}
}

