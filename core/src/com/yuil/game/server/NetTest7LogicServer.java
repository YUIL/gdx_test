package com.yuil.game.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.JsonReader;
import com.yuil.game.entity.B2dGameObject;
import com.yuil.game.entity.GameWorldB2d;
import com.yuil.game.entity.message.C2S_B2D_ADD_GAMEOBJECT;
import com.yuil.game.entity.message.C2S_B2D_APPLY_FORCE;
import com.yuil.game.entity.message.C2S_B2D_CHANGE_APPLY_FORCE_STATE;
import com.yuil.game.entity.message.C2S_B2D_GET_GAMEOBJECT;
import com.yuil.game.entity.message.C2S_B2D_REMOVE_GAMEOBJECT;
import com.yuil.game.entity.message.GameMessageType;
import com.yuil.game.entity.message.S2C_B2D_CHANGE_APPLY_FORCE_STATE;
import com.yuil.game.entity.message.S2C_B2D_GET_ALL_GAMEOBJECT;
import com.yuil.game.entity.message.S2C_B2D_GET_GAMEOBJECT;
import com.yuil.game.entity.message.S2C_B2D_REMOVE_GAMEOBJECT;
import com.yuil.game.entity.message.S2C_TEST;
import com.yuil.game.net.message.GAME_MESSAGE;
import com.yuil.game.net.message.GAME_MESSAGE_ARRAY;
import com.yuil.game.net.message.Message;
import com.yuil.game.net.message.MessageHandler;
import com.yuil.game.net.udp.MessageProcessor;
import com.yuil.game.net.udp.Session;
import com.yuil.game.net.udp.UdpMessageListener;
import com.yuil.game.net.udp.UdpServer;
import com.yuil.game.util.DataUtil;
import com.yuil.game.util.Log;

public class NetTest7LogicServer implements UdpMessageListener {

	volatile UdpServer udpServer;
	JsonReader jsonReader = new JsonReader();
	volatile boolean stoped = false;
	volatile GameWorldB2d gameWorld;
	int autoBoardCastInterval = 100;
	long nextAutoBoardCastTime = 0;
	volatile int syncCount = 0;
	volatile int syncNum = 0;
	long disoposeMessageCount = 0;
	MessageProcessor messageProcessor;
	ExecutorService threadPool = Executors.newSingleThreadExecutor();
	volatile Thread gameWorldThread;

	volatile int autoAddIterval = 10000;
	volatile long nextAutoAddTime = 0;
	//volatile int autoNum = 0;

	public class GameWorldLogic implements Runnable {

		public GameWorldLogic() {
			// TODO Auto-generated constructor stub
		}

		int updateInterval = 10;
		long nextUpdateTime = 0;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			nextAutoAddTime = System.currentTimeMillis();
			nextUpdateTime = System.currentTimeMillis();
			boolean needBoard = false;
			// nextAutoBoardCastTime = System.currentTimeMillis();
			while (!stoped) {
				if (System.currentTimeMillis() > nextUpdateTime) {
					nextUpdateTime += updateInterval;
					/*
					 * if (gameWorld.getGameObjectArray().size < 3) { if
					 * (System.currentTimeMillis() > nextAutoAddTime) {
					 * nextAutoAddTime += autoAddIterval;
					 * gameWorld.addBoxGameObject(GameObjectCreation.random(
					 * autoNum)); autoNum++; needBoard = true; } }
					 */

					for (int i = 0; i < gameWorld.getGameObjectArray().size; i++) {
						B2dGameObject gameObject = gameWorld.getGameObjectArray().get(i);
						if (gameObject.getBody().getPosition().y < -50) {
							gameWorld.getGameObjectRemoveQueue().add(gameObject);
							needBoard = true;
						}
					}
					gameWorld.update(updateInterval / 1000f);

					if (needBoard) {
						addSyncNum();;
					}
					/*
					 * if (System.currentTimeMillis()-nextAutoBoardCastTime>
					 * autoBoardCastInterval) {
					 * nextAutoBoardCastTime+=autoBoardCastInterval;
					 * boardCast("{gago:"+str+"}"); }
					 */
					if (syncNum > syncCount) {
						boardCast_GAME_MESSAGE(S2C_B2D_GET_ALL_GAMEOBJECT.getBytes(gameWorld.getGameObjectArray()), false);
						syncCount++;
					}

				} else {
					try {
						Thread.sleep(updateInterval);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				needBoard = false;
			}
			System.out.println("worldLogic stoped");
		}

	}

	public NetTest7LogicServer() {
		
		
		Box2D.init();
		gameWorld = new GameWorldB2d();
		messageProcessor = new UdpMessageProcessor() {
			@Override
			public void run() {
				if (data.length < Message.TYPE_LENGTH) {
					return;
				}
				int typeOrdinal = DataUtil.bytesToInt(DataUtil.subByte(data, Message.TYPE_LENGTH, 0));
				// System.out.println("type:"+type);
				byte[] src = DataUtil.subByte(data, data.length - Message.TYPE_LENGTH, Message.TYPE_LENGTH);
			
				handlerMap.get(typeOrdinal).handle(src);
				
			}

		};
	}

	public void addSyncNum(){
		synchronized(this){
			syncNum++;
		}
	}
	
	public  void boardCast_GAME_MESSAGE(Message message, boolean isImmediately) {
		boardCast(GAME_MESSAGE.getBytes(message.toBytes()), isImmediately);
	}
	
	public  void boardCast_GAME_MESSAGE(byte[] data, boolean isImmediately) {
		boardCast(GAME_MESSAGE.getBytes(data), isImmediately);
	}

	public  void boardCast_GAME_MESSAGE_ARRAY(Message[] messages, boolean isImmediately) {
		boardCast(new GAME_MESSAGE_ARRAY(messages).toBytes(), isImmediately);
	}
	
	public  void boardCast(byte[] bytes, boolean isImmediately) {
		for (int i = 0; i < udpServer.sessionArray.size; i++) {
			Session session = udpServer.sessionArray.get(i);
			udpServer.send(bytes, session, isImmediately);
		}
	}

	public  void boardCast(String str, boolean isImmediately) {
		boardCast(str.getBytes(), isImmediately);
	}

	public void start() {
		System.out.println("LogicServer start!");
		Runnable gameWorldLogic = new GameWorldLogic();
		gameWorldThread = new Thread(gameWorldLogic);
		gameWorldThread.start();

	}

	@Override
	public void disposeUdpMessage(Session session, byte[] data) {
		// TODO Auto-generated method stub
		messageProcessor.setSession(session);
		messageProcessor.setData(data);
		threadPool.execute(messageProcessor);
	}

	public class UdpMessageProcessor extends MessageProcessor{
		Map<Integer,MessageHandler>  handlerMap=new HashMap<Integer, MessageHandler>();
		public UdpMessageProcessor(){
			handlerMap.put(GameMessageType.C2S_B2D_APPLY_FORCE.ordinal(), new MessageHandler(){
				public void handle(byte[] src){
					long id;
					B2dGameObject gameObject;
					C2S_B2D_APPLY_FORCE gameMessage_c2s_rpc = new C2S_B2D_APPLY_FORCE(src);
					id = gameMessage_c2s_rpc.gameObjectId;
					gameObject = gameWorld.findGameObject(id);
					if (gameObject != null) {
						float forceX = gameMessage_c2s_rpc.forceX;
						float forceY = gameMessage_c2s_rpc.forceY;
						if (gameObject.getSpeed() < gameObject.getMaxSpeed()) {
							if (forceX == 0) {
								if (gameObject.getBody().getLinearVelocity().y < 1
										&& gameObject.getBody().getLinearVelocity().y > -1) {
									gameObject.applyForce(forceX, forceY);
									// boardCast(recvString);
									addSyncNum();
								}
							} else {
								if (forceX > 0 && gameObject.getBody().getLinearVelocity().x < 10
										|| forceX < 0 && gameObject.getBody().getLinearVelocity().x > -10) {
									gameObject.applyForce(forceX, forceY);
									// boardCast(recvString);
									addSyncNum();
								}
							}

						}
					}
				}
			});
			
			handlerMap.put(GameMessageType.C2S_B2D_ADD_GAMEOBJECT.ordinal(), new MessageHandler(){
				public void handle(byte[] src){
					long id;
					B2dGameObject gameObject;
					C2S_B2D_ADD_GAMEOBJECT gameMessage_c2s_ago = new C2S_B2D_ADD_GAMEOBJECT(src);
					id = gameMessage_c2s_ago.b2dBoxBaseInformation.gameObjectId;
					gameObject = gameWorld.findGameObject(id);
					if (gameObject != null) {
						udpServer.send(GAME_MESSAGE.getBytes(S2C_B2D_GET_GAMEOBJECT.getBytes(gameObject)), session,
								false);

					} else {
						Log.println("add");
						gameWorld.getGameObjectCreationQueue().add(gameMessage_c2s_ago.b2dBoxBaseInformation);
						addSyncNum();
					
					}
				}
			});
			
			handlerMap.put(GameMessageType.C2S_B2D_GET_ALL_GAMEOBJECT.ordinal(), new MessageHandler(){
				public void handle(byte[] src){
					udpServer.send(
							GAME_MESSAGE.getBytes(S2C_B2D_GET_ALL_GAMEOBJECT.getBytes(gameWorld.getGameObjectArray())),
							session, false);
				}
			});
			
			handlerMap.put(GameMessageType.C2S_B2D_REMOVE_GAMEOBJECT.ordinal(), new MessageHandler(){
				public void handle(byte[] src){
					B2dGameObject gameObject;
					C2S_B2D_REMOVE_GAMEOBJECT gameMessage_c2s_rgo = new C2S_B2D_REMOVE_GAMEOBJECT(src);
					gameObject = gameWorld.findGameObject(gameMessage_c2s_rgo.gameObjectId);
					if (gameObject != null) {
						gameWorld.getGameObjectRemoveQueue().add(gameObject);
						S2C_B2D_REMOVE_GAMEOBJECT gameMessage_s2c_rgo = new S2C_B2D_REMOVE_GAMEOBJECT();
						gameMessage_s2c_rgo.gameObjectId = gameObject.getId();
						boardCast_GAME_MESSAGE(gameMessage_s2c_rgo.toBytes(), false);
					}
				}
			});
			
			handlerMap.put(GameMessageType.C2S_B2D_GET_GAMEOBJECT.ordinal(), new MessageHandler(){
				public void handle(byte[] src){

					B2dGameObject gameObject;
					C2S_B2D_GET_GAMEOBJECT gameMessage_c2s_ggo = new C2S_B2D_GET_GAMEOBJECT(src);
					gameObject = gameWorld.findGameObject(gameMessage_c2s_ggo.gameObjectId);
					if (gameObject != null) {
						udpServer.send(GAME_MESSAGE.getBytes(S2C_B2D_GET_GAMEOBJECT.getBytes(gameObject)), session,
								false);

					} else {
						S2C_B2D_REMOVE_GAMEOBJECT gameMessage_s2c_rgo = new S2C_B2D_REMOVE_GAMEOBJECT();
						gameMessage_s2c_rgo.gameObjectId = gameMessage_c2s_ggo.gameObjectId;
						udpServer.send(GAME_MESSAGE.getBytes(gameMessage_s2c_rgo.toBytes()), session, false);
					}
				}
			});
			
			handlerMap.put(GameMessageType.C2S_B2D_CHANGE_APPLY_FORCE_STATE.ordinal(), new MessageHandler(){
				public void handle(byte[] src){

					long id;
					B2dGameObject gameObject;
					C2S_B2D_CHANGE_APPLY_FORCE_STATE gameMessage_c2s_change_apply_force_state = new C2S_B2D_CHANGE_APPLY_FORCE_STATE(
							src);
					id = gameMessage_c2s_change_apply_force_state.gameObjectId;
					gameObject = gameWorld.findGameObject(id);
					if (gameObject != null) {
						gameObject.changeApplyForceState(gameMessage_c2s_change_apply_force_state.applyForceState);

						Message responseMessage = new S2C_B2D_CHANGE_APPLY_FORCE_STATE(src);
						boardCast_GAME_MESSAGE(responseMessage.toBytes(),false);
						addSyncNum();
					}
				}
			});
			
			handlerMap.put(GameMessageType.C2S_TEST.ordinal(), new MessageHandler(){
				public void handle(byte[] src){
					Message responseMessage = new S2C_TEST();
					//boardCast_GAME_MESSAGE(responseMessage, false);
					udpServer.send(GAME_MESSAGE.getBytes(responseMessage.toBytes()), session, false);
				}
			});
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
}
