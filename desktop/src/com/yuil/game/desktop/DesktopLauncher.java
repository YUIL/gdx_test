package com.yuil.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.yuil.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		/*config.vSyncEnabled=false;
		config.foregroundFPS=5;
		config.backgroundFPS=5;*/
		config.width=800;
		config.height=480;
		config.title="yuil";
		MyGdxGame.openId="11111111111111111111111111111111";
		new LwjglApplication(new MyGdxGame(), config);
	}
}
