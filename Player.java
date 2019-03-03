import java.lang.Math;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Player {
	
	Socket socket;
	PrintWriter out;
	BufferedReader in;

	Map<Integer, ArrayList<String>> shipToCoordinates;
	Map<String, Integer> coordinatesToShipType;

	int[][] board;

	int numAlive; 

	public Player(Socket socket) {
		try {
			this.socket = socket;
			this.out = new PrintWriter(socket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.board = initializeBoard();
			this.shipToCoordinates = initializeCoordinatesMap();
			this.coordinatesToShipType = new HashMap<String, Integer>();
			this.numAlive = 0;
			
		} catch (IOException io) {
			System.out.println(io);
		}
	}
	
	boolean takeTurn(Player opponent) {
		out.println(Constants.YOUR_TURN_MSG);
		out.println(Constants.FORMAT_MSG_3);

		String input = null;
		List<Integer> nums = new ArrayList<Integer>();

		int x = -2;
		int y = -2;

		try {
			while(true) {
				if((input = in.readLine()) != null) {
					nums = extractInput(input, 2);
					x = nums.get(0);
					y = nums.get(1);

					if(isInputValid(x, y, opponent))
						break;
					// TODO: add option for player to view their board at their leisure? i.e. when input = "b"
				}
			}
		} catch(IOException io) {
			System.out.println(io);
		}

		if(opponent.isAvailable(x, y)) {
			out.println(Constants.MISSED_SHOT_MSG);
			opponent.updateBoard(x, y, -1);
			opponent.out.println(Constants.MISSED_SHOT_UPDATE_MSG);
			opponent.out.println(Constants.DISPLAY_BOARD_MSG);
			opponent.printBoardUpdate(x,y);
			opponent.out.println();
		}
		
		if(opponent.containsShip(x, y)) {
			out.println(Constants.HIT_SHOT_MSG);

			opponent.updateBoard(x, y, 2);
			opponent.out.println(Constants.HIT_SHOT_UPDATE_MSG);
			opponent.out.println(Constants.DISPLAY_BOARD_MSG);
			opponent.printBoardUpdate(x, y);
			opponent.out.println("Before " + opponent.numAlive);
			opponent.numAlive--;
			opponent.out.println("You now have " + opponent.numAlive + " coordinates alive.");

			String xy = x + "," + y;
			int shipType = opponent.coordinatesToShipType.get(x + "," + y);
			ArrayList<String> coordinatesList = opponent.shipToCoordinates.get(shipType);

			// Remove coordinates from both of opponent's maps
			//  - Remove entire key-value pair
			opponent.coordinatesToShipType.remove(xy); 

			// - Update value (coordinate list)
			coordinatesList.remove(xy);
			opponent.shipToCoordinates.put(shipType, coordinatesList); 
			
			if(coordinatesList.isEmpty()) {
				opponent.out.println(Constants.SHIP_DEAD_MSG_1(shipType));
				out.println(Constants.SHIP_DEAD_MSG_2(shipType));
			}

			if(opponent.lost()) {
				// GAME OVER
				return true;
			}
		}
		return false;
	}

	boolean addShip() {
		out.println(Constants.FORMAT_MSG_1);
		out.println(Constants.FORMAT_MSG_2);
		
		String input = null;
		try {
			while(true) {
				if((input = in.readLine()) != null){
					break;
				}
			}
		} catch(IOException io) {
			System.out.println(io);
		}


		List<Integer> nums = extractInput(input, 4);

		if(nums.size() != 4) {
			out.println(Constants.ERR_MSG_SIZE(4));
			out.println();
			return false;
		} 

		int x0 = nums.get(0);
		int x1 = nums.get(2);
		int y0 = nums.get(1);
		int y1 = nums.get(3);
		boolean isHorizontal = x0 == x1;
		boolean isVertical = y0 == y1;

		if(!withinBounds(x0, y0) && !withinBounds(x1, y1)) {
			out.println(Constants.ERR_MSG_BOUNDS);
			out.println();
			return false;
		} else if(!isHorizontal && !isVertical) {
			out.println(Constants.ERR_MSG_ORIENTATION);
			out.println();
			return false;
		} 

		int distance = isHorizontal ? Math.abs(y0 - y1) : Math.abs(x0 - x1);
		
		if(shipToCoordinates.containsKey(distance)) {
			if(!shipToCoordinates.get(distance).isEmpty()) {
				out.println(Constants.ERR_MSG_ALREADY_USED(distance + 1));
				out.println();
				return false;
			} 
		} else {
			out.println(Constants.ERR_MSG_LENGTH);
			out.println();
			return false;
		}


		int small = isHorizontal ? (y0 < y1 ? y0 : y1) : (x0 < x1 ? x0 : x1);
		int big = isHorizontal ? (y0 < y1 ? y1 : y0) : (x0 < x1 ? x1 : x0);
		int c = isHorizontal ? x0 : y0;

		// check if this range is unoccupied first.
		for(int i = small; i <= big; i++) {
			if(isHorizontal && !isAvailable(c, i)) {
				out.println(Constants.ERR_MSG_UNAVAILABLE);
				out.println();
				return false;
			} else if(isVertical && !isAvailable(i, c)) {
				out.println(Constants.ERR_MSG_UNAVAILABLE);
				out.println();
				return false;
			}
		}

		ArrayList<String> coordinates = new ArrayList<>();
		for(int i = small; i <= big; i++) {
			if(isHorizontal && isAvailable(c, i)) {
				this.board[c][i] = Constants.SHIP_POS;
				this.numAlive++;
				String xy = c + "," + i;
				coordinates.add(xy);
				coordinatesToShipType.put(xy, distance);
			} else if(isVertical && isAvailable(i, c)) {
				this.board[i][c] = Constants.SHIP_POS;
				this.numAlive++;
				String xy = i + "," + c;
				coordinates.add(xy);
				coordinatesToShipType.put(xy, distance);
			}
		} 
		
		shipToCoordinates.put(distance, coordinates);
		out.println(Constants.ADD_SHIP_MSG(distance + 1));
		return true;
	}


	// Helper functions

	int[][] initializeBoard() {
		int[][] emptyBoard = new int[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
		for (int[] row : emptyBoard) 
            Arrays.fill(row, Constants.EMPTY_POS); 
        return emptyBoard;
	}

	Map<Integer, ArrayList<String>> initializeCoordinatesMap() {
		Map<Integer, ArrayList<String>> emptyMap = new HashMap<Integer, ArrayList<String>>() {{
	        put(Constants.SMALL_SHIP, new ArrayList<String>());
	        put(Constants.AVG_SHIP, new ArrayList<String>());
	        put(Constants.BIG_SHIP,new ArrayList<String>());
	    }};
	    return emptyMap;
	}

	void printBoard() {
		for(int[] row : this.board) {
			out.println(Arrays.toString(row));
		}
	}

	void printBoardUpdate(int x, int y) {
		int rowCount = 0;
		for(int[] row : this.board) {
			String[] temp = Arrays.toString(row).split("[\\[\\]]")[1].split(", ");

			if(rowCount == x)
				temp[y] = "(" + temp[y] + ")";
			else 
				temp[y] = " " + temp[y] + " ";
			
			out.println(Arrays.toString(temp));
			rowCount++;
		}
	}

	void updateBoard(int x, int y, int value) {
		this.board[x][y] = value;
	}

	List<Integer> extractInput(String input, int size) {
		List<String> list = Arrays.asList(input.split(","));
		List<Integer> nums = list.stream().map(Integer::parseInt).collect(Collectors.toList());

		if(nums.size() != size) {
			out.println(Constants.ERR_MSG_SIZE(size));
			out.println();
		} 

		return nums;

	}

	boolean isInputValid(int x, int y, Player opponent){
		if(!withinBounds(x, y)) {
			out.println(Constants.ERR_MSG_BOUNDS);
			out.println();
			return false;
		} else if(opponent.shotAlready(x, y)){
			out.println(Constants.ERR_MSG_ALREADY_SHOT);
			out.println();
			return false;
		} 
		return true;
	}

	boolean isAvailable(int x, int y) {
		return board[x][y] == 0;
	}

	boolean withinBounds(int x, int y) {
		return x >= 0 && x < Constants.BOARD_SIZE && y >=0 && y < Constants.BOARD_SIZE;
	}

	boolean shotAlready(int x, int y){
		return this.board[x][y] == -1 || this.board[x][y] == 2;
	}

	boolean containsShip(int x, int y){
		return this.board[x][y] == 1;
	}

	boolean lost() {
		return this.numAlive == 0;
	}
}