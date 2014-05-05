package nl.plusminos.gdx.morbidscorpion.minefield;

import java.util.ArrayList;
import java.util.List;

import nl.plusminos.gdx.morbidscorpion.utils.GridDirection;

public class MinefieldModel {
	Mine[][] mines;
	List<Mine> safePlaces;
	private boolean gameOver = false;
	int fieldWidth, fieldHeight;
	
	public MinefieldModel(int width, int height) {
		mines = new Mine[width][height];
		safePlaces = new ArrayList<Mine>(width * height);
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				mines[x][y] = new Mine(x, y);
				safePlaces.add(mines[x][y]);
			}
		}
		
		fieldWidth = width;
		fieldHeight = height;
	}
	
	public void initialize(int amountOfMines, int startX, int startY) {
		Mine m, n;
		
		for (int i = 0; i < amountOfMines; i++) {
			m = safePlaces.get((int) (Math.random() * safePlaces.size()));
			if (m.getX() == startX && m.getY() == startY) {
				safePlaces.remove(m);
			} else {
				m.setMine();
				safePlaces.remove(m);
				
				for (GridDirection dir : GridDirection.values()) {
					n = getNeighbour(m.getX(), m.getY(), dir);
					if (n != null) {
						n.setSurrounding((byte) (n.getSurrounding() + 1));
					}
				}
			}
		}
		
		safePlaces = null;
		
	}
	
	public void toggleFlag(int x, int y) {
		mines[x][y].toggleFlag();
	}
	
	public void uncover(int x, int y) {
		Mine m = mines[x][y];
		
		if (m.hasMine()) {
			setGameOver();
			return;
		} else if (m.hasFlag()) {
			return;
		} else if (m.hasNoCover() && m.getSurrounding() > 0) {
			groupUncover(m);
		} else if (m.hasNoCover()) {
			return;
		} else {
			doDijkstra(m);
		}
	}
	
	private void groupUncover(Mine m) {
		int amountOfFlags = 0;
		Mine n;
		
		for (GridDirection dir : GridDirection.values()) {
			n = getNeighbour(m.getX(), m.getY(), dir);
			if (n.hasFlag()) {
				amountOfFlags++;
			}
		}
		
		if (amountOfFlags == m.getSurrounding()) {
			for (GridDirection dir : GridDirection.values()) {
				n = getNeighbour(m.getX(), m.getY(), dir);
				if (!n.hasFlag() && n.hasMine()) {
					setGameOver();
					break;
				} else {
					if (!n.hasFlag()) {
						n.setUncovered();
					}
				}
			}
		}
	}
	
	private void doDijkstra(Mine m) {
		List<Mine> toCheck = new ArrayList<Mine>(50);
		boolean[][] checked = new boolean[fieldWidth][fieldHeight];
		
		toCheck.add(m);
		checked[m.getX()][m.getY()] = true;
		
		Mine n;
		while (toCheck.size() > 0) {
			m = toCheck.get(0);
			
			m.setUncovered();
			
			if (m.getSurrounding() == 0) {
				for (GridDirection dir : GridDirection.values()) {
					n = getNeighbour(m.getX(), m.getY(), dir);
					if (n != null && !n.hasNoCover() && !checked[n.getX()][n.getY()]) {
						toCheck.add(n);
						checked[n.getX()][n.getY()] = true;
					}
				}
			}
			
			toCheck.remove(m);
		}
	}
	
	private void setGameOver() {
		gameOver = true;
	}
	
	private Mine getNeighbour(int x, int y, GridDirection dir) {
		if (x < 0 || x >= fieldWidth || y < 0 || y > fieldHeight) {
			throw new NullPointerException("Coordinate (" + x + ", " + y + ") is not on the field");
		}
		
		switch (dir) {
			case TOP:
				if (y < fieldHeight - 1) {
					return mines[x][y + 1];
				}
				break;
			case RIGHT:
				if (x < fieldWidth - 1) {
					return mines[x + 1][y];
				}
				break;
			case BOTTOM:
				if (y > 0) {
					return mines[x][y - 1];
				}
				break;
			case LEFT:
				if (x > 0) {
					return mines[x - 1][y];
				}
				break;
			case BOTTOMLEFT:
				if (x > 0 && y > 0) {
					return mines[x - 1][y - 1];
				}
				break;
			case BOTTOMRIGHT:
				if (x < fieldWidth - 1 && y > 0) {
					return mines[x + 1][y - 1];
				}
				break;
			case TOPLEFT:
				if (x > 0 && y < fieldHeight - 1) {
					return mines[x - 1][y + 1];
				}
				break;
			case TOPRIGHT:
				if (x < fieldWidth - 1 && y < fieldHeight - 1) {
					return mines[x + 1][y + 1];
				}
				break;
		}
		
		return null;
	}
	
	public String toString() {
		return toString("normal");
	}
	
	public String toString(String mode) {
		Mine m;	
		String result = "";
		for (int y = 0; y < fieldHeight; y++) {
			for (int x = 0; x < fieldWidth; x++) {
				m = mines[x][y];
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
		
		mfm.uncover(2, 2);
		System.out.println(mfm.toString() + "\n");
	}
}
