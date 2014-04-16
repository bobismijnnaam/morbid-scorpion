package nl.plusminos.gdx.morbidscorpion.gamestates;

import nl.plusminos.harness.gdx.gamestates.Gamestate;
import nl.plusminos.harness.gdx.gamestates.GamestateAdapter;

public class Menu extends GamestateAdapter {

	@Override
	public Gamestate instantiate() {
		return new Menu();
	}

	@Override
	public String getStateID() {
		return "menu";
	}

}
