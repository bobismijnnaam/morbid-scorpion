package nl.plusminos.gdx.morbidscorpion.gamestates;

import nl.plusminos.harness.gdx.gamestates.Gamestate;
import nl.plusminos.harness.gdx.gamestates.GamestateAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
	private OrthographicCamera camera;
	
	private String msg = "No touch yet";
	
	private Texture logoTexture;
	private Sprite logoSprite;
	private boolean zooming = false;
	private float startSize;
	private Vector2 previousPosition;
	private Vector2 startOffset;
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		logoTexture = new Texture(Gdx.files.internal("logo.png"));
		logoSprite = new Sprite(logoTexture);
		logoSprite.setPosition(0, 0);
		logoSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		startSize = logoSprite.getWidth();
	}
	
	@Override
	public void render() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		font.draw(batch, msg, 100, 100);
		
		logoSprite.draw(batch);
		
		batch.end();
	}
	
	@Override
	public void dispose() {
		logoTexture.dispose();
	}
	
	@Override
	public boolean zoom(float initialDistance, float distance) {
//		msg = "ZOOM | ID: " + initialDistance + " D: " + distance;
		
//		if (!zooming) {
//			zooming = true;
//			startSize = logoSprite.getWidth();
//		}
//		
//		if (zooming) {
//			float factor = distance / initialDistance;
//			logoSprite.setSize(startSize * factor, startSize * factor);
//		}
		
		return false;
	}
	
	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		
		if (!zooming) {
			zooming = true;
			startSize = logoSprite.getWidth();
			previousPosition = initialPointer1.cpy();
			
			startOffset = initialPointer1.cpy();
			startOffset.y = Gdx.graphics.getHeight() - startOffset.y;
			startOffset.x -= logoSprite.getX();
			startOffset.y -= logoSprite.getY();
		}
		
		float initialDistance = initialPointer1.dst(initialPointer2);
		float distance = pointer1.dst(pointer2);
		
		float factor = distance / initialDistance;
		logoSprite.setSize(startSize * factor, startSize * factor);
		
		Vector2 p1 = pointer1.cpy();
		p1.y = Gdx.graphics.getHeight() - p1.y;
		
		Vector2 currentOffset = startOffset.cpy().scl(factor);
		Vector2 newPos = p1.cpy().sub(currentOffset);
		logoSprite.setPosition(newPos.x, newPos.y);
		
		msg = "IV: " + initialPointer1.toString() + " | CV: " + pointer1.toString();
		
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//		startX = screenX;
//		startY = Gdx.graphics.getHeight() - screenY;
//		
//		chosenPointer = pointer;
		
		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
//		if (pointer == chosenPointer) {
//			float dx = screenX - startX;
//			float dy = (Gdx.graphics.getHeight() - screenY) - startY;
//			
//			logoSprite.setPosition(logoSprite.getX() + dx, logoSprite.getY() + dy);
//			
//			startX = screenX;
//			startY = Gdx.graphics.getHeight() - screenY;
//		}
		
		return false;
	}

	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		msg = "TU | X: " + screenX + " TY: " + screenY;
		
		zooming = false;
		
		return false;
	}
}
