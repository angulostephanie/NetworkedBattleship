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
			
		} catch (IOException io){
			System.out.println(io);
		}
	}
	
	void takeTurn(Player opponent) {
		out.println(Constants.YOUR_TURN_MSG);
		out.println(Constants.FORMAT_MSG_3);

		String input = null;
		try {
			while(true) {
				if((input = in.readLine()) != null) {
					break;
				}
			}
		} catch(IOException io) {
			System.out.println(io);
		}

		List<String> list = Arrays.asList(input.split(","));
		List<Integer> nums = list.stream().map(Integer::parseInt).collect(Collectors.toList());

		if(nums.size() != 2) {
			out.println(Constants.ERR_MSG_SIZE(2));
			out.println();
		} 

		int x = nums.get(0);
		int y = nums.get(1);

		if(!withinBounds(x, y)) {
			out.println(Constants.ERR_MSG_BOUNDS);
			out.println();
		}
		// check if this player has already shot at this location
		// if player hasn't, check if value is 1 or 0.
		// update the other player's board accordingly
		// update other player's numAlive and shipToCoordinates map
		// probably dont need numAlive anymore, now that i'm thinking about it
		// logic
	}

	void updateBoard(int x, int y, int value) {
		this.board[x][y] = value;
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


		List<String> list = Arrays.asList(input.split(","));
		List<Integer> nums = list.stream().map(Integer::parseInt).collect(Collectors.toList());

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
				coordinates.add(c + "," + i);
			} else if(isVertical && isAvailable(i, c)) {
				this.board[i][c] = Constants.SHIP_POS;
				this.numAlive++;
				coordinates.add(i + "," + c);
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

	boolean isAvailable(int x, int y) {
		return board[x][y] == 0;
	}

	boolean withinBounds(int x, int y) {
		return x >= 0 && x < Constants.BOARD_SIZE && y >=0 && y < Constants.BOARD_SIZE;
	}

	boolean lost() {
		return this.numAlive == 0;
	}
}