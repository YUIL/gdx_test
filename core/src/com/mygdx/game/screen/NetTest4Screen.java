package com.mygdx.game.screen;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.net.Player;
import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpServer;
import com.mygdx.game.stage.StageManager;
import com.mygdx.game.util.ActorInputListenner;
import com.mygdx.game.util.GameManager;

/**
 * Created by i008 on 2015/8/27.
 */
public class NetTest4Screen extends TestScreen2D {

	volatile Label console;
	String recvString = null;
	volatile UdpServer server;
	Texture texture = new Texture(Gdx.files.internal("images/button_0.png"));
	volatile Session session;
	int temp1 = 0;
	JsonValue jsonValue;
	JsonReader jsonReader = new JsonReader();
	String guiXmlPath = "gui/NetTest4Gui.xml";
	volatile Player selfPlayer;
	volatile Player remotePlayer;
	Map<String, Player> playerMap;
	int speed = 100;
	boolean justPressZ = false;
	Thread screenLogicThread;
	ScreenLogic screenLogic;

	public NetTest4Screen(Game game) {
		super(game);
		StageManager.guiFactor.setStageFromXml(stage, guiXmlPath);
		inputProcess();
		GameManager.setInputProcessor(stage);
		selfPlayer = new Player(((TextArea) stage.getRoot().findActor("localPort")).getText());
		remotePlayer = new Player(((TextArea) stage.getRoot().findActor("remotePort")).getText());
		console = ((Label) (stage.getRoot().findActor("console")));
		console.setSize(200, 200);
		screenLogic = new ScreenLogic(1) {
			public void run() {
				while (!isStoped) {
					try {
						if (server != null) {
							if (session != null) {
								while (!session.getRecvMessageQueue().isEmpty()) {
									recvString = new String(session.getRecvMessageQueue().poll().getData());
									jsonValue = jsonReader.parse(recvString);
									if (jsonValue.get("move") != null) {
										if (jsonValue.get("move").asInt() == 0) {
											remotePlayer.setMove(false);
										} else {
											// System.out.println(System.currentTimeMillis());
											remotePlayer.setMove(true);
										}
									} else if (jsonValue.get("x") != null) {
										remotePlayer.getRectangle().setX(jsonValue.get("x").asFloat());
										remotePlayer.getRectangle().setY(jsonValue.get("y").asFloat());
									} else if (jsonValue.get("time") != null) {
										System.out.println("finaldelay:"
												+ (System.currentTimeMillis() - jsonValue.get("time").asLong()));

									} else if (jsonValue.get("user") != null) {
										// System.out.println(jsonValue.toString().length());
										console.setText(jsonValue.toString());
										System.out.println(jsonValue.toString());

									}

								}
							} else {
								if (!server.sessionMap.isEmpty()) {
									for (Map.Entry<Long, Session> entry : server.sessionMap.entrySet()) {
										session = entry.getValue();
									}
								}
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
						// System.err.println("session:"+session.getRecvMessageQueue());

					}

					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		screenLogicThread = new Thread(screenLogic);
		screenLogicThread.start();

	}

	@Override
	public void render(float delta) {

		super.render(delta);

		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			sendMessage("{time:" + System.currentTimeMillis() + "}");
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			selfPlayer.getRectangle().setX(selfPlayer.getRectangle().getX() - speed * delta);
			sendMessage(
					("{" + "x:" + selfPlayer.getRectangle().getX() + ",y:" + selfPlayer.getRectangle().getY() + "}"));

		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			selfPlayer.getRectangle().setX(selfPlayer.getRectangle().getX() + speed * delta);
			sendMessage(
					("{" + "x:" + selfPlayer.getRectangle().getX() + ",y:" + selfPlayer.getRectangle().getY() + "}"));

		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
			sendMessage(("{" + "getUser:{name:yuil}}"));

		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
			System.out.println("send login");
			sendMessage(("{" + "login:{name:yuil}}"));

		}

		if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
			if (!justPressZ) {
				// System.out.println(System.currentTimeMillis());
				selfPlayer.setMove(true);
				sendMessage("{move:" + "1}");
			}
			justPressZ = true;

		} else if (justPressZ) {
			selfPlayer.setMove(false);
			sendMessage("{move:" + "0}");
			justPressZ = !justPressZ;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.P)) {
			// cam.zoom -= (0.2*delta);
			// If the Q Key is pressed, subtract 0.02 from the Camera's Zoom
		}
		if (Gdx.input.isKeyPressed(Input.Keys.L)) {
			// cam.zoom += (0.2*delta);
			// If the Q Key is pressed, subtract 0.02 from the Camera's Zoom
		}

		if (selfPlayer.isMove()) {
			selfPlayer.getRectangle().setX(selfPlayer.getRectangle().getX() + speed * delta);

		}
		if (remotePlayer.isMove()) {
			remotePlayer.getRectangle().setX(remotePlayer.getRectangle().getX() + speed * delta);
		}

		batch.begin();
		batch.draw(texture, selfPlayer.getRectangle().getX(), selfPlayer.getRectangle().getY());
		batch.draw(texture, remotePlayer.getRectangle().getX(), remotePlayer.getRectangle().getY());
		batch.end();

		// netMessageProcess();
	}

	public void sendMessage(String str) {
		if (server == null) {
			System.err.println("updServer==null");
		} else {
			if (session == null) {
				session = new Session(new Random().nextLong());
				session.setContactorAddress(
						new InetSocketAddress(((TextArea) (stage.getRoot().findActor("remoteIp"))).getText(),
								Integer.parseInt(((TextArea) (stage.getRoot().findActor("remotePort"))).getText())));
				server.sessionMap.put(session.getId(), session);
			}
			server.send(str.getBytes(), session);
		}
	}

	public void netMessageProcess() {
		try {
			if (server != null) {
				if (session != null) {
					while (!session.getRecvMessageQueue().isEmpty()) {
						recvString = new String(session.getRecvMessageQueue().poll().getData());
						jsonValue = jsonReader.parse(recvString);
						if (jsonValue.get("move") != null) {
							if (jsonValue.get("move").asInt() == 0) {
								remotePlayer.setMove(false);
							} else {
								// System.out.println(System.currentTimeMillis());
								remotePlayer.setMove(true);
							}
						} else if (jsonValue.get("x") != null) {
							remotePlayer.getRectangle().setX(jsonValue.get("x").asFloat());
							remotePlayer.getRectangle().setY(jsonValue.get("y").asFloat());
						} else if (jsonValue.get("time") != null) {
							System.out.println(
									"finaldelay:" + (System.currentTimeMillis() - jsonValue.get("time").asLong()));

						}

					}
				} else {
					if (!server.sessionMap.isEmpty()) {
						for (Map.Entry<Long, Session> entry : server.sessionMap.entrySet()) {
							session = entry.getValue();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			// System.err.println("session:"+session.getRecvMessageQueue());

		}

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		if (server != null) {
			server.stop();
		}
		screenLogic.stop();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
		skin.dispose();
		server.stop();
		screenLogic.stop();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

	}

	public void inputProcess() {
		stage.getRoot().findActor("start").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				int port = Integer.parseInt(((TextArea) (stage.getRoot().findActor("localPort"))).getText());
				System.out.println("server start at port:" + port);
				server = new UdpServer(port);
				server.start();

			}
		});
		stage.getRoot().findActor("send").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

			}
		});
		stage.getRoot().findActor("login").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				sendMessage(("{" + "login:{name:"+((TextArea)stage.getRoot().findActor("userName")).getText()+"}}"));
			}
		});
		stage.getRoot().findActor("getUser").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				sendMessage(("{" + "getUser:{name:"+((TextArea)stage.getRoot().findActor("userName")).getText()+"}}"));
			}
		});
	}
}
