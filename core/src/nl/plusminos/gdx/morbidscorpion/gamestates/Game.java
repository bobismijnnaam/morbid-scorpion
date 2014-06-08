package nl.plusminos.gdx.morbidscorpion.gamestates;

import nl.plusminos.gdx.morbidscorpion.utils.Global;
import nl.plusminos.gdx.morbidscorpion.utils.PinchableCamera;
import nl.plusminos.harness.gdx.gamestates.GamestateAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Matrix4;

public class Game extends GamestateAdapter {
	private BitmapFont font = new BitmapFont();
	private SpriteBatch batch;
	private PinchableCamera camera;
	
	private String msg = "No touch yet";
	
	private Texture logoTexture;
	private Sprite logoSprite;
	
	FrameBuffer fbo;
	TextureRegion fboRegion;
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		
		camera = new PinchableCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setDraggable(true);
		camera.minSize.y = 36;
		camera.maxSize.y = 128;
//		camera.enforceSizeConstraint(true);
		
		msg = "Camera.near := " + camera.far;
		
		addProcessor(new GestureDetector(camera));
		addProcessor(camera);
		
		logoTexture = new Texture(Gdx.files.internal("logo.png"));
		logoSprite = new Sprite(logoTexture);
		
		// Generate texture
		fbo = new FrameBuffer(Format.RGBA8888, 1024, 1024, false);
		fboRegion = new TextureRegion(fbo.getColorBufferTexture());
		fboRegion.flip(false, false);
		
		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(true, fbo.getWidth(), fbo.getHeight());
		
		SpriteBatch batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		
		BitmapFont bmf = Global.lekton.getFont(60);
		bmf.setColor(Color.WHITE);
		
		fbo.begin();
		batch.begin();
		
//		bmf.draw(batch, "0 1 2 3 4 5 6 7 8 9", 0, 0);
		logoSprite.draw(batch);
		
		batch.end();
		fbo.end();
		
		batch.dispose();
	}
	
	@Override
	public void render() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
//		logoSprite.draw(batch);
		
		// font.draw(batch, msg, 100, 100);
		
		batch.draw(fboRegion, 0, 0);
		
//		Global.lekton.getFont(60).draw(batch, "TEST", 100, 100);
		
		batch.end();
	}
	
	@Override
	public void dispose() {
		logoTexture.dispose();
		fbo.dispose();
	}
}
