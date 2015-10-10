package com.yuil.game.entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.utils.Disposable;

public class BtGameObject implements Disposable{
	public ModelInstance instance;
/*	public btDefaultMotionState motionState;
	public btCollisionShape collisionShape;
	public btRigidBodyConstructionInfo rigidBodyConstructionInfo;*/
	public btRigidBody rigidBody;
/*	float mass;
	public Vector3 inertia=new Vector3();*/
	
	/*public BtGameObject(Model model,btCollisionShape collisionShape,float mass, Vector3 inertia){
		this.instance=new ModelInstance(model);
		this.collisionShape=collisionShape;
		this.mass=mass;
		this.inertia=inertia;
		
		motionState=new btDefaultMotionState();
		rigidBodyConstructionInfo=new btRigidBodyConstructionInfo(mass, null, collisionShape, inertia);
		collisionShape.calculateLocalInertia(mass, inertia);
		rigidBody=new btRigidBody(rigidBodyConstructionInfo);
		rigidBody.setMotionState(motionState);
	}*/
	public BtGameObject(ModelInstance instance,btRigidBody rigidbody){
		this.instance=instance;
		this.rigidBody=rigidbody;
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
/*		 motionState.dispose();;
		 collisionShape.dispose();;
		 rigidBodyConstructionInfo.dispose();;*/
		 rigidBody.dispose();;
	}

}
