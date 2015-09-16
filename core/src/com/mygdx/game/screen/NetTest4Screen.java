package com.mygdx.game.screen;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.entity.GameObject;
import com.mygdx.game.entity.GameWorld;
import com.mygdx.game.input.ActorInputListenner;
import com.mygdx.game.input.KeyboardStatus;
import com.mygdx.game.net.Player;
import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpServer;
import com.mygdx.game.stage.StageManager;
import com.mygdx.game.util.GameManager;

/**
 * Created by i008 on 2015/8/27.
 */
public class NetTest4Screen extends TestScreen2D {

	volatile Label console;
	String recvString = null;
	volatile UdpServer udpServer;
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
	long lastAutoSendTime=0;
	long lastSendTime=0;
	long lastRecvTime=0;
	int autoSendIterval=5000;
	boolean aButtonPress=false;
	boolean dButtonPress=false;
	volatile GameWorld gameWorld=new GameWorld();
	KeyboardStatus keyboardStatus=new KeyboardStatus();
	String gameObjectName=null;
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
						if (udpServer != null) {
							if(System.currentTimeMillis()-lastAutoSendTime>autoSendIterval){
								lastAutoSendTime=System.currentTimeMillis();
								sendMessage("");
							}
							if (session != null) {
								
								/*while (!session.getRecvMessageQueue().isEmpty()) {
									recvString = new String(session.getRecvMessageQueue().poll().getData());
									if (recvString!=null){
										disposeMessage();
									}
									
									

								}*/
							} else {
								if (udpServer.sessionArray.size!=0) {
									for (Iterator<Session> iterator = udpServer.sessionArray.iterator(); iterator.hasNext();) {
										session = iterator.next();
										
									}
								}
							}
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

	public void disposeMessage(){
		//System.out.println(recvString.getBytes().length);
		jsonValue=jsonReader.parse(recvString);
		
		if (jsonValue.get("ago")!=null){
			GameObject gameObject=new GameObject(jsonValue.get("ago").get("name").asString());
			gameObject.setPosition(new Vector3(jsonValue.get("ago").get("p").get("x").asFloat(), jsonValue.get("ago").get("p").get("y").asFloat(), 0));
			gameWorld.addGameObject(gameObject);
		} else if (jsonValue.get("cgo") != null) {
			//System.out.println(System.currentTimeMillis()-lastSendTime);
			GameObject gameObject=gameWorld.findGameObject(jsonValue.get("cgo").getString("name"));
			if(gameObject!=null){
				if (jsonValue.get("cgo").get("p")!=null) {
					gameObject.setPosition(new Vector3(jsonValue.get("cgo").get("p").getFloat("x"), jsonValue.get("cgo").get("p").getFloat("y"), 0));
					gameObject.setInertiaForce(new Vector3(jsonValue.get("cgo").get("i").getFloat("x"), jsonValue.get("cgo").get("i").getFloat("y"), 0));
				}else if (jsonValue.get("cgo").get("i")!=null) {
					gameObject.setInertiaForce(new Vector3(jsonValue.get("cgo").get("i").getFloat("x"), jsonValue.get("cgo").get("i").getFloat("y"), 0));
					sendMessage("{ggo:{name:"+jsonValue.get("cgo").getString("name")+"}}");
				}
			}else{
				sendMessage("{ggo:{name:"+jsonValue.get("cgo").getString("name")+"}}");
			}
		}else if (jsonValue.get("ggo") != null) {
			GameObject gameObject;
			gameObject=gameWorld.findGameObject(jsonValue.get("ggo").getString("name"));
			if(gameObject==null){
				gameObject=new GameObject(jsonValue.get("ggo").getString("name"));
				gameWorld.addGameObject(gameObject);
			}
			gameObject.setPosition(new Vector3(jsonValue.get("ggo").get("p").getFloat("x"), jsonValue.get("ggo").get("p").getFloat("y"), 0));;
			gameObject.setInertiaForce(new Vector3(jsonValue.get("ggo").get("i").getFloat("x"), jsonValue.get("ggo").get("i").getFloat("y"), 0));
		} else if (jsonValue.get("rgo") != null) {
			GameObject gameObject=gameWorld.findGameObject(jsonValue.get("rgo").getString("name"));
			if(gameObject!=null){
				gameWorld.getGameObjectArray().removeValue(gameObject, true);
			}

		}else if (jsonValue.get("move") != null) {
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
	public void disposeSendMessage(String message){
		JsonValue jsonValue;
		JsonReader jsonReader = new JsonReader();
		jsonValue=jsonReader.parse(message);
		if (jsonValue.get("ago")!=null){
			GameObject gameObject=new GameObject(jsonValue.get("ago").get("name").asString());
			gameObject.setPosition(new Vector3(jsonValue.get("ago").get("x").asFloat(), jsonValue.get("ago").get("y").asFloat(), 0));
			gameWorld.addGameObject(gameObject);
		}
	}
	
	private void aPressedAction(){
		
	}
	private void dPressedAction(){
		
	}
	private void zPressAction(){
		sendMessage("{ago:{name:"+gameObjectName+",p:{x:100,y:0}}}");

	}
	private void xPressAction(){
		if(gameWorld.findGameObject(gameObjectName)!=null)
			sendMessage("{rgo:{name:"+gameObjectName+"}}");
	
	}
	private void gPressAction(){
		sendMessage("{ggo:{name:"+gameObjectName+"}}");
	}
	
	private void aJustPressAction(){
		sendMessage("{cgo:{name:"+gameObjectName+",i:{x:-200,y:0}}}");

	}
	private void aJustUpAction(){
		sendMessage("{cgo:{name:"+gameObjectName+",i:{x:0,y:0}}}");

	}
	
	private void dJustPressAction(){
		sendMessage("{cgo:{name:"+gameObjectName+",i:{x:200,y:0}}}");

	}
	private void dJustUpAction(){
		sendMessage("{cgo:{name:"+gameObjectName+",i:{x:0,y:0}}}");

	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		/*if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			aPressAction();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			dPressAction();
		}*/
		if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
			zPressAction();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
			xPressAction();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
			gPressAction();
		}
		
		
		/*if(aButtonPress){
			aPressAction();
		}
		if (dButtonPress){
			dPressAction();
		}*/
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			//aPressAction();
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
		
/*
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
		}*/

/*		if (selfPlayer.isMove()) {
			selfPlayer.getRectangle().setX(selfPlayer.getRectangle().getX() + speed * delta);

		}
		if (remotePlayer.isMove()) {
			remotePlayer.getRectangle().setX(remotePlayer.getRectangle().getX() + speed * delta);
		}*/

		/*
		batch.draw(texture, selfPlayer.getRectangle().getX(), selfPlayer.getRectangle().getY());
		batch.draw(texture, remotePlayer.getRectangle().getX(), remotePlayer.getRectangle().getY());
*/		
		batch.begin();
		for (int i = 0; i < gameWorld.getGameObjectArray().size; i++) {
			GameObject gameObject=gameWorld.getGameObjectArray().get(i);
			gameObject.setPosition(new Vector3(gameObject.getPosition().x+gameObject.getInertiaForce().x*delta, gameObject.getPosition().y+gameObject.getInertiaForce().y*delta, 0));
			batch.draw(texture, gameWorld.getGameObjectArray().get(i).getPosition().x, gameWorld.getGameObjectArray().get(i).getPosition().y);
		}
		batch.end();

		// netMessageProcess();
	}

	public boolean sendMessage(String str) {
		if(str==null){
			System.err.println("message==null");
		}else{
			//disposeSendMessage(str);
			if (udpServer == null) {
				System.err.println("updServer==null");
			} else {
				if (session == null) {
					session = new Session(new Random().nextLong());
					session.setContactorAddress(
							new InetSocketAddress(((TextArea) (stage.getRoot().findActor("remoteIp"))).getText(),
									Integer.parseInt(((TextArea) (stage.getRoot().findActor("remotePort"))).getText())));
					udpServer.sessionArray.add(session);
				}
				return udpServer.send(str.getBytes(), session);
			}
		}
		return false;
		
	}
/*
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
*/
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		if (udpServer != null) {
			udpServer.stop();
		}
		screenLogic.stop();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
		skin.dispose();
		udpServer.stop();
		screenLogic.stop();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

	}
	
	private boolean initUdpServer(int port){
		if(port<10000){
			try {
				System.out.println("try port:"+port);
				udpServer = new UdpServer(port);
				return true;
			} catch (BindException e) {
				System.out.println(port+" exception!");
				// TODO: handle exception
				port++;
				initUdpServer(port);
			}
		}else{
			System.err.println("port must <10000!");
		}
		return false;
	}

	public void inputProcess() {
		stage.getRoot().findActor("start").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if(udpServer==null){
					int port = Integer.parseInt(((TextArea) (stage.getRoot().findActor("localPort"))).getText());
					System.out.println("server start at port:" + port);
					initUdpServer(port);
					
					udpServer.start();
				}	
			}
		});
		stage.getRoot().findActor("send").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				sendMessage(((TextArea)stage.getRoot().findActor("message")).getText());
			}
		});
		stage.getRoot().findActor("login").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				gameObjectName=((TextArea)stage.getRoot().findActor("userName")).getText();

				sendMessage(("{" + "login:{name:"+gameObjectName+"}}"));
			}
		});
		stage.getRoot().findActor("getUser").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				gameObjectName=((TextArea)stage.getRoot().findActor("userName")).getText();

				sendMessage(("{" + "getUser:{name:"+gameObjectName+"}}"));
			}
		});
		stage.getRoot().findActor("A").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				aJustUpAction();
			}
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				aJustPressAction();
				return true;
			}
		});
		stage.getRoot().findActor("D").addListener(new ActorInputListenner() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				dJustUpAction();
			}
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				dJustPressAction();
				return true;
			}
		});
		stage.getRoot().findActor("Z").addListener(new ActorInputListenner() {

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				gameObjectName=((TextArea)stage.getRoot().findActor("userName")).getText();

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
				gameObjectName=((TextArea)stage.getRoot().findActor("userName")).getText();

				gPressAction();
			}
		});
		stage.getRoot().findActor("delSession").addListener(new ActorInputListenner() {
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				session=null;
			}
		});
	}
}
