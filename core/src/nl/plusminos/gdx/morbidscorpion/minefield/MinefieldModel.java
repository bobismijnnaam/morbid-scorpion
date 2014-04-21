package nl.plusminos.gdx.morbidscorpion.minefield;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class MinefieldModel {
	
	enum Direction {
		TOP,
		RIGHT,
		BOTTOM,
		LEFT
	}
	
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
		Mine m;
		
		for (int i = 0; i < amountOfMines; i++) {
			m = safePlaces.get((int) (Math.random() * safePlaces.size()));
			if (m.getX() == startX && m.getY() == startY) {
				safePlaces.remove(m);
			} else {
				m.setMine();
				safePlaces.remove(m);
			}
		}
		
		safePlaces = null;
	}
	
	public void toggleFlag(int x, int y) {
		if (mines[x][y].hasFlag()) {
			mines[x][y].unsetFlagged();
		} else {
			mines[x][y].setFlagged();
		}
	}
	
	public void uncover(int x, int y) {
		List<Mine> toCheck = new ArrayList<Mine>(50);
		Mine m = mines[x][y];
		
		if (m.hasMine()) {
			setGameOver();
			return;
		} else if (m.hasFlag() || m.hasNoCover()) {
			return;
		}
		
		m.setUncovered();
		
		while (toCheck.size() > 0) {
			m = toCheck.get(0);
		}
	}
	
	private void setGameOver() {
		gameOver = true;
	}
	
	private Mine getNeighbour(int x, int y, Direction dir) {
		if (x < 0 || x >= fieldWidth || y < 0 || y > fieldHeight) {
			throw new NullPointerException("Coordinate (" + x + ", " + y + ") is not on the field");
		}
		
		switch (dir) {
			case TOP:
				if (y > 0) {
					return mines[x][y - 1];
				}
				break;
			case RIGHT:
				
				break;
			case BOTTOM:
				break;
			case LEFT:
				break;
		}
		
		return null;
	}

}
