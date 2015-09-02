package com.mygdx.game.screen;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.Map.Entry;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.mygdx.game.inputprocessor.InputProcessor;
import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpMessage;
import com.mygdx.game.net.udp.UdpServer;
import com.mygdx.game.stage.StageManager;
import com.mygdx.game.util.ActorInputListenner;
import com.mygdx.game.util.GameManager;
import com.mygdx.game.util.JavaDataConverter;

public class NetTest3Screen extends TestScreen {
	String guiXmlPath;
	Stage stage;
	Skin skin;
	SpriteBatch batch;
	BitmapFont bf;
	UdpServer server;
	volatile Session session;
	String str="no message";
	public NetTest3Screen(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
		bf=new BitmapFont();
		batch = new SpriteBatch();
		guiXmlPath = "data/NetTest3Gui.xml";
		skin = StageManager.defaultSkin;
		stage = new Stage();
		StageManager.guiFactor.setStageFromXml(stage, guiXmlPath);



		inputProcess();
		GameManager.setInputProcessor(stage);

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
        Session session;
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		if(server!=null&&!server.sessionMap.isEmpty()) {
			for (Entry<Long, Session> entry : server.sessionMap.entrySet()) {
				session = entry.getValue();
				if (!session.getRecvMessageQueue().isEmpty()) {
					str = new String(session.getRecvMessageQueue().poll().getData());
				}
			}
		}
		stage.draw();
		batch.begin();
		bf.draw(batch, str, 250, 250);
		batch.end();
		InputProcessor.handleInput(game, delta);
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
		if(server!=null) {
			server.stop();
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
		skin.dispose();
		server.stop();

	}

	public void inputProcess() {
		stage.getRoot().findActor("send")
				.addListener(new ActorInputListenner() {
					public void touchUp(InputEvent event, float x, float y,
							int pointer, int button) {

						if(session==null) {
							session = new Session(new Random().nextLong());
							session.setContactorAddress(new InetSocketAddress("127.0.0.1", Integer.parseInt(((TextArea) (stage.getRoot()
									.findActor("port"))).getText())));
							server.sessionMap.put(session.getId(), session);
						}
                        TextArea tx = ((TextArea) (stage.getRoot()
                                .findActor("sequenceId")));
                        tx.setText(String.valueOf(session.getLastSendMessage().getSequenceId()  + 1));
                      //  tx.setText(String.valueOf(Integer.parseInt(tx.getText()) + 1));
						UdpMessage message = new UdpMessage();
						message.setSessionId(session.getId());
						message.setSequenceId(Integer
                                .parseInt(((TextArea) (stage.getRoot()
                                        .findActor("sequenceId"))).getText()));
						message.setType(Integer.parseInt(((TextArea) (stage
                                .getRoot().findActor("type"))).getText()));
						message.setLength(Integer.parseInt(((TextArea) (stage
                                .getRoot().findActor("length"))).getText()));
						message.setData(JavaDataConverter.intToBytes(Integer
                                .parseInt(((TextArea) (stage.getRoot()
                                        .findActor("data"))).getText())));

						server.send(message, session);




					}
				});
		stage.getRoot().findActor("start")
				.addListener(new ActorInputListenner() {
					public void touchUp(InputEvent event, float x, float y,
										int pointer, int button) {
						int port=Integer
								.parseInt(((TextArea) (stage.getRoot()
										.findActor("localport"))).getText());
                        System.out.println("port:"+port);
						server = new UdpServer(port);
						server.start();
					}
				});
	}
	public synchronized void messageParse(){
		
	}
}
