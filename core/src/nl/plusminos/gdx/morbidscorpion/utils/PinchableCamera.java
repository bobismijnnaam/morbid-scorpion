package nl.plusminos.gdx.morbidscorpion.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

//TODO: Maybe add extra parameters such that you can also make it rotate or something
public class PinchableCamera extends OrthographicCamera {

	public PinchableCamera() {
		super();
	}

	public PinchableCamera(float viewportWidth, float viewportHeight) {
		super(viewportWidth, viewportHeight);
	}
	
	private boolean zooming = false;
	private float startWidth, startHeight;
	private Vector3 pointer1Projected;
	private float initialDistance;
	
	private boolean draggable = false;
	private int lockedPointer = -1;
	private Vector3 pointerProjected;
	
	public final Vector2 minSize = new Vector2(-1, -1);
	public final Vector2 maxSize = new Vector2(-1, -1);
	
	public void pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		if (!zooming) {
			zooming = true;
			startWidth = viewportWidth;
			startHeight = viewportHeight;

			pointer1Projected = unproject(new Vector3(initialPointer1, 0.0f));
			
			initialDistance = initialPointer1.dst(initialPointer2);
		}
		
		float distance = pointer1.dst(pointer2);
		float factor = initialDistance / distance;
		
		viewportWidth = startWidth * factor;
		viewportHeight = startHeight * factor;
		
		enforceSizeConstraint(false);
		
		Vector2 dPointer = pointer1.cpy();
		dPointer.y = Gdx.graphics.getHeight() - pointer1.y;
		
		dPointer.sub(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);
		
		dPointer.scl(1/(float) Gdx.graphics.getWidth(), 1/(float) Gdx.graphics.getHeight());
		
		dPointer.scl(-viewportWidth, -viewportHeight);
		
		position.set(pointer1Projected.cpy().add(new Vector3(dPointer, 0.0f)));
		
		update();
	}
	
	public void setDraggable(boolean p) {
		draggable = p;
	}
	
	/**
	 * Presumes aspect ratio is meant to be maintained
	 * @param doUpdate
	 */
	public void enforceSizeConstraint(boolean doUpdate) {
		if (minSize.x != -1 && viewportWidth < minSize.x) {
			viewportWidth = minSize.x;
			viewportHeight = minSize.x * Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
		}
		if (minSize.y != -1 && viewportHeight < minSize.y) {
			viewportHeight = minSize.y;
			viewportWidth = minSize.y * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
		}
		if (maxSize.x != -1 && viewportWidth > maxSize.x) {
			viewportWidth = maxSize.x;
			viewportHeight = maxSize.x * Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
		}
		if (maxSize.y != -1 && viewportHeight > maxSize.y) {
			viewportHeight = maxSize.y;
			viewportWidth = maxSize.y * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
		}
		
		if (doUpdate) {
			update();
		}
	}
	
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		if (draggable && !zooming) {
			if (lockedPointer == -1) {
				lockedPointer = pointer;
				
				pointerProjected = unproject(new Vector3(screenX, screenY, 0.0f));
				
				System.out.println("[PinchableCamera] Touchdown: " + pointer);
			}
		}
		
		return false;
	}

	public boolean touchDragged (int screenX, int screenY, int pointer) {
		if (pointer == lockedPointer && !zooming) {
			Vector2 dPointer = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
			
			dPointer.sub(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);
			
			dPointer.scl(1/(float) Gdx.graphics.getWidth(), 1/(float) Gdx.graphics.getHeight());
			
			dPointer.scl(-viewportWidth, -viewportHeight);
			
			position.set(pointerProjected.cpy().add(new Vector3(dPointer, 0.0f)));
			
			update();
		}
		
		return false;
	}
	
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		if (draggable) {
			if (pointer == lockedPointer) {
				lockedPointer = -1;
				
				System.out.println("[PinchableCamera] Touchup: " + pointer);
			}
		}
		
		zooming = false;
		
		return false;
	}

}
