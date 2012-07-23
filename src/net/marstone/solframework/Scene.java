package net.marstone.solframework;


import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * This class represents the most common game element: Scene
 * A scene is consisted by some renderable items, which are limited in
 * and it is the smallest logical unit, which can make any decision with in
 * Typical scenes are:
 * A menu screen, a level of game or an ending movie
 * @author marstone
 * @since 2010/11/09
 */
public class Scene {

	private State state = null;
	private Coordinator coordinator;

	List<Item> items = new ArrayList<Item>();
	
	public List<Item> getItems() {
		return items;
	}

	public Coordinator getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(Coordinator coordinator) {
		this.coordinator = coordinator;
	}

	public void setState(State state) {
		this.state = state;
	}

	public State getState() {
		return state;
	}
	
	/**
	 * At least give me a state, then i can acknowledge where i am
	 * @param state
	 */
	public Scene(State state, Coordinator coordinator) {
		this.state = state;
		this.coordinator = coordinator;
	}
	
	public void show() {
		if(null != this.coordinator) 
			this.coordinator.show();
	}
	
	public void shut() {
		if(null != this.coordinator) 
			this.coordinator.shut();
	}
	
	/**
	 * 
	 * @param time
	 * @param elapsedTime
	 * @return
	 */
    public State update(int time, int elapsedTime) {
		for(Item item : this.items) {
			item.update(time, elapsedTime);
		}
    	return this.coordinator.update(time, elapsedTime);
    }
    
	public void render(GL10 gl) {
		// Clear screen
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		// Scene view matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		
		for(Item item : this.items) {
			item.render(gl);
		}
	}


	public boolean addItem(Item item) {
		if(null != this.items)
			return this.items.add(item);
		
		return false;
	}
	
	public boolean removeItem(Item item) {
		if(null != this.items)
			return this.items.remove(item);
		
		return false;
	}

	public GameSol getGame() {
		if(this.state.machine instanceof GameSol) 
			return (GameSol)this.state.machine;
		else
			return Settings.TheGameInstance;
	}

	public Input getInput() {
		return this.getGame().input;
	}	
}
