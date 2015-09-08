package com.mygdx.game.screen;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.mygdx.game.input.ActorInputListenner;
import com.mygdx.game.input.InputProcessor;
import com.mygdx.game.net.Client;
import com.mygdx.game.net.Server;
import com.mygdx.game.stage.StageManager;
import com.mygdx.game.util.GameManager;


public class NetTest2Screen extends TestScreen {

	String guiXmlPath;
	Stage stage;
	Skin skin;
	SpriteBatch batch;
	Server server;
	public NetTest2Screen(Game game) {
		super(game);
		
		batch = new SpriteBatch();
		guiXmlPath = "data/NetTest2Gui.xml";
		skin = StageManager.defaultSkin;

		stage = new Stage();
		StageManager.guiFactor.setStageFromXml(stage, guiXmlPath, skin);
		server = new Server();
		try {
			server.serverSocket=new DatagramSocket(9091);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Client client=new Client();
		client.setSocketAddress(new InetSocketAddress("uyuil.com",9091));
		server.clientMap.put("server", client);
		try {
			server.startService(9091);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Image image=new Image(skin,"textfield");
		image.setX(250);image.setY(250);
		stage.addActor(image);
		
		
		
		stage.getRoot().findActor("Button1").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y,
				int pointer, int button) {
				//String userName="闃夸粈椤�";
				String userName=((TextArea)(stage.getRoot().findActor("userName"))).getText();
				String password=((TextArea)(stage.getRoot().findActor("password"))).getText();
				boolean temp=(server.clientMap.get("server").send(("{'login':{'userName':'"+userName+"','password':'"+password+"'}}").getBytes()));
				System.out.println(temp);
				/*byte[] b="{'login':{'userName':'123','password':'123'}}".getBytes();
				System.out.println(new String(b));*/
				
			}
		});
		
		GameManager.setInputProcessor(stage);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		super.resize(width, height);

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
		server.stop();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
		skin.dispose();
		server.stop();
	}

}
