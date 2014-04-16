package nl.plusminos.gdx.morbidscorpion.gamestates;

import nl.plusminos.harness.gdx.gamestates.Gamestate;
import nl.plusminos.harness.gdx.gamestates.GamestateAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game extends GamestateAdapter {

	@Override
	public Gamestate instantiate() {
		return new Game();
	}

	@Override
	public String getStateID() {
		return "game";
	}
	
	private BitmapFont font = new BitmapFont();
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	private String msg = "No touch yet";
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	@Override
	public void render() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		font.draw(batch, msg, 100, 100);
		
		batch.end();
	}
	
	@Override
	public boolean zoom(float initialDistance, float distance) {
		msg = "ZOOM | ID: " + initialDistance + " D: " + distance;
		
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		msg = "TU | X: " + screenX + " TY: " + screenY;
		
		return false;
	}
}
