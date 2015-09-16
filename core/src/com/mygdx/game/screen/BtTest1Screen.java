package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entity.BtGameObject;
import com.mygdx.game.util.GameManager;

public class BtTest1Screen extends TestScreen {

	public PerspectiveCamera camera;
	CameraInputController camController;
	
	ModelBatch modelBatch=new ModelBatch();;
	Environment lights;
	ModelBuilder modelBuilder = new ModelBuilder();

	btCollisionConfiguration collisionConfiguration;
	btCollisionDispatcher dispatcher;
	btBroadphaseInterface broadphase;
	btConstraintSolver solver;
	btDynamicsWorld collisionWorld;
	Vector3 gravity = new Vector3(0, -9.81f, 0);
	
	public btDefaultMotionState ballMotionState;
	public btCollisionShape ballCollisionShape;
	public btRigidBodyConstructionInfo ballRigidBodyConstructionInfo;
	
	public btDefaultMotionState groundMotionState;
	public btCollisionShape groundCollisionShape;
	public btRigidBodyConstructionInfo groundRigidBodyConstructionInfo;

	Array<btDefaultMotionState> motionStates = new Array<btDefaultMotionState>();
	Array<btRigidBodyConstructionInfo> bodyInfos = new Array<btRigidBodyConstructionInfo>();
	Array<btCollisionShape> shapes = new Array<btCollisionShape>();
	
	
	BtGameObject ground;
	Array<BtGameObject> balls=new Array<BtGameObject>();
	Model groundModel;
	Model ballModel;
	
	Vector3 tempVector=new Vector3();
	
	boolean stoped=true;
	public BtTest1Screen(Game game) {
		super(game);
		
		
		lights = new Environment();
		lights.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.2f, 0.2f, 0.2f, 1.f));
		lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -0.5f, -1f, -0.7f));
		
		
		// Set up the camera
		final float width = Gdx.graphics.getWidth();
		final float height = Gdx.graphics.getHeight();
		if (width > height)
			camera = new PerspectiveCamera(67f, 3f * width / height, 3f);
		else
			camera = new PerspectiveCamera(67f, 3f, 3f * height / width);
		camera.position.set(10f, 10f, 10f);
		camera.lookAt(0, 0, 0);
		camera.update();
		camController = new CameraInputController(camera);
		
		Bullet.init();
		// Create the bullet world
		collisionConfiguration = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfiguration);
		broadphase = new btDbvtBroadphase();
		solver = new btSequentialImpulseConstraintSolver();
		collisionWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
		collisionWorld.setGravity(gravity);
		
		btCollisionShape groundShape = new btBoxShape(tempVector.set(20, 0, 20));
		shapes.add(groundShape);
		btRigidBodyConstructionInfo groundInfo = new btRigidBodyConstructionInfo(0f, null, groundShape, Vector3.Zero);
		bodyInfos.add(groundInfo);
		//----------------------------------------------------
		btCollisionShape ballShape = new btSphereShape(0.5f);
		shapes.add(ballShape);
		ballShape.calculateLocalInertia(1f, tempVector);
		btRigidBodyConstructionInfo ballInfo = new btRigidBodyConstructionInfo(1f, null, ballShape, tempVector);
		bodyInfos.add(ballInfo);
		
		
		//Create the ground 
		createGround();
		collisionWorld.addRigidBody(ground.rigidBody);
		//Create balls
		createBalls();
		for (BtGameObject ball : balls) {
			collisionWorld.addRigidBody(ball.rigidBody);
		}
		//obe=new BtGameObject(model, collisionShape, mass)
		
		GameManager.setInputProcessor(camController);
	}
	
	void createBalls(){
		ballModel = modelBuilder.createSphere(
				1f,
				1f,
				1f,
				10,
				10,
				new Material(ColorAttribute.createDiffuse(Color.RED), ColorAttribute.createSpecular(Color.WHITE), FloatAttribute
					.createShininess(64f)), Usage.Position | Usage.Normal);
		
		btCollisionShape ballShape = new btSphereShape(0.5f);
		shapes.add(ballShape);
		ballShape.calculateLocalInertia(1f, tempVector);
		btRigidBodyConstructionInfo ballInfo = new btRigidBodyConstructionInfo(1f, null, ballShape, tempVector);
		bodyInfos.add(ballInfo);
		
		for (int i = 0; i < 10; i++) {
			
			ModelInstance ballInstance = new ModelInstance(ballModel);
			
			//ballInstance.transform.trn( 10f * MathUtils.random(), 10f * MathUtils.random(),10f * MathUtils.random());
			btDefaultMotionState ballMotionState = new btDefaultMotionState();
			ballMotionState.setWorldTransform(ballInstance.transform);
			btRigidBody ballBody = new btRigidBody(ballInfo);
			ballBody.setMotionState(ballMotionState);
			
			
			//BtGameObject ball=new BtGameObject(ballModel,  new btballShape(0.5f), 1f,Vector3.Zero);
			BtGameObject ball=new BtGameObject(ballInstance, ballBody);
			ball.instance.transform.trn(MathUtils.random()*10,MathUtils.random()*10,MathUtils.random()*10);
			ball.rigidBody.getMotionState().setWorldTransform(ball.instance.transform);
			ball.rigidBody.setMotionState(ball.rigidBody.getMotionState());
			balls.add(ball);
		}
	}
	
	void createGround(){
		
		groundModel = modelBuilder.createRect(
				20f,
				0f,
				-20f,
				-20f,
				0f,
				-20f,
				-20f,
				0f,
				20f,
				20f,
				0f,
				20f,
				0,
				1,
				0,
				new Material(ColorAttribute.createDiffuse(Color.BLUE), ColorAttribute.createSpecular(Color.WHITE), FloatAttribute
					.createShininess(16f)), Usage.Position | Usage.Normal);
		
		btCollisionShape groundShape = new btBoxShape(tempVector.set(20, 0, 20));
		shapes.add(groundShape);
		btRigidBodyConstructionInfo groundInfo = new btRigidBodyConstructionInfo(0f, null, groundShape, Vector3.Zero);
		bodyInfos.add(groundInfo);
		
		ModelInstance ground = new ModelInstance(groundModel);
		btDefaultMotionState groundMotionState = new btDefaultMotionState();
		groundMotionState.setWorldTransform(ground.transform);
		motionStates.add(groundMotionState);
		btRigidBody groundBody = new btRigidBody(groundInfo);
		groundBody.setMotionState(groundMotionState);
		
		this.ground=new BtGameObject(ground,groundBody);
		this.ground.instance.transform.trn(0,0,0);
		groundBody.getMotionState().setWorldTransform(ground.transform);
	}

	
	
	
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);
		/*Matrix4 m=new Matrix4();
		balls.get(0).motionState.getWorldTransform(m);
		System.out.println("a:"+m);
		*/
		if(!stoped)
			collisionWorld.stepSimulation(delta, 5);
		
		
		/*balls.get(0).motionState.getWorldTransform(m);
		System.out.println("b:"+m);*/
		//ground.motionState.getWorldTransform(ground.instance.transform);
		for (BtGameObject ball : balls) {
			ball.rigidBody.getWorldTransform(ball.instance.transform);
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.T)){
			stoped=!stoped;
		}
		
		modelBatch.begin(camera);
		modelBatch.render(ground.instance, lights);
		for (BtGameObject ball : balls) {
			modelBatch.render(ball.instance,lights);
		}
		modelBatch.end();
	
		super.render(delta);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		ground.dispose();
		groundModel.dispose();
		
		for (BtGameObject ball : balls) {
			ball.dispose();
		}
		balls.clear();
		ballModel.dispose();
		
		
		collisionWorld.dispose();
		solver.dispose();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfiguration.dispose();
		
		
		for (btDefaultMotionState motionState : motionStates)
			motionState.dispose();
		motionStates.clear();
		for (btCollisionShape shape : shapes)
			shape.dispose();
		shapes.clear();
		for (btRigidBodyConstructionInfo info : bodyInfos)
			info.dispose();
		bodyInfos.clear();
	}
	
}
