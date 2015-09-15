package com.mygdx.game.entity;

import java.util.Random;

public class GameObjectCreation {
	
	public GameObjectCreation(String name, float x, float y, float angle, float angularVelocity, float width,
			float height, float density, float lx, float ly) {
		super();
		this.name = name;
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.angularVelocity = angularVelocity;
		this.width = width;
		this.height = height;
		this.density = density;
		this.lx = lx;
		this.ly = ly;
	}
	public String name;
	public float x;
	public float y;
	public float angle;
	public float angularVelocity;
	public float width;
	public float height;
	public float density;
	public float lx;
	public float ly;
	
	public static GameObjectCreation random(String name){
		Random r=new Random();
		return new GameObjectCreation(name, r.nextFloat()*30-15, 30, 1, 0, 2, 2, 1, 0, 0);
	}
}
