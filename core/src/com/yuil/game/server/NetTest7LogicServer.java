package com.yuil.game.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.JsonReader;
import com.yuil.game.entity.B2DGameObject;
import com.yuil.game.entity.GameWorldB2D;
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
import com.yuil.game.net.message.Message;
import com.yuil.game.net.udp.MessageProcessor;
import com.yuil.game.net.udp.Session;
import com.yuil.game.net.udp.UdpMessageListener;
import com.yuil.game.net.udp.UdpServer;
import com.yuil.game.util.DataUtil;

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
					/*
					 * if (gameWorld.getGameObjectArray().size < 3) { if
					 * (System.currentTimeMillis() > nextAutoAddTime) {
					 * nextAutoAddTime += autoAddIterval;
					 * gameWorld.addBoxGameObject(GameObjectCreation.random(
					 * autoNum)); autoNum++; needBoard = true; } }
					 */

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
						boardCast(S2C_B2D_GET_ALL_GAMEOBJECT.getBytes(gameWorld.getGameObjectArray()), false);
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
		messageProcessor = new MessageProcessor() {
			@Override
			public void run() {
				if (data.length < Message.TYPE_BYTE_LENGTH) {
					return;
				}
				int typeOrdinal = DataUtil.bytesToInt(DataUtil.subByte(data, Message.TYPE_BYTE_LENGTH, 0));
				// System.out.println("type:"+type);
				byte[] src = DataUtil.subByte(data, data.length - Message.TYPE_BYTE_LENGTH, Message.TYPE_BYTE_LENGTH);
				long id;
				B2DGameObject gameObject;
				Message responseMessage;
				switch (GameMessageType.values()[typeOrdinal]) {
				case C2S_B2D_APPLY_FORCE:
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
					C2S_B2D_ADD_GAMEOBJECT gameMessage_c2s_ago = new C2S_B2D_ADD_GAMEOBJECT(src);
					id = gameMessage_c2s_ago.b2dBoxBaseInformation.gameObjectId;
					gameObject = gameWorld.findGameObject(id);
					if (gameObject != null) {
						udpServer.send(GAME_MESSAGE.getBytes(S2C_B2D_GET_GAMEOBJECT.getBytes(gameObject)), session,
								false);

					} else {
						// System.out.println("create Box,
						// id:"+gameMessage_c2s_ago.b2dBoxBaseInformation.gameObjectId);
						gameWorld.getGameObjectCreationQueue().add(gameMessage_c2s_ago.b2dBoxBaseInformation);
						boardCastNum++;
						/*
						 * GameMessage_s2c_ago gameMessage_s2c_ago=new
						 * GameMessage_s2c_ago();
						 * gameMessage_s2c_ago.b2dBoxBaseInformation=
						 * gameMessage_c2s_ago.b2dBoxBaseInformation;
						 * udpServer.send(gameMessage_s2c_ago.toBytes(),
						 * session);
						 */
					}
					break;
				case C2S_B2D_GET_ALL_GAMEOBJECT:
					udpServer.send(
							GAME_MESSAGE.getBytes(S2C_B2D_GET_ALL_GAMEOBJECT.getBytes(gameWorld.getGameObjectArray())),
							session, false);
					break;
				case C2S_B2D_REMOVE_GAMEOBJECT:
					C2S_B2D_REMOVE_GAMEOBJECT gameMessage_c2s_rgo = new C2S_B2D_REMOVE_GAMEOBJECT(src);
					gameObject = gameWorld.findGameObject(gameMessage_c2s_rgo.gameObjectId);
					if (gameObject != null) {
						gameWorld.getGameObjectRemoveQueue().add(gameObject);
						S2C_B2D_REMOVE_GAMEOBJECT gameMessage_s2c_rgo = new S2C_B2D_REMOVE_GAMEOBJECT();
						gameMessage_s2c_rgo.gameObjectId = gameObject.getId();
						boardCast(gameMessage_s2c_rgo.toBytes(), false);
					}
					break;
				case C2S_B2D_GET_GAMEOBJECT:
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
					break;
				case C2S_B2D_CHANGE_APPLY_FORCE_STATE:
					C2S_B2D_CHANGE_APPLY_FORCE_STATE gameMessage_c2s_change_apply_force_state = new C2S_B2D_CHANGE_APPLY_FORCE_STATE(
							src);
					id = gameMessage_c2s_change_apply_force_state.gameObjectId;
					gameObject = gameWorld.findGameObject(id);
					if (gameObject != null) {
						gameObject.changeApplyForceState(gameMessage_c2s_change_apply_force_state.applyForceState);

						responseMessage = new S2C_B2D_CHANGE_APPLY_FORCE_STATE(src);
						boardCast(responseMessage.toBytes(),false);
						boardCastNum++;
					}

					break;

				case C2S_TEST:
					System.out.println("c2s_test");
					responseMessage = new S2C_TEST();
					boardCast(responseMessage, false);
					boardCast(responseMessage, false);
					boardCast(responseMessage, false);
					break;
				default:
					break;
				}
			}

		};
	}

	public synchronized void boardCast(Message message, boolean isImmediately) {

		boardCast(message.toBytes(), isImmediately);
	}

	public synchronized void boardCast(byte[] bytes, boolean isImmediately) {

		for (int i = 0; i < udpServer.sessionArray.size; i++) {
			Session session = udpServer.sessionArray.get(i);
			udpServer.send(GAME_MESSAGE.getBytes(bytes), session, isImmediately);
		}
	}

	public synchronized void boardCast(String str, boolean isImmediately) {
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

}
