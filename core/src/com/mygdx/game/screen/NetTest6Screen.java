package com.mygdx.game.screen;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
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
import com.mygdx.game.net.udp.UdpServer;
import com.mygdx.game.stage.StageManager;
import com.mygdx.game.util.GameManager;

public class NetTest6Screen extends TestScreen2D {
	OrthographicCamera camera=new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
	TextureRegion textureRegion = new TextureRegion(new Texture(Gdx.files.internal("images/role1.png")));
	volatile UdpServer udpServer;
	volatile Session session;
	String recvString = null;
	volatile GameWorldB2D gameWorld=new GameWorldB2D();
	private Box2DDebugRenderer debugRenderer;
	JsonValue jsonValue;
	JsonReader jsonReader = new JsonReader();
	KeyboardStatus keyboardStatus=new KeyboardStatus();
	String gameObjectName=null;
	long lastAutoSendTime=0;
	int autoSendIterval=5000;
	Thread screenLogicThread;
	ScreenLogic screenLogic;
	int speed=50;
	public NetTest6Screen(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
		camera.position.set(0, 1, 0);
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
		//System.out.println(gameWorld.getGameObjectArray().size);
		
		super.render(delta);
		camera.update();
		batch.getProjectionMatrix().set(camera.combined);
		debugRenderer.render(gameWorld.getBox2dWorld(), camera.combined);
		batch.begin();
		for (int i = 0; i < gameWorld.getGameObjectArray().size; i++) {
			 GameObjectB2D gameObject=gameWorld.getGameObjectArray().get(i);
			//gameObject.setPosition(new Vector3(gameObject.getPosition().x+gameObject.getInertiaForce().x*delta, gameObject.getPosition().y+gameObject.getInertiaForce().y*delta, 0));
			//batch.draw(textureRegion, gameObject.getPosition().x, gameObject.getPosition().y);
			
			
			Vector2 position = gameObject.getPosition(); // that's the box's center position
			float angle = MathUtils.radiansToDegrees * gameObject.getBody().getAngle(); // the rotation angle around the center
			batch.draw(textureRegion, position.x - 1, position.y - 1, // the bottom left corner of the box, unrotated
				1f, 1f, // the rotation center relative to the bottom left corner of the box
				gameObject.getWidth(), gameObject.getHeight(), // the width and height of the box
				1, 1, // the scale on the x- and y-axis
				angle);
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
		if(Gdx.input.isKeyJustPressed(Input.Keys.L)){
			lPressAction();
		}
		
		
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.T)){
			for (int i = 0; i < gameWorld.getGameObjectArray().size; i++) {
				Vector2 p=gameWorld.getGameObjectArray().get(i).getPosition();
				System.out.println("p:"+gameWorld.getGameObjectArray().get(i).getBody().getPosition());
				System.out.println(gameWorld.getGameObjectArray().get(i).getBody().getLinearVelocity());
				gameWorld.getGameObjectArray().get(i).getBody().setTransform(p.x-10, p.y, 0);
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.Y)){
			for (int i = 0; i < gameWorld.getGameObjectArray().size; i++) {
				Vector2 p=gameWorld.getGameObjectArray().get(i).getPosition();
				System.out.println("p:"+gameWorld.getGameObjectArray().get(i).getBody().getPosition());
				System.out.println(gameWorld.getGameObjectArray().get(i).getBody().getLinearVelocity());
				gameWorld.getGameObjectArray().get(i).getBody().setTransform(p.x+10, p.y, 0);
			}
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
	
	private void initScreenLogic(){
		screenLogic = new ScreenLogic(1) {
			public void run() {
				lastRunTime=System.currentTimeMillis();
				while (!isStoped) {
					if (System.currentTimeMillis()-lastRunTime>1) {
						lastRunTime+=delay;
						gameWorld.update(delay/1000f);
					}
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
		if(jsonValue.get("gago")!=null){
			System.out.println(recvString);
			jsonValue=jsonValue.get(0);
			for (int i = 0; i < jsonValue.size; i++) {
				
			}
		}
	}
	

	
	private void lPressAction(){
		System.out.println("l");
		sendMessage("{gago:}");
	}
	
	private void zPressAction(){
		sendMessage("{ago:{n:"+gameObjectName+",t:{p:{x:100,y:300}},s:{w:"+textureRegion.getRegionWidth()+",h:"+textureRegion.getRegionHeight()+"}}}");

	}
	private void xPressAction(){
		if(gameWorld.findGameObject(gameObjectName)!=null)
			sendMessage("{rgo:{name:"+gameObjectName+"}}");
	
	}
	private void cPressAction(){
		if(gameWorld.findGameObject(gameObjectName)!=null)
			sendMessage("{cgo:{name:"+gameObjectName+",set:{i:{x:0,y:0}}}}");
	
	}
	private void gPressAction(){
		sendMessage("{ggo:{name:"+gameObjectName+"}}");
	}
	
	private void aJustPressAction(){
		GameObjectB2D gameObject=gameWorld.findGameObject(gameObjectName);
		if (gameObject!=null) {
			sendMessage("{cgo:{name:"+gameObjectName+",add:{i:{x:"+(-1*speed)+"}}}}");
		}
	}
	private void aJustUpAction(){
		GameObjectB2D gameObject=gameWorld.findGameObject(gameObjectName);
		if (gameObject!=null) {
			//if(gameObject.getInertiaForce().x!=0)
				sendMessage("{cgo:{name:"+gameObjectName+",add:{i:{x:"+(speed)+"}}}}");
			
		}
		//System.out.println("object count:"+gameWorld.getGameObjectArray().size);
	}
	
	private void dJustPressAction(){
		GameObjectB2D gameObject=gameWorld.findGameObject(gameObjectName);
		if (gameObject!=null) {
			sendMessage("{cgo:{name:"+gameObjectName+",add:{i:{x:"+(speed)+"}}}}");
		}
	}
	private void dJustUpAction(){
		GameObjectB2D gameObject=gameWorld.findGameObject(gameObjectName);
		if (gameObject!=null) {
			//if(gameObject.getInertiaForce().x!=0)
				sendMessage("{cgo:{name:"+gameObjectName+",add:{i:{x:"+(-1*speed)+"}}}}");
		}
	}
	private void wJustPressAction(){
		GameObjectB2D gameObject=gameWorld.findGameObject(gameObjectName);
		if (gameObject!=null) {
			sendMessage("{cgo:{name:"+gameObjectName+",add:{i:{x:200,y:0}}}}");
		}
	}
	private void wJustUpAction(){
		GameObjectB2D gameObject=gameWorld.findGameObject(gameObjectName);
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
