package nl.plusminos.gdx.morbidscorpion;

import nl.plusminos.gdx.morbidscorpion.gamestates.Game;
import nl.plusminos.gdx.morbidscorpion.gamestates.Load;
import nl.plusminos.gdx.morbidscorpion.gamestates.Menu;
import nl.plusminos.harness.gdx.gamestates.GamestateManager;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MorbidScorpion extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	GamestateManager gm;
	
	@Override
	public void create () {
//		batch = new SpriteBatch();
//		img = new Texture("badlogic.jpg");
		
		gm = new GamestateManager(1/60, new Load());
		gm.addState(new Game());
		gm.addState(new Menu());
		
		gm.setState("game");
		
		gm.update();
	}

	@Override
	public void render () {
		// Update GamestateMachine
		gm.update();
	}
	
	@Override
	public void pause() {
		gm.pause();
	}
	
	@Override
	public void resume() {
		gm.resume();
	}
	
	@Override
	public void resize(int width, int height) {
		gm.resize(width, height);
	}
	
	@Override
	public void dispose() {
		// Dispose GamestateMachine
		gm.dispose();
	}
}
