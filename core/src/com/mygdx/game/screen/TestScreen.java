package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

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
