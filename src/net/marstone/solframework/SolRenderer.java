package net.marstone.solframework;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

public class SolRenderer implements GLSurfaceView.Renderer {

	int width;
	int height;

	GameSol game = null;
	
	public SolRenderer(GameSol game) {
		this.game = game;
	}
	
	@Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {   
		this.game.onRendererCreated(gl, config);
    }

	@Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
		Log.i("SolRenderer", "onSurfaceChanged::(w, h) = (" + w + ", " + h +")" );
    	
    	this.width = w;
    	this.height = h;
    	gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glViewport(0,0,w,h);
		GLU.gluPerspective(gl, 45.0f, ((float)w)/h, 1f, 100f);
		
    	this.game.onRendererChanged(gl, w, h);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		Log.i("SolRenderer", "onDrawFrame::current state is " + this.game.currentState);
		
    	this.game.update(0, 0);
		this.game.render(gl);
		
		this.game.input.clear();
	}
		
	/**
	 * Make a direct NIO FloatBuffer from an array of floats
	 * @param arr The array
	 * @return The newly created FloatBuffer
	 */
	public static FloatBuffer makeFloatBuffer(float[] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(arr);
		fb.position(0);
		return fb;
	}

	/**
	 * Make a direct NIO IntBuffer from an array of ints
	 * @param arr The array
	 * @return The newly created IntBuffer
	 */
	public static IntBuffer makeFloatBuffer(int[] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
		bb.order(ByteOrder.nativeOrder());
		IntBuffer ib = bb.asIntBuffer();
		ib.put(arr);
		ib.position(0);
		return ib;
	}
	
	public static ByteBuffer makeByteBuffer(Bitmap bmp) {
		ByteBuffer bb = ByteBuffer.allocateDirect(bmp.getHeight()*bmp.getWidth()*4);
		bb.order(ByteOrder.BIG_ENDIAN);
		IntBuffer ib = bb.asIntBuffer();

		for (int y = 0; y < bmp.getHeight(); y++)
			for (int x=0;x<bmp.getWidth();x++) {
				int pix = bmp.getPixel(x, bmp.getHeight()-y-1);
				// Convert ARGB -> RGBA
				byte alpha = (byte)((pix >> 24)&0xFF);
				byte red = (byte)((pix >> 16)&0xFF);
				byte green = (byte)((pix >> 8)&0xFF);
				byte blue = (byte)((pix)&0xFF);
								
				ib.put(((red&0xFF) << 24) | 
					   ((green&0xFF) << 16) |
					   ((blue&0xFF) << 8) |
					   ((alpha&0xFF)));
			}
		ib.position(0);
		bb.position(0);
		return bb;
	}

	/**
	 * Create a texture and send it to the graphics system
	 * @param gl The GL object
	 * @param bmp The bitmap of the texture
	 * @param reverseRGB Should the RGB values be reversed?  (necessary workaround for loading .pngs...)
	 * @return The newly created identifier for the texture.
	 */
	public static int loadTexture(GL10 gl, Bitmap bmp) {
		int[] tmp_tex = new int[1];

		gl.glGenTextures(1, tmp_tex, 0);
		int tex = tmp_tex[0];
		loadTexture(tex, GL10.GL_TEXTURE_2D, bmp, gl);
		return tex;
	}

	
	static public void loadTexture(int texture, int type, Bitmap bmp, GL10 gl) {
		loadTexture(texture, type, bmp.getWidth(), bmp.getHeight(), makeByteBuffer(bmp), gl);
	}
	
	static public void loadTexture(int texture, int type, int width, int height, ByteBuffer bb, GL10 gl) {
		gl.glBindTexture(type, texture);
		gl.glTexImage2D(type, 0, GL10.GL_RGBA, width, height, 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, null);
		gl.glTexSubImage2D(type, 0, 0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, bb);
		gl.glTexParameterf(type, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(type, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	}

	
}