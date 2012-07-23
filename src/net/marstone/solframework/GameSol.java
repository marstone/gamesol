package net.marstone.solframework;


import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.widget.Toast;

/**
 * This is Marstone.NET Android Game Framework. This framework provides: 
 * 1.basic game item description and render 
 * 2.simple math and physics computing 
 * 3.state machine to control the game flow. To some extent, a game is a state machine. 
 * 4.message queue for user input and other events.
 * 
 * @since 2010/05/07, @author marstone.net
 * the original code name "sol" is "Soccer Online" for short
 * which started in 2006 by marstone & george, with .NET XNA
 */
public class GameSol extends StateMachine {

	Activity gameActivity = null;

	Activity applicationContext = null;
	
	Input input = null;
	
	/**
	 * The only Game Instance
	 */
	public GameSol(Activity context, InputStream config) {
		// construct the state machine
		super();
		
		// Save application context
		this.gameActivity = context;
		
		this.input = new Input();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(config);
			Element root = dom.getDocumentElement();
			
			// Load states
			NodeList states = root.getElementsByTagName("states").item(0).getChildNodes();
			for (int i = 0; i < states.getLength(); i++) {
				Node stateNode = states.item(i);
				if(stateNode instanceof Element)
				{
					Element stateEl = (Element)stateNode;
					String id =  stateEl.getAttribute("id");
					State state = new State(id);
					this.addState(state);
					// Create coordinator by reflection, which handles the detailed game logic.
					String coordClass = stateEl.getAttribute("coordinator");
					if(null == coordClass || "".equals(coordClass))
						coordClass = CoordinatorBase.class.getName();
					Coordinator coordinator = this.createCoordinator(coordClass);
					Scene scene = new Scene(state, coordinator);
					coordinator.init(scene);
					state.setScene(scene);
				}
			}
			// Initial state
			String initial = root.getAttribute("initial");
			this.setInitialState(initial);
			
			// Load actions
			NodeList actions = root.getElementsByTagName("actions").item(0).getChildNodes();
			for (int i = 0; i < actions.getLength(); i++) {
				Node actionNode = actions.item(i);
				if(actionNode instanceof Element)
				{
					Element actionEl = (Element)actionNode;
					String id = actionEl.getAttribute("id");
					String headId = actionEl.getAttribute("head");
					String tailId = actionEl.getAttribute("tail");
					Action action = new Action(id);
					this.addAction(action, headId, tailId);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * The game state machine controls the full game logics.
	 */
	public StateMachine stateMachine = null;
	
	SolView gameView = null;
	SolRenderer gameRenderer = null;
	
	public Scene getCurrentScene() {
		return this.currentState.scene;
	}
	
	public void run() {
		// this.gameActivity = new Intent(this.applicationContext, GameActivity.class);
		// this.applicationContext.startActivity(this.gameActivity);
		SolView view = new SolView(this.gameActivity, this);
		this.gameRenderer = new SolRenderer(this);
		view.setRenderer(this.gameRenderer);
		view.setFocusable(true);
		this.gameView = view;
		this.gameActivity.setContentView(view);
		this.onStart();
	}

	public State onStart() {
		Toast toast = Toast.makeText(this.gameActivity, "game start.", Toast.LENGTH_SHORT);
		toast.show();
		this.currentState = this.getInitialState();
		return this.currentState;
	}
	
	public State onPause() {
		return null;
	}

	public State onResume() {
		return null;
	}
	
	/**
	 * 
	 *	Class [] classParm = null;
	 *	Object [] objectParm = null;
	 *	Class cl = Class.forName(className);
	 *	java.lang.reflect.Constructor co = cl.getConstructor(classParm);
	 *	Coordinator coordinator = (Coordinator)co.newInstance(objectParm);
	 *
	 * @param className
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Coordinator createCoordinator(String className) {
		try {
			Class cl = Class.forName(className);
			java.lang.reflect.Constructor co = cl.getConstructor();
			Coordinator coordinator = (Coordinator)co.newInstance();
			return coordinator;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	public State update(int time, int elapsedTime) {
		Scene scene = this.getCurrentScene();
		if(null != scene)
			return scene.update(time, elapsedTime);
		else 
			return null;
	}

	public void render(GL10 gl) {
		Scene scene = this.getCurrentScene();
		if(null != scene)
			scene.render(gl);
	}

	public void onRendererChanged(GL10 gl, int w, int h) {
		Scene scene = this.getCurrentScene();
		if(null != scene)
			scene.getCoordinator().onRendererChanged(gl, w, h);
	}

	public void onRendererCreated(GL10 gl, EGLConfig config) {
		Scene scene = this.getCurrentScene();
		if(null != scene)
			scene.getCoordinator().onRendererCreated(gl, config);
	}

	public static String echo(String hi) {
		return hi;
	}
}
