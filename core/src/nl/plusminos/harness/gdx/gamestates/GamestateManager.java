package nl.plusminos.harness.gdx.gamestates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;

// TODO: Shader support
// TODO: Comments!
public class GamestateManager implements ApplicationListener {
	
	// Singleton variable
	private static GamestateManager instance = null;
	
	// Gamestate machine variables
	private float fixedTimestep = 0; // To indicate time between logic frames - 0 for variable timestep
	private String nextState = ""; // The next state to change to
	private String pushedState = ""; // The state that should be pushed on top of the current state
	private boolean popAState = false; // If a state should be popped off the stack
	private float passedTime = 0; // The time since the last logic loop was started
	
	private ArrayList<Gamestate> stateStack = new ArrayList<Gamestate>(); // Stack of states
	private HashMap<String, Gamestate> stateRepo = new HashMap<String, Gamestate>(); // All added states are saved here
	
	/**
	 * Initializes gamestate manager. At least one gamestate should be supplied. The first gamestate in the list
	 * will be the starting state. To change this just make a call to {@link #setState(String)} afterwards.
	 * @param fixedTimestep Set to zero for variable timestep. Otherwise it's the time between logic loop calls
	 * @param batch The spritebatch to use
	 * @param camera The camera to use
	 * @param gamestates Initial gamestates you want to set. You can also do this later with addState()
	 */
	public GamestateManager(float fixedTimestep, Gamestate...gamestates) {
		if (instance == null) {
			// Set timestep
			this.fixedTimestep = fixedTimestep;
			
			// Add all gamestates to collection
			for (Gamestate g : gamestates) {
				stateRepo.put(g.getStateID(), g);
			}
			
			// Check if enough states were added
			if (gamestates.length == 0) {
				throw new NullPointerException();
			}
			
			// Set the starting state
			setState(gamestates[0].getStateID());
			
			// Lastly, set the singleton instance
			instance = this;
		}
	}
	
	/**
	 * @return The Gamestate singleton instance
	 */
	public static GamestateManager get() {
		if (instance == null) throw new NullPointerException();
		
		return instance;
	}
	
	/**
	 * Updates the gamestatemanager
	 */
	public void update() {
		changeStates();
		
		render();
	}
	
	/**
	 * Adds a state to the gamestate collection
	 * @param newGamestate The gamestate to be added
	 */
	public void addState(Gamestate newGamestate) {
		stateRepo.put(newGamestate.getStateID(), newGamestate);
	}
	
	/**
	 * Clears the stack of states, and then sets the next state
	 * @param newStateID The next state to start
	 */
	public void setState(String newStateID) {
		// Check if EXIT was called
		if (!nextState.equals("EXIT")) {
			nextState = newStateID;
		}
	}
	
	/**
	 * Pushes a state on top of the stack
	 * @param newStateID The state to be pushed on top of the stack
	 */
	public void pushState(String newStateID) {
		pushedState = newStateID;
	}
	
	/**
	 * Pops one state off the stack
	 */
	public void popState() {
		popAState = true;
	}
	
	/**
	 * Handles next states, pushed/popped states. First next states are handled, then pushed states and lastly
	 * popped states are handled. If gamestates don't exist or there's only 1 state on the stack a NullPointerException
	 * will be thrown.
	 */
	private void changeStates() {
		if (!nextState.equals("")) { // Check if a next state was set
			if (nextState.equals("EXIT")) { // Exit if the set state is EXIT
				Gdx.app.exit();
			}
			
			// Retrieve a new instance of the next state
			Gamestate startGamestate = stateRepo.get(nextState);
			
			// Dispose of all previous active states
			for (Gamestate gs : stateStack) {
				gs.dispose();
			}
			
			if (startGamestate == null) { // If no gamestate was found with this ID, return an exception
				throw new NullPointerException("Unknown state: " + nextState);
			}else {
				// Empty stack
				stateStack.clear();
				
				// Add the new state & initialize it
				stateStack.add(startGamestate.instantiate());
				getState().create();
				
				// Set the input to that state
				setInputToCurrentState();
				
				// Reset the variable so it won't be added again
				nextState = "";
			}
		}
		
		if (!pushedState.equals("")) { // Check if a push state was set
			getState().toBack(); // Notify top state of getting pushed to the background
			
			// Get a new instance of the pushed state
			Gamestate newStateInstance = stateRepo.get(pushedState);
			
			if (newStateInstance == null) { // If the state was not found, throw an exception
				throw new NullPointerException("Unknown state: " + pushedState);
			} else {
				
				// Add the new state on top of the stack and initialize it
				stateStack.add(newStateInstance.instantiate());
				getState().create();
				
				// Set input to the current state
				setInputToCurrentState();
				
				// Reset the variable so the state won't be pushed again
				pushedState = "";
			}
		}
		
		if (popAState) { // Check if a state has to be popped
			if (stateStack.size() <= 1) { // If there are 1 or less states left throw an exception
				throw new NullPointerException("No more states left on stack");
			} else {
				// Retrieve and dispose the state, and remove it from the stack
				Gamestate stateToPop = getState();
				stateToPop.dispose();
				stateStack.remove(stateStack.size() - 1);
				
				// Notify the back state that is has regained focus
				getState().toFront();
				
				// Set the input to the current state
				setInputToCurrentState();
				
				// Reset the variable so it won't pop another state
				popAState = false;
			}
		}
	}
	
	/**
	 * An internal function that sets the input to the topmost state
	 */
	private void setInputToCurrentState() {
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(getState());
		multiplexer.addProcessor(new GestureDetector(getState()));
		
		Gdx.input.setInputProcessor(multiplexer);
	}
	
	/**
	 * @return The gamestate stack size
	 */
	public int getStackSize() {
		return stateStack.size();
	}
	
	/**
	 * @return The active state
	 */
	private Gamestate getState() {
		return stateStack.get(stateStack.size() - 1);
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
		getState().resize(width, height);
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
			getState().logic(Gdx.graphics.getDeltaTime());
			
			// Find out which state has transparency set to false.
			// All states between and including that state and the active state
			// have to be drawn, to create this overlay effect
			int startState = 0;
			for (int i = stateStack.size() - 1; i > -1; i--) {
				if (!stateStack.get(i).isTransparent()) {
					startState = i;
					
					break;
				}
			}
			
			// Draw all states from the bottom up
			for (int i = startState; i < stateStack.size(); i++) {
				stateStack.get(i).render();
			}
		} else {
			// This is the fixed update step, variable rendering game programming pattern
			// More info here: http://gameprogrammingpatterns.com/game-loop.html
			
			// Keep track of how much time has passed since the last logic loop
			passedTime += Gdx.graphics.getDeltaTime();
			
			// While there is time to, execute another logic loop and deduce the
			// passedTime by the timestep
			while (passedTime > fixedTimestep) {
				passedTime -= fixedTimestep;
				
				stateStack.get(stateStack.size() - 1).logic(fixedTimestep);
			}
			
			// Find out which state has transparency set to false.
			// All states between and including that state and the active state
			// have to be drawn, to create this overlay effect
			int startState = 0;
			for (int i = stateStack.size() - 1; i > -1; i--) {
				if (!stateStack.get(i).isTransparent()) {
					startState = i;
					
					break;
				}
			}
			
			// Draw all states from the bottom up
			for (int i = startState; i < stateStack.size(); i++) {
				stateStack.get(i).render();
			}
		}
	}
	
	/**
	 * Passes on the pause event. It is not passed on to other states in the stack,
	 * since they should already be in a sort of paused state.
	 */
	@Override
	public void pause() {
		getState().pause();
	}
	
	/**
	 * Passes on the resume event. It is not passed on to other states in the stack,
	 * since they should stay paused.
	 */
	@Override
	public void resume() {
		getState().resume();
	}
	
	/**
	 * Disposes of all the active gamestates and gamestates in the repo. If the client has
	 * been programming properly the constructers haven't been loading stuff (and thus disposing repo states
	 * is redundant) but people are stupid these days.
	 */
	@Override
	public void dispose() {
		for (Gamestate gs : stateStack) {
			gs.dispose();
		}
		
		for (Entry<String, Gamestate> gameState : stateRepo.entrySet()) {
			gameState.getValue().dispose();
		}
	}
}
