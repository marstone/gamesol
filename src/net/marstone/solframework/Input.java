package net.marstone.solframework;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Generate Input Cache
 * Store Input Events for ONE Frame
 * @author marstone
 * @since 2010
 */
public class Input {

	/**
	 * All events in time order
	 */
	public List<Parcelable> events;
	
	/**
	 * KeyEvents, Key of the map is KeyCode
	 */
	public Map<Integer, KeyEvent> keyboards;
	
	/**
	 * MotionEvents, Key of the map is Action
	 */
	public Map<Integer, MotionEvent> motions;
	
	public Input() {
		this.clear();
	}
	
	public void clear() {
		this.keyboards = new HashMap<Integer, KeyEvent>();
		this.motions = new HashMap<Integer, MotionEvent>();
		this.events = new ArrayList<Parcelable>();
	}
	
	public void queue(Parcelable event) {
		if(event instanceof KeyEvent) {
			KeyEvent ke = (KeyEvent)event;
			this.keyboards.put(ke.getKeyCode(), ke);
		} else if (event instanceof MotionEvent) {
			MotionEvent me = (MotionEvent)event;
			this.motions.put(me.getAction(), me);
		}
		this.events.add(event);
	}
}
