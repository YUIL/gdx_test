package com.yuil.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.yuil.game.MyGdxGame;
import com.yuil.game.input.ActorInputListenner;
import com.yuil.game.stage.StageManager;
import com.yuil.game.util.GameManager;

public class LoginScreen extends TestScreen2D {

	public LoginScreen(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
		StageManager.guiFactor.setStage(stage, "gui/LoginGui.xml");
		GameManager.setInputProcessor(stage);
		inputProcess();
	}



	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
				Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
						Gdx.graphics.getHeight());
				Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1.f);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
				super.render(delta);

	}
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		//System.out.println("resize");
		super.resize(width, height);
		
		
	}
	private void inputProcess() {
		// TODO Auto-generated method stub
		stage.getRoot().findActor("login").addListener(new ActorInputListenner() {

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				//(MyGdxGame)game).openId;
				MyGdxGame.openId=((TextArea) stage.getRoot().findActor("userName")).getText();
				game.setScreen(new NetTest7Screen(game));
				
			}
		});
	}
}
