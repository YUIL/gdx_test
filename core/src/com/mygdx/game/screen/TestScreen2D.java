package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.stage.StageManager;

public class TestScreen2D extends TestScreen {
	SpriteBatch batch;
	//OrthographicCamera cam;
	Viewport viewport;
	Skin skin;
	Stage stage;

	public TestScreen2D(Game game) {
		super(game);
		batch = new SpriteBatch();
		skin = StageManager.defaultSkin;
		stage = new Stage();

		//cam = new OrthographicCamera();
		// viewport=new FillViewport(800, 480, cam);
		viewport = new StretchViewport(800, 480, stage.getCamera());
		stage.setViewport(viewport);
		StageManager.superStage.setViewport(viewport);
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.setProjectionMatrix(stage.getCamera().projection);
		
		//cam.translate(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		

	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		super.resize(width, height);
		stage.getViewport().update(width, height, true);
	}

}
