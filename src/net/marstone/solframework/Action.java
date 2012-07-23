package net.marstone.solframework;

import java.util.UUID;

public class Action {

	State head = null;
	State tail = null;
	private String actionId = UUID.randomUUID().toString();
	
	StateMachine machine = null;
	
	public StateMachine getMachine() {
		return machine;
	}

	public void setMachine(StateMachine machine) {
		this.machine = machine;
	}

	
	public Action(String actionId) {
		this.setActionId(actionId);
	}
	
	public Action(String actionId, State head, State tail) {
		this.setActionId(actionId);
		this.head = head;
		this.tail = tail;
	}

	void setActionId(String actionId) {
		this.actionId = actionId;
	}

	String getActionId() {
		return actionId;
	}

	/**
	 * will be called when state change
	 * @return
	 */
	public boolean transit() {
		if(null != head.scene) head.scene.shut();
		if(null != tail.scene) tail.scene.show();
		return true;
	}
	
}
