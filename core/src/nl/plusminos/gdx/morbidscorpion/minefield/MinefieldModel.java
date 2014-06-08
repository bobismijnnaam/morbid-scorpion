package nl.plusminos.gdx.morbidscorpion.minefield;

import nl.plusminos.gdx.morbidscorpion.utils.GridDirection;

import com.badlogic.gdx.utils.Array;

public class MinefieldModel {
	Cell[][] cells;
	Array<Cell> safeCells;
	private boolean gameOver = false;
	int fieldWidth, fieldHeight;
	
	public MinefieldModel(int width, int height) {
		cells = new Cell[width][height];
		safeCells = new Array<Cell>(width * height);
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				cells[x][y] = new Cell(x, y);
				safeCells.add(cells[x][y]);
			}
		}
		
		fieldWidth = width;
		fieldHeight = height;
	}
	
	public void initialize(int amountOfMines, int startX, int startY) {
		Cell m, n;
		
		// Remove start position from possible places
		Cell startMine = new Cell(startX, startY);
		safeCells.removeValue(startMine, false); // false means use .equals for comparison
		
		// Place a mine at a random position and increase te minecounter of surrounding mines
		for (int i = 0; i < amountOfMines; i++) {
			int pickedCellIndex = (int) (Math.random() * safeCells.size);
			m = safeCells.removeIndex(pickedCellIndex);
			m.setMine();
			
			for (GridDirection dir : GridDirection.values()) {
				n = getNeighbour(m.getX(), m.getY(), dir);
				if (n != null) {
					n.setSurrounding((byte) (n.getSurrounding() + 1));
				}
			}
		}
		
		// Disable safeplaces array
		safeCells = null;
	}
	
	public void toggleFlag(int x, int y) {
		cells[x][y].toggleFlag();
	}
	
	public void uncover(int x, int y, boolean allowSectorUncover) {
		Cell c = cells[x][y];
		
		if (c.hasMine()) {
			setGameOver();
			return;
		} else if (c.hasFlag()) {
			return;
		} else if (c.hasNoCover() && c.getSurrounding() > 0 && allowSectorUncover) {
			uncoverSector(c);
		} else if (c.hasNoCover()) {
			return;
		} else {
			doDijkstra(c);
		}
	}
	
	private void uncoverSector(Cell c) {
		int amountOfFlags = 0;
		Cell cc;
		
		for (GridDirection dir : GridDirection.values()) {
			cc = getNeighbour(c.getX(), c.getY(), dir);
			if (cc.hasFlag()) {
				amountOfFlags++;
			}
		}
		
		if (amountOfFlags == c.getSurrounding()) {
			for (GridDirection dir : GridDirection.values()) {
				cc = getNeighbour(c.getX(), c.getY(), dir);
				uncover(cc.getX(), cc.getY(), false);
			}
		}
	}
	
	private void doDijkstra(Cell c) {
		Array<Cell> toCheck = new Array<Cell>(50);
		boolean[][] checked = new boolean[fieldWidth][fieldHeight];
		
		toCheck.add(c);
		checked[c.getX()][c.getY()] = true;
		
		Cell cc;
		while (toCheck.size > 0) {
			c = toCheck.get(0);
			
			c.setUncovered();
			
			if (c.getSurrounding() == 0) {
				for (GridDirection dir : GridDirection.values()) {
					cc = getNeighbour(c.getX(), c.getY(), dir);
					if (cc != null && !cc.hasNoCover() && !checked[cc.getX()][cc.getY()]) {
						toCheck.add(cc);
						checked[cc.getX()][cc.getY()] = true;
					}
				}
			}
			
			toCheck.removeValue(c, false);
		}
	}
	
	private void setGameOver() {
		gameOver = true;
	}
	
	private Cell getNeighbour(int x, int y, GridDirection dir) {
		if (x < 0 || x >= fieldWidth || y < 0 || y > fieldHeight) {
			throw new NullPointerException("Coordinate (" + x + ", " + y + ") is not on the field");
		}
		
		switch (dir) {
			case TOP:
				if (y < fieldHeight - 1) {
					return cells[x][y + 1];
				}
				break;
			case RIGHT:
				if (x < fieldWidth - 1) {
					return cells[x + 1][y];
				}
				break;
			case BOTTOM:
				if (y > 0) {
					return cells[x][y - 1];
				}
				break;
			case LEFT:
				if (x > 0) {
					return cells[x - 1][y];
				}
				break;
			case BOTTOMLEFT:
				if (x > 0 && y > 0) {
					return cells[x - 1][y - 1];
				}
				break;
			case BOTTOMRIGHT:
				if (x < fieldWidth - 1 && y > 0) {
					return cells[x + 1][y - 1];
				}
				break;
			case TOPLEFT:
				if (x > 0 && y < fieldHeight - 1) {
					return cells[x - 1][y + 1];
				}
				break;
			case TOPRIGHT:
				if (x < fieldWidth - 1 && y < fieldHeight - 1) {
					return cells[x + 1][y + 1];
				}
				break;
		}
		
		return null;
	}
	
	public int getWidth() {
		return fieldWidth;
	}
	
	public int getHeight() {
		return fieldHeight;
	}
	
	public String toString() {
		return toString("normal");
	}
	
	public String toString(String mode) {
		Cell m;	
		String result = "";
		for (int y = 0; y < fieldHeight; y++) {
			for (int x = 0; x < fieldWidth; x++) {
				m = cells[x][y];
				if (m.hasNoCover()) {
					if (m.hasMine()) {
						result += "X";
					} else if (m.getSurrounding() > 0) {
						result += "" + m.getSurrounding();
					} else {
						result += " ";
					}
				} else {
					if (m.hasFlag()) {
						result += "F";
					} else if (m.hasMine() && mode.equals("mines")) {
						result += "M";
					} else if (m.getSurrounding() > 0 && mode.equals("weights")) { 
						result += "" + m.getSurrounding();
					} else {
						result += "O";
					}
				}
			}
			
			result += "\n";
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		MinefieldModel mfm = new MinefieldModel(100, 100);
		mfm.initialize(60, 2, 2);
		System.out.println(mfm.toString("mines") + "\n");
		
		mfm.uncover(2, 2, true);
		System.out.println(mfm.toString() + "\n");
	}
}
