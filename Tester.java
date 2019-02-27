public class Tester {
	public static void main(String[] args) {
		Player test = new Player();
		test.printBoard();
		System.out.println();
		System.out.println();
		int count = 0;
		while(count < 3) {
			boolean canAddShip = test.addShip();
			test.printBoard();
			if(canAddShip) {
				count++;
			}
		}
		//test.addShip();
		System.out.println();
		test.printBoard();
	}
}