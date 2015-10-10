package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screen.LoginScreen;
import com.mygdx.game.screen.MainMenuScreen;
import com.mygdx.game.screen.NetTest7Screen;
import com.mygdx.game.stage.StageManager;

public class MyGdxGame extends Game {
	//public static volatile String openId="11111111111111111111111111111111";
	public static volatile String openId=null;

	@Override
	public void create() {
		StageManager.init();
		StageManager.game = this;
		//this.setScreen(new MainMenuScreen(this));
		this.setScreen(new NetTest7Screen(this));

	}

}
