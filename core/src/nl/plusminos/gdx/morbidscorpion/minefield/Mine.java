package nl.plusminos.gdx.morbidscorpion.minefield;

public class Mine {
	
	// FORMAT:
	// First bit: whether it has a mine
	// Second bit: whether it has a flag
	// Third bit: whether is has been uncovered
	// Fourth, fifth, sixth and seventh bit: amount of mines around this mine
	private byte settings = 0;
	private int x, y;

	public Mine(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Mine(int x, int y, boolean mine, boolean flagged, boolean uncovered, byte surrounding) {
		if (surrounding > 8) {
			throw new NullPointerException("Maximum amount of mines around a position is 8");
		}
		
		this.x = x;
		this.y = y;
		
		if (mine) setMine();
		if (flagged) setFlagged();
		if (uncovered) setUncovered();
		setSurrounding(surrounding);
	}
	
	public void reset() {
		settings = 0;
	}
	
	public void setMine() {
		settings = (byte) (settings | 1);
	}
	
	public void unsetMine() {
		settings = (byte) (settings & 254);
	}
	
	public void setFlagged() {
		settings = (byte) (settings | 2);
	}
	
	public void unsetFlagged() {
		settings = (byte) (settings & 253);
	}
	
	public void setUncovered() {
		settings = (byte) (settings | 4);
	}
	
	public void unsetUncovered() {
		settings = (byte) (settings & 251);
	}
	
	public void setSurrounding(byte n) {
		if (n > 8) {
			throw new NullPointerException("Maximum amount of mines around a position is 8");
		}
		
		settings = (byte) ((settings & 135) | (n << 3));
	}
	
	public boolean hasMine() {
		return (settings & 1) == 1;
	}
	
	public boolean hasFlag() {
		return (settings & 2) == 2;
	}
	
	public boolean hasNoCover() {
		return (settings & 4) == 4;
	}
	
	public byte getSurrounding() {
		return (byte) (settings >> 3);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	/**
	 * (Almost!) useless test routine
	 */
	public static void main(String[] args) {
		Mine m = new Mine(0, 0);
		m.setMine();
		Mine.testAssert(m.hasMine(), "Mine", "True", ""+m.hasMine());
		
		m.unsetMine();
		Mine.testAssert(!m.hasMine(), "Unmine", "False", ""+m.hasMine());
		
		m.setFlagged();
		Mine.testAssert(m.hasFlag(), "Flag", "True", ""+m.hasFlag());
		
		m.unsetFlagged();
		Mine.testAssert(!m.hasFlag(), "Unflag", "False", ""+m.hasFlag());
		
		m.setSurrounding((byte) 5);
		m.setFlagged();
		m.setUncovered();
		Mine.testAssert(m.getSurrounding() == 5 && m.hasFlag() && m.hasNoCover() && !m.hasMine(), "Usecase", "True", "False");
		
	}
	
	public static void testAssert(boolean assessment, String test, String expected, String returned) {
		System.out.println("Test \"" + test + "\": " + (assessment ? "SUCCESS" : "FAILURE"));
		if (!assessment) {
			System.out.println("Returned: " + returned);
			System.out.println("Expected: " + expected);
		}
	}
}
