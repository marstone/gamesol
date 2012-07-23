package net.marstone.solframework;



/// <summary>
/// Shape is the base class of any object that is renderable
/// </summary>
public abstract class Shape {
	// / <summary>
	// / The vertex buffer used by this shape
	// / </summary>
	// protected VertexBuffer buffer;
	// protected VertexDeclaration vertexDecl;

	// / <summary>
	// / The current world matrix used to render this shape
	// / </summary>
	android.opengl.Matrix w;
	// protected Matrix world;
	protected Vector3 position;

	GameSol game = null;

	/// <summary>
	/// Creates a new shape. Calls the virtual Create method to generate any
	// vertex buffers etc
	/// </summary>
	public Shape(GameSol game) {
		this.game = game;
		this.create();
	}

	/// <summary>
	/// Creates the vertex buffers etc. This routine is called on object
	// creation and on device reset etc
	/// </summary>
	abstract void create();

	/// <summary>
	/// Renders the shape. Base class does nothing
	/// </summary>
	public void render() {
	}

	/// <summary>
	/// Updates the shape. Base class does nothing
	/// </summary>
	/// <param name="time">Game Time in million seconds</param>
	/// <param name="elapsedTime">Elapsed game time since last call</param>
	public void update(int time, int elapsedTime) {
		// Nothing for now
	}

}
