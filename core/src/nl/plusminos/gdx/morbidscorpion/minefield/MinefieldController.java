package nl.plusminos.gdx.morbidscorpion.minefield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MinefieldController implements GestureListener {

	private Camera camera;
	private MinefieldAction primaryAction = MinefieldAction.UNCOVER;
	private MinefieldModel model;

	public MinefieldController(Camera camera, MinefieldModel model) {
		this.camera = camera;
		this.model = model;
	}
	
	public void setPrimaryAction(MinefieldAction action) {
		this.primaryAction = action;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		int[] pos = screenToField(x, y);
		
		if (pos == null) return false;
		
		switch (primaryAction) {
			case UNCOVER:
				model.uncover(pos[0], pos[1], true);
				break;
			case PLANT:
				model.toggleFlag(pos[0], pos[1]);
				break;
			default:
				Gdx.app.debug("MinefieldController", "Default case in switch statement in MineFieldController.tap()");
				break;
		}
		
		return true;
	}

	@Override
	public boolean longPress(float x, float y) {
		int[] pos = screenToField(x, y);
		
		if (pos == null) return false;
		
		switch (primaryAction) {
			case UNCOVER:
				model.toggleFlag(pos[0], pos[1]);
				break;
			case PLANT:
				model.uncover(pos[0], pos[1], true);
				break;
			default:
				Gdx.app.debug("MinefieldController", "Default case in switch statement in MineFieldController.longPress()");
				break;
		}
		
		return true;
	}
	
	public int[] screenToField(float x, float y) {
		Vector3 unprojected = camera.unproject(new Vector3(x, y, 0f));
		int fieldX = (int) unprojected.x;
		int fieldY = (int) unprojected.y;
		
		if (fieldX < 0 || fieldY < 0 || fieldX >= model.getWidth() || fieldY >= model.getHeight()) {
			return null;
		}
		
		return new int[]{fieldX, fieldY};
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

}
