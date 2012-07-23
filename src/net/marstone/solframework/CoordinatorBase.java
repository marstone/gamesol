package net.marstone.solframework;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CoordinatorBase implements Coordinator{
	
	protected Scene scene = null;
	
	@Override
	public void init(Scene scene) {
		this.scene = scene;
	}
	
	@Override
	public void show() {
	}

	@Override
	public void shut() {
	}

	@Override
	public State update(int time, int elapsedTime) {
		return this.scene.getState();
	}

	@Override
	public void onRendererChanged(GL10 gl, int w, int h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRendererCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		
	}

}
