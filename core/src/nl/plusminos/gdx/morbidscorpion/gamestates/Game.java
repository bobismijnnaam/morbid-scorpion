package nl.plusminos.gdx.morbidscorpion.gamestates;

import nl.plusminos.gdx.morbidscorpion.utils.PinchableCamera;
import nl.plusminos.harness.gdx.gamestates.GamestateAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;

public class Game extends GamestateAdapter {
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
		camera.minSize.y = 36;
		camera.maxSize.y = 128;
		camera.enforceSizeConstraint(true);
		
		logoTexture = new Texture(Gdx.files.internal("logo.png"));
		logoSprite = new Sprite(logoTexture);
		
		msg = "Camera.near := " + camera.far;
		
		addProcessor(new GestureDetector(camera));
		addProcessor(camera);
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
}
