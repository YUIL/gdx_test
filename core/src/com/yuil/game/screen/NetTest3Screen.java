package com.yuil.game.screen;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.Iterator;
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
import com.yuil.game.input.ActorInputListenner;
import com.yuil.game.input.InputProcessor;
import com.yuil.game.net.udp.Session;
import com.yuil.game.net.udp.UdpMessage;
import com.yuil.game.net.udp.UdpServer;
import com.yuil.game.stage.StageManager;
import com.yuil.game.util.ByteUtil;
import com.yuil.game.util.GameManager;

public class NetTest3Screen extends TestScreen {
	String guiXmlPath;
	Stage stage;
	Skin skin;
	SpriteBatch batch;
	BitmapFont bf;
	UdpServer udpServer;
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
		StageManager.guiFactor.setStage(stage, guiXmlPath);



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
		super.render(delta);
        Session session;
        if (udpServer.sessionArray.size!=0) {
			/*for (Iterator<Session> iterator = udpServer.sessionArray.iterator(); iterator
					.hasNext();) {
				session = iterator.next();
				if (!session.getRecvMessageQueue().isEmpty()) {
					str = new String(session.getRecvMessageQueue().poll().getData());
				}
			}*/
		}
		
		stage.draw();
		batch.begin();
		bf.draw(batch, str, 250, 250);
		batch.end();
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
		if(udpServer!=null) {
			udpServer.stop();
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
		skin.dispose();
		udpServer.stop();

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
							udpServer.sessionArray.add(session);
						}
                        TextArea tx = ((TextArea) (stage.getRoot()
                                .findActor("sequenceId")));
                        tx.setText(String.valueOf(session.lastSendSequenceId  + 1));
                      //  tx.setText(String.valueOf(Integer.parseInt(tx.getText()) + 1));
						UdpMessage message = new UdpMessage();
						message.setSessionId(session.getId());
						message.setSequenceId(Integer
                                .parseInt(((TextArea) (stage.getRoot()
                                        .findActor("sequenceId"))).getText()));
						message.setType(Byte.parseByte(((TextArea) (stage
                                .getRoot().findActor("type"))).getText()));
						message.setLength(Integer.parseInt(((TextArea) (stage
                                .getRoot().findActor("length"))).getText()));
						message.setData(ByteUtil.intToBytes(Integer
                                .parseInt(((TextArea) (stage.getRoot()
                                        .findActor("data"))).getText())));

						udpServer.send(message, session);




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
						try {
							udpServer = new UdpServer(port);
						} catch (BindException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						udpServer.start();
					}
				});
	}
	public synchronized void messageParse(){
		
	}
}
