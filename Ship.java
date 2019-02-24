class Ship {
	int[] points;
	int size;
	boolean alive;
	Ship(int size) {
		this.size = size;
		this.points = new int[size];
		this.alive = true;
	}


	boolean isDead() {
		for(int point: points) {
			return !(point == 1);
		}
		alive = false;
		return true;
	}

}