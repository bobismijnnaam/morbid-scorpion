package nl.plusminos.gdx.morbidscorpion.gamestates;

import nl.plusminos.gdx.morbidscorpion.utils.PinchableCamera;
import nl.plusminos.harness.gdx.gamestates.Gamestate;
import nl.plusminos.harness.gdx.gamestates.GamestateAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

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
	private PinchableCamera camera; 
	
	private String msg = "No touch yet";
	
	private Texture logoTexture;
	private Sprite logoSprite;
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		camera = new PinchableCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setDraggable(true);
		
		logoTexture = new Texture(Gdx.files.internal("logo.png"));
		logoSprite = new Sprite(logoTexture);
		logoSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		msg = "Camera.near := " + camera.far;
	}
	
	@Override
	public void render() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		logoSprite.draw(batch);
		
		font.draw(batch, msg, 100, 100);
		
		batch.end();
	}
	
	@Override
	public void dispose() {
		logoTexture.dispose();
	}
	
	@Override
	public boolean zoom(float initialDistance, float distance) {
		
		return false;
	}
	
	@Override
	public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		camera.pinch(initialPointer1, initialPointer2, pointer1, pointer2);
		
		return false;
	}
	
	@Override
	public boolean longPress (float x, float y) {
		
		return false;
	}
	
	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		camera.touchDown(screenX, screenY, pointer, button);
		
		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		camera.touchDragged(screenX, screenY, pointer);
		
		return false;
	}

	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		camera.touchUp(screenX, screenY, pointer, button);
		
		return false;
	}
}
