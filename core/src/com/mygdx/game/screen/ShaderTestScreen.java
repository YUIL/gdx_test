package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.mygdx.game.input.InputProcessor;
import com.mygdx.game.shader.TestShader;
import com.mygdx.game.util.GameManager;

public class ShaderTestScreen extends TestScreen {


	public PerspectiveCamera cam;
	public CameraInputController camController;
	public Shader shader;
	public RenderContext renderContext;
	public Model model;
	public Environment environment;
	public Renderable renderable;

	public ShaderTestScreen(Game game) {
		super(game);

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f,
				0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f,
				-0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		cam.position.set(2f, 2f, 2f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		camController = new CameraInputController(cam);
		GameManager.setInputProcessor(camController);

		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createSphere(2f, 2f, 2f, 20, 20, new Material(),
				Usage.Position | Usage.Normal | Usage.TextureCoordinates);

		NodePart blockPart = model.nodes.get(0).parts.get(0);

		renderable = new Renderable();
		blockPart.setRenderable(renderable);
		renderable.environment = null;
		renderable.worldTransform.idt();
		//renderable.primitiveType = GL20.GL_LINES;

		renderContext = new RenderContext(new DefaultTextureBinder(
				DefaultTextureBinder.WEIGHTED, 1));

		shader = new TestShader();
		shader.init();
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

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		renderContext.begin();
		shader.begin(cam, renderContext);
		shader.render(renderable);
		shader.end();
		renderContext.end();

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
		shader.dispose();
		model.dispose();
	}

}
