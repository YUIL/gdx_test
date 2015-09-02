package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.inputprocessor.InputProcessor;
import com.mygdx.game.net.Player;
import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpServer;
import com.mygdx.game.stage.StageManager;
import com.mygdx.game.util.ActorInputListenner;
import com.mygdx.game.util.GameManager;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Random;


/**
 * Created by i008 on 2015/8/27.
 */
public class NetTest4Screen extends TestScreen {

    String recvString = null;
    volatile UdpServer server;
    Texture texture = new Texture(Gdx.files.internal("images/button_0.png"));
    volatile Session session;
    int temp1 = 0;
    JsonValue jsonValue;
    JsonReader jsonReader = new JsonReader();
    Stage stage = null;
    Skin skin = null;
    SpriteBatch batch;
    String guiXmlPath = "gui/NetTest4Gui.xml";
    volatile Player selfPlayer;
    volatile Player remotePlayer;
    Map<String, Player> playerMap;
    int speed=100;
    boolean justPressZ=false;
    Thread screenLogicThread;
    ScreenLogic screenLogic;
    public NetTest4Screen(Game game) {
        super(game);

        batch = new SpriteBatch();
        skin = StageManager.defaultSkin;
        stage = new Stage();
        StageManager.guiFactor.setStageFromXml(stage, guiXmlPath);
        inputProcess();
        GameManager.setInputProcessor(stage);
        selfPlayer = new Player(((TextArea) stage.getRoot().findActor("localport")).getText());
        remotePlayer = new Player(((TextArea) stage.getRoot().findActor("remoteport")).getText());
        screenLogic=new ScreenLogic(1){
            public void run() {
                while(!isStoped){
                    try {
                        if (server != null) {
                            if (session != null) {
                                while (!session.getRecvMessageQueue().isEmpty()) {
                                    recvString = new String(session.getRecvMessageQueue().poll().getData());
                                    jsonValue = jsonReader.parse(recvString);                                    
                                    if(jsonValue.get("move")!=null){
                                        if(jsonValue.get("move").asInt()==0){
                                            remotePlayer.setMove(false);
                                        }else{
                                            //System.out.println(System.currentTimeMillis());
                                            remotePlayer.setMove(true);
                                        }
                                    }else if(jsonValue.get("x")!=null){
                                        remotePlayer.getRectangle().setX(jsonValue.get("x").asFloat());
                                        remotePlayer.getRectangle().setY(jsonValue.get("y").asFloat());
                                    }else if(jsonValue.get("time")!=null){
                                        System.out.println("finaldelay:"+(System.currentTimeMillis()-jsonValue.get("time").asLong()));

                                    }else if(jsonValue.get("user")!=null){
                                        System.out.println(jsonValue.toString());

                                    }

                                }
                            } else {
                                if (!server.sessionMap.isEmpty()){
                                    for (Map.Entry<Long, Session> entry : server.sessionMap.entrySet()) {
                                        session = entry.getValue();
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        //System.err.println("session:"+session.getRecvMessageQueue());

                    }

                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        screenLogicThread=new Thread(screenLogic);
        screenLogicThread.start();

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            sendMessage("{time:"+System.currentTimeMillis()+"}");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            selfPlayer.getRectangle().setX(selfPlayer.getRectangle().getX() - speed * delta);
            sendMessage(("{" + "x:" + selfPlayer.getRectangle().getX() + ",y:" + selfPlayer.getRectangle().getY() + "}"));

        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            selfPlayer.getRectangle().setX(selfPlayer.getRectangle().getX() + speed*delta);
           sendMessage(("{" + "x:" + selfPlayer.getRectangle().getX() + ",y:" + selfPlayer.getRectangle().getY() + "}"));

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
        	System.out.println("send");
           sendMessage(("{" + "getUser:{name:yuil}}"));

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
        	System.out.println("send");
           sendMessage(("{" + "login:{name:yuil}}"));

        }
        
        if(Gdx.input.isKeyPressed(Input.Keys.Z)){
        	if(!justPressZ){
        		//System.out.println(System.currentTimeMillis());
        		selfPlayer.setMove(true);
        		sendMessage("{move:"+"1}");
        	}
        	justPressZ=true;
        	
        }else if(justPressZ){
        	selfPlayer.setMove(false);
    		sendMessage("{move:"+"0}");
        	justPressZ=!justPressZ;
        }
        
        if(selfPlayer.isMove()){
        	selfPlayer.getRectangle().setX(selfPlayer.getRectangle().getX()+speed*delta);
        	
        }
        if(remotePlayer.isMove()){
        	remotePlayer.getRectangle().setX(remotePlayer.getRectangle().getX()+speed*delta);
        }

        batch.begin();
        batch.draw(texture, selfPlayer.getRectangle().getX(), selfPlayer.getRectangle().getY());
        batch.draw(texture, remotePlayer.getRectangle().getX(), remotePlayer.getRectangle().getY());
        batch.end();
        InputProcessor.handleInput(game, delta);
        //netMessageProcess();
    }
    public void sendMessage(String str){
        if(session==null) {
            session = new Session(new Random().nextLong());
            session.setContactorAddress(new InetSocketAddress(((TextArea) (stage.getRoot()
                    .findActor("remoteip"))).getText(), Integer
                    .parseInt(((TextArea) (stage.getRoot()
                            .findActor("remoteport"))).getText())));
            server.sessionMap.put(session.getId(), session);
        }
        server.send(str.getBytes(), session);

    }
    public void netMessageProcess() {
    	try {
    		if (server != null) {
                if (session != null) {
                    while (!session.getRecvMessageQueue().isEmpty()) {
                        recvString = new String(session.getRecvMessageQueue().poll().getData());
                        jsonValue = jsonReader.parse(recvString);
                        if(jsonValue.get("move")!=null){
                        	if(jsonValue.get("move").asInt()==0){
                        		remotePlayer.setMove(false);
                        	}else{
                        		//System.out.println(System.currentTimeMillis());
                        		remotePlayer.setMove(true);
                        	}
                        }else if(jsonValue.get("x")!=null){
                        	remotePlayer.getRectangle().setX(jsonValue.get("x").asFloat());
                        	remotePlayer.getRectangle().setY(jsonValue.get("y").asFloat());
                        }else if(jsonValue.get("time")!=null){
                            System.out.println("finaldelay:"+(System.currentTimeMillis()-jsonValue.get("time").asLong()));

                        }
                        
                    }
                } else {
                    if (!server.sessionMap.isEmpty()){
                        for (Map.Entry<Long, Session> entry : server.sessionMap.entrySet()) {
                            session = entry.getValue();
                        }
                    }
                }
            }
		} catch (Exception e) {
			// TODO: handle exception
			//System.err.println("session:"+session.getRecvMessageQueue());
			
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

    public void inputProcess() {
        stage.getRoot().findActor("start")
                .addListener(new ActorInputListenner() {
                    public void touchUp(InputEvent event, float x, float y,
                                        int pointer, int button) {
                        int port = Integer
                                .parseInt(((TextArea) (stage.getRoot()
                                        .findActor("localport"))).getText());
                        System.out.println("server start at port:" + port);
                        server = new UdpServer(port);
                        server.start();

                    }
                });
        stage.getRoot().findActor("send")
                .addListener(new ActorInputListenner() {
                    public void touchUp(InputEvent event, float x, float y,
                                        int pointer, int button) {

                    }
                });
    }
}
