package nl.plusminos.gdx.morbidscorpion;

import nl.plusminos.harness.gdx.gamestates.GamestateManager;
import nl.plusminos.harness.gdx.gamestates.StateAction;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MorbidScorpion extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	GamestateManager gm;
	
	@Override
	public void create () {
		gm = new GamestateManager(1/60, "nl.plusminos.gdx.morbidscorpion.gamestates");
		
		gm.changeState(StateAction.SET, "Game");
		
		gm.update();
		
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
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
