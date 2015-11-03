package com.yuil.game.screen;



import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.yuil.game.input.InputProcessor;
import com.yuil.game.stage.StageManager;

public abstract class  TestScreen implements Screen {
	public class ScreenLogic implements Runnable{
		volatile int delay=1;
		volatile boolean isStoped=false;
		//long lastRunTime=0;
		public ScreenLogic(){
			this(1);
		}
		public ScreenLogic(int delay){
			this.delay=delay;
		}
		public  void stop(){
			isStoped=true;
		}
		@Override
		public void run() {
			while(!isStoped){
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
		InputProcessor.handleInput(game, delta);
		StageManager.showSuperStage(delta);
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

	}

}
