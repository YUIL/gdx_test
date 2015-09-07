package com.mygdx.game.entity;

import com.badlogic.gdx.utils.Array;

public class GameWorld {
	Array<GameObject> gameObjectArray=new Array<GameObject>();
	public GameWorld(){
		
	}
	
	public GameObject findGameObject(String name){
		for (int i = 0; i < gameObjectArray.size; i++) {
			if(name.equals(gameObjectArray.get(i).getName())){
				return gameObjectArray.get(i);
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
}
