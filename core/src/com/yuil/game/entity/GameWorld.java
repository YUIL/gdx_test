package com.yuil.game.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


public class GameWorld {
	Array<GameObject> gameObjectArray=new Array<GameObject>();
	Array<GameObject> beCollidedGameObjectArray=new Array<GameObject>();
	Vector3 targePosition=new Vector3();
	Rectangle collisionDetectionRectangle=new Rectangle();
	Vector3 gravity=new Vector3(0,-100,0);
	int horizontal=-200;
	boolean noGravityCalcuate=false;
	public GameWorld(){
		
	}
	
	public GameObject findGameObject(String name){
		return findGameObject(name, gameObjectArray);
	}
	
	public GameObject findGameObject(String name,Array<GameObject> gameObjectArray){
		if(name!=null){
			for (int i = 0; i < gameObjectArray.size; i++) {
				if(name.equals(gameObjectArray.get(i).getName())){
					return gameObjectArray.get(i);
				}
			}
		}
		return null;
	}
	
	public boolean addGameObject(GameObject gameObject){
		if(findGameObject(gameObject.getName())==null){
			gameObjectArray.add(gameObject);
		}
		return false;
	}

	public Array<GameObject> getGameObjectArray() {
		return gameObjectArray;
	}

	public void setGameObjectArray(Array<GameObject> gameObjectArray) {
		this.gameObjectArray = gameObjectArray;
	}
	
	public void update(float delta){
		for (int i = 0; i < gameObjectArray.size; i++) {
			GameObject gameObject=gameObjectArray.get(i);			
			if(!gameObject.getInertiaForce().isZero()){
				
				targePosition.x=gameObject.getPosition().x+gameObject.getInertiaForce().x*gameObject.weight*delta;
				targePosition.y=gameObject.getPosition().y+gameObject.getInertiaForce().y*gameObject.weight*delta;
				targePosition.z=gameObject.getPosition().z+gameObject.getInertiaForce().z*gameObject.weight*delta;	
				//collisionDetection(gameObject);
				collisionDetectionRectangle.setPosition(targePosition.x, targePosition.y);
				collisionDetectionRectangle.setSize(gameObject.getRectangle().width, gameObject.getRectangle().height);
				collisionDetection(gameObject, collisionDetectionRectangle);
				if(beCollidedGameObjectArray.size==0&&targePosition.y>=horizontal){
					gameObject.setPosition(targePosition);			
				}else{
					gameObject.inertiaForce.y=0;
				}		
			}
			if(gameObject.getPosition().y+(gravity.y*delta)>horizontal){//计算重力
				collisionDetectionRectangle.setPosition(gameObject.getPosition().x, gameObject.getPosition().y+(gravity.y*delta));
				collisionDetectionRectangle.setSize(gameObject.getRectangle().width, gameObject.getRectangle().height);
				if (!haveCollision(gameObject, collisionDetectionRectangle)) {
					gameObject.inertiaForce.y+=(gravity.y*gameObject.getWeight()*delta);
				}
				
			}	
		}
	}
	public Array<GameObject> getBeCollidedGameObjectArray() {
		return beCollidedGameObjectArray;
	}

	public void setBeCollidedGameObjectArray(Array<GameObject> beCollidedGameObjectArray) {
		this.beCollidedGameObjectArray = beCollidedGameObjectArray;
	}

	public void collisionDetection(GameObject gameObject){
		for (int i = 0; i <gameObjectArray.size; i++) {
			if (gameObject!=gameObjectArray.get(i)) {
				if(gameObject.getRectangle().overlaps(gameObjectArray.get(i).getRectangle())){
					beCollidedGameObjectArray.add(gameObjectArray.get(i));
				}
			}
		}
	}
	public void collisionDetection(GameObject gameObject,Rectangle rectangle){
		collisionDetection(gameObject, rectangle, gameObjectArray,beCollidedGameObjectArray);
	}
	public void collisionDetection(GameObject gameObject,Rectangle rectangle,Array<GameObject> gameObjectArray,Array<GameObject> beCollidedGameObjectArray){
		for (int i = 0; i <gameObjectArray.size; i++) {
			if (gameObject!=gameObjectArray.get(i)) {
				if(rectangle.overlaps(gameObjectArray.get(i).getRectangle())){
					beCollidedGameObjectArray.add(gameObjectArray.get(i));
				}
			}
		}
	}
	
	public boolean haveCollision(GameObject gameObject,Rectangle rectangle){
		for (int i = 0; i <gameObjectArray.size; i++) {
			if (gameObject!=gameObjectArray.get(i)) {
				if(rectangle.overlaps(gameObjectArray.get(i).getRectangle())){
					return true;
				}
			}
		}
		return false;
	}
}
