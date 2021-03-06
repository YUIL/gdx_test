package com.yuil.game.screen;

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
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.sun.glass.ui.SystemClipboard;
import com.sun.org.glassfish.gmbal.GmbalException;
import com.yuil.game.entity.GameObject;
import com.yuil.game.entity.MyGameWorld1;
import com.yuil.game.input.ActorInputListenner;
import com.yuil.game.input.KeyboardStatus;
import com.yuil.game.net.udp.Session;
import com.yuil.game.net.udp.UdpServer;
import com.yuil.game.stage.StageManager;
import com.yuil.game.util.GameManager;


public class NetTest5Screen extends TestScreen2D {
	Texture texture = new Texture(Gdx.files.internal("images/role1.png"));
	volatile UdpServer udpServer;
	volatile Session session;
	String recvString = null;
	volatile MyGameWorld1 myGameWorld1=new MyGameWorld1();
	JsonValue jsonValue;
	JsonReader jsonReader = new JsonReader();
	KeyboardStatus keyboardStatus=new KeyboardStatus();
	String gameObjectName=null;
	long lastAutoSendTime=0;
	int autoSendIterval=5000;
	Thread screenLogicThread;
	ScreenLogic screenLogic;
	int speed=50;
	long nextUpdateTime=0;
	int updateInterval=10;
//	volatile GameWorld
	public NetTest5Screen(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
		StageManager.guiFactor.setStage(stage, "gui/NetTest5Gui.xml");
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
		//System.out.println(gameWorld.getGameObjectArray().size);
		super.render(delta);
		
		batch.begin();
		for (int i = 0; i < myGameWorld1.getGameObjectArray().size; i++) {
			 GameObject gameObject=myGameWorld1.getGameObjectArray().get(i);
			//gameObject.setPosition(new Vector3(gameObject.getPosition().x+gameObject.getInertiaForce().x*delta, gameObject.getPosition().y+gameObject.getInertiaForce().y*delta, 0));
			batch.draw(texture, gameObject.getPosition().x, gameObject.getPosition().y);
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
		
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			if (!keyboardStatus.iswJustPress()) {
				 //dJustPressAction();
				
			}
			keyboardStatus.setwJustPress(true);
		} else if (keyboardStatus.iswJustPress()) {
			wJustUpAction();
			keyboardStatus.setwJustPress(!keyboardStatus.iswJustPress());
		}
		
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
			cPressAction();
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
		
		screenLogic.stop();
		stopUdpServer();
	}

	@Override
	public void dispose() {
		super.dispose();
		screenLogic.stop();
		stopUdpServer();
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
	
	public void stopUdpServer(){
		if (udpServer != null) {
			udpServer.stop();
		}
	}
	
	private void initScreenLogic(){
		screenLogic = new ScreenLogic(1) {
			public void run() {
				//lastRunTime=System.currentTimeMillis();
				nextUpdateTime=System.currentTimeMillis();
				while (!isStoped) {
					
					if (System.currentTimeMillis()>nextUpdateTime) {
						nextUpdateTime+=updateInterval;
						myGameWorld1.update(1/1000f);
						myGameWorld1.getBeCollidedGameObjectArray().clear();
					}
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
			gameObject.setRectangle(gameObject.getPosition().x, gameObject.getPosition().y,
					jsonValue.get("ago").get("r").get("width").asFloat(),
					jsonValue.get("ago").get("r").get("height").asFloat());
			myGameWorld1.addGameObject(gameObject);
		} else if (jsonValue.get("cgo") != null) {
			jsonValue=jsonValue.get("cgo");

			//System.out.println(System.currentTimeMillis()-lastSendTime);
			GameObject gameObject=myGameWorld1.findGameObject(jsonValue.getString("name"));
			if(gameObject!=null){
				if(jsonValue.get("set")!=null){
					jsonValue=jsonValue.get("set");
					if (jsonValue.get("p") != null) {
						gameObject.setPosition(new Vector3(jsonValue.get("p").getFloat("x"),
								jsonValue.get("p").getFloat("y"), 0));
						gameObject.setInertiaForce(new Vector3(jsonValue.get("i").getFloat("x"),
								jsonValue.get("i").getFloat("y"), 0));
					} else if (jsonValue.get("i") != null) {
						jsonValue = jsonValue.get("i");
						if (jsonValue.get("x") != null) {
							gameObject.getInertiaForce().x = jsonValue.getFloat("x");
						}
						if (jsonValue.get("y") != null) {
							gameObject.getInertiaForce().y = jsonValue.getFloat("y");

						}
					}
				}else if(jsonValue.get("add")!=null){
					jsonValue=jsonValue.get("add");
					if (jsonValue.get("p") != null) {
						gameObject.setPosition(new Vector3(gameObject.getPosition().x+jsonValue.get("p").getFloat("x"),
								gameObject.getPosition().y+jsonValue.get("p").getFloat("y"), 0));
						gameObject.setInertiaForce(new Vector3(gameObject.getInertiaForce().x+jsonValue.get("i").getFloat("x"),
								gameObject.getInertiaForce().y+jsonValue.get("i").getFloat("y"), 0));
					} else if (jsonValue.get("i") != null) {
						jsonValue = jsonValue.get("i");
						if (jsonValue.get("x") != null) {
							gameObject.getInertiaForce().x += jsonValue.getFloat("x");
						}
						if (jsonValue.get("y") != null) {
							gameObject.getInertiaForce().y += jsonValue.getFloat("y");

						}
					}
				}
			}else{
				sendMessage("{ggo:{name:"+jsonValue.getString("name")+"}}");
			}
		}else if (jsonValue.get("ggo") != null) {
			System.out.println(recvString);
			if(jsonValue.get("ggo").getString("name").equals("")){
				
			}else{
				GameObject gameObject;
				gameObject=myGameWorld1.findGameObject(jsonValue.get("ggo").getString("name"));
				if(gameObject==null){
					gameObject=new GameObject(jsonValue.get("ggo").getString("name"));
					myGameWorld1.addGameObject(gameObject);
				}
				gameObject.setPosition(new Vector3(jsonValue.get("ggo").get("p").getFloat("x"), jsonValue.get("ggo").get("p").getFloat("y"), 0));;
				gameObject.setRectangle(gameObject.getPosition().x, gameObject.getPosition().y,
						jsonValue.get("ggo").get("r").get("width").asFloat(),
						jsonValue.get("ggo").get("r").get("height").asFloat());
				gameObject.setInertiaForce(new Vector3(jsonValue.get("ggo").get("i").getFloat("x"), jsonValue.get("ggo").get("i").getFloat("y"), 0));
			}
			
		} else if (jsonValue.get("rgo") != null) {
			GameObject gameObject=myGameWorld1.findGameObject(jsonValue.get("rgo").getString("name"));
			if(gameObject!=null){
				myGameWorld1.getGameObjectArray().removeValue(gameObject, true);
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
					udpServer.sessionArray.add(session);
				}
				return udpServer.send(str.getBytes(), session,false);
			}
		}
		return false;
		
	}
	
	
	
	private void zPressAction(){
		sendMessage("{ago:{name:"+gameObjectName+",p:{x:100,y:300},r:{width:"+texture.getWidth()+",height:"+texture.getHeight()+"}}}");

	}
	private void xPressAction(){
		if(myGameWorld1.findGameObject(gameObjectName)!=null)
			sendMessage("{rgo:{name:"+gameObjectName+"}}");
	
	}
	private void cPressAction(){
		if(myGameWorld1.findGameObject(gameObjectName)!=null)
			sendMessage("{cgo:{name:"+gameObjectName+",set:{i:{x:0,y:0}}}}");
	
	}
	private void gPressAction(){
		sendMessage("{ggo:{name:"+gameObjectName+"}}");
	}
	
	private void aJustPressAction(){
		GameObject gameObject=myGameWorld1.findGameObject(gameObjectName);
		if (gameObject!=null) {
			sendMessage("{cgo:{name:"+gameObjectName+",add:{i:{x:"+(-1*speed)+"}}}}");
		}
	}
	private void aJustUpAction(){
		GameObject gameObject=myGameWorld1.findGameObject(gameObjectName);
		if (gameObject!=null) {
			//if(gameObject.getInertiaForce().x!=0)
				sendMessage("{cgo:{name:"+gameObjectName+",add:{i:{x:"+(speed)+"}}}}");
			
		}
		//System.out.println("object count:"+gameWorld.getGameObjectArray().size);
	}
	
	private void dJustPressAction(){
		GameObject gameObject=myGameWorld1.findGameObject(gameObjectName);
		if (gameObject!=null) {
			sendMessage("{cgo:{name:"+gameObjectName+",add:{i:{x:"+(speed)+"}}}}");
		}
	}
	private void dJustUpAction(){
		GameObject gameObject=myGameWorld1.findGameObject(gameObjectName);
		if (gameObject!=null) {
			//if(gameObject.getInertiaForce().x!=0)
				sendMessage("{cgo:{name:"+gameObjectName+",add:{i:{x:"+(-1*speed)+"}}}}");
		}
	}
	private void wJustPressAction(){
		GameObject gameObject=myGameWorld1.findGameObject(gameObjectName);
		if (gameObject!=null) {
			sendMessage("{cgo:{name:"+gameObjectName+",add:{i:{x:200,y:0}}}}");
		}
	}
	private void wJustUpAction(){
		GameObject gameObject=myGameWorld1.findGameObject(gameObjectName);
		if (gameObject!=null) {
			sendMessage("{cgo:{name:"+gameObjectName+",add:{i:{x:0,y:200}}}}");
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
		
		stage.getRoot().findActor("W").addListener(new ActorInputListenner() {
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				gameObjectName=((TextArea)stage.getRoot().findActor("userName")).getText();

				wJustUpAction();
			}
		});
		
	}
}
