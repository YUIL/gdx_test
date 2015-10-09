package com.mygdx.game.screen;

import java.net.BindException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.database.dao.DaoFactory;
import com.mygdx.game.database.dao.UserDao;
import com.mygdx.game.entity.User;
import com.mygdx.game.input.ActorInputListenner;
import com.mygdx.game.net.udp.UdpServer;
import com.mygdx.game.stage.StageManager;
import com.mygdx.game.util.GameManager;

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
		super.render(delta);
	}
	private void inputProcess() {
		// TODO Auto-generated method stub
		stage.getRoot().findActor("login").addListener(new ActorInputListenner() {

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				//(MyGdxGame)game).openId;
				
				
			}
		});
	}
}
