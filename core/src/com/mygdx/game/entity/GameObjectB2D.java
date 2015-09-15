package com.mygdx.game.entity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Shape.Type;

public class GameObjectB2D {
	String name;
	Body body;
	int maxSpeed=100;
	float width, height;
	Queue<GameObjectUpdate> gameObjectCreationQueue=new LinkedList<GameObjectUpdate>();

	

	public GameObjectB2D(String name) {
		this.name = name;
	}

	public GameObjectB2D(String name, Body body) {
		this.name = name;
		this.body = body;
	}
	
	
	public void applyForce(float forceX,float forceY){
		body.applyForce(forceX, forceY, getPosition().x, getPosition().y, true);
	}

	public float getSpeed(){
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

	public String toJson() {

		return "{n:" + name + ",t:{p:{x:" + body.getPosition().x + ",y:" + body.getPosition().y + "},a:"
				+ body.getAngle() + "},av:"+body.getAngularVelocity()+",s:{w:" + width + ",h:" + height + "},d:" + getDensity() + ",l:{x:"
				+ body.getLinearVelocity().x + ",y:" + body.getLinearVelocity().y + "}}";
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getWidth() {
		return width;
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
	
	public Queue<GameObjectUpdate> getGameObjectCreationQueue() {
		return gameObjectCreationQueue;
	}

	public void setGameObjectCreationQueue(Queue<GameObjectUpdate> gameObjectCreationQueue) {
		this.gameObjectCreationQueue = gameObjectCreationQueue;
	}

	public synchronized void update(float x,float y,float angle,float angularVelocity,float width,float height,float density,float lx,float ly){
		
		this.body.setTransform(x, y, angle);
		this.body.setAngularVelocity(angularVelocity);
		this.width=width;
		this.height=height;
		this.body.getFixtureList().get(0).setDensity(density);
		this.body.setLinearVelocity(lx, ly);
		
	}
	
public synchronized void update(GameObjectUpdate gameObjectUpdate){
		
		update(gameObjectUpdate.x, gameObjectUpdate.y, gameObjectUpdate.angle, gameObjectUpdate.angularVelocity, gameObjectUpdate.width, gameObjectUpdate.height, gameObjectUpdate.density, gameObjectUpdate.lx, gameObjectUpdate.ly);
		
	}
}
