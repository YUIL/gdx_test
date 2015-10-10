package com.yuil.game.entity;



import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.yuil.game.entity.info.B2dBoxBaseInformation;

public class GameWorldB2D {
	private World box2dWorld;
//	public boolean lock=false;
	//private ArrayList<Body> boxes = new ArrayList<Body>();
	Queue<B2dBoxBaseInformation> gameObjectCreationQueue=new LinkedList<B2dBoxBaseInformation>();
	Queue<B2DGameObject> gameObjectRemoveQueue=new LinkedList<B2DGameObject>();
	Array<B2DGameObject> gameObjectArray=new Array<B2DGameObject>();
	B2DGameObject ground=new B2DGameObject(0);
	
	
	
	public Queue<B2DGameObject> getGameObjectRemoveQueue() {
		return gameObjectRemoveQueue;
	}
	public void setGameObjectRemoveQueue(Queue<B2DGameObject> gameObjectRemoveQueue) {
		this.gameObjectRemoveQueue = gameObjectRemoveQueue;
	}
	public Queue<B2dBoxBaseInformation> getGameObjectCreationQueue() {
		return gameObjectCreationQueue;
	}
	public void setGameObjectCreationQueue(Queue<B2dBoxBaseInformation> gameObjectCreationQueue) {
		this.gameObjectCreationQueue = gameObjectCreationQueue;
	}
	public GameWorldB2D() {
		super();
		createPhysicsWorld();
	}
	public synchronized void update(float delta){
		for (int i = 0; i < gameObjectRemoveQueue.size(); i++) {
			B2DGameObject gameObject=gameObjectRemoveQueue.poll();
			removeGameObject(gameObject);
		}
		for (int i = 0; i < gameObjectCreationQueue.size(); i++) {
			B2dBoxBaseInformation gameObjectCreation=gameObjectCreationQueue.poll();
			addBoxGameObject(gameObjectCreation);
		}
		for (int i = 0; i < gameObjectArray.size; i++) {
			B2DGameObject gameObject=gameObjectArray.get(i);
			if (gameObject.getGameObjectUpdateQueue().size()>0) {
				gameObject.update(gameObject.getGameObjectUpdateQueue().poll());
			}
		}
		box2dWorld.step(delta, 8, 3);
	}
	public synchronized void updateGameObject(B2DGameObject gameObject,float x,float y,float angle,float angularVelocity,float width,float height,float density,float lx,float ly){
	
		gameObject.update(x, y, angle, angularVelocity, width, height, density, lx, ly);

	}
	public B2DGameObject findGameObject(long id){
		for (int i = 0; i < gameObjectArray.size; i++) {
			B2DGameObject gameObject=gameObjectArray.get(i);
			if (gameObject.id==id) {
				return gameObject;
			}
		}
		return null;
	}
	
	public boolean removeGameObject(long id){
		B2DGameObject gameObject=findGameObject(id);
		if (gameObject!=null){
			removeGameObject(gameObject);
			return true;
		}
		return false;
	}
/*	public B2DGameObject findGameObject(String name){
		for (int i = 0; i < gameObjectArray.size; i++) {
			B2DGameObject gameObject=gameObjectArray.get(i);
			if (gameObject.name.equals(name)) {
				return gameObject;
			}
		}
		return null;
	}
	
	public boolean removeGameObject(String name){
		B2DGameObject gameObject=findGameObject(name);
		if (gameObject!=null){
			removeGameObject(gameObject);
			return true;
		}
		return false;
	}*/
	
	public void removeGameObject(B2DGameObject gameObject){
		if (gameObjectArray.contains(gameObject, true)) {
			box2dWorld.destroyBody(gameObject.body);
			gameObjectArray.removeValue(gameObject, true);
		}
		
	}
	
	public B2DGameObject addBoxGameObject(long id,float x,float y,float width,float height,float density){
		return addBoxGameObject(id, x, y, 0,0,width, height, density,0,0);
	}
	public B2DGameObject addBoxGameObject(B2dBoxBaseInformation creation){
		return addBoxGameObject(creation.gameObjectId, 
				creation.x, creation.y, creation.angle, creation.angularVelocity, creation.width, creation.height, creation.density, creation.lx, creation.ly);
	}
	public B2DGameObject addBoxGameObject(long id,float x,float y,float angle,float angularVelocity,float width,float height,float density,float lx,float ly){
		B2DGameObject gameObject=createBoxGameObject(id, x, y, angle,angularVelocity,width, height, density,lx,ly);
		gameObjectArray.add(gameObject);
		return gameObject;
	}
	
	public B2DGameObject createBoxGameObject(long id,float x,float y,float angle,float angularVelocity,float width,float height,float density,float lx,float ly){
		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(width/2, height/2);
		
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyType.DynamicBody;
		boxBodyDef.position.x = x;
		boxBodyDef.position.y = y;
		boxBodyDef.angle=angle;
		Body boxBody = box2dWorld.createBody(boxBodyDef);

		boxBody.createFixture(boxPoly, density);
		
		B2DGameObject gameObject=createGameObject(id, boxBody);
		gameObject.width=width;
		gameObject.height=height;
		boxBody.setLinearVelocity(lx, ly);
		//boxBody.setFixedRotation(true);
		
		boxBody.setAngularVelocity(angularVelocity);
		return gameObject;
		
		// add the box to our list of boxes
	}
	public Array<B2DGameObject> getGameObjectArray() {
		return gameObjectArray;
	}
	public void setGameObjectArray(Array<B2DGameObject> gameObjectArray) {
		this.gameObjectArray = gameObjectArray;
	}
	
	public B2DGameObject createGameObject(long id,Body body){
		return new B2DGameObject(id, body);
	}
/*	public B2DGameObject createGameObject(String name,Body body){
		return new B2DGameObject(name, body);
	}*/
	
	private void createGround(){
		PolygonShape groundPoly = new PolygonShape();
		groundPoly.setAsBox(30, 1);


		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyType.StaticBody;
		groundBodyDef.position.x = 0;
		groundBodyDef.position.y = 0;
		if(!box2dWorld.isLocked()){
			ground.body = box2dWorld.createBody(groundBodyDef);
		}
		


		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = groundPoly;
		fixtureDef.filter.groupIndex = 0;
		
		ground.body.createFixture(fixtureDef);
		groundPoly.dispose();
		
	}
	public World getBox2dWorld() {
		return box2dWorld;
	}
	public void setBox2dWorld(World box2dWorld) {
		this.box2dWorld = box2dWorld;
	}
	private void createPhysicsWorld () {

		box2dWorld = new World(new Vector2(0, -10), true);
		System.out.println("box2dWorld has create");
		createGround();

		//addBoxGameObject("test", 5, 50, 2, 2, 1);
		
		
		ChainShape chainShape = new ChainShape();
		chainShape.createLoop(new Vector2[] {new Vector2(-5, 8),new Vector2(-10, 10), new Vector2(-10, 5), new Vector2(10, 5), new Vector2(10, 11),});
		BodyDef chainBodyDef = new BodyDef();
		chainBodyDef.type = BodyType.StaticBody;
		Body chainBody =box2dWorld.createBody(chainBodyDef);
		chainBody.createFixture(chainShape, 0);
		chainShape.dispose();

		/*ChainShape chainShape = new ChainShape();
		chainShape.createLoop(new Vector2[] {new Vector2(-10, 10), new Vector2(-10, 5), new Vector2(10, 5), new Vector2(10, 11),});
		BodyDef chainBodyDef = new BodyDef();
		chainBodyDef.type = BodyType.StaticBody;
		Body chainBody = world.createBody(chainBodyDef);
		chainBody.createFixture(chainShape, 0);
		chainShape.dispose();*/

		box2dWorld.setContactListener(new ContactListener() {
			@Override
			public void beginContact (Contact contact) {
			}

			@Override
			public void endContact (Contact contact) {
			}

			@Override
			public void preSolve (Contact contact, Manifold oldManifold) {
			}

			@Override
			public void postSolve (Contact contact, ContactImpulse impulse) {
			}
		});
	}
	
	
	
/*	public String gameObjectArrayToString(){
		return gameObjectArrayToString(gameObjectArray);
	}*/
	
	/*public String gameObjectArrayToString(Array<B2DGameObject> array){
		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append("[");
		if (array.size>0){
			for (int i = 0; i < array.size; i++) {
				stringBuffer.append(array.get(i).toJson());
				stringBuffer.append(",");
			}
			stringBuffer.delete(stringBuffer.length()-1, stringBuffer.length());
		}
		stringBuffer.append("]");
		return new String(stringBuffer);
	}*/
}
