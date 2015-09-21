package com.mygdx.game.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.JsonReader;
import com.mygdx.game.entity.B2DGameObject;
import com.mygdx.game.entity.GameWorldB2D;
import com.mygdx.game.entity.message.GameMessage;
import com.mygdx.game.entity.message.GameMessageType;
import com.mygdx.game.entity.message.GameMessage_c2s_ago;
import com.mygdx.game.entity.message.GameMessage_c2s_ggo;
import com.mygdx.game.entity.message.GameMessage_c2s_rgo;
import com.mygdx.game.entity.message.GameMessage_c2s_rpc;
import com.mygdx.game.entity.message.GameMessage_s2c_gago;
import com.mygdx.game.entity.message.GameMessage_s2c_ggo;
import com.mygdx.game.entity.message.GameMessage_s2c_rgo;
import com.mygdx.game.net.udp.MessageProcessor;
import com.mygdx.game.net.udp.Session;
import com.mygdx.game.net.udp.UdpMessageListener;
import com.mygdx.game.net.udp.UdpServer;
import com.mygdx.game.util.ByteUtil;

public class NetTest7LogicServer implements UdpMessageListener {

	volatile UdpServer udpServer;
	JsonReader jsonReader = new JsonReader();
	volatile boolean stoped = false;
	volatile GameWorldB2D gameWorld;
	int autoBoardCastInterval = 100;
	long nextAutoBoardCastTime = 0;
	volatile int boardCastCound = 0;
	volatile int boardCastNum = 0;
	long disoposeMessageCount = 0;
	MessageProcessor messageProcessor;
	ExecutorService threadPool = Executors.newSingleThreadExecutor();
	volatile Thread gameWorldThread;

	volatile int autoAddIterval = 10000;
	volatile long nextAutoAddTime = 0;
	volatile int autoNum = 0;

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
					/*if (gameWorld.getGameObjectArray().size < 3) {
						if (System.currentTimeMillis() > nextAutoAddTime) {
							nextAutoAddTime += autoAddIterval;
							gameWorld.addBoxGameObject(GameObjectCreation.random(autoNum));
							autoNum++;
							needBoard = true;
						}
					}*/

					for (int i = 0; i < gameWorld.getGameObjectArray().size; i++) {
						B2DGameObject gameObject = gameWorld.getGameObjectArray().get(i);
						if (gameObject.getBody().getPosition().y < -50) {
							gameWorld.getGameObjectRemoveQueue().add(gameObject);
							needBoard = true;
						}
					}
					gameWorld.update(updateInterval / 1000f);

					if (needBoard) {
						boardCastNum++;
					}
					/*
					 * if (System.currentTimeMillis()-nextAutoBoardCastTime>
					 * autoBoardCastInterval) {
					 * nextAutoBoardCastTime+=autoBoardCastInterval;
					 * boardCast("{gago:"+str+"}"); }
					 */
					if (boardCastNum > boardCastCound) {
						boardCast(GameMessage_s2c_gago.getBytesFromB2dGameObjecArray(gameWorld.getGameObjectArray()));
						boardCastCound++;
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
		gameWorld = new GameWorldB2D();
		messageProcessor=new MessageProcessor() {
			@Override
			public void run() {
				if (data.length>GameMessage.TYPE_BYTE_LENGTH) {	
					int typeOrdinal = ByteUtil.bytesToInt(ByteUtil.subByte(data, GameMessage.TYPE_BYTE_LENGTH, 0));
					//System.out.println("type:"+type);
					byte[] src = ByteUtil.subByte(data, data.length -GameMessage.TYPE_BYTE_LENGTH, GameMessage.TYPE_BYTE_LENGTH);
					long id;
					B2DGameObject gameObject;
					switch (GameMessageType.values()[typeOrdinal]) {
					case C2S_B2D_APPLY_FORCE:
						GameMessage_c2s_rpc gameMessage_c2s_rpc = new GameMessage_c2s_rpc(src);
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
										boardCastNum++;
									}
								} else {
									if (forceX > 0 && gameObject.getBody().getLinearVelocity().x < 10
											|| forceX < 0 && gameObject.getBody().getLinearVelocity().x > -10) {
										gameObject.applyForce(forceX, forceY);
										// boardCast(recvString);
										boardCastNum++;
									}
								}

							}
						}
						break;
					case C2S_B2D_ADD_GAMEOBJECT:
						GameMessage_c2s_ago gameMessage_c2s_ago = new GameMessage_c2s_ago(src);
						id = gameMessage_c2s_ago.b2dBoxBaseInformation.gameObjectId;
						gameObject = gameWorld.findGameObject(id);
						if (gameObject != null) {
							
						}else{
							//System.out.println("create Box, id:"+gameMessage_c2s_ago.b2dBoxBaseInformation.gameObjectId);
							gameWorld.getGameObjectCreationQueue().add(gameMessage_c2s_ago.b2dBoxBaseInformation);
							boardCastNum++;
							/*GameMessage_s2c_ago gameMessage_s2c_ago=new GameMessage_s2c_ago();
							gameMessage_s2c_ago.b2dBoxBaseInformation=gameMessage_c2s_ago.b2dBoxBaseInformation;
							udpServer.send(gameMessage_s2c_ago.toBytes(), session);*/
						}
						break;
					case C2S_B2D_GET_ALL_GAMEOBJECT:
						udpServer.send(GameMessage_s2c_gago.getBytesFromB2dGameObjecArray(gameWorld.getGameObjectArray()), session);
						break;
					case C2S_B2D_REMOVE_GAMEOBJECT:
						GameMessage_c2s_rgo gameMessage_c2s_rgo=new GameMessage_c2s_rgo(src);
						gameObject=gameWorld.findGameObject(gameMessage_c2s_rgo.gameObjectId);
						if(gameObject!=null){
							gameWorld.getGameObjectRemoveQueue().add(gameObject);
							GameMessage_s2c_rgo gameMessage_s2c_rgo=new GameMessage_s2c_rgo();
							gameMessage_s2c_rgo.gameObjectId=gameObject.getId();
							boardCast(gameMessage_s2c_rgo.toBytes());
						}					
						break;
					case C2S_B2D_GET_GAMEOBJECT:
						GameMessage_c2s_ggo gameMessage_c2s_ggo=new GameMessage_c2s_ggo(src);
						gameObject=gameWorld.findGameObject(gameMessage_c2s_ggo.gameObjectId);
						if(gameObject!=null){
							udpServer.send(GameMessage_s2c_ggo.getBytesFromB2dGameObject(gameObject), session);
							
						}else{
							GameMessage_s2c_rgo gameMessage_s2c_rgo=new GameMessage_s2c_rgo();
							gameMessage_s2c_rgo.gameObjectId=gameMessage_c2s_ggo.gameObjectId;
							udpServer.send(gameMessage_s2c_rgo.toBytes(), session);
						}
						break;
					default:
						break;
					}
				}
			}
		};
	}
	
	public synchronized void boardCast(byte[] bytes) {
		Session session;
		for (int i = 0; i < udpServer.sessionArray.size; i++) {
			session = udpServer.sessionArray.get(i);
			udpServer.send(bytes, session);
		}
	}

	public synchronized void boardCast(String str) {
		boardCast(str.getBytes());
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

}
