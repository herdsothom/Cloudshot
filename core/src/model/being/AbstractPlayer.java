package model.being;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import model.GameObjectInterface;
import model.collectable.AbstractWeapon;

import com.badlogic.gdx.math.Rectangle;


import java.util.List;

/**
 * Provides basic character structure, location, size etc.
 * 
 * @author Jeremy Southon
 */
public abstract class AbstractPlayer implements GameObjectInterface, EntityInterface, InputProcessor, java.io.Serializable{

	/**
	 * Used to represent the different states of the player
	 */
	public static enum player_state {
		ALIVE, DEAD
	}

	protected player_state playerState = player_state.ALIVE;

	/* variables used in player physics */
	protected Vector2 pos;
	protected Vector2 velocity;
	protected Vector2 gravity;
	protected float speed;
	protected float jumpSpeed = 700;
	final static float MAX_VELOCITY = 7f;

	protected int health;
	protected int damage;
	protected Rectangle boundingBox;

	// Variables of player actions
	protected boolean canJump = true;
	protected  boolean jumping = false;
	protected boolean attacking = false;
	protected boolean grounded = false;
	protected boolean movingLeft;
	protected boolean movingRight;
	/** Players inventory */
	protected List<AbstractWeapon> inventory;
	/** Position of the mouse*/
	protected Vector2 aimedAt = new Vector2(50,50);

	//Box2D
	World world;
	Body body;
	FixtureDef playerProperties;

	public AbstractPlayer(Vector2 position, int width, int height, int hp, float speed, World world) {
		this.world = world;
		health = hp;
		pos = position;
		this.speed=speed;
		velocity = new Vector2(0,0);
		boundingBox = new Rectangle(pos.x,pos.y, width, height);
		// init player constants
		gravity = new Vector2(0, -1);
		//Box2D
		this.world = world;
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(pos);
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.linearDamping = 10;

		body = world.createBody(bodyDef);

		playerProperties = new FixtureDef();
		playerProperties.friction = 10f;
		CircleShape circle = new CircleShape();
		circle.setRadius(5);

		playerProperties.shape = circle;
		body.createFixture(playerProperties);
	}

	/**
	 * Updates forces acting on player, therefore updating his pos over time
	 *
	 * If the player has a velocity we add this to his position
	 * Update player y to that of the Box2D body (for the gravity effect).
	 */
	public void update(Array<Rectangle> tiles) {
		if(movingLeft){
			body.applyLinearImpulse(new Vector2(-1000,0),body.getWorldCenter(),true);
		}
		if(movingRight){
			body.applyLinearImpulse(new Vector2(1000,0),body.getWorldCenter(),true);
		}
		collisionChecks(null);
		updateActionsPlayerDoing();
		//Updating Player Position
		pos.set(body.getPosition());
		//updating players bounding box position
		boundingBox = new Rectangle(pos.x,pos.y+15,boundingBox.width,boundingBox.height);
	}

	/**
	 * Method constantly updates the fields which indicate the actions the player is
	 * preforming such as moving left,right..
	 * */
	private void updateActionsPlayerDoing(){
		//checks if dead
		if(health<=0){
			playerState = player_state.DEAD;
		}
		if(velocity.x<0) movingLeft = true;
		else if (velocity.x>0)movingRight=true;
	}

	/***
	 * Loops ..a
	 */
	protected void collisionChecks(Array<Rectangle> tiles) {
		Array<Contact> contactList = world.getContactList();
		for(int i = 0; i < contactList.size; i++) {
			Contact contact = contactList.get(i);
			if(contact.isTouching() && (contact.getFixtureA() == body.getFixtureList().first() ||
					contact.getFixtureB() == body.getFixtureList().first())) {
				//on ground
				grounded = true;
				jumping = false;
			}else {
				grounded = false;
			}
		}
	}
	/**
	 * Updates moving left and right fields appropriately
	 * and updates the velocity by speed;
	 * */
	public void moveRight() {
		movingLeft = false;
		movingRight = true;
		//velocity.x += speed;
		body.applyLinearImpulse(new Vector2(1000,0),body.getWorldCenter(),true);
	}
	/**
	 * Updates moving left and right fields appropriately
	 * and updates the velocity by speed;
	 * */
	public void moveLeft() {
		movingRight = false;
		movingLeft = true;
		body.applyLinearImpulse(new Vector2(-1000,0),body.getWorldCenter(),true);
	}

	/**
	 * applies players jump speed onto Box2D body
	 * */
	public void jump(){
		body.applyLinearImpulse(new Vector2(0,jumpSpeed),body.getWorldCenter(),true);
		this.grounded = false;
		jumping = true;
	}

	/**
	 * Inflicts param damage onto players current health
	 *
	 * @param damage to inflict on player
	 * */
	public void hit(int damage){
		this.health-=damage;
	}

	/**
	 * @return List of AbstractWeapons which the player has in inventory
	 * */
	public List<AbstractWeapon> getInventory() {
		return inventory;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public player_state getPlayerState() {
		return playerState;
	}

	public void setPlayerState(player_state playerState) {
		this.playerState = playerState;
	}

	public Vector2 getPos() {
		return pos;
	}

	public void setPos(Vector2 pos) {
		this.pos = pos;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public Vector2 getAimedAt(){ return aimedAt; }

	public Rectangle getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(Rectangle boundingBox) {
		this.boundingBox = boundingBox;
	}

	public boolean getIsAttacking(){ return attacking; }

	public abstract boolean attack(AbstractEnemy enemy);

	public abstract void shoot();

	@Override
	public boolean keyDown(int keycode) {
		//Player is dead cant move
		if(playerState == player_state.DEAD)return false;
		switch (keycode){
			case Input.Keys.A:
				moveLeft();

				break;
			case Input.Keys.D:
				moveRight();
				break;
			case Input.Keys.W:
				if(grounded)
					jump();
				break;
			case Input.Keys.SPACE:
				attacking = true;
				break;
			case Input.Keys.F:
				shoot();
			default:
				velocity = new Vector2(0,0);
				break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode){
			case Input.Keys.A:
				movingLeft = false;
				break;
			case Input.Keys.D:
				movingRight = false;
				break;
			case Input.Keys.W:
				jumping = false;
				break;
			case Input.Keys.SPACE:
				attacking = false;
				break;
			default:
				velocity = new Vector2(0,0);
				break;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		aimedAt = new Vector2(screenX,screenY);
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
