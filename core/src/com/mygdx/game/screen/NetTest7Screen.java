package com.mygdx.game.screen;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.JsonReader;
import com.mygdx.game.entity.B2DGameObject;
import com.mygdx.game.entity.GameWorldB2D;
import com.mygdx.game.entity.info.B2dBoxBaseInformation;
import com.mygdx.game.entity.message.C2S_B2D_ADD_GAMEOBJECT;
import com.mygdx.game.entity.message.C2S_B2D_APPLY_FORCE;
import com.mygdx.game.entity.message.C2S_B2D_GET_GAMEOBJECT;
import com.mygdx.game.entity.message.C2S_B2D_REMOVE_GAMEOBJECT;
import com.mygdx.game.entity.message.C2S_LOGIN;
import com.mygdx.game.entity.message.GameMessageType;
import com.mygdx.game.entity.message.S2C_B2D_GET_ALL_GAMEOBJECT;
import com.mygdx.game.entity.message.S2C_B2D_GET_GAMEOBJECT;
import com.mygdx.game.entity.message.S2C_B2D_REMOVE_GAMEOBJECT;
import com.mygdx.game.input.ActorInputListenner;
import com.mygdx.game.input.KeyboardStatus;
import com.mygdx.game.net.ClientSocket;
import com.mygdx.game.net.message.GAME_MESSAGE;
import com.mygdx.game.net.message.Message;
import com.mygdx.game.net.message.USER_MESSAGE;
import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpMessageListener;
import com.mygdx.game.stage.StageManager;
import com.mygdx.game.util.ByteUtil;
import com.mygdx.game.util.GameManager;

public class NetTest7Screen extends TestScreen2D implements UdpMessageListener {
	OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10);
	TextureRegion textureRegion = new TextureRegion(new Texture(Gdx.files.internal("images/role1.png")));
	TextureRegion backgroundRegion = new TextureRegion(
			new Texture(Gdx.files.internal("images/NetTest6_background2.png")));

	volatile ClientSocket clientSocket = null;
	String recvString = null;
	volatile GameWorldB2D gameWorld = new GameWorldB2D();
	private Box2DDebugRenderer debugRenderer;
	JsonReader jsonReader = new JsonReader();
	KeyboardStatus keyboardStatus = new KeyboardStatus();
	long gameObjectId = -1;
	long lastAutoSendTime = 0;
	int autoSendIterval = 5000;
	Thread screenLogicThread;
	ScreenLogic screenLogic;
	int speed = 50;
	long nextUpdateTime = 0;
	int updateInterval = 10;
	volatile boolean disposing = false;

	public NetTest7Screen(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
		camera.position.set(0, Gdx.graphics.getHeight() / 20, 0);
		StageManager.guiFactor.setStage(stage, "gui/NetTest7Gui.xml");
		inputProcess();
		GameManager.setInputProcessor(stage);
		debugRenderer = new Box2DDebugRenderer();
		initScreenLogic();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		// System.out.println(gameWorld.getGameObjectArray().size);

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1.f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		stage.getCamera().update();
		batch.getProjectionMatrix().set(stage.getCamera().combined);
		batch.begin();
		batch.draw(backgroundRegion, 0 - camera.position.x * 10 - 205, 0 - camera.position.y * 10, 0, 0,
				backgroundRegion.getRegionWidth(), backgroundRegion.getRegionHeight(), 1.5f, 1.5f, 0);
		batch.end();
		camera.update();
		batch.getProjectionMatrix().set(camera.combined);

		// debugRenderer.render(gameWorld.getBox2dWorld(), camera.combined);
		batch.begin();
		for (int i = 0; i < gameWorld.getGameObjectArray().size; i++) {
			B2DGameObject gameObject = gameWorld.getGameObjectArray().get(i);
			// gameObject.setPosition(new
			// Vector3(gameObject.getPosition().x+gameObject.getInertiaForce().x*delta,
			// gameObject.getPosition().y+gameObject.getInertiaForce().y*delta,
			// 0));
			// batch.draw(textureRegion, gameObject.getPosition().x,
			// gameObject.getPosition().y);
			// System.out.println(gameObject.getBody().isFixedRotation());
			Vector2 position = gameObject.getPosition(); // that's the box's
															// center position
			float angle = MathUtils.radiansToDegrees * gameObject.getBody().getAngle();
			batch.draw(textureRegion, position.x - gameObject.getWidth() / 2, position.y - gameObject.getHeight() / 2,
					gameObject.getWidth() / 2f, gameObject.getHeight() / 2f, gameObject.getWidth(),
					gameObject.getHeight(), Gdx.graphics.getWidth() / 800f, Gdx.graphics.getHeight() / 480f, angle);
		}
		batch.end();
		super.render(delta);
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			// aPressAction();
			if (!keyboardStatus.isaJustPress()) {
				aJustPressAction();

			}
			keyboardStatus.setaJustPress(true);
		} else if (keyboardStatus.isaJustPress()) {
			aJustUpAction();
			keyboardStatus.setaJustPress(!keyboardStatus.isaJustPress());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			if (!keyboardStatus.isdJustPress()) {
				dJustPressAction();

			}
			keyboardStatus.setdJustPress(true);
		} else if (keyboardStatus.isdJustPress()) {
			dJustUpAction();
			keyboardStatus.setdJustPress(!keyboardStatus.isdJustPress());
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			if (!keyboardStatus.iswJustPress()) {
				// dJustPressAction();
				wJustPressAction();
			}
			keyboardStatus.setwJustPress(true);
		} else if (keyboardStatus.iswJustPress()) {
			wJustUpAction();
			keyboardStatus.setwJustPress(!keyboardStatus.iswJustPress());
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
			cPressAction();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
			lPressAction();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
			B2DGameObject gameObject = gameWorld.findGameObject(gameObjectId);
			camera.position.x = gameObject.getPosition().x;
			camera.position.y = gameObject.getPosition().y;
		}

		/*
		 * if (Gdx.input.isKeyJustPressed(Input.Keys.T)) { for (int i = 0; i <
		 * gameWorld.getGameObjectArray().size; i++) { GameObjectB2D gameObject
		 * = gameWorld.getGameObjectArray() .get(i); Vector2 p =
		 * gameObject.getPosition(); System.out.println("p:" +
		 * gameObject.getBody().getPosition());
		 * System.out.println(gameObject.getBody().getLinearVelocity()); //
		 * gameObject.getBody().setTransform(p.x-10, p.y, 0);
		 * gameObject.getBody().getWorldPoint(p);
		 * gameObject.getBody().applyForce(10000, 0, p.x, p.y, true);
		 * 
		 * } } if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) { for (int i = 0; i
		 * < gameWorld.getGameObjectArray().size; i++) { Vector2 p =
		 * gameWorld.getGameObjectArray().get(i).getPosition();
		 * System.out.println("p:" +
		 * gameWorld.getGameObjectArray().get(i).getBody() .getPosition());
		 * System.out.println(gameWorld.getGameObjectArray().get(i)
		 * .getBody().getLinearVelocity());
		 * gameWorld.getGameObjectArray().get(i).getBody() .setTransform(p.x +
		 * 10, p.y, 0); } }
		 */
	}

	@Override
	public void resize(int width, int height) {

		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void hide() {
		super.hide();
		screenLogic.stop();
		clientSocket.close();
		debugRenderer.dispose();
		gameWorld.getBox2dWorld().dispose();
	}

	@Override
	public void dispose() {
		super.dispose();
		screenLogic.stop();
		clientSocket.close();
		debugRenderer.dispose();
		gameWorld.getBox2dWorld().dispose();
	}

	public boolean sendGameMessage(Message message) {
		boolean temp = clientSocket.sendMessage(new GAME_MESSAGE(message.toBytes()).toBytes());
		return temp;
	}

	private void initScreenLogic() {
		screenLogic = new ScreenLogic(1) {
			public void run() {
				nextUpdateTime = System.currentTimeMillis();
				while (!isStoped) {
					if (System.currentTimeMillis() > nextUpdateTime) {
						nextUpdateTime += updateInterval;
						// if (!disposing) {
						gameWorld.update(updateInterval / 1000f);
						// }
						B2DGameObject gameObject = gameWorld.findGameObject(gameObjectId);
						if (gameObject != null) {
							camera.position.x = gameObject.getPosition().x;
							camera.position.y = gameObject.getPosition().y;
						}
						for (int i = 0; i < gameWorld.getGameObjectArray().size; i++) {
							B2DGameObject gameObject2 = gameWorld.getGameObjectArray().get(i);
							if (gameObject2.getPosition().y < -50) {
								C2S_B2D_GET_GAMEOBJECT gameMessage_c2s_ggo = new C2S_B2D_GET_GAMEOBJECT();
								gameMessage_c2s_ggo.gameObjectId = gameObject2.getId();
								sendGameMessage(gameMessage_c2s_ggo);
							}
						}

					}
					try {
						if (clientSocket != null) {
							if (System.currentTimeMillis() - lastAutoSendTime > autoSendIterval) {
								lastAutoSendTime = System.currentTimeMillis();
								clientSocket.sendMessage("");
							}
							/*
							 * if (session != null) {
							 * 
							 * while (!session.getRecvMessageQueue().isEmpty())
							 * { recvString = new String(session
							 * .getRecvMessageQueue().poll() .getData()); if
							 * (recvString != null) { disposeMessage(); } } }
							 * else { if (udpServer.sessionArray.size!=0) {
							 * session=udpServer.sessionArray.get(0); } }
							 */
						}
					} catch (Exception e) {
						// TODO: handle exception
						// System.err.println("session:"+session.getRecvMessageQueue());
						e.printStackTrace();
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

	public void disposeMessage() {

	}

	private void lPressAction() {
		System.out.println("l");
		// sendMessage("{gago:}");
	}

	private void zPressAction() {
		float x = new Random().nextFloat() * 20;
		C2S_B2D_ADD_GAMEOBJECT gameMessage_c2s_ago = new C2S_B2D_ADD_GAMEOBJECT();
		gameMessage_c2s_ago.b2dBoxBaseInformation.gameObjectId = gameObjectId;
		gameMessage_c2s_ago.b2dBoxBaseInformation.x = x;
		gameMessage_c2s_ago.b2dBoxBaseInformation.y = 30;
		gameMessage_c2s_ago.b2dBoxBaseInformation.angle = 0;
		gameMessage_c2s_ago.b2dBoxBaseInformation.angularVelocity = 0;
		gameMessage_c2s_ago.b2dBoxBaseInformation.width = 2;
		gameMessage_c2s_ago.b2dBoxBaseInformation.height = 2;
		gameMessage_c2s_ago.b2dBoxBaseInformation.density = 1;
		gameMessage_c2s_ago.b2dBoxBaseInformation.lx = 0;
		gameMessage_c2s_ago.b2dBoxBaseInformation.ly = 0;
		sendGameMessage(gameMessage_c2s_ago);

	}

	private void xPressAction() {
		if (gameWorld.findGameObject(gameObjectId) != null) {
			C2S_B2D_REMOVE_GAMEOBJECT gameMessage = new C2S_B2D_REMOVE_GAMEOBJECT();
			gameMessage.gameObjectId = gameObjectId;
			sendGameMessage(gameMessage);
		}
		// sendMessage("{rgo:{n:" + gameObjectName + "}}");

	}

	private void cPressAction() {
		if (gameWorld.findGameObject(gameObjectId) != null) {
		}
		// sendMessage("{cgo:{n:" + gameObjectName + ",set:{i:{x:0,y:0}}}}");

	}

	private void gPressAction() {
		System.out.println("gg");
		C2S_B2D_GET_GAMEOBJECT gameMessage = new C2S_B2D_GET_GAMEOBJECT();
		gameMessage.gameObjectId = gameObjectId;
		sendGameMessage(gameMessage);
		// sendMessage("{ggo:{n:" + gameObjectName + "}}");
	}

	private void aJustPressAction() {
		B2DGameObject gameObject = gameWorld.findGameObject(gameObjectId);
		if (gameObject != null) {
			C2S_B2D_APPLY_FORCE gameMessage_c2s_rpc = new C2S_B2D_APPLY_FORCE();
			gameMessage_c2s_rpc.gameObjectId = gameObjectId;
			gameMessage_c2s_rpc.forceX = -1000;
			gameMessage_c2s_rpc.forceY = 0;
			sendMoveMessage(gameMessage_c2s_rpc);
			// sendMessage("{rpc:{af:{n:" + gameObjectName +
			// ",fx:-1000,fy:0}}}");
		}
	}

	private void aJustUpAction() {
		/*
		 * GameObjectB2D gameObject = gameWorld.findGameObject(gameObjectName);
		 * if (gameObject != null) {
		 * 
		 * 
		 * }
		 */
		// System.out.println("object
		// count:"+gameWorld.getGameObjectArray().size);
	}

	private void dJustPressAction() {
		B2DGameObject gameObject = gameWorld.findGameObject(gameObjectId);
		if (gameObject != null) {
			C2S_B2D_APPLY_FORCE gameMessage_c2s_rpc = new C2S_B2D_APPLY_FORCE();
			gameMessage_c2s_rpc.gameObjectId = gameObjectId;
			gameMessage_c2s_rpc.forceX = 1000;
			gameMessage_c2s_rpc.forceY = 0;
			sendMoveMessage(gameMessage_c2s_rpc);
		}
	}

	private void dJustUpAction() {
		/*
		 * GameObjectB2D gameObject = gameWorld.findGameObject(gameObjectName);
		 * if (gameObject != null) { // if(gameObject.getInertiaForce().x!=0)
		 * sendMessage("{cgo:{n:" + gameObjectName + ",add:{i:{x:" + (-1 *
		 * speed) + "}}}}"); }
		 */
	}

	private void wJustPressAction() {
		B2DGameObject gameObject = gameWorld.findGameObject(gameObjectId);
		if (gameObject != null) {
			C2S_B2D_APPLY_FORCE gameMessage_c2s_rpc = new C2S_B2D_APPLY_FORCE();
			gameMessage_c2s_rpc.gameObjectId = gameObjectId;
			gameMessage_c2s_rpc.forceX = 0;
			gameMessage_c2s_rpc.forceY = 6000;
			sendMoveMessage(gameMessage_c2s_rpc);
		}
	}

	private void wJustUpAction() {
		/*
		 * GameObjectB2D gameObject = gameWorld.findGameObject(gameObjectName);
		 * if (gameObject != null) { sendMessage("{cgo:{name:" + gameObjectName
		 * + ",add:{i:{x:0,y:200}}}}"); }
		 */
	}

	public void sendMoveMessage(C2S_B2D_APPLY_FORCE gameMessage_c2s_rpc) {

		B2DGameObject gameObject = gameWorld.findGameObject(gameMessage_c2s_rpc.gameObjectId);
		if (gameObject != null) {
			float forceX = gameMessage_c2s_rpc.forceX;
			if (gameObject.getSpeed() < gameObject.getMaxSpeed()) {
				if (forceX == 0) {
					if (gameObject.getBody().getLinearVelocity().y < 1
							&& gameObject.getBody().getLinearVelocity().y > -1) {
						sendGameMessage(gameMessage_c2s_rpc);
					}
				} else {
					if (forceX > 0 && gameObject.getBody().getLinearVelocity().x < 10
							|| forceX < 0 && gameObject.getBody().getLinearVelocity().x > -10) {
						sendGameMessage(gameMessage_c2s_rpc);
					}
				}

			}
		}

	}

	public void inputProcess() {
		stage.getRoot().findActor("start").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (clientSocket == null) {
					int port = Integer.parseInt(((TextArea) (stage.getRoot().findActor("localPort"))).getText());
					clientSocket = new ClientSocket(port,
							((TextArea) (stage.getRoot().findActor("remoteIp"))).getText(),
							Integer.parseInt(((TextArea) (stage.getRoot().findActor("remotePort"))).getText()),
							NetTest7Screen.this);

				}
			}
		});
		stage.getRoot().findActor("send").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				clientSocket.sendMessage(((TextArea) stage.getRoot().findActor("message")).getText());
			}
		});

		stage.getRoot().findActor("A").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				aJustUpAction();
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				aJustPressAction();
				return true;
			}
		});
		stage.getRoot().findActor("D").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				dJustUpAction();
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				dJustPressAction();
				return true;
			}
		});
		stage.getRoot().findActor("Z").addListener(new ActorInputListenner() {

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				stage.unfocus(stage.getRoot().findActor("userName"));
				gameObjectId = Long.parseLong(((TextArea) stage.getRoot().findActor("userName")).getText());

				zPressAction();
			}
		});
		stage.getRoot().findActor("X").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				xPressAction();
			}
		});
		stage.getRoot().findActor("G").addListener(new ActorInputListenner() {

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				gameObjectId = Long.parseLong(((TextArea) stage.getRoot().findActor("userName")).getText());

				gPressAction();
			}
		});

		stage.getRoot().findActor("W").addListener(new ActorInputListenner() {

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				gameObjectId = Long.parseLong(((TextArea) stage.getRoot().findActor("userName")).getText());
				wJustPressAction();
			}
		});

		stage.getRoot().findActor("login").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				sendGameMessage(new USER_MESSAGE(new C2S_LOGIN("11111111111111111111111111111111").toBytes()));
			}
		});

	}

	@Override
	public void disposeUdpMessage(Session session, byte[] data) {

		System.out.println("disposing");
		// disposing=true;
		// if(session==null)this.session=session;
		int typeOrdinal = ByteUtil.bytesToInt(ByteUtil.subByte(data, Message.TYPE_BYTE_LENGTH, 0));
		System.out.println("type:" + GameMessageType.values()[typeOrdinal]);
		byte[] src = ByteUtil.subByte(data, data.length - Message.TYPE_BYTE_LENGTH, Message.TYPE_BYTE_LENGTH);
		B2DGameObject gameObject;
		switch (GameMessageType.values()[typeOrdinal]) {
		case S2C_B2D_GET_ALL_GAMEOBJECT:
			S2C_B2D_GET_ALL_GAMEOBJECT gameMessage_s2c_gago = new S2C_B2D_GET_ALL_GAMEOBJECT(src);
			for (int i = 0; i < gameMessage_s2c_gago.b2dBoxBaseInformationArray.size; i++) {
				B2dBoxBaseInformation info = gameMessage_s2c_gago.b2dBoxBaseInformationArray.get(i);
				System.out.println(info.toString());
				gameObject = gameWorld.findGameObject(info.gameObjectId);
				if (gameObject != null) {
					gameObject.getGameObjectUpdateQueue().add(info);
				} else {
					gameWorld.getGameObjectCreationQueue().add(info);
				}
			}

			break;
		case S2C_B2D_REMOVE_GAMEOBJECT:
			S2C_B2D_REMOVE_GAMEOBJECT gameMessage_s2c_rgo = new S2C_B2D_REMOVE_GAMEOBJECT(src);
			gameObject = gameWorld.findGameObject(gameMessage_s2c_rgo.gameObjectId);
			if (gameObject != null) {
				gameWorld.getGameObjectRemoveQueue().add(gameObject);
			}
			break;
		case S2C_B2D_GET_GAMEOBJECT:
			S2C_B2D_GET_GAMEOBJECT gameMessage_s2c_ggo = new S2C_B2D_GET_GAMEOBJECT(src);
			gameObject = gameWorld.findGameObject(gameMessage_s2c_ggo.b2dBoxBaseInformation.gameObjectId);
			if (gameObject == null) {
				System.out.println(gameMessage_s2c_ggo.b2dBoxBaseInformation);
				gameWorld.getGameObjectCreationQueue().add(gameMessage_s2c_ggo.b2dBoxBaseInformation);
			} else {
				gameObject.getGameObjectUpdateQueue().add(gameMessage_s2c_ggo.b2dBoxBaseInformation);
			}
			break;
		default:
			break;
		}
	}

}
