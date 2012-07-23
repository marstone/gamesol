package net.marstone.solframework;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The game state machine controls the full game logics.
 * 
 * A finite state transducer is a sextuple (Σ,Γ,S,s0,δ,ω), where:
 * Σ is the input alphabet (a finite non empty set of symbols).
 * Γ is the output alphabet (a finite, non-empty set of symbols).
 * S is a finite, non-empty set of states.
 * s0 is the initial state, an element of S. In a Nondeterministic finite state machine, s0 is a set of initial states.
 * δ is the state-transition function: .
 * ω is the output function.
 */
public class StateMachine {

	Map<String, State> stateMap = new HashMap<String, State>();
	Map<String, Action> actionMap = new HashMap<String, Action>();

	public State initialState = null;
	
	public State getInitialState() {
		return this.findState(this.initialState);
	}

	/**
	 * Set initial state, the state must be added to the state Machine first.
	 * @param initialState
	 * @return
	 */
	public boolean setInitialState(State initialState) {
		State state = this.findState(initialState);
		if(null == state) return false;
		this.initialState = state;
		return true;
	}
	
	/**
	 * Set initial state, the state must be added to the state Machine first.
	 * @param initialState
	 * @return
	 */
	public boolean setInitialState(String initialStateId) {
		if(null == initialStateId || "".equals(initialStateId)) return false;
		State state = this.findState(initialStateId);
		return this.setInitialState(state);
	}

	public State terminateState = null;
	
	public State currentState = null;
	
	public State previousState = null;
	
	public StateMachine() {

	}
				
	/**
	 * Transit to next state.
	 * @param nextState
	 * @return false if no action OR the action trigger returned false 
	 */
	public boolean Transit(State nextState) {
		Action action = this.findAction(currentState, nextState);
		if(null == action) {
			// Transit Failed.
			return false;
		} else {
			boolean success = action.transit();
			if(true == success) {
				this.previousState = this.currentState; 
				this.currentState = nextState;
			}
			return success;
		}
	}
	/**
	 * Find an arc by given head & tail vertex
	 * @param head
	 * @param tail
	 */
	public Action findAction(State head, State tail) {
		if(null == head || null == tail) return null;
		for(Action action : actionMap.values())
			if(head.equals(action.head) && tail.equals(action.tail))
				return action;
		return null;
	}
	
	/**
	 * Find an arc by given head & tail ids
	 * @param headId
	 * @param tailId
	 * @return
	 */
	public Action findAction(String headId, String tailId) {
		State head = this.findState(headId);
		State tail = this.findState(tailId);
		return this.findAction(head, tail);
	}
	
	
	/**
	 * Find all arcs start from given head vertex
	 */
	public List<Action> findActions(State head) {
		List<Action> list = new ArrayList<Action>();
		if(null == head) return list;
		for(Action action : actionMap.values())
			if(head.equals(action.head))
				list.add(action);
		return list;
	}
	

	/**
	 * Find all arcs start from given head vertex id
	 * @param headId
	 * @return
	 */
	public List<Action> findActions(String headId) {
		State head = this.findState(headId);
		return this.findActions(head);
	}
	
	
	/**
	 * get the state instance in the machine by state.stateId
	 * @param state
	 * @return
	 */
	public State findState(State state) {
		if(null == state) return null;
		return this.findState(state.getStateId());
	}
	

	/**
	 * Find state by given Id
	 * @param stateId
	 * @return
	 */
	public State findState(String stateId) {
		if(this.stateMap.containsKey(stateId))
			return this.stateMap.get(stateId);
		else
			return null;
	}
	
	/**
	 * Add a state
	 * @param state
	 * @return
	 */
	public boolean addState(State state) {
		if(null == state) return false;
		State s = this.findState(state);
		if(null != s) return false;
		this.stateMap.put(state.getStateId(), state);
		state.setMachine(this);
		return true;
	}

	
	public boolean removeState(String stateId) {
		State state = this.findState(stateId);
		if(null == state) return false;
		this.stateMap.remove(state);
		// TODO remove actions
		return true;
	}
	
	/**
	 * 
	 * @param action
	 * @param headId
	 * @param tailId
	 * @return
	 */
	public boolean addAction(Action action, String headId, String tailId) {
		// Parameter valid
		if(null == action || null == headId || null == tailId) return false;
		// Action valid
		if(null == action.getActionId() || this.actionMap.containsKey(action.getActionId())) return false;
		State head = this.findState(headId);
		State tail = this.findState(tailId);
		// head/tail valid
		if(null == head || null == tail) return false;
		action.head = head;
		action.tail = tail;
		this.actionMap.put(action.getActionId(), action);
		action.setMachine(this);
		return true;
	}
}
