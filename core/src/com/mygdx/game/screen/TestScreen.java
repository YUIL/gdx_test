package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.inputprocessor.InputProcessor;
import com.mygdx.game.stage.StageManager;

public abstract class  TestScreen implements Screen {
	public class ScreenLogic implements Runnable{
		volatile long delay=1;
		volatile boolean isStoped=false;
		public ScreenLogic(){

		}
		public ScreenLogic(long delay){
			this.delay=delay;
		}
		public synchronized void stop(){
			isStoped=true;
		}
		@Override
		public void run() {
			while(!isStoped){

				System.out.println("runing");
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	Game game;
	public TestScreen (Game game){
		this.game=game;
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		InputProcessor.handleInput(game, delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		Actor mainMenu=(StageManager.superStage.getRoot().findActor("MainMenu"));
		mainMenu.setX(Gdx.graphics.getWidth()-100);
		mainMenu.setY(Gdx.graphics.getHeight()-30);
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

	}

}
