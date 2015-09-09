package com.mygdx.game.screen;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.entity.GameObject;
import com.mygdx.game.entity.GameWorld;
import com.mygdx.game.input.ActorInputListenner;
import com.mygdx.game.input.KeyboardStatus;
import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpServer;
import com.mygdx.game.stage.StageManager;
import com.mygdx.game.util.GameManager;


public class NetTest5Screen extends TestScreen2D {
	Texture texture = new Texture(Gdx.files.internal("images/button_0.png"));
	volatile UdpServer udpServer;
	volatile Session session;
	String recvString = null;
	volatile GameWorld gameWorld=new GameWorld();
	JsonValue jsonValue;
	JsonReader jsonReader = new JsonReader();
	KeyboardStatus keyboardStatus=new KeyboardStatus();
	String gameObjectName=null;
	long lastAutoSendTime=0;
	int autoSendIterval=5000;
	Thread screenLogicThread;
	ScreenLogic screenLogic;
//	volatile GameWorld
	public NetTest5Screen(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
		StageManager.guiFactor.setStageFromXml(stage, "gui/NetTest5Gui.xml");
		inputProcess();
		GameManager.setInputProcessor(stage);
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
		super.render(delta);
		batch.begin();
		for (int i = 0; i < gameWorld.getGameObjectArray().size; i++) {
			GameObject gameObject=gameWorld.getGameObjectArray().get(i);
			gameObject.setPosition(new Vector3(gameObject.getPosition().x+gameObject.getInertiaForce().x*delta, gameObject.getPosition().y+gameObject.getInertiaForce().y*delta, 0));
			batch.draw(texture, gameWorld.getGameObjectArray().get(i).getPosition().x, gameWorld.getGameObjectArray().get(i).getPosition().y);
		}
		batch.end();
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
	}

	@Override
	public void dispose() {
		super.dispose();
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
	
	private void initScreenLogic(){
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
								
								while (!session.getRecvMessageQueue().isEmpty()) {
									recvString = new String(session.getRecvMessageQueue().poll().getData());
									if (recvString!=null){
										disposeMessage();
									}
									
									

								}
							} else {
								if (!udpServer.sessionMap.isEmpty()) {
									for (Map.Entry<Long, Session> entry : udpServer.sessionMap.entrySet()) {
										session = entry.getValue();
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
			if(jsonValue.get("ggo").getString("name").equals("")){
				
			}else{
				GameObject gameObject;
				gameObject=gameWorld.findGameObject(jsonValue.get("ggo").getString("name"));
				if(gameObject==null){
					gameObject=new GameObject(jsonValue.get("ggo").getString("name"));
					gameWorld.addGameObject(gameObject);
				}
				gameObject.setPosition(new Vector3(jsonValue.get("ggo").get("p").getFloat("x"), jsonValue.get("ggo").get("p").getFloat("y"), 0));;
				gameObject.setInertiaForce(new Vector3(jsonValue.get("ggo").get("i").getFloat("x"), jsonValue.get("ggo").get("i").getFloat("y"), 0));
			}
			
		} else if (jsonValue.get("rgo") != null) {
			GameObject gameObject=gameWorld.findGameObject(jsonValue.get("rgo").getString("name"));
			if(gameObject!=null){
				gameWorld.getGameObjectArray().removeValue(gameObject, true);
			}
		}
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
					udpServer.sessionMap.put(session.getId(), session);
				}
				return udpServer.send(str.getBytes(), session);
			}
		}
		return false;
		
	}
	
	
	
	private void zPressAction(){
		sendMessage("{ago:{name:"+gameObjectName+",p:{x:100,y:0},r:{width:"+texture.getWidth()+",height:"+texture.getHeight()+"}}}");

	}
	private void xPressAction(){
		if(gameWorld.findGameObject(gameObjectName)!=null)
			sendMessage("{rgo:{name:"+gameObjectName+"}}");
	
	}
	private void gPressAction(){
		sendMessage("{ggo:{name:"+gameObjectName+"}}");
	}
	
	private void aJustPressAction(){
		if (gameWorld.findGameObject(gameObjectName)!=null) {
			sendMessage("{cgo:{name:"+gameObjectName+",i:{x:-200,y:0}}}");
		}
	}
	private void aJustUpAction(){
		if (gameWorld.findGameObject(gameObjectName)!=null) {
			sendMessage("{cgo:{name:"+gameObjectName+",i:{x:0,y:0}}}");
		}
	}
	
	private void dJustPressAction(){
		if (gameWorld.findGameObject(gameObjectName)!=null) {
			sendMessage("{cgo:{name:"+gameObjectName+",i:{x:200,y:0}}}");
		}
	}
	private void dJustUpAction(){
		if (gameWorld.findGameObject(gameObjectName)!=null) {
			sendMessage("{cgo:{name:"+gameObjectName+",i:{x:0,y:0}}}");
		}
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
		
	}
}
