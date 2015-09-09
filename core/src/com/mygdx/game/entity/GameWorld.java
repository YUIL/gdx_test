package com.mygdx.game.entity;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class GameWorld {
	Array<GameObject> gameObjectArray=new Array<GameObject>();
	Vector3 temp=new Vector3();
	public GameWorld(){
		
	}
	
	public GameObject findGameObject(String name){
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
			temp.x=gameObject.getPosition().x+gameObject.getInertiaForce().x*delta;
			temp.y=gameObject.getPosition().y+gameObject.getInertiaForce().y*delta;
			temp.z=gameObject.getPosition().z+gameObject.getInertiaForce().z*delta;
			gameObject.setPosition(temp);
			
		}
	}
}
