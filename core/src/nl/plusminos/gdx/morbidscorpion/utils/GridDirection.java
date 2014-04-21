package nl.plusminos.gdx.morbidscorpion.utils;

public enum GridDirection {
	TOP,
	RIGHT,
	BOTTOM,
	LEFT,
	TOPLEFT,
	TOPRIGHT,
	BOTTOMLEFT,
	BOTTOMRIGHT;
	
	public static final GridDirection[] axisAligned = {TOP, RIGHT, BOTTOM, LEFT};
	public static final GridDirection[] diagonal = {TOPRIGHT, TOPLEFT, BOTTOMRIGHT, BOTTOMLEFT};
	
	public boolean isAxisAligned(GridDirection dir) {
		for (GridDirection hor : axisAligned) {
			if (dir == hor) return true;
		}
		
		return false;
	}
	
	public boolean isDiagonal(GridDirection dir) {
		for (GridDirection diag : diagonal) {
			if (dir == diag) return true;
		}
		
		return false;
	}
}
