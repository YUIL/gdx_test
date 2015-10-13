package com.yuil.game;

import com.badlogic.gdx.Game;
import com.yuil.game.screen.LoginScreen;
import com.yuil.game.screen.MainMenuScreen;
import com.yuil.game.screen.NetTest7Screen;
import com.yuil.game.stage.StageManager;

public class MyGdxGame extends Game {
	//public static volatile String openId="11111111111111111111111111111111";
	public static volatile String openId=null;

	@Override
	public void create() {
		StageManager.init();
		StageManager.game = this;
		//this.setScreen(new MainMenuScreen(this));
		this.setScreen(new LoginScreen(this));

	}

}
