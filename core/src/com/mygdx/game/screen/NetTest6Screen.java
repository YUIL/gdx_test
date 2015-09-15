package com.mygdx.game.screen;

import java.net.BindException;
import java.net.InetSocketAddress;
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
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.entity.GameObjectB2D;
import com.mygdx.game.entity.GameWorldB2D;
import com.mygdx.game.input.ActorInputListenner;
import com.mygdx.game.input.KeyboardStatus;
import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpMessage;
import com.mygdx.game.net.udp.UdpMessageListener;
import com.mygdx.game.net.udp.UdpServer;
import com.mygdx.game.stage.StageManager;
import com.mygdx.game.util.GameManager;

public class NetTest6Screen extends TestScreen2D implements UdpMessageListener{
	OrthographicCamera camera = new OrthographicCamera(
			Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10);
	TextureRegion textureRegion = new TextureRegion(new Texture(
			Gdx.files.internal("images/role1.png")));
	TextureRegion backgroundRegion = new TextureRegion(new Texture(
			Gdx.files.internal("images/NetTest6_background2.png")));
	volatile UdpServer udpServer;
	volatile Session session;
	String recvString = null;
	volatile GameWorldB2D gameWorld = new GameWorldB2D();
	private Box2DDebugRenderer debugRenderer;
	JsonReader jsonReader = new JsonReader();
	KeyboardStatus keyboardStatus = new KeyboardStatus();
	String gameObjectName = null;
	long lastAutoSendTime = 0;
	int autoSendIterval = 5000;
	Thread screenLogicThread;
	ScreenLogic screenLogic;
	int speed = 50;
	long nextUpdateTime=0;
	int updateInterval=10;
	volatile boolean disposing=false;

	public NetTest6Screen(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
		camera.position.set(0, Gdx.graphics.getHeight() /20, 0);
		StageManager.guiFactor.setStageFromXml(stage, "gui/NetTest6Gui.xml");
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

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1.f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		stage.getCamera().update();
		batch.getProjectionMatrix().set(stage.getCamera().combined);
		batch.begin();
		batch.draw(backgroundRegion,0-camera.position.x*10-205,0-camera.position.y*10,
				0, 0,
				backgroundRegion.getRegionWidth(),backgroundRegion.getRegionHeight(),
				1.5f,1.5f,
				0);
		batch.end();
		camera.update();
		batch.getProjectionMatrix().set(camera.combined);
		
		
		//debugRenderer.render(gameWorld.getBox2dWorld(), camera.combined);
		batch.begin();
		for (int i = 0; i < gameWorld.getGameObjectArray().size; i++) {
			GameObjectB2D gameObject = gameWorld.getGameObjectArray().get(i);
			// gameObject.setPosition(new
			// Vector3(gameObject.getPosition().x+gameObject.getInertiaForce().x*delta,
			// gameObject.getPosition().y+gameObject.getInertiaForce().y*delta,
			// 0));
			// batch.draw(textureRegion, gameObject.getPosition().x,
			// gameObject.getPosition().y);
		//System.out.println(gameObject.getBody().isFixedRotation());
			Vector2 position = gameObject.getPosition(); // that's the box's center position
			float angle = MathUtils.radiansToDegrees
					* gameObject.getBody().getAngle();
			batch.draw(textureRegion, position.x - gameObject.getWidth() / 2,
					position.y - gameObject.getHeight() / 2, 
					gameObject.getWidth() / 2f, gameObject.getHeight() / 2f, 
					gameObject.getWidth(), gameObject.getHeight(), 
					1, 1,
					angle);
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
		if(Gdx.input.isKeyJustPressed(Input.Keys.T)){
			GameObjectB2D gameObject=gameWorld.findGameObject(gameObjectName);
			camera.position.x=gameObject.getPosition().x;
			camera.position.y=gameObject.getPosition().y;
		}

		/*if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
			for (int i = 0; i < gameWorld.getGameObjectArray().size; i++) {
				GameObjectB2D gameObject = gameWorld.getGameObjectArray()
						.get(i);
				Vector2 p = gameObject.getPosition();
				System.out.println("p:" + gameObject.getBody().getPosition());
				System.out.println(gameObject.getBody().getLinearVelocity());
				// gameObject.getBody().setTransform(p.x-10, p.y, 0);
				gameObject.getBody().getWorldPoint(p);
				gameObject.getBody().applyForce(10000, 0, p.x, p.y, true);

			}
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
			for (int i = 0; i < gameWorld.getGameObjectArray().size; i++) {
				Vector2 p = gameWorld.getGameObjectArray().get(i).getPosition();
				System.out.println("p:"
						+ gameWorld.getGameObjectArray().get(i).getBody()
								.getPosition());
				System.out.println(gameWorld.getGameObjectArray().get(i)
						.getBody().getLinearVelocity());
				gameWorld.getGameObjectArray().get(i).getBody()
						.setTransform(p.x + 10, p.y, 0);
			}
		}*/
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
		stopUdpServer();
		debugRenderer.dispose();
		gameWorld.getBox2dWorld().dispose();
	}

	@Override
	public void dispose() {
		super.dispose();
		screenLogic.stop();
		stopUdpServer();
		debugRenderer.dispose();
		gameWorld.getBox2dWorld().dispose();
	}

	private boolean initUdpServer(int port) {
		if (port < 10000) {
			try {
				System.out.println("try port:" + port);
				udpServer = new UdpServer(port);
				udpServer.setUdpMessageListener(this);
				return true;
			} catch (BindException e) {
				System.out.println(port + " exception!");
				// TODO: handle exception
				port++;
				initUdpServer(port);
			}
		} else {
			System.err.println("port must <10000!");
		}
		return false;
	}

	public void stopUdpServer() {
		if (udpServer != null) {
			udpServer.stop();
		}
	}

	public boolean sendMessage(String str) {
		if (str == null) {
			System.err.println("message==null");
		} else {
			// disposeSendMessage(str);
			if (udpServer == null) {
				System.err.println("updServer==null");
			} else {
				if (session == null) {
					System.err.println("session==null");
					
					session=udpServer.createSession(new Random().nextLong(), new InetSocketAddress(
							((TextArea) (stage.getRoot().findActor("remoteIp")))
							.getText(), Integer
							.parseInt(((TextArea) (stage.getRoot()
									.findActor("remotePort")))
									.getText())));
				}
				return udpServer.send(str.getBytes(), session);
			}
		}
		return false;

	}

	private void initScreenLogic() {
		screenLogic = new ScreenLogic(1) {
			public void run() {
				nextUpdateTime = System.currentTimeMillis();
				while (!isStoped) {
					if (System.currentTimeMillis() > nextUpdateTime) {
						nextUpdateTime += updateInterval;
						if (!disposing) {
							gameWorld.update(updateInterval / 1000f);
						}
						GameObjectB2D gameObject=gameWorld.findGameObject(gameObjectName);
						if(gameObject!=null){
							camera.position.x=gameObject.getPosition().x;
							camera.position.y=gameObject.getPosition().y;
						}
						
					}
					try {
						if (udpServer != null) {
							if (System.currentTimeMillis() - lastAutoSendTime > autoSendIterval) {
								lastAutoSendTime = System.currentTimeMillis();
								sendMessage("");
							}
							/*if (session != null) {

								while (!session.getRecvMessageQueue().isEmpty()) {
									recvString = new String(session
											.getRecvMessageQueue().poll()
											.getData());
									if (recvString != null) {
										disposeMessage();
									}
								}
							} else {
								if (udpServer.sessionArray.size!=0) {
									session=udpServer.sessionArray.get(0);
								}
							}*/
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
		sendMessage("{gago:}");
	}

	private void zPressAction() {
		float x=new Random().nextFloat()*20;
		
		sendMessage("{"+ 
						"ago:{n:" + gameObjectName + 
							",t:{p:{x:"+x+",y:30},a:0}"+ 
							",av:0"+
							",s:{w:"+ 2 + ",h:"+ 2 + "}"+ 
							",d:1"+
							",l:{x:0,y:0} "+
						"}"+ 
					"}");

	}

	private void xPressAction() {
		if (gameWorld.findGameObject(gameObjectName) != null)
			sendMessage("{rgo:{n:" + gameObjectName + "}}");

	}

	private void cPressAction() {
		if (gameWorld.findGameObject(gameObjectName) != null)
			sendMessage("{cgo:{n:" + gameObjectName + ",set:{i:{x:0,y:0}}}}");

	}

	private void gPressAction() {
		sendMessage("{ggo:{n:" + gameObjectName + "}}");
	}

	private void aJustPressAction() {
		GameObjectB2D gameObject = gameWorld.findGameObject(gameObjectName);
		if (gameObject != null) {
			sendMoveMessage("{rpc:{af:{n:" + gameObjectName + ",fx:-1000,fy:0}}}");
			//sendMessage("{rpc:{af:{n:" + gameObjectName + ",fx:-1000,fy:0}}}");
		}
	}

	private void aJustUpAction() {
	/*	GameObjectB2D gameObject = gameWorld.findGameObject(gameObjectName);
		if (gameObject != null) {
			

		}*/
		// System.out.println("object count:"+gameWorld.getGameObjectArray().size);
	}

	private void dJustPressAction() {
		GameObjectB2D gameObject = gameWorld.findGameObject(gameObjectName);
		if (gameObject != null) {
			//sendMessage("{rpc:{af:{n:" + gameObjectName + ",fx:1000,fy:0}}}");
			sendMoveMessage("{rpc:{af:{n:" + gameObjectName + ",fx:1000,fy:0}}}");
		}
	}

	private void dJustUpAction() {
		/*GameObjectB2D gameObject = gameWorld.findGameObject(gameObjectName);
		if (gameObject != null) {
			// if(gameObject.getInertiaForce().x!=0)
			sendMessage("{cgo:{n:" + gameObjectName + ",add:{i:{x:"
					+ (-1 * speed) + "}}}}");
		}*/
	}

	private void wJustPressAction() {
		GameObjectB2D gameObject = gameWorld.findGameObject(gameObjectName);
		if (gameObject != null) {
			//sendMessage("{rpc:{af:{n:" + gameObjectName + ",fx:0,fy:6000}}}");
			sendMoveMessage("{rpc:{af:{n:" + gameObjectName + ",fx:0,fy:6000}}}");
		}
	}

	private void wJustUpAction() {
		/*GameObjectB2D gameObject = gameWorld.findGameObject(gameObjectName);
		if (gameObject != null) {
			sendMessage("{cgo:{name:" + gameObjectName
					+ ",add:{i:{x:0,y:200}}}}");
		}*/
	}
	
	public void sendMoveMessage(String str){
		JsonValue jsonValue = jsonReader.parse(str);
		 if (jsonValue.get("rpc") != null) {
			 jsonValue = jsonValue.get("rpc");
			 if(jsonValue.get("af")!=null){
				 jsonValue = jsonValue.get("af");
				 String name=jsonValue.getString("n");
				 GameObjectB2D gameObject=gameWorld.findGameObject(name);
				 if (gameObject!=null){
					 float forceX=jsonValue.getFloat("fx");
					 if(gameObject.getSpeed()<gameObject.getMaxSpeed()){
						 if(forceX==0){
							 if(gameObject.getBody().getLinearVelocity().y<1&&gameObject.getBody().getLinearVelocity().y>-1){
								sendMessage(str);
							 }
						 }else{
							 if (forceX>0&&gameObject.getBody().getLinearVelocity().x<10||forceX<0&&gameObject.getBody().getLinearVelocity().x>-10) {
								 sendMessage(str);
							}
						 }
						
						 
					 }
				 }
					
			 }	
			 
		 }
	}

	public void inputProcess() {
		stage.getRoot().findActor("start")
				.addListener(new ActorInputListenner() {
					public void touchUp(InputEvent event, float x, float y,
							int pointer, int button) {
						if (udpServer == null) {
							int port = Integer.parseInt(((TextArea) (stage
									.getRoot().findActor("localPort")))
									.getText());
							System.out.println("server start at port:" + port);
							initUdpServer(port);

							udpServer.start();
						}
					}
				});
		stage.getRoot().findActor("send")
				.addListener(new ActorInputListenner() {
					public void touchUp(InputEvent event, float x, float y,
							int pointer, int button) {
						sendMessage(((TextArea) stage.getRoot().findActor(
								"message")).getText());
					}
				});

		stage.getRoot().findActor("A").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				aJustUpAction();
			}

			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				aJustPressAction();
				return true;
			}
		});
		stage.getRoot().findActor("D").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				dJustUpAction();
			}

			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				dJustPressAction();
				return true;
			}
		});
		stage.getRoot().findActor("Z").addListener(new ActorInputListenner() {

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				gameObjectName = ((TextArea) stage.getRoot().findActor(
						"userName")).getText();

				zPressAction();
			}
		});
		stage.getRoot().findActor("X").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {

				xPressAction();
			}
		});
		stage.getRoot().findActor("G").addListener(new ActorInputListenner() {

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				gameObjectName = ((TextArea) stage.getRoot().findActor(
						"userName")).getText();

				gPressAction();
			}
		});

		stage.getRoot().findActor("W").addListener(new ActorInputListenner() {

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				gameObjectName = ((TextArea) stage.getRoot().findActor(
						"userName")).getText();

				wJustPressAction();
			}
		});

	}

	@Override
	public void disposeUdpMessage(Session session, UdpMessage message) {
		// TODO Auto-generated method stub
		disposing=true;
		if(session==null)this.session=session;
		System.out.println(recvString);
		recvString=new String(message.getData());
		JsonValue jsonValue = jsonReader.parse(recvString);
		if (jsonValue.get("rpc") != null) {
			jsonValue = jsonValue.get("rpc");
			if(jsonValue.get("af")!=null){
				jsonValue = jsonValue.get("af");
				String name=jsonValue.getString("n");
				GameObjectB2D gameObject=gameWorld.findGameObject(name);
				if (gameObject!=null){
					/*float forceX=jsonValue.getFloat("fx");
					float forceY=jsonValue.getFloat("fy");
					gameObject.applyForce(forceX, forceY);*/
					sendMessage("{ggo:{n:" + name + "}}");
				}
				
			}

		} else {
			if (jsonValue.get("gago") != null) {
				jsonValue = jsonValue.get("gago");
				for (int i = 0; i < jsonValue.size; i++) {
					String name=jsonValue.get(i).getString("n");
					float x=jsonValue.get(i).get("t").get("p").getFloat("x");
					float y=jsonValue.get(i).get("t").get("p").getFloat("y");
					float angle=jsonValue.get(i).get("t").getFloat("a");
					float angularVelocity=jsonValue.get(i).getFloat("av");
					float width=jsonValue.get(i).get("s").getFloat("w");
					float height=jsonValue.get(i).get("s").getFloat("h");
					float density=jsonValue.get(i).getFloat("d");
					float lx=jsonValue.get(i).get("l").getFloat("x");
					float ly=jsonValue.get(i).get("l").getFloat("y");
					GameObjectB2D gameObject=gameWorld.findGameObject(name);
					if (gameObject!=null) {
						//System.out.println("update gameObject");
						gameWorld.updateGameObject(gameObject,x, y, angle,angularVelocity, width, height, density, lx, ly);
					}else {
						gameObject=gameWorld.addBoxGameObject(name, x, y, angle,angularVelocity, width, height, density, lx, ly);
						System.out.println(gameObject.toJson());
					}
				}
			}else if(jsonValue.get("ago") != null) {
				jsonValue=jsonValue.get("ago");
				String name=jsonValue.getString("n");
				
				GameObjectB2D gameObject=gameWorld.findGameObject(name);
				if (gameObject!=null) {
					
				}else {
					float x=jsonValue.get("t").get("p").getFloat("x");
					float y=jsonValue.get("t").get("p").getFloat("y");
					float angle=jsonValue.get("t").getFloat("a");
					float angularVelocity=jsonValue.getFloat("av");
					float width=jsonValue.get("s").getFloat("w");
					float height=jsonValue.get("s").getFloat("h");
					float density=jsonValue.getFloat("d");
					float lx=jsonValue.get("l").getFloat("x");
					float ly=jsonValue.get("l").getFloat("y");
					gameWorld.addBoxGameObject(name, x, y, angle, angularVelocity,width, height, density, lx, ly);
				}
			}else if(jsonValue.get("rgo") != null) {
				jsonValue=jsonValue.get("rgo");
				String name=jsonValue.getString("n");
				GameObjectB2D gameObject=gameWorld.findGameObject(name);
				if (gameObject!=null) {
					gameWorld.removeGameObject(name);
				}
			}else if(jsonValue.get("ggo") != null) {
				jsonValue=jsonValue.get("ggo");
				String name=jsonValue.getString("n");
				float x=jsonValue.get("t").get("p").getFloat("x");
				float y=jsonValue.get("t").get("p").getFloat("y");
				float angle=jsonValue.get("t").getFloat("a");
				float angularVelocity=jsonValue.getFloat("av");
				float width=jsonValue.get("s").getFloat("w");
				float height=jsonValue.get("s").getFloat("h");
				float density=jsonValue.getFloat("d");
				float lx=jsonValue.get("l").getFloat("x");
				float ly=jsonValue.get("l").getFloat("y");
				GameObjectB2D gameObject=gameWorld.findGameObject(name);
				if (gameObject!=null) {
					gameWorld.updateGameObject(gameObject,x, y, angle,angularVelocity, width, height, density, lx, ly);
				}else{
					gameWorld.addBoxGameObject(name, x, y, angle, angularVelocity,width, height, density, lx, ly);
				}
			}
			disposing=false;
		}
	}
}
