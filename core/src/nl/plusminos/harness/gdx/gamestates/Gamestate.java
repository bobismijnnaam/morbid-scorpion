package nl.plusminos.harness.gdx.gamestates;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputMultiplexer;

public abstract class Gamestate extends InputMultiplexer implements ApplicationListener {
	// Meta functions
	
	/**
	 * Returns whether or not the next state on the stack should be drawn or not
	 * @return True if the next state should be drawn, false if not
	 */
	public abstract boolean isTransparent();
	
	// Game screen functions
	
	/**
	 * Logic is performed here
	 * @param deltaTime The time since the last logic call
	 */
	public abstract void logic(double deltaTime);
	
	// Gamestate stack functions
	
	/**
	 * Is called when the state gains focus
	 */
	public abstract void show();
	
	/**
	 * Is called when the state loses focus
	 */
	public abstract void hide();
}