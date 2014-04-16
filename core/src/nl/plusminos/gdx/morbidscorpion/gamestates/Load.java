package nl.plusminos.gdx.morbidscorpion.gamestates;

import nl.plusminos.harness.gdx.gamestates.Gamestate;
import nl.plusminos.harness.gdx.gamestates.GamestateAdapter;

public class Load extends GamestateAdapter {

	@Override
	public Gamestate instantiate() {
		return new Load();
	}

	@Override
	public String getStateID() {
		return "load";
	}

}
