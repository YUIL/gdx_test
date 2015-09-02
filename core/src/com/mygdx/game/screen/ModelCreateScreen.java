package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
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
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.mygdx.game.inputprocessor.InputProcessor;
import com.mygdx.game.stage.StageManager;
import com.mygdx.game.util.GameManager;

public class ModelCreateScreen extends TestScreen {

	InputProcessor inputProcessor;
	public Environment environment;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public Model model;
	public ModelInstance instance;
	
	public ModelCreateScreen(Game game){
		super(game);
		inputProcessor=new InputProcessor();
		environment=new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight,0.4f,0.4f,0.4f,1f));
		environment.add(new DirectionalLight().set(0.8f,0.8f,0.8f,-1f,-0.8f,0.2f));
		
		modelBatch=new ModelBatch();
		
		cam=new PerspectiveCamera(67,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		cam.position.set(10f,10f,10f);
		cam.lookAt(0,0,0);
		cam.near=1f;
		cam.far=300f;
		cam.update();
		
		camController=new CameraInputController(cam);
		GameManager.setInputProcessor(camController);
		
		ModelBuilder modelBuilder =new ModelBuilder();
		model=modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position|Usage.Normal);
		instance=new ModelInstance(model);
		
		InputMultiplexer multiplexer = new InputMultiplexer();
		
		multiplexer.addProcessor(StageManager.superStage);
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
		Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);
		
		modelBatch.begin(cam);
		modelBatch.render(instance,environment);
		modelBatch.end();
		InputProcessor.handleInput(game,delta);
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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
		modelBatch.dispose();
		model.dispose();
		
	}

}
