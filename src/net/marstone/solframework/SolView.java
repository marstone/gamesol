package net.marstone.solframework;


import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;


public class SolView extends GLSurfaceView {
	protected Context context;
	protected GameSol game;
	protected int fps;

	/**
	 * Constructor
	 */
	public SolView(Context context, GameSol game) {
		this(context, game, -1);
	}

	/**
	 * Constructor for animated views
	 * @param c The View's context
	 * @param fps The frames per second for the animation.
	 */
	public SolView(Context context, GameSol game, int fps) {
		super(context);
		this.context = context;
		this.game = game;
		this.fps = fps;
	}


	protected int loadTexture(GL10 gl, int resource) {
		return SolRenderer.loadTexture(gl, BitmapFactory.decodeResource(context.getResources(), resource));
	}
	
	public void loadTexture(int texture, int type,  int resource, GL10 gl) {
		SolRenderer.loadTexture(texture, type, BitmapFactory.decodeResource(context.getResources(), resource), gl);
	}

	
	@Override
	public boolean onTouchEvent(final MotionEvent event) {
	    queueEvent(new Runnable() {
	        public void run() {
	        	game.input.queue(event);
	        }
	    });
	    return super.onTouchEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, final KeyEvent event) {
	    queueEvent(new Runnable() {
	        public void run() {
	        	game.input.queue(event);
	        }
	    });
		return super.onKeyDown(keyCode, event);
	}

}