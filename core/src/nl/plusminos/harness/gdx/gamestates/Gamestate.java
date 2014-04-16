package nl.plusminos.harness.gdx.gamestates;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;

public interface Gamestate extends ApplicationListener, InputProcessor, GestureListener {
	// Meta functions
	
	/**
	 * Returns a new instance of this class
	 * @return a new instance of this gamestate type
	 */
	public Gamestate instantiate();
	
	/**
	 * Returns the ID of the current state. Useful for distinguishing between states.
	 * The ID is probably be a name but essentially only has to be a unique identifier
	 * (within your game)
	 * @return The ID of the state
	 */
	public String getStateID();
	
	/**
	 * Returns whether or not the next state on the stack should be drawn or not
	 * @return True if the next state should be drawn, false if not
	 */
	public boolean isTransparent();
	
	// Game screen functions
	
	/**
	 * Logic is performed here
	 * @param deltaTime The time since the last logic call
	 */
	public void logic(float deltaTime);
	
	// Gamestate stack functions
	
	/**
	 * Is called when the state gains focus
	 */
	public void toFront();
	
	/**
	 * Is called when the state loses focus
	 */
	public void toBack();
}