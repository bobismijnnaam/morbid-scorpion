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
	
	boolean zooming = false;
	float startWidth, startHeight;
	Vector3 pointer1Projected;
	float initialDistance;
	
	boolean draggable = false;
	int lockedPointer = -1;
	Vector3 pointerProjected;
	
	public void pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		if (!zooming) {
			zooming = true;
			startWidth = viewportWidth;
			startHeight = viewportHeight;

			pointer1Projected = unproject(new Vector3(initialPointer1, 0.0f));
			
			initialDistance = initialPointer1.dst(initialPointer2);
			
//			lockedPointer = 0;
		}
		
		float distance = pointer1.dst(pointer2);
		float factor = initialDistance / distance;
		
		viewportWidth = startWidth * factor;
		viewportHeight = startHeight * factor;
		
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
