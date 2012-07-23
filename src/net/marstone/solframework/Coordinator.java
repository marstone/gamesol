package net.marstone.solframework;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * The real logic processor for a Scene
 * @author marstone
 */
public interface Coordinator {
	
	void init(Scene scene);
	
	State update(int time, int elapsedTime);
	
	void shut();
	
	void show();
	
    void onRendererCreated(GL10 gl, EGLConfig config);

    void onRendererChanged(GL10 gl, int w, int h);
}
