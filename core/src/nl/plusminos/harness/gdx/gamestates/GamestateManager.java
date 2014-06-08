package nl.plusminos.harness.gdx.gamestates;

import java.util.EmptyStackException;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;

public class GamestateManager implements ApplicationListener {
	public class GamestateNotFoundException extends NullPointerException {
		private static final long serialVersionUID = -4025872731854248080L;

		public GamestateNotFoundException(String msg) {
			super(msg);
		}
	}
	
	// Gamestate machine variables
	private double fixedTimestep = 0; // To indicate time between logic frames - 0 for variable timestep
	private double passedTime = 0; // The time since the last logic loop was started
	private final String gamestatePackage;
	private String stateClassName = "";
	private StateAction action = StateAction.NONE;
	
	private Array<Gamestate> stateStack = new Array<Gamestate>(); // Stack of states
	
	
	/**
	 * Initializes gamestate manager. At least one gamestate should be supplied. The first gamestate in the list
	 * will be the starting state. To change this just make a call to {@link #setState(String)} afterwards.
	 * @param fixedTimestep Set to zero for variable timestep. Otherwise it's the time between logic loop calls
	 * @param batch The spritebatch to use
	 * @param camera The camera to use
	 * @param gamestates Initial gamestates you want to set. You can also do this later with addState()
	 */
	public GamestateManager(float fixedTimestep, String gamestatePackage) {
		// Set timestep
		this.fixedTimestep = fixedTimestep;
		
		// Save the package with gamestates
		// Append "." if it's not there 
		if (!gamestatePackage.endsWith(".")) {
			this.gamestatePackage = gamestatePackage + ".";
		} else {
			this.gamestatePackage = gamestatePackage;
		}
	}
	
	/**
	 * Updates the gamestatemanager
	 */
	public void update() {
		changeStates();
		
		render();
	}
	
	/**
	 * To set an action for the gamestate machine if there is no target state involved
	 * @param action The action to perform (POP, EXIT, NONE)
	 */
	public void changeState(StateAction action) {
		changeState(action, "");
	}
	
	/**
	 * To set an action if there is a target state involved
	 * @param action The action to perform (PUSH, SET)
	 * @param state The state to target
	 */
	public void changeState(StateAction action, String state) {
		stateClassName = state;
		this.action = action;
	}
	
	/**
	 * Handles next states, pushed/popped states. If gamestates don't exist or there's only 1 state on the stack a NullPointerException
	 * will be thrown.
	 * @throws GameStateNotFoundException if given state cannot be found in the given package
	 * @throws EmptyStackException if the stack has only 1 state or less.
	 */
	private void changeStates() {
		boolean err = false;
		
		switch (action) {
			case POP:
				if (stateStack.size <= 1) {
					throw new EmptyStackException();
				}
				
				Gamestate popped = stateStack.pop();
				popped.dispose();
				
				stateStack.peek().show();
				setInputToCurrentState();
				break;
			case PUSH:
				stateStack.peek().hide();
				
				Gamestate pushedState = null;
				try {
					pushedState = (Gamestate) Class.forName(gamestatePackage + stateClassName).newInstance();
				} catch (InstantiationException e) {
					err = true;
				} catch (IllegalAccessException e) {
					err = true;
				} catch (ClassNotFoundException e) {
					err = true;
				} 
				if (err) throw new GamestateNotFoundException(String.format("Gamestate %s could not be found in package %s", stateClassName, gamestatePackage));
				
				pushedState.create();
				stateStack.add(pushedState);
				setInputToCurrentState();
				break;
			case SET:
				// Dispose of all previous active states
				for (Gamestate gs : stateStack) {
					gs.dispose();
				}
				stateStack.clear();
				
				Gamestate nextState = null;
				try {
					nextState = (Gamestate) Class.forName(gamestatePackage + stateClassName).newInstance();
				} catch (InstantiationException e) {
					err = true;
				} catch (IllegalAccessException e) {
					err = true;
				} catch (ClassNotFoundException e) {
					err = true;
				} 
				if (err) throw new GamestateNotFoundException(String.format("Gamestate %s could not be found in package %s", stateClassName, gamestatePackage));
				
				nextState.create();
				stateStack.add(nextState);
				setInputToCurrentState();
				break;
			case EXIT:
				Gdx.app.exit();
				break;
			case NONE:
				// Move along please
				break;
			default:
				// Idem
				break;
			
		}
		
		action = StateAction.NONE;
		stateClassName = "";
	}
	
	/**
	 * An internal function that sets the input to the topmost state
	 */
	private void setInputToCurrentState() {		
		Gdx.input.setInputProcessor(stateStack.peek());
	}
	
	/**
	 * @return The gamestate stack size
	 */
	public int getStackSize() {
		return stateStack.size;
	}
	
	/**
	 * Trivial implementation of create
	 */
	@Override
	public void create() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Pass on the resize event
	 */
	@Override
	public void resize(int width, int height) {
		stateStack.peek().resize(width, height);
	}
	
	/**
	 * Render the gamestate(s)
	 */
	@Override
	public void render() {
		// Clear the screen.
		// If user wants to retain graphics he can use a framebuffer of some sort
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (fixedTimestep == 0) { // If timestep is variable just execute logic & render as often as possible
			// Run the current gamestate's logic loop with variable timestep
			stateStack.peek().logic(Gdx.graphics.getDeltaTime());
		} else {
			// This is the fixed update step, variable rendering game programming pattern
			// More info here: http://gameprogrammingpatterns.com/game-loop.html
			
			// Keep track of how much time has passed since the last logic loop
			passedTime += Gdx.graphics.getDeltaTime();
			
			// While there is time to, execute another logic loop and deduce the
			// passedTime by the timestep
			while (passedTime > fixedTimestep) {
				passedTime -= fixedTimestep;
				
				stateStack.peek().logic(fixedTimestep);
			}
		}
		
		// Find out which state has transparency set to false.
		// All states between and including that state and the active state
		// have to be drawn, to create this overlay effect
		int startState = 0;
		for (int i = stateStack.size - 1; i > -1; i--) {
			if (!stateStack.get(i).isTransparent()) {
				startState = i;
				
				break;
			}
		}
		
		// Draw all states from the bottom up
		for (int i = startState; i < stateStack.size; i++) {
			stateStack.get(i).render();
		}
	}
	
	/**
	 * Passes on the pause event. It is not passed on to other states in the stack,
	 * since they should already be in a sort of paused state.
	 */
	@Override
	public void pause() {
		stateStack.peek().pause();
	}
	
	/**
	 * Passes on the resume event. It is not passed on to other states in the stack,
	 * since they should stay paused.
	 */
	@Override
	public void resume() {
		stateStack.peek().resume();
	}
	
	/**
	 * Disposes of all the active gamestates.
	 */
	@Override
	public void dispose() {
		for (Gamestate gs : stateStack) {
			gs.dispose();
		}
	}
}
