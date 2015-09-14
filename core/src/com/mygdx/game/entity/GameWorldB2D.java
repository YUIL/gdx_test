package com.mygdx.game.entity;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class GameWorldB2D {
	private World box2dWorld;
	//private ArrayList<Body> boxes = new ArrayList<Body>();
	Array<GameObjectB2D> gameObjectArray=new Array<GameObjectB2D>();
	GameObjectB2D ground=new GameObjectB2D("ground");
	public GameWorldB2D() {
		super();
		createPhysicsWorld();
	}
	public void update(float delta){
		box2dWorld.step(delta, 8, 3);
	}
	public GameObjectB2D findGameObject(String name){
		for (int i = 0; i < gameObjectArray.size; i++) {
			GameObjectB2D gameObject=gameObjectArray.get(i);
			if (gameObject.name.equals(name)) {
				return gameObject;
			}
		}
		return null;
	}
	
	public boolean removeGameObject(String name){
		GameObjectB2D gameObject=findGameObject(name);
		if (gameObject!=null){
			removeGameObject(gameObject);
			return true;
		}
		return false;
	}
	
	public void removeGameObject(GameObjectB2D gameObject){
		box2dWorld.destroyBody(gameObject.body);
		gameObjectArray.removeValue(gameObject, true);
	}
	
	public GameObjectB2D addBoxGameObject(String name,float x,float y,float width,float height,float density){
		return addBoxGameObject(name, x, y, 0,0,width, height, density,0,0);
	}
	public GameObjectB2D addBoxGameObject(String name,float x,float y,float angle,float angularVelocity,float width,float height,float density,float lx,float ly){
		GameObjectB2D gameObject=createBoxGameObject(name, x, y, angle,angularVelocity,width, height, density,lx,ly);
		gameObjectArray.add(gameObject);
		return gameObject;
	}
	
	public GameObjectB2D createBoxGameObject(String name,float x,float y,float angle,float angularVelocity,float width,float height,float density,float lx,float ly){
		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(width/2, height/2);
		
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyType.DynamicBody;
		boxBodyDef.position.x = x;
		boxBodyDef.position.y = y;
		boxBodyDef.angle=angle;
		Body boxBody = box2dWorld.createBody(boxBodyDef);

		boxBody.createFixture(boxPoly, density);
		
		GameObjectB2D gameObject=createGameObject(name, boxBody);
		gameObject.width=width;
		gameObject.height=height;
		boxBody.setLinearVelocity(lx, ly);
		//boxBody.setFixedRotation(true);
		
		boxBody.setAngularVelocity(angularVelocity);
		return gameObject;
		
		// add the box to our list of boxes
	}
	public Array<GameObjectB2D> getGameObjectArray() {
		return gameObjectArray;
	}
	public void setGameObjectArray(Array<GameObjectB2D> gameObjectArray) {
		this.gameObjectArray = gameObjectArray;
	}
	public GameObjectB2D createGameObject(String name,Body body){
		return new GameObjectB2D(name, body);
	}
	
	private void createGround(){
		PolygonShape groundPoly = new PolygonShape();
		groundPoly.setAsBox(30, 1);


		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyType.StaticBody;
		groundBodyDef.position.x = 0;
		groundBodyDef.position.y = 0;
		ground.body = box2dWorld.createBody(groundBodyDef);


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
	
	public String gameObjectArrayToString(){
		return gameObjectArrayToString(gameObjectArray);
	}
	
	public String gameObjectArrayToString(Array<GameObjectB2D> array){
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
	}
}
