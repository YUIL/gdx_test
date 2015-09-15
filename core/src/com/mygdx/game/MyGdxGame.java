package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screen.MainMenuScreen;
import com.mygdx.game.screen.NetTest6Screen;
import com.mygdx.game.stage.StageManager;

public class MyGdxGame extends Game {

	@Override
	public void create() {
		StageManager.init();
		StageManager.game = this;
		this.setScreen(new MainMenuScreen(this));
	}

}
