package com.yuil.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yuil.game.stage.StageManager;

public class ViewportTest1Screen extends TestScreen{
	Array<Viewport> viewports;
	Array<String> names;
	Stage stage;
	Label label;
	String guiXmlPath = "gui/NetTest4Gui.xml";
	public ViewportTest1Screen(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
		stage = new Stage();
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		label = new Label("", skin);

		Table root = new Table(skin);
		root.setFillParent(true);
		root.setBackground(skin.getDrawable("default-pane"));
		root.debug().defaults().space(6);
		root.add(new TextButton("Button 1", skin));
		root.add(new TextButton("Button 2", skin)).row();
		root.add("Press spacebar to change the viewport:").colspan(2).row();
		root.add(label).colspan(2);
		stage.addActor(root);
		StageManager.guiFactor.setStage(stage, guiXmlPath);

		viewports = getViewports(stage.getCamera());
		names = getViewportNames();

		stage.setViewport(viewports.first());
		label.setText(names.first());

		Gdx.input.setInputProcessor(new InputMultiplexer(new InputAdapter() {
			public boolean keyDown (int keycode) {
				if (keycode == Input.Keys.SPACE) {
					int index = (viewports.indexOf(stage.getViewport(), true) + 1) % viewports.size;
					label.setText(names.get(index));
					Viewport viewport = viewports.get(index);
					stage.setViewport(viewport);
					resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				}
				return false;
			}
			public boolean touchDragged (int screenX, int screenY, int pointer) {
				int index = (viewports.indexOf(stage.getViewport(), true) + 1) % viewports.size;
				label.setText(names.get(index));
				Viewport viewport = viewports.get(index);
				stage.setViewport(viewport);
				resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				return false;
			}
		}, stage));
	}
	public void render (float delta) {
		stage.act();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	public void dispose () {
		stage.dispose();
	}

	static public Array<String> getViewportNames () {
		Array<String> names = new Array();
		names.add("StretchViewport");
		names.add("FillViewport");
		names.add("FitViewport");
		names.add("ExtendViewport: no max");
		names.add("ExtendViewport: max");
		names.add("ScreenViewport: 1:1");
		names.add("ScreenViewport: 0.75:1");
		names.add("ScalingViewport: none");
		return names;
	}

	static public Array<Viewport> getViewports (Camera camera) {
		int minWorldWidth = 640;
		int minWorldHeight = 480;
		int maxWorldWidth = 800;
		int maxWorldHeight = 480;

		Array<Viewport> viewports = new Array();
		viewports.add(new StretchViewport(minWorldWidth, minWorldHeight, camera));
		viewports.add(new FillViewport(minWorldWidth, minWorldHeight, camera));
		viewports.add(new FitViewport(minWorldWidth, minWorldHeight, camera));
		viewports.add(new ExtendViewport(minWorldWidth, minWorldHeight, camera));
		viewports.add(new ExtendViewport(minWorldWidth, minWorldHeight, maxWorldWidth, maxWorldHeight, camera));
		viewports.add(new ScreenViewport(camera));

		ScreenViewport screenViewport = new ScreenViewport(camera);
		screenViewport.setUnitsPerPixel(0.75f);
		viewports.add(screenViewport);

		viewports.add(new ScalingViewport(Scaling.none, minWorldWidth, minWorldHeight, camera));
		return viewports;
	}

}
