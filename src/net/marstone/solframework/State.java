package net.marstone.solframework;


import java.util.UUID;

public class State {

	
	String stateId = UUID.randomUUID().toString();
	Scene scene = null;
	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	StateMachine machine = null;
	
	public StateMachine getMachine() {
		return machine;
	}

	public void setMachine(StateMachine machine) {
		this.machine = machine;
	}

	/**
	 * The constructor, the unique Id and defaultScene are needed.
	 * @param stateId
	 * @param scene
	 */
	public State(String stateId) {
		this.stateId = stateId;
	}

	public String getStateId() {
		return stateId;
	}


	public void setStateId(String stateId) {
		this.stateId = stateId;
	}


	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof State) {
			if(null != this.stateId)
				return this.stateId.equals(((State)obj).getStateId());
			else 
				return this.stateId == ((State)obj).getStateId();
		}
		return false;
	}
	
	@Override
	public String toString(){
		return "[State:" + this.stateId + "]";
	}
}
