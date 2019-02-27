import java.lang.Math;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Player {
	
	int[][] board;
	// 0 =  empty/nothing
	// 1 = ship is present and alive
	// -1 = other player hit ship
	// 2 the other player shot and missed

	int numAlive; 
	// needs to update when other player hits ship 

	Map<Integer, Integer> ships = new HashMap<Integer, Integer>() {{
        put(Constants.SMALL_SHIP,0);
        put(Constants.AVG_SHIP,0);
        put(Constants.BIG_SHIP,0);
    }};

	Socket socket;
	PrintWriter out;
	BufferedReader in;

	// for testing purposes aka Tester.java
	public Player() {
		this.board = initializeBoard();
		this.numAlive = 0;
	}

	public Player(Socket socket) {
		try {
			this.socket = socket;
			this.out = new PrintWriter(socket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.board = initializeBoard();
			this.numAlive = 0;
			
		} catch (IOException io){
			System.out.println(io);
		}
	}
	
	void takeTurn() {
		// logic
	}

	boolean addShip() {
		out.println(Constants.FORMAT_MSG_1);
		out.println(Constants.FORMAT_MSG_2);
		
		String input = null;
		try {
			while(true){
				if((input = in.readLine()) != null){
					break;
				}
			}
		} catch(IOException io){
			System.out.println(io);
		}

		/*
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();
		*/

		List<String> list = Arrays.asList(input.split(","));
		List<Integer> nums = list.stream().map(Integer::parseInt).collect(Collectors.toList());

		if(nums.size() != 4) {
			out.println(Constants.ERR_MSG_SIZE);
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
		
		if(ships.containsKey(distance)) {
			if(ships.get(distance) == 1) {
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

		for(int i = small; i <= big; i++) {
			if(isHorizontal && isAvailable(c, i)) {
				this.board[c][i] = 1;
				this.numAlive++;
			} else if(isVertical && isAvailable(i, c)) {
				this.board[i][c] = 1;
				this.numAlive++;
			}
		} 

		ships.put(distance, 1);
		out.println(Constants.ADD_SHIP_MSG(distance + 1));
		return true;
	}


	// Helper functions

	int[][] initializeBoard() {
		int[][] emptyBoard = new int[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
		for (int[] row : emptyBoard) 
            Arrays.fill(row, 0); 
        return emptyBoard;
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

	boolean hasAnAliveShip() {
		return this.numsAlive > 0;
	}
}