package com.mygdx.game.entity;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class GameObject {
	String name=null;
	Matrix4 transform=new Matrix4();
	Vector3 position=new Vector3();
	Vector3 inertiaForce=new Vector3();
	public Vector3 getInertiaForce() {
		return inertiaForce;
	}
	public void setInertiaForce(Vector3 inertiaForce) {
		this.inertiaForce = inertiaForce;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
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
		return "{name:"+name+",p:{"+"x:"+getPosition().x+",y:"+getPosition().y+",z:"+getPosition().z+"}"+",i:{"+"x:"+getInertiaForce().x+",y:"+getInertiaForce().y+",z:"+getInertiaForce().z+"}}";
	}
}
