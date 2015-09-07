package com.mygdx.game.entity;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class GameObject {
	String name=null;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	Matrix4 transform=new Matrix4();
	Vector3 position=new Vector3();
	public GameObject(){
		
	}
	public GameObject(String name){
		this.name=name;
	}
	
	
	public Matrix4 getTransform() {
		return transform;
	}
	public void setTransform(Matrix4 transform) {
		this.transform = transform;
	}
	public Vector3 getPosition() {
		return transform.getTranslation(position);
	}
	public void setPosition(Vector3 position) {
		transform.setTranslation(position);
	}
	
	public String toJson(){
		return "{name:"+name+",position:{"+"x:"+getPosition().x+",y:"+getPosition().y+",z:"+getPosition().z+"}}";
	}
}
