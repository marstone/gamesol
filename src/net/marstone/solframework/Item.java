package net.marstone.solframework;


import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.Matrix;

/// <summary>
/// SceneItem is any item that can be in a scenegraph
/// Be aware SceneItem extends ArrayList, so it is a TREE.
/// </summary>
@SuppressWarnings("serial")
public abstract class Item extends ArrayList<Item> {

	/**
	 * Collision Radius of this item
	 */
	protected float radius;

	/**
	 * Should this item be deleted?
	 */
	protected boolean delete;

	/**
	 * Simulation paused for this items, nothing will update
	 */
	private boolean paused;

	/**
	 * The root SceneItem
	 */
	protected Item root;

	/**
	 * The parent SceneItem
	 */
	protected Item parent;

	// / <summary>
	// / Shape is the actual renderable object
	// / </summary>
	protected Shape shape;

	// / <summary>
	// / The position of this item
	// / </summary>
	android.opengl.GLU g;

	protected Vector3 position;

	// / <summary>
	// / The velocity of this item
	// / </summary>
	protected Vector3 velocity;

	// / <summary>
	// / The acceleration of the item
	// / </summary>
	protected Vector3 acceleration;

	// / <summary>
	// / The current rotation of this item
	// / </summary>
	protected Vector3 rotation;

	// / <summary>
	// / The scaling transformation for this object
	// / </summary>
	protected Vector3 scale = new Vector3(1f, 1f, 1f);

	// / <summary>
	// / The center of rotation and scaling
	// / </summary>
	protected Vector3 center;

	public Scene scene;

	// / <summary>
	// / Default constructor, does nothing special
	// / </summary>
	public Item(Scene scene) {
		this.scene = scene;
	}

	/**
	 * Creates a SceneItem with a shape to be rendered at an initial position
	 * @param shape The shape to be rendered for this item
	 * @param initialPosition The initial position of the item
	 */
	public Item(Scene scene, Shape shape, Vector3 initialPosition) {
		this.shape = shape;
		this.position = initialPosition;
		this.scene = scene;
	}

	/**
	 * Creates a SceneItem with a shape to be ed
	 * @param shape The shape to be ed for this item
	 */
	public Item(Scene scene, Shape shape) {
		this.shape = shape;
		this.scene = scene;
	}
	


	/**
	 * Creates a SceneItem with a shape to be ed at an initial position
	 * @param initialPosition The initial position of the item
	 */

	public Item(Scene scene, Vector3 initialPosition) {
		this.position = initialPosition;
		this.scene = scene;
	}

	
	/**
	 * Adds an item to the Scene Node
	 * @param childItem : The item to add
	 */
	public boolean add(Item childItem) {
		// A new custom 'add' that sets the parent and the root properties
		// on the child item
		childItem.parent = this;
		if (this.root == null) {
			childItem.root = this;
		} else {
			childItem.root = this.root;
		}

		// Call the 'real' add method on the dictionary
		super.add(childItem);
		return true;
	}

	/**
	 * Updates any values associated with this scene item and its children
	 * @param time: Game time
	 * @param elapsedTime : Elapsed game time since last call
	 */
	public void update(int time, int elapsedTime) {
		if (!paused) {
			// Do the basic acceleration/velocity/position updates
			this.velocity.add(Vector3.mul(this.acceleration, (float) elapsedTime));
			this.position.add(Vector3.mul(this.velocity, (float) elapsedTime));
		}

		// If this item has something to draw then update it
		if (shape != null) {
			/*
			 * shape.World = Matrix.CreateTranslation(-center) *
			 * Matrix.CreateScale(scale) * Matrix.CreateRotationX(rotation.X) *
			 * Matrix.CreateRotationY(rotation.Y) *
			 * Matrix.CreateRotationZ(rotation.Z) *
			 * Matrix.CreateTranslation(position + center);
			 */
			if (!paused) {
				shape.update(time, elapsedTime);
			}
		}

		// Update each child item
		for (Item item : this) {
			item.update(time, elapsedTime);
		}

		// Remove any items that need deletion
		for (int i = this.size() - 1; i >= 0; i--)
			if (this.get(i).delete)
				this.remove(i);
	}


	/**
	 * Render any items associated with this scene item and its children
	 */
	public void render(GL10 gl) {
		// If this item has something to draw then draw it
		if (shape != null) {
			shape.render();
		}

		// Then render all of the child nodes
		for (Item item : this) {
			item.render(gl);
		}
	}

	/**
	 * Checks if there is a collision between the this and the passed in item
	 * @return True if there is a collision
	 */ 
	public boolean collide(Item item) { 
		//Until we get collision meshe sorted just do a simple sphere (well circle!) check 
		
		// item.position.negate()
		
		if ((position.add(item.position.negate())).length() < radius + item.radius) 
			return true;
		
		
		//If we are a ship and we are a long, thin pencil, 
		//we have additional extents, check those, too! 
		/*
		Ship shipItem = item as Ship; 
		if (shipItem != null && shipItem.ExtendedExtent != null) { 
			Matrix localRotation = Matrix.CreateRotationZ(shipItem.Rotation.Z);
			Vector4 extendedPosition = Vector4.Transform(shipItem.ExtendedExtent[0],
							localRotation); 
			Vector3 localPosition = shipItem.Position + 
				new Vector3(extendedPosition.X, extendedPosition.Y, extendedPosition.Z); 
			if((Position - localPosition).Length() < radius + item.Radius) 
				return true;
		 
			extendedPosition = Vector4.Transform(shipItem.ExtendedExtent[1],
							localRotation); 
			localPosition = shipItem.Position + 
				new Vector3(extendedPosition.X, extendedPosition.Y, extendedPosition.Z); 
			if((Position - localPosition).Length() < radius + item.Radius) 
				return true;
		}
		 
		Ship ship = this as Ship; 
		if (ship != null && ship.ExtendedExtent != null) {
			Matrix localRotation = Matrix.CreateRotationZ(ship.Rotation.Z);
			Vector4 extendedPosition = Vector4.Transform(ship.ExtendedExtent[0],localRotation); 
		Vector3 localPosition = ship.Position + 
			new Vector3(extendedPosition.X, extendedPosition.Y, extendedPosition.Z); 
		if((localPosition - item.Position).Length() < radius + item.Radius) 
			return true;
	 
		extendedPosition = Vector4.Transform(ship.ExtendedExtent[1], localRotation); 
		localPosition = ship.Position + 
			new Vector3(extendedPosition.X, extendedPosition.Y, extendedPosition.Z); 
		if((localPosition - item.Position).Length() < radius + item.Radius) 
			return true; 
		}
		*/
		
		return false; 
	}
	
	float[] m = new float[16];

	public void rotate(float angle, float x, float y, float z) {
		// int mOffset = 0;
		Matrix.rotateM(m, 0, angle, x, y, z);
	
	}

	public void move(float x, float y, float z) {
		this.position.x += x;
		this.position.y += y;
		this.position.z += z;
	}

	public void moveTo(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}
	
    public abstract void init();
}
