package com.yuil.game.entity;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.yuil.game.entity.info.B2dBoxBaseInformation;

public class B2dGameObject {
	// String name;
	long id;
	Body body;
	int maxSpeed = 10;
	int strength = 100;
	float width, height;
	Queue<B2dBoxBaseInformation> gameObjectUpdateQueue = new ConcurrentLinkedQueue<B2dBoxBaseInformation>();
	MotionState motionState = new MotionState();
	
	public void changeApplyForceState(byte state){
		motionState.applyForceState=state;
	}

	public void doMotion() {
		switch (motionState.applyForceState) {
		case 1:
			if (getSpeed() < maxSpeed) {
				if (getBody().getLinearVelocity().x < 10&&getBody().getLinearVelocity().x > -10) {
					applyForce(-1 * strength, 0);
				}
				
			}

			break;
		case 2:
			if (getSpeed() < maxSpeed) {
				if (getBody().getLinearVelocity().x < 10&&getBody().getLinearVelocity().x > -10) {
					applyForce(strength, 0);
				}
			
			}

			break;
		default:

			break;
		}
	}

	public class MotionState {
		volatile byte applyForceState;

	}

	public B2dGameObject(long id) {
		this.id = id;
	}

	public B2dGameObject(long id, Body body) {
		this.id = id;
		this.body = body;
		body.setUserData(this);
	}

	/*
	 * public B2DGameObject(String name) { this.name = name; }
	 * 
	 * public B2DGameObject(String name, Body body) { this.name = name;
	 * this.body = body; }
	 */

	public void applyForce(float forceX, float forceY) {
		body.applyForce(forceX, forceY, getPosition().x, getPosition().y, true);
	}

	public float getSpeed() {
		return Vector2.len(body.getLinearVelocity().x, body.getLinearVelocity().y);

	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public Vector2 getPosition() {
		return body.getPosition();

	}

	public float getDensity() {
		return body.getFixtureList().get(0).getDensity();
	}

	public void setPosition(Vector2 position) {
		this.body.setTransform(position, body.getAngle());
	}

	public void setTransform(Vector2 position, float angle) {
		this.body.setTransform(position, angle);
	}

	/*
	 * public String toJson() {
	 * 
	 * return "{n:" + name + ",t:{p:{x:" + body.getPosition().x + ",y:" +
	 * body.getPosition().y + "},a:" + body.getAngle() +
	 * "},av:"+body.getAngularVelocity()+",s:{w:" + width + ",h:" + height +
	 * "},d:" + getDensity() + ",l:{x:" + body.getLinearVelocity().x + ",y:" +
	 * body.getLinearVelocity().y + "}}";
	 * 
	 * }
	 */

	/*
	 * public String getName() { return name; }
	 * 
	 * public void setName(String name) { this.name = name; }
	 */

	public float getWidth() {
		return width;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public Queue<B2dBoxBaseInformation> getGameObjectUpdateQueue() {
		return gameObjectUpdateQueue;
	}
	/*
	 * public void setGameObjectCreationQueue(Queue<GameObjectUpdate>
	 * gameObjectCreationQueue) { this.gameObjectCreationQueue =
	 * gameObjectCreationQueue; }
	 */

	public synchronized void update(float x, float y, float angle, float angularVelocity, float width, float height,
			float density, float lx, float ly) {

		this.body.setTransform(x, y, angle);
		this.body.setAngularVelocity(angularVelocity);
		this.width = width;
		this.height = height;
		this.body.getFixtureList().get(0).setDensity(density);
		this.body.setLinearVelocity(lx, ly);

	}

	public synchronized void update(B2dBoxBaseInformation gameObjectUpdate) {

		update(gameObjectUpdate.x, gameObjectUpdate.y, gameObjectUpdate.angle, gameObjectUpdate.angularVelocity,
				gameObjectUpdate.width, gameObjectUpdate.height, gameObjectUpdate.density, gameObjectUpdate.lx,
				gameObjectUpdate.ly);

	}
}
