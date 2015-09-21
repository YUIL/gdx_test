package com.mygdx.game.screen;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Iterator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.mygdx.game.input.ActorInputListenner;
import com.mygdx.game.input.InputProcessor;
import com.mygdx.game.stage.StageManager;
import com.mygdx.game.util.GameManager;
import com.mygdx.game.util.ByteUtil;

public class NetTestScreen extends TestScreen {

	public NetTestScreen(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}
/*	String guiXmlPath;
	Stage stage;
	Skin skin;
	SpriteBatch batch;
	Server server;

	public NetTestScreen(Game game) {
		super(game);

		batch = new SpriteBatch();
		guiXmlPath = "data/NetTestGui.xml";
		skin = StageManager.defaultSkin;
		stage = new Stage();
		StageManager.guiFactor.setStageFromXml(stage, guiXmlPath, skin);
		
		
		server = new Server();
		Client client = new Client();
	
		server.clientMap.put("server", client);


		Actor actor = stage.getRoot().findActor("Receive");
		actor.addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				System.out.println("startService");
				try {
					int port = Integer.parseInt(((TextArea) (stage.getRoot()
							.findActor("ReceivePort"))).getText());
					server.startService(port);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		actor = stage.getRoot().findActor("Send");
		actor.addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				try {
					if(server.clientMap.get("server").getCurrentSendMessage()!=null){
						//server.clientMap.get("test").setCurrentSendMessageLocked(false);
						System.out.println("上一条消息还没发完！");
					}else{
						//server.clientMap.get("test").setCurrentSendMessageLocked(false);
					UdpMessage message = new UdpMessage();
					message.setSequenceId(Integer.parseInt(((TextArea) (stage
							.getRoot().findActor("sequenceId"))).getText()));
					message.setType(Integer.parseInt(((TextArea) (stage
							.getRoot().findActor("type"))).getText()));
					message.setLength(Integer.parseInt(((TextArea) (stage
							.getRoot().findActor("length"))).getText()));
					message.setData(ByteUtil.intToBytes(Integer
							.parseInt(((TextArea) (stage.getRoot()
									.findActor("data"))).getText())));
					String str="asdpupoiupoiupiupoi;klj;km,nmbm,jhgdrtbgcnbvfkghfkhglujghkj,hjguytkjtdtrefydjhgftyfjhvghjftfjhgfjdftydcjhgfhgfytfhgvcdhcghjfcjgxhjfgchgcfghxgfhcjfgkchgfjhgcghcjhgfjhfgvhgchjtgcyhcghclkajfs;zixujvnalkfjl;aksjdlllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllkajflk";
					message.setLength(str.length());
					message.setData(str.getBytes());
					//server.SendUdpMessage(client.getSocketAddress(), message);
					// server.send(9091, 9092, message);

					
						server.clientMap.get("server").setCurrentSendMessage(message);
						
						TextArea tx = ((TextArea) (stage.getRoot()
								.findActor("sequenceId")));
						tx.setText(String.valueOf(Integer.parseInt(tx.getText()) + 1));
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		client.setLastSequenceId(0);
		client.setSocketAddress(new InetSocketAddress("uyuil.com",
				Integer.parseInt(((TextArea) (stage.getRoot()
						.findActor("SendPort"))).getText())));
		client.setTimeOut(20);
		try {
			server.serverSocket=new DatagramSocket(Integer.parseInt(((TextArea) (stage
					.getRoot().findActor("ReceivePort"))).getText()));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GameManager.setInputProcessor(stage);
		
		try {
			server.startService(9091);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

		try {
			// server.startService(Integer.parseInt(((ReceivePort)
			// (stage.getRoot().findActor("ReceivePort"))).getText()));
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}

		for (Iterator<?> iterator = server.clientMap.iterator(); iterator
				.hasNext();) {

			@SuppressWarnings("unchecked")
			Entry<String, Client> entry = (Entry<String, Client>) iterator
					.next();
			Client client = entry.value;

			for (int i = 0; i < client.messageQueue.size(); i++) {

				UdpMessage message = client.messageQueue.peek();
				if (message.type == 2) {
					server.clientMap.remove(entry.key);
				}
				((Label) stage.getRoot().findActor("console")).setText(message
						.toString());
			}
		}

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
		batch.dispose();
		skin.dispose();
		stage.dispose();
		
		server.stop();
	}
*/
}
