import java.lang.Math;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Player {

	public static int BOARD_SIZE = 10;
	
	int[][] board;
	int[][] otherBoard;
	int numAlive;
	Socket socket;
	PrintWriter out;
	BufferedReader in;


	// for testing purposes aka Tester.java
	public Player() {
		this.board = initializeBoard();
		this.otherBoard = initializeBoard();
		this.numAlive = 0;
	}

	public Player(Socket socket) {
		try{
		this.socket = socket;
		this.out = new PrintWriter(socket.getOutputStream(), true);
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.board = initializeBoard();
		this.otherBoard = initializeBoard();
		this.numAlive = 0;
		} catch (IOException io){
			System.out.println(io);
		}
	}
	
	int[][] initializeBoard() {
		int[][] emptyBoard = new int[BOARD_SIZE][BOARD_SIZE];
		for (int[] row : emptyBoard) 
            Arrays.fill(row, 0); 
        return emptyBoard;
	}

	boolean addShip() {
		System.out.println("Format to place a ship on the board is: ");
		System.out.println("x0, y0, x1, y1");
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();
		List<String> list = Arrays.asList(input.split(","));
		List<Integer> nums = list.stream().map(Integer::parseInt).collect(Collectors.toList());

		if(nums.size() != 4) {
			System.out.println("please enter 4 numbers");
			System.out.println();
			return false;
		} 

		int x0 = nums.get(0);
		int x1 = nums.get(2);
		int y0 = nums.get(1);
		int y1 = nums.get(3);
		boolean isHorizontal = x0 == x1;
		boolean isVertical = y0 == y1;

		if(!withinBounds(x0, y0) && !withinBounds(x1, y1)) {
			System.out.println("coordinates out of bounds, try again");
			System.out.println();
			return false;
		} else if(!isHorizontal && !isVertical) {
			System.out.println("ships must be horizontal or vertical, try again");
			System.out.println();
			return false;
		} 

		if(isHorizontal) {
			int distance = Math.abs(y0 - y1);
			if(distance >= 2 && distance < 4) {
				int small = y0 < y1 ? y0 : y1;
				int big = y0 < y1 ? y1 : y0;
				for(int i = small; i <= big; i++) {
					if(!isAvailable(x0, i)) {
						System.out.println("already have a ship in this range, try again");
						System.out.println();
						return false;
					}
				}

				for(int i = small; i <= big; i++) {
					if(isAvailable(x0, i)) {
						this.board[x0][i] = 1;
					}
				}
			}  
		} else {
			int distance = Math.abs(x0 - x1);
			System.out.println("distance " + distance);
			if(distance >= 2 && distance < 4) {
				int small = x0 < x1 ? x0 : x1;
				int big = x0 < x1 ? x1 : x0;

				for(int i = small; i <= big; i++) {
					if(!isAvailable(i, y0)) {
						System.out.println("already have a ship in this range, try again");
						System.out.println();
						return false;
					}
				}

				for(int i = small; i <= big; i++) {
					if(isAvailable(i, y0)) {
						this.board[i][y0] = 1;
					}
				}
			}  
		}

		System.out.println(Arrays.toString(nums.toArray()));
		return true;
	}


	boolean isAvailable(int x, int y) {
		return board[x][y] == 0;
	}

	/*boolean isSpaceAvailable() {

	}*/

	boolean withinBounds(int x, int y) {
		return x >= 0 && x < BOARD_SIZE && y >=0 && y < BOARD_SIZE;
	}

	void printBoard() {
		System.out.println(Arrays.deepToString(board));
	}
}