package net.marstone.solframework;


import java.util.concurrent.Semaphore;

public class Settings {

	/**
	 * The one and only game instance.
	 */
	public static GameSol TheGameInstance = null;
	
	/**
	 * Render mutex lock.
	 */
	public static final Semaphore RenderSemaphore = new Semaphore(1);
}
