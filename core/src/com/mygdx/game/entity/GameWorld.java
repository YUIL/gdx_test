package com.mygdx.game.entity;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class GameWorld {
	Array<GameObject> gameObjectArray=new Array<GameObject>();
	Array<GameObject> beCollidedGameObjectArray=new Array<GameObject>();
	Vector3 originPosition=new Vector3();
	Vector3 targePosition=new Vector3();
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
				originPosition=gameObject.getPosition();
				targePosition.x=gameObject.getPosition().x+gameObject.getInertiaForce().x*delta;
				targePosition.y=gameObject.getPosition().y+gameObject.getInertiaForce().y*delta;
				targePosition.z=gameObject.getPosition().z+gameObject.getInertiaForce().z*delta;	
				gameObject.setPosition(targePosition);
				collisionDetection(gameObject);
				if (beCollidedGameObjectArray.size!=0) {
					gameObject.setPosition(originPosition);
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
}
