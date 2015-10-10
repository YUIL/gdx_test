package com.yuil.game.entity;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class GameObject {
	String name=null;
	Matrix4 transform=new Matrix4();
	Vector3 position=new Vector3();
	Vector3 inertiaForce=new Vector3();
	Rectangle rectangle=new Rectangle();
	int weight=3;
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
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
		this.rectangle.x=position.x;
		this.rectangle.y=position.y;
		transform.setTranslation(position);
	}
	
	public Rectangle getRectangle() {
		return rectangle;
	}
	public void setRectangle(float x,float y,float width,float height) {
		this.rectangle.x=x;
		this.rectangle.y=y;
		this.rectangle.width=width;
		this.rectangle.height=height;
	}
	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}
	public String toJson(){
		return "{name:"+name+",p:{"+"x:"+getPosition().x+",y:"+getPosition().y+",z:"+getPosition().z+"}"+",i:{"+"x:"+getInertiaForce().x+",y:"+getInertiaForce().y+",z:"+getInertiaForce().z+"},r:{"+"width:"+getRectangle().width+",height:"+getRectangle().height+"}}";
	}
	@Override
	public String toString() {
		return "GameObject [name=" + name + ", transform=" + transform
				+ ", position=" + position + ", inertiaForce=" + inertiaForce
				+ "]";
	}
	
}
