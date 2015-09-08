package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.shader.TestShader;
import com.mygdx.game.shader.TestShader.TestColorAttribute;
import com.mygdx.game.util.GameManager;

public class ShaderTest2Screen extends TestScreen {
	
	   public PerspectiveCamera cam;
	   public CameraInputController camController;
	   public Shader shader;
	   public Model model;
	   public Array<ModelInstance> instances = new Array<ModelInstance>();
	   public ModelBatch modelBatch;

	   
	public ShaderTest2Screen(Game game) {
		super(game);
	      cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	       cam.position.set(0f, 8f, 8f);
	       cam.lookAt(0,0,0);
	       cam.near = 1f;
	       cam.far = 300f;
	       cam.update();

	       camController = new CameraInputController(cam);
	       GameManager.setInputProcessor(camController);

	       ModelBuilder modelBuilder = new ModelBuilder();
	       model = modelBuilder.createSphere(2f, 2f, 2f, 20, 20,
	         new Material(),
	         Usage.Position | Usage.Normal | Usage.TextureCoordinates);

	       for (int x = -5; x <= 5; x+=2) {
	         for (int z = -5; z<=5; z+=2) {
	        	 ModelInstance instance = new ModelInstance(model, x, 0, z);
	             ColorAttribute attrU = new TestColorAttribute(TestColorAttribute.DiffuseU, (x+5f)/10f, 1f - (z+5f)/10f, 0, 1);
	             instance.materials.get(0).set(attrU);
	             ColorAttribute attrV = new TestColorAttribute(TestColorAttribute.DiffuseV, 1f - (x+5f)/10f, 0, (z+5f)/10f, 1);
	             instance.materials.get(0).set(attrV);
	             instances.add(instance);
	             
	         }
	       }

	       shader = new TestShader();
	       shader.init();

	       modelBatch = new ModelBatch();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
	    camController.update();

	    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

	    modelBatch.begin(cam);
	    for (ModelInstance instance : instances)
	        modelBatch.render(instance, shader);
	    modelBatch.end();
	    
	    com.mygdx.game.input.InputProcessor.handleInput(game,delta);
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
	       shader.dispose();
	       model.dispose();
	       modelBatch.dispose();
	}

}
